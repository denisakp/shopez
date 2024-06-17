package handler

import (
	"github.com/gin-gonic/gin"
	"log"
	"net/http"
	"shopez/darkshield/model"
	"shopez/darkshield/model/apperr"
)

func (h *Handler) Login(c *gin.Context) {
	var req model.LoginReq

	if ok := bindData(c, &req); !ok {
		return
	}

	jwt, err := h.AuthenticationService.Login(c, req)
	if err != nil {
		log.Printf("Error logging in: %+v\n", err.Error())
		c.JSON(apperr.Status(err), gin.H{"error": err.Error()})
		return
	}

	c.JSON(http.StatusOK, jwt)
}
