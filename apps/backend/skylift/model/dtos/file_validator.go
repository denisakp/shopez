package dtos

import (
	"github.com/go-playground/validator/v10"
	"mime/multipart"
	"path/filepath"
	"reflect"
	"strings"
)

const uploadLimitSize = 8 * 1014 * 1024 // 8MB

var allowedExtensions = map[string]int{
	".pdf":  uploadLimitSize,
	".jpg":  uploadLimitSize,
	".jpeg": uploadLimitSize,
	".png":  uploadLimitSize,
}

var FileValidator validator.Func = func(fl validator.FieldLevel) bool {
	files, ok := fl.Field().Interface().([]*multipart.FileHeader)
	if !ok {
		return false
	}

	var validationErrors []string
	for _, file := range files {
		ext := strings.ToLower(filepath.Ext(file.Filename))
		maxSize, allowed := allowedExtensions[ext]

		if !allowed {
			validationErrors = append(validationErrors, file.Filename+": invalid file extension")
		} else if file.Size > int64(maxSize) {
			validationErrors = append(validationErrors, file.Filename+": file size exceeds the limit")
		}
	}

	if len(validationErrors) > 0 {
		fl.Parent().FieldByName("ValidationErrors").Set(reflect.ValueOf(validationErrors))
		return false
	}

	return true
}
