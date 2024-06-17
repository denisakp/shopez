package handler

import (
	"github.com/gin-gonic/gin"
	"testing"
)

func TestSignup(t *testing.T) {
	gin.SetMode(gin.TestMode)

	t.Run("Email and password are required", func(t *testing.T) {
		t.Skip("Not implemented")
	})

	t.Run("Email must be valid", func(t *testing.T) {
		t.Skip("Not implemented")
	})

	t.Run("Email must be unique", func(t *testing.T) {
		t.Skip("Not implemented")
	})

	t.Run("Password must be at least 8 characters", func(t *testing.T) {
		t.Skip("Not implemented")
	})
}
