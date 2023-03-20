package app

import (
	announcementHttp "bagus2x/temanlomba/announcement/delivery/http"
	announcementRepo "bagus2x/temanlomba/announcement/repository"
	announcementUc "bagus2x/temanlomba/announcement/usecase"
	authHttp "bagus2x/temanlomba/auth/delivery/http"
	authRepo "bagus2x/temanlomba/auth/repository"
	authUc "bagus2x/temanlomba/auth/usecase"
	competitionHttp "bagus2x/temanlomba/competition/delivery/http"
	competitionRepo "bagus2x/temanlomba/competition/repository"
	competitionUc "bagus2x/temanlomba/competition/usecase"
	_ "bagus2x/temanlomba/docs"
	favCompetitionHttp "bagus2x/temanlomba/favcompetition/delivery/http"
	favCompetitionRepo "bagus2x/temanlomba/favcompetition/repository"
	favCompetitionUc "bagus2x/temanlomba/favcompetition/usecase"
	favUserHttp "bagus2x/temanlomba/favuser/delivery/http"
	favUserRepo "bagus2x/temanlomba/favuser/repository"
	favUserUc "bagus2x/temanlomba/favuser/usecase"
	friendshipHttp "bagus2x/temanlomba/friendship/delivery/http"
	friendshipRepo "bagus2x/temanlomba/friendship/repository"
	friendshipUc "bagus2x/temanlomba/friendship/usecase"
	invitationHttp "bagus2x/temanlomba/invitation/delivery/http"
	invitationRepo "bagus2x/temanlomba/invitation/repository"
	invitationUc "bagus2x/temanlomba/invitation/usecase"
	messageHttp "bagus2x/temanlomba/message/delivery/http"
	messageWs "bagus2x/temanlomba/message/delivery/ws"
	messageRepo "bagus2x/temanlomba/message/repository"
	messageUc "bagus2x/temanlomba/message/usecase"
	notificationHttp "bagus2x/temanlomba/notification/delivery/http"
	notificationRepo "bagus2x/temanlomba/notification/repository"
	notificationUc "bagus2x/temanlomba/notification/usecase"
	storageRepo "bagus2x/temanlomba/storage/repository"
	storageUc "bagus2x/temanlomba/storage/usecase"
	testimonyHttp "bagus2x/temanlomba/testimony/delivery/http"
	testimonyRepo "bagus2x/temanlomba/testimony/repository"
	testimonyUc "bagus2x/temanlomba/testimony/usecase"
	userHttp "bagus2x/temanlomba/user/delivery/http"
	userRepo "bagus2x/temanlomba/user/repository"
	userUc "bagus2x/temanlomba/user/usecase"
	"context"
	"database/sql"
	"fmt"
	"github.com/dgraph-io/badger/v3"
	"github.com/gofiber/fiber/v2"
	"github.com/gofiber/fiber/v2/middleware/logger"
	fiberRecover "github.com/gofiber/fiber/v2/middleware/recover"
	"github.com/gofiber/swagger"
	_ "github.com/lib/pq"
	"github.com/sirupsen/logrus"
	"time"
)

// Run app
// @title          Tl API
// @version        1.0
// @description    This is a sample swagger for Fiber
// @termsOfService http://swagger.io/terms/
// @contact.name   API Support
// @contact.email  fiber@swagger.io
// @license.name   Apache 2.0
// @license.url    http://www.apache.org/licenses/LICENSE-2.0.html
// @host           localhost:8080
// @BasePath       /
func Run() {
	logrus.SetReportCaller(true)

	cfg := initializeConfig()

	pgDB, err := initializePostgres(cfg.dbHost, cfg.dbPort, cfg.dbUser, cfg.dbPassword, cfg.dbName, cfg.dbSslMode)
	defer func(db *sql.DB) {
		err := db.Close()
		if err != nil {
			logrus.Fatalln(err)
		}
	}(pgDB)
	if err != nil {
		logrus.Fatalln(err)
	}

	err = pgDB.Ping()
	if err != nil {
		logrus.Fatalln(err)
	}

	firebaseApp := initializeFirebase()
	messagingClient, err := firebaseApp.Messaging(context.Background())
	if err != nil {
		logrus.Fatalln(err)
	}

	app := fiber.New(fiber.Config{ErrorHandler: errorHandler})

	// Global middleware
	app.Use(logger.New())
	app.Use(fiberRecover.New())

	// Utils
	app.Get("/test-fcm", testFirebaseMessaging(messagingClient))
	app.Get("/swagger/*", swagger.HandlerDefault)

	bgDB, err := openBadger()

	storageRepository := storageRepo.NewCloudinaryStorageRepository(cfg.cdName, cfg.cdApiKey, cfg.cdSecret)
	userRepository := userRepo.NewPostgresUserRepository(pgDB)
	testimonyRepository := testimonyRepo.NewPostgresTestimonyRepository(pgDB)
	favUserRepository := favUserRepo.NewPostgresFavUserRepository(pgDB)
	authRepository := authRepo.NewBadgerAuthRepository(bgDB)
	messageRepository := messageRepo.NewMessageRepository(pgDB)
	friendshipRepository := friendshipRepo.NewFriendshipPostgresRepository(pgDB)
	competitionRepository := competitionRepo.NewPostgresCompetitionRepository(pgDB)
	favCompetitionRepository := favCompetitionRepo.NewPostgresFavCompetitionRepository(pgDB)
	announcementRepository := announcementRepo.NewPostgresAnnouncementRepository(pgDB)
	invitationRepository := invitationRepo.NewInvitationPostgresRepository(pgDB)
	notificationRepository := notificationRepo.NewPostgresNotificationRepository(pgDB)

	storageUseCase := storageUc.NewStorageUseCase(storageRepository)
	userUseCase := userUc.NewUserUseCase(userRepository, friendshipRepository, favUserRepository, storageUseCase, 60*time.Second)
	favUserUseCase := favUserUc.NewFavUserUseCase(favUserRepository, userUseCase, userRepository)
	testimonyUseCase := testimonyUc.NewTestimonyUseCase(testimonyRepository, userRepository)
	authUseCase := authUc.NewAuthUseCase(
		userRepository,
		authRepository,
		60*time.Second,
		cfg.atKey,
		cfg.atLifetime,
		cfg.rtKey,
		cfg.rtLifetime,
		cfg.smtpHost,
		cfg.smtpPort,
		cfg.smtpSenderName,
		cfg.smtpAuthEmail,
		cfg.smtpAuthPassword,
	)
	messageUseCase := messageUc.NewMessageUseCase(messageRepository, userRepository, 60*time.Second)
	competitionUseCase := competitionUc.NewCompetitionUseCase(competitionRepository, userUseCase, storageUseCase, favCompetitionRepository)
	favCompetitionUseCase := favCompetitionUc.NewFavCompetitionUseCase(favCompetitionRepository, competitionUseCase, userRepository)
	announcementUseCase := announcementUc.NewAnnouncementUseCase(announcementRepository, userRepository, storageUseCase)
	notificationUseCase := notificationUc.NewNotificationUseCase(notificationRepository)
	invitationUseCase := invitationUc.NewInvitationUseCase(invitationRepository, storageUseCase, notificationUseCase, userRepository)
	friendshipUseCase := friendshipUc.NewFriendshipUseCase(friendshipRepository, userRepository, favUserRepository, notificationUseCase)

	authMiddleware := authHttp.NewAuthMiddleware(authUseCase)

	authHttp.NewAuthHandler(app, authUseCase, &authMiddleware)
	userHttp.NewUserHandler(app, userUseCase, &authMiddleware)
	testimonyHttp.NewTestimonyHandler(app, testimonyUseCase, &authMiddleware)
	favUserHttp.NewFavUserHandler(app, favUserUseCase, &authMiddleware)
	messageHttp.NewChatHandler(app, messageUseCase, &authMiddleware)
	messageWs.NewMessageHandler(app, messageUseCase, &authMiddleware)
	friendshipHttp.NewFriendshipHandler(app, friendshipUseCase, &authMiddleware)
	competitionHttp.NewCompetitionHandler(app, competitionUseCase, &authMiddleware)
	favCompetitionHttp.NewFavCompetitionHandler(app, favCompetitionUseCase, &authMiddleware)
	announcementHttp.NewAnnouncementHandler(app, announcementUseCase, &authMiddleware)
	invitationHttp.NewInvitationHandler(app, invitationUseCase, &authMiddleware)
	notificationHttp.NewNotificationHandler(app, notificationUseCase, &authMiddleware)

	// Default 404 handler if path is not found
	app.Use(error404handler)

	err = app.Listen(fmt.Sprintf(":%d", cfg.appPort))
	if err != nil {
		logrus.Fatalln(err)
	}
}

func initializePostgres(host string, port int, user string, password string, name string, sslMode string) (*sql.DB, error) {
	dataSource := fmt.Sprintf("host=%s port=%d user=%s password=%s dbname=%s sslmode=%s", host, port, user, password, name, sslMode)
	logrus.Println(dataSource)
	db, err := sql.Open("postgres", dataSource)
	if err != nil {
		return nil, err
	}

	return db, nil
}

func openBadger() (*badger.DB, error) {
	return badger.Open(badger.DefaultOptions("/tmp/badger"))
}
