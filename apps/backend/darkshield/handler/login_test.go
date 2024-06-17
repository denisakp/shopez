package handler

import (
	"github.com/gin-gonic/gin"
	"testing"
)

func TestLogin(t *testing.T) {
	gin.SetMode(gin.TestMode)

	t.Run("Email and password are required", func(t *testing.T) {
		t.Skip("Not implemented")
	})

	t.Run("Email must be valid", func(t *testing.T) {
		t.Skip("Not implemented")
	})

	t.Run("login succeed", func(t *testing.T) {
		t.Skip("Not implemented")
	})

	t.Run("login failed", func(t *testing.T) {
		t.Skip("Not implemented")
	})
}
