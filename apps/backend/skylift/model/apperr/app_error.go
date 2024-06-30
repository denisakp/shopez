package apperr

import (
	"errors"
	"fmt"
	"net/http"
)

type Type string

const (
	Authorization Type = "AUTHORIZATION"
	BadRequest    Type = "BAD_REQUEST"
	Internal      Type = "INTERNAL"
	NotFound      Type = "NOT_FOUND"
)

type Error struct {
	Type    Type   `json:"type"`
	Message string `json:"message"`
}

func (e *Error) Error() string {
	return e.Message
}

func (e *Error) Status() int {
	switch e.Type {
	case Authorization:
		return http.StatusUnauthorized
	case BadRequest:
		return http.StatusBadRequest
	case NotFound:
		return http.StatusNotFound
	case Internal:
		return http.StatusInternalServerError
	default:
		return http.StatusInternalServerError
	}
}

func Status(err error) int {
	var e *Error
	if errors.As(err, &e) {
		return e.Status()
	}
	return http.StatusInternalServerError
}

func NewNotFound(message string) *Error {
	return &Error{Type: NotFound, Message: fmt.Sprintf("not found: %v", message)}
}

func NewBadRequest(message string) *Error {
	return &Error{Type: BadRequest, Message: fmt.Sprintf("bad request: %v", message)}
}

func NewAuthorization(message string) *Error {
	return &Error{Type: Authorization, Message: message}
}

func NewInternal(message string) *Error {
	return &Error{Type: Internal, Message: message}
}
