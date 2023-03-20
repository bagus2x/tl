package repository

import (
	"bagus2x/temanlomba/domain"
	"context"
	"database/sql"
)

type messageRepository struct {
	db *sql.DB
}

func NewMessageRepository(db *sql.DB) domain.MessageRepository {
	return &messageRepository{db}
}

func (c *messageRepository) Insert(ctx context.Context, message *domain.Message) error {
	query := `
		INSERT INTO
			Message
			(sender_id, receiver_id, description, file, unread, created_at)
		VALUES 
		    ($1, $2, $3, $4, $5, $6)
		RETURNING
			id
	`
	err := c.db.QueryRowContext(
		ctx, query,
		message.SenderId,
		message.ReceiverId,
		message.Description,
		message.File,
		message.Unread,
		message.CreatedAt,
	).Scan(&message.ID)
	if err != nil {
		return err
	}

	return nil
}

func (c *messageRepository) Update(ctx context.Context, message *domain.Message) error {
	query := `
		UPDATE
			Message
		SET
			description = $1, file = $2, unread = $3
		WHERE
		    id = $4
	`
	_, err := c.db.ExecContext(
		ctx, query,
		message.Description,
		message.File,
		message.Unread,
		message.ID,
	)
	if err != nil {
		return err
	}

	return nil
}

func (c *messageRepository) GetByUsersId(ctx context.Context, userId1, userId2 int64) ([]domain.Message, error) {
	query := `
		SELECT 
			id, sender_id, receiver_id, description, file, unread, created_at
		FROM
			Message
		WHERE
			(sender_id = $1 AND receiver_id = $2) OR (receiver_id = $1 AND sender_id = $2) 
		ORDER BY
			 created_at
		DESC
	`

	rows, err := c.db.QueryContext(ctx, query, userId1, userId2)
	if err != nil {
		return []domain.Message{}, err
	}

	messages := make([]domain.Message, 0)

	for rows.Next() {
		var message domain.Message

		err := rows.Scan(&message.ID, &message.SenderId, &message.ReceiverId, &message.Description, &message.File, &message.Unread, &message.CreatedAt)

		messages = append(messages, message)

		if err != nil {
			return []domain.Message{}, err
		}
	}

	return messages, nil
}

func (c *messageRepository) TotalUnread(ctx context.Context, senderId, receiverId int64) (int, error) {
	query := `SELECT COUNT(*) FROM Message WHERE (sender_id = $1 AND receiver_id = $2 AND unread = true)`
	var total int
	err := c.db.QueryRowContext(ctx, query, senderId, receiverId).Scan(&total)
	return total, err
}

func (c *messageRepository) GetLastMessagesByUserId(ctx context.Context, userId int64) ([]domain.Message, error) {
	query := `
		SELECT 
			m1.id, m1.sender_id, m1.receiver_id, m1.description, m1.file, m1.unread, m1.created_at
		FROM
			Message m1
		WHERE
			m1.id = (
				SELECT
				    MAX(m2.id) 
				FROM 
				    Message m2 
				WHERE
				    (m2.sender_id = m1.sender_id AND m2.receiver_id = m1.receiver_id) OR 
				    (m2.sender_id = m1.receiver_id AND m2.receiver_id = m1.sender_id) 
			) AND
		    (m1.sender_id = $1 OR m1.receiver_id = $1)
		ORDER BY
			 m1.created_at
		DESC
	`

	rows, err := c.db.QueryContext(ctx, query, userId)
	if err != nil {
		return []domain.Message{}, err
	}

	messages := make([]domain.Message, 0)

	for rows.Next() {
		var message domain.Message

		err := rows.Scan(&message.ID, &message.SenderId, &message.ReceiverId, &message.Description, &message.File, &message.Unread, &message.CreatedAt)

		messages = append(messages, message)

		if err != nil {
			return []domain.Message{}, err
		}
	}

	return messages, nil
}
