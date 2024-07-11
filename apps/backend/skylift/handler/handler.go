package handler

import (
	"github.com/gin-gonic/gin"
	"shopez/skylift/model"
)

type Handler struct {
	UploadService model.UploadService
}

type Config struct {
	R             *gin.Engine
	ApiVersion    string
	UploadService model.UploadService
}

func NewHandler(c *Config) {
	h := &Handler{
		UploadService: c.UploadService,
	}

	g := c.R.Group(c.ApiVersion)

	g.POST("/upload", h.Upload)

	g.GET("/health", func(c *gin.Context) {
		c.JSON(200, gin.H{
			"status": "ok",
		})
	})
}
