package app

import (
	"os"
	"strconv"
	"time"
)

type config struct {
	// Application PORT
	appPort int
	// Access token key
	atKey string
	// Access token lifetime
	atLifetime time.Duration
	// Access token key
	rtKey string
	// Refresh token lifetime
	rtLifetime time.Duration
	// Cache size
	cacheSize int
	// Database host
	dbHost string
	// Database appPort
	dbPort int
	// Database name
	dbName string
	// Database user
	dbUser string
	// Database password
	dbPassword string
	// Database sslMode
	dbSslMode string
	// SMTP host
	smtpHost string
	// SMTP port
	smtpPort int
	// SMTP sender name
	smtpSenderName string
	// SMTP auth email
	smtpAuthEmail string
	// SMTP auth password
	smtpAuthPassword string
	cdName           string
	cdApiKey         string
	cdSecret         string
}

func initializeConfig() config {
	appPort, err := strconv.Atoi(os.Getenv("PORT"))
	if err != nil {
		panic(err)
	}

	atKey := os.Getenv("ACCESS_TOKEN_KEY")
	if len(atKey) == 0 {
		panic("Access token key must be filled")
	}

	atLifetimeMinutes, err := strconv.ParseInt(os.Getenv("ACCESS_TOKEN_LIFETIME_MINUTES"), 10, 64)
	if err != nil {
		panic(err)
	}

	rtKey := os.Getenv("REFRESH_TOKEN_KEY")
	if len(rtKey) == 0 {
		panic("Refresh token key must be filled")
	}

	rtLifetimeHours, err := strconv.ParseInt(os.Getenv("REFRESH_TOKEN_LIFETIME_HOURS"), 10, 64)
	if err != nil {
		panic(err)
	}

	cacheSize, err := strconv.Atoi(os.Getenv("CACHE_SIZE"))
	if err != nil {
		panic(err)
	}

	dbHost := os.Getenv("DB_HOST")
	if len(dbHost) == 0 {
		panic("Database host must be filled")
	}

	dbPort, err := strconv.Atoi(os.Getenv("DB_PORT"))
	if err != nil {
		panic(err)
	}

	dbName := os.Getenv("DB_NAME")
	if len(dbName) == 0 {
		panic("Database name must be filled")
	}

	dbUser := os.Getenv("DB_USERNAME")
	if len(dbUser) == 0 {
		panic("Database user must be filled")
	}

	dbPassword := os.Getenv("DB_PASSWORD")
	if len(dbPassword) == 0 {
		panic("Database password must be filled")
	}

	dbSslMode := os.Getenv("SSL_MODE")
	if len(dbSslMode) == 0 {
		panic("Database ssl mode must be filled")
	}

	smtpHost := os.Getenv("SMTP_HOST")
	if len(dbSslMode) == 0 {
		panic("Database ssl mode must be filled")
	}

	smtpPort, err := strconv.Atoi(os.Getenv("SMTP_PORT"))
	if err != nil {
		panic(err)
	}

	smtpSenderName := os.Getenv("SMTP_SENDER_NAME")
	if len(dbSslMode) == 0 {
		panic("Database ssl mode must be filled")
	}

	smtpAuthEmail := os.Getenv("SMTP_AUTH_EMAIL")
	if len(dbSslMode) == 0 {
		panic("Database ssl mode must be filled")
	}

	smtpAuthPassword := os.Getenv("SMTP_AUTH_PASSWORD")
	if len(dbSslMode) == 0 {
		panic("Database ssl mode must be filled")
	}
	cdName := os.Getenv("CLOUDINARY_CLOUD_NAME")
	if len(dbSslMode) == 0 {
		panic("CLOUDINARY_CLOUD_NAME ssl mode must be filled")
	}

	cdApiKey := os.Getenv("CLOUDINARY_API_KEY")
	if len(dbSslMode) == 0 {
		panic("CLOUDINARY_API_KEY ssl mode must be filled")
	}

	cdSecret := os.Getenv("CLOUDINARY_API_SECRET")
	if len(dbSslMode) == 0 {
		panic("CLOUDINARY_API_SECRET ssl mode must be filled")
	}

	return config{
		appPort:          appPort,
		atKey:            atKey,
		atLifetime:       time.Duration(atLifetimeMinutes) * time.Minute,
		rtKey:            rtKey,
		rtLifetime:       time.Duration(rtLifetimeHours) * time.Hour,
		cacheSize:        cacheSize * 1024 * 1024,
		dbHost:           dbHost,
		dbPort:           dbPort,
		dbName:           dbName,
		dbUser:           dbUser,
		dbPassword:       dbPassword,
		dbSslMode:        dbSslMode,
		smtpHost:         smtpHost,
		smtpPort:         smtpPort,
		smtpSenderName:   smtpSenderName,
		smtpAuthEmail:    smtpAuthEmail,
		smtpAuthPassword: smtpAuthPassword,
		cdName:           cdName,
		cdApiKey:         cdApiKey,
		cdSecret:         cdSecret,
	}
}
