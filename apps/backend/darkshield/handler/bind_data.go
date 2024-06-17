package handler

import (
	"errors"
	"fmt"
	"github.com/gin-gonic/gin"
	"github.com/go-playground/validator/v10"
	"shopez/darkshield/model/apperr"
)

type invalidArgument struct {
	Field string `json:"field"`
	Value string `json:"value"`
	Tag   string `json:"tag"`
	Param string `json:"param"`
}

func bindData(c *gin.Context, req interface{}) bool {
	if c.ContentType() != "application/json" {
		msg := fmt.Sprintf("%s only accepts Content-Type application/json", c.FullPath())
		err := apperr.NewBadRequest(msg)

		c.JSON(err.Status(), gin.H{"error": err})
		return false
	}

	if err := c.ShouldBind(req); err != nil {
		var errs validator.ValidationErrors
		if errors.As(err, &errs) {
			var invalidArgs []invalidArgument

			for _, err := range errs {
				invalidArgs = append(invalidArgs, invalidArgument{
					err.Field(),
					err.Value().(string),
					err.Tag(),
					err.Param(),
				})
			}
			err := apperr.NewBadRequest("invalid request parameters")
			c.JSON(err.Status(), gin.H{"error": err, "invalid_args": invalidArgs})
			return false
		}
		fallback := apperr.NewInternal()

		c.JSON(fallback.Status(), gin.H{"error": fallback})
	}
	return true
}
