package repository

import (
	"bagus2x/temanlomba/domain"
	"context"
	"database/sql"
	sq "github.com/Masterminds/squirrel"
	"github.com/sirupsen/logrus"
)

type notificationRepository struct {
	pq sq.StatementBuilderType
}

func NewPostgresNotificationRepository(db *sql.DB) domain.NotificationRepository {
	pq := sq.StatementBuilder.PlaceholderFormat(sq.Dollar).RunWith(db)
	return &notificationRepository{pq}
}

func (n *notificationRepository) Create(ctx context.Context, notification *domain.Notification) error {
	err := n.pq.
		Insert("Notification").
		Columns("user_id", "data_id", "photo", "title", "subtitle1", "subtitle2", "description", "unread", "created_at").
		Values(
			notification.UserId,
			notification.DataId,
			notification.Photo,
			notification.Title,
			notification.Subtitle1,
			notification.Subtitle2,
			notification.Description,
			notification.Unread,
			notification.CreatedAt,
		).
		Suffix("RETURNING id").
		QueryRowContext(ctx).
		Scan(&notification.ID)
	return err
}

func (n *notificationRepository) GetByUserId(ctx context.Context, userId int64) ([]domain.Notification, error) {
	rows, err := n.pq.
		Select("id", "user_id", "data_id", "photo", "title", "subtitle1", "subtitle2", "description", "unread", "created_at").
		From("Notification").
		Where(sq.Eq{"user_id": userId}).
		OrderByClause("created_at DESC").
		QueryContext(ctx)
	if err != nil {
		return nil, err
	}

	notifications := make([]domain.Notification, 0)

	for rows.Next() {
		var notification domain.Notification

		err := rows.Scan(
			&notification.ID,
			&notification.UserId,
			&notification.DataId,
			&notification.Photo,
			&notification.Title,
			&notification.Subtitle1,
			&notification.Subtitle2,
			&notification.Description,
			&notification.Unread,
			&notification.CreatedAt,
		)
		if err != nil {
			return nil, err
		}

		notifications = append(notifications, notification)
	}

	return notifications, nil
}

func (n *notificationRepository) Update(ctx context.Context, notification *domain.Notification) error {
	affected, err := n.pq.
		Update("Notification").
		Set("user_id", notification.UserId).
		Set("data_id", notification.DataId).
		Set("photo", notification.Photo).
		Set("title", notification.Title).
		Set("subtitle1", notification.Subtitle1).
		Set("subtitle2", notification.Subtitle2).
		Set("description", notification.Description).
		Set("unread", notification.Unread).
		Where(sq.Eq{"id": notification.ID}).
		ExecContext(ctx)
	rowsAffected, _ := affected.RowsAffected()
	logrus.Info("Rows affected ", rowsAffected)
	if err != nil {
		return err
	}

	return nil
}

func (n *notificationRepository) CountUnread(ctx context.Context, userId int64) (int, error) {
	var counter = 0
	err := n.pq.
		Select("COUNT(*)").
		From("Notification").
		Where(sq.Eq{"user_id": userId, "unread": true}).
		QueryRowContext(ctx).
		Scan(&counter)
	if err != nil {
		return 0, err
	}

	return counter, nil
}
