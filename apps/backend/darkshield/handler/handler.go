package handler

import (
	"github.com/gin-gonic/gin"
	"shopez/darkshield/model"
)

type Handler struct {
	AuthenticationService model.AuthenticationService
}

type Config struct {
	R                     *gin.Engine
	ApiVersion            string
	AuthenticationService model.AuthenticationService
}

func NewHandler(c *Config) {
	h := &Handler{
		AuthenticationService: c.AuthenticationService,
	}

	g := c.R.Group(c.ApiVersion)
	{
		a := g.Group("/auth")
		a.POST("/signup", h.Signup)
		a.POST("/login", h.Login)
	}

	g.GET("/ping", func(c *gin.Context) {
		c.JSON(200, gin.H{
			"ping": "pong",
		})
	})
}
