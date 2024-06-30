package handler

import (
	"github.com/gin-gonic/gin"
	"github.com/gin-gonic/gin/binding"
	"net/http"
	"shopez/uploader/model/apperr"
	"shopez/uploader/model/dtos"
)

func (h *Handler) Upload(c *gin.Context) {
	var req dtos.FileUploadReq

	if err := c.ShouldBindWith(&req, binding.FormMultipart); err != nil {
		// custom error handling
		if len(req.ValidationErrors) > 0 {
			c.JSON(http.StatusBadRequest, gin.H{"error": req.ValidationErrors})
			return
		}

		// fallback
		c.JSON(http.StatusBadRequest, gin.H{"error": err.Error()})
		return
	}

	form, err := c.MultipartForm()
	if err != nil {
		c.String(http.StatusBadRequest, "Failed to get multipart form")
		return
	}

	files, exists := form.File["items"]
	if !exists {
		c.JSON(http.StatusBadRequest, gin.H{"error": "images[] key not found in form data"})
		return
	}

	response, err := h.UploadService.UploadFile(c, files)

	if err != nil {
		c.JSON(apperr.Status(err), gin.H{"error": err.Error()})
		return
	}

	c.JSON(http.StatusOK, gin.H{"count": len(files), "locations": response})
}
