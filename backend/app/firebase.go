package app

import (
	"context"
	firebase "firebase.google.com/go/v4"
	"firebase.google.com/go/v4/messaging"
	"github.com/gofiber/fiber/v2"
	"google.golang.org/api/option"
	"log"
)

func initializeFirebase() *firebase.App {
	opt := option.WithCredentialsFile("firebase_service_account.json")
	app, err := firebase.NewApp(context.Background(), nil, opt)
	if err != nil {
		log.Fatalf("error initializing app: %v\n", err)
	}

	return app
}

func testFirebaseMessaging(client *messaging.Client) func(c *fiber.Ctx) error {
	return func(c *fiber.Ctx) error {
		_, err := client.Send(
			context.Background(),
			&messaging.Message{
				Data: map[string]string{
					"sender":  c.Query("sender"),
					"message": c.Query("message"),
				},
				Notification: &messaging.Notification{
					Title: "You have message from " + c.Query("sender"),
					Body:  c.Query("message"),
				},
				Token: "c8wPOqw3T2OZm-N1AxyXD6:APA91bGJwk9jFsitg5B5CbBwyBX72v45T4-JnUVS6NAA54mkJPlYCV2fzcdqJhFGph7xs6qu0dzZEycl1WnhtUjay48ryXW5J3bjhnW4zrbv5_8blo6I8db1U8jKnFuTMcodIALRP9IK",
			},
		)
		return err
	}
}
