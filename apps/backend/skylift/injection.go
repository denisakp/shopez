package main

import (
	"github.com/gin-gonic/gin"
	"github.com/gin-gonic/gin/binding"
	"github.com/go-playground/validator/v10"
	"shopez/uploader/config"
	"shopez/uploader/handler"
	"shopez/uploader/model/dtos"
	"shopez/uploader/repository"
	"shopez/uploader/services"
)

func inject(cfg *config.UploaderConfig) (*gin.Engine, error) {

	// repository layer
	s3Repository := repository.NewUploadRepository(cfg.MinioConfig)

	// service layer
	uploadService := services.NewUploadService(&services.USConfig{
		UploadRepository: s3Repository,
	})

	router := gin.Default()
	router.MaxMultipartMemory = 8 << 20 // 8 MiB

	if v, ok := binding.Validator.Engine().(*validator.Validate); ok {
		err := v.RegisterValidation("validfile", dtos.FileValidator)
		if err != nil {
			return nil, err
		}
	}

	handler.NewHandler(&handler.Config{
		R:             router,
		UploadService: uploadService,
		ApiVersion:    cfg.ApiVersion,
	})

	return router, nil
}
