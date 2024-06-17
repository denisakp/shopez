package jwt

import (
	"github.com/golang-jwt/jwt/v5"
	"github.com/gookit/goutil"
	"strings"
)

type StructJwtHelper struct {
	claims       jwt.MapClaims
	realmRoles   []string
	accountRoles []string
	scopes       []string
}

func NewJwtHelper(c jwt.MapClaims) *StructJwtHelper {
	return &StructJwtHelper{
		claims:       c,
		realmRoles:   parseRealmRoles(c),
		accountRoles: parseAccountRoles(c),
		scopes:       parseScopes(c),
	}
}

func parseRealmRoles(claims jwt.MapClaims) []string {
	var realmRoles []string
	if realmAccess, ok := claims["realm_access"].(map[string]interface{}); ok {
		if roles, ok := realmAccess["roles"].([]interface{}); ok {
			realmRoles = make([]string, len(roles))
			for i, role := range roles {
				if roleStr, ok := role.(string); ok {
					realmRoles[i] = roleStr
				}
			}
		}
	}

	return realmRoles
}

func parseAccountRoles(claims jwt.MapClaims) []string {
	var accountRoles []string
	if resourceAccess, ok := claims["resource_access"].(map[string]interface{}); ok {
		if account, ok := resourceAccess["account"].(map[string]interface{}); ok {
			if roles, ok := account["roles"].([]interface{}); ok {
				accountRoles = make([]string, len(roles))
				for i, role := range roles {
					if roleStr, ok := role.(string); ok {
						accountRoles[i] = roleStr
					}
				}
			}
		}
	}

	return accountRoles
}

func parseScopes(claims jwt.MapClaims) []string {
	var scopes []string
	if scope, ok := claims["scope"].(string); ok {
		scopes = strings.Split(scope, " ")
	}
	return scopes
}

func (j *StructJwtHelper) GetUserId() (string, error) {
	return j.claims.GetSubject()
}

func (j *StructJwtHelper) TokenHasScope(scope string) bool {
	return goutil.Contains(j.scopes, scope)
}
