package handler

import (
	"github.com/gin-gonic/gin"
	"log"
	"net/http"
	"shopez/darkshield/model"
	"shopez/darkshield/model/apperr"
)

func (h *Handler) Signup(c *gin.Context) {
	var req model.SignupReq

	if ok := bindData(c, &req); !ok {
		return
	}

	ctx := c.Request.Context()
	jwt, err := h.AuthenticationService.Register(ctx, req)
	if err != nil {
		log.Printf("Error signing up: %+v\n", err.Error())
		c.JSON(apperr.Status(err), gin.H{"error": err})
		return
	}

	c.JSON(http.StatusCreated, jwt)
}
