package dtos

import (
	"mime/multipart"
)

type FileUploadReq struct {
	Items            []*multipart.FileHeader `form:"items" binding:"required,validfile"`
	ValidationErrors []string                `form:"-"`
}
