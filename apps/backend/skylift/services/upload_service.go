package services

import (
	"context"
	"mime/multipart"
	"shopez/skylift/model"
	"shopez/skylift/model/apperr"
)

// uploadService implements the model.UploadService interface
type uploadService struct {
	UploadRepository model.UploadRepository
}

// USConfig holds the configuration for the upload service
type USConfig struct {
	UploadRepository model.UploadRepository
}

// NewUploadService creates a new upload service
func NewUploadService(cfg *USConfig) model.UploadService {
	return &uploadService{
		UploadRepository: cfg.UploadRepository,
	}
}

// UploadFile uploads a file to the S3 bucket
func (us *uploadService) UploadFile(ctx context.Context, items []*multipart.FileHeader) (interface{}, error) {
	objectFilePath, err := us.UploadRepository.S3FileUpload(ctx, items)

	if err != nil {
		return "", apperr.NewInternal("failed to upload file")
	}

	return objectFilePath, nil
}
