package model

import (
	"context"
	"mime/multipart"
)

type UploadService interface {
	UploadFile(ctx context.Context, item []*multipart.FileHeader) (interface{}, error)
}

type UploadRepository interface {
	S3FileUpload(ctx context.Context, item []*multipart.FileHeader) (interface{}, error)
}
