package mocks

import (
	"context"
	"github.com/Nerzal/gocloak/v13"
	"github.com/stretchr/testify/mock"
)

type MockAuthenticationService struct {
	mock.Mock
}

func (m *MockAuthenticationService) Signup(ctx context.Context, user gocloak.User, password, role string) (*gocloak.User, error) {
	ret := m.Called(ctx, user, password, role)

	var r0 *gocloak.User
	if ret.Get(0) != nil {
		r0 = ret.Get(0).(*gocloak.User)
	}

	var r1 error
	if ret.Get(1) != nil {
		r1 = ret.Get(1).(error)
	}

	return r0, r1
}
