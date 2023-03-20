package ws

import (
	authHttp "bagus2x/temanlomba/auth/delivery/http"
	"bagus2x/temanlomba/domain"
	"context"
	"encoding/json"
	"fmt"
	"github.com/gofiber/fiber/v2"
	"github.com/gofiber/websocket/v2"
	"github.com/sirupsen/logrus"
	"log"
	"strconv"
	"time"
)

type messageHandler struct {
	messageUseCase domain.MessageUseCase
	authMiddleware *authHttp.AuthMiddleware
}

type client struct {
	*websocket.Conn
	userId int64
	roomId string
}

type message struct {
	roomId string
	body   []byte
}

var clients = make(map[*client]struct{})
var register = make(chan *client)
var unregister = make(chan *client)
var broadcaster = make(chan message)

func NewMessageHandler(router fiber.Router, messageUseCase domain.MessageUseCase, authMiddleware *authHttp.AuthMiddleware) {
	go observe()

	h := messageHandler{messageUseCase, authMiddleware}

	router.Get("/message/monitor", h.monitor)
	router.Get("/message/chat/:receiverId", h.authMiddleware.Authenticate, upgradeToWs, websocket.New(h.join))
}

func (h *messageHandler) join(c *websocket.Conn) {
	senderId, _ := c.Locals("userId").(int64)
	receiverId, _ := strconv.ParseInt(c.Params("receiverId"), 10, 64)
	roomId := generateKey(senderId, receiverId)

	client := &client{
		Conn:   c,
		userId: senderId,
		roomId: roomId,
	}

	defer func() {
		unregister <- client
		err := c.Close()
		if err != nil {
			logrus.Error(err)
			return
		}
	}()

	register <- client

	for {
		messageType, body, err := c.ReadMessage()
		if err != nil {
			if websocket.IsUnexpectedCloseError(err, websocket.CloseGoingAway, websocket.CloseAbnormalClosure) {
				logrus.Error("read error:", err)
			}
			return
		}

		var req domain.SendMessageRequest

		err = json.Unmarshal(body, &req)
		if err != nil {
			logrus.Error(err)
			continue
		}

		req.SenderId = senderId
		req.ReceiverId = receiverId
		req.Unread = isUnread(receiverId)

		res, err := h.messageUseCase.Send(context.Background(), &req)
		if err != nil {
			logrus.Error(err)
			continue
		}

		resBody, err := json.Marshal(res)
		if err != nil {
			continue
		}

		if messageType == websocket.TextMessage {
			// Broadcast the received body
			broadcaster <- message{
				roomId: client.roomId,
				body:   resBody,
			}
		} else {
			log.Println("websocket body received of type", messageType)
		}
	}
}

func isUnread(receiverId int64) bool {
	for client := range clients {
		if client.userId == receiverId {
			return false
		}
	}
	return true
}

func observe() {
	for {
		select {
		case client := <-register:
			add(client)
			logrus.Info("client registered")

		case message := <-broadcaster:
			broadcast(message)
			logrus.Info("message received: ", string(message.body))

		case client := <-unregister:
			remove(client)
			logrus.Info("client unregistered")
		}
	}
}

func add(client *client) {
	if client != nil {
		clients[client] = struct{}{}
	}

}

func remove(client *client) {
	delete(clients, client)
}

func broadcast(message message) {
	for client := range clients {
		if client.roomId == message.roomId {
			if err := client.WriteMessage(websocket.TextMessage, message.body); err != nil {
				logrus.Error(err)

				if err := client.WriteMessage(websocket.CloseMessage, []byte{}); err != nil {
					logrus.Error(err)
					return
				}

				if err := client.Close(); err != nil {
					logrus.Error(err)
					return
				}

				delete(clients, client)
				return
			}
		}
	}
}

func generateKey(userId1, userId2 int64) string {
	var key string

	if userId1 < userId2 {
		key = fmt.Sprintf("%d-%d", userId1, userId2)
	} else {
		key = fmt.Sprintf("%d-%d", userId2, userId1)
	}

	return key
}

func (_ *messageHandler) monitor(c *fiber.Ctx) error {
	str := fmt.Sprintf(`Date: %s | Number of active clients: %d`, time.Now().String(), len(clients))
	for key := range clients {
		fmt.Println(key.roomId)
	}
	return c.SendString(str)
}
