package main

import (
	"github.com/gin-gonic/gin"
	"shopez/darkshield/config"
	"shopez/darkshield/handler"
	"shopez/darkshield/repository"
	"shopez/darkshield/service"
)

func inject(cfg *config.DarkshieldConfig) (*gin.Engine, error) {

	// repository layer
	kcIamRepository := repository.NewKeycloakAuthRepository(cfg.KeycloakIamConfig)

	// service layer
	authenticationService := service.NewAuthenticationService(&service.ASConfig{
		AuthenticationRepository: kcIamRepository,
	})

	router := gin.Default()

	handler.NewHandler(&handler.Config{
		R:                     router,
		AuthenticationService: authenticationService,
		ApiVersion:            cfg.ApiVersion,
	})

	return router, nil
}
