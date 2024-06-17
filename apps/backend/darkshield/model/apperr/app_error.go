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
	Conflict      Type = "CONFLICT" // used for resource duplicate entry
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
	case Internal:
		return http.StatusInternalServerError
	case Conflict:
		return http.StatusConflict
	case NotFound:
		return http.StatusNotFound
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

func NewBadRequest(message string) *Error {
	return &Error{Type: BadRequest, Message: fmt.Sprintf("bad request: %v", message)}
}

func NewAuthorization(message string) *Error {
	return &Error{Type: Authorization, Message: message}
}

func NewInternal() *Error {
	return &Error{Type: Internal, Message: fmt.Sprintf("internal server error")}
}

func NewConflict(resource, value string) *Error {
	return &Error{Type: Conflict, Message: fmt.Sprintf("resource: %v with value: %v already exists", resource, value)}
}

func NewNotFound(resource, value string) *Error {
	return &Error{Type: NotFound, Message: fmt.Sprintf("resource: %v with value: %v not found", resource, value)}
}
