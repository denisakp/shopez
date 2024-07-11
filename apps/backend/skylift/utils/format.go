package utils

import (
	"golang.org/x/text/runes"
	"golang.org/x/text/transform"
	"golang.org/x/text/unicode/norm"
	"log"
	"strings"
	"unicode"
)

// removeAccents removes accents from a string
func removeAccents(s string) (string, error) {
	t := transform.Chain(norm.NFD, runes.Remove(runes.In(unicode.Mn)), norm.NFC)
	rst, _, err := transform.String(t, s)
	if err != nil {
		return "", err
	}
	return rst, nil
}

// FormatFileName formats the filename by replacing special chars with underscores
func FormatFileName(filename string) string {
	// replace special chars with underscores
	filename, err := removeAccents(filename)
	if err != nil {
		log.Printf("unable to remove accents from filename: %v", err)
		return filename
	}

	// remove all special chars except letters, digits, dots, and underscores
	filename = strings.Map(func(r rune) rune {
		if unicode.IsLetter(r) || unicode.IsDigit(r) || r == '.' || r == '_' {
			return r
		}
		return '_'
	}, filename)

	return filename
}
