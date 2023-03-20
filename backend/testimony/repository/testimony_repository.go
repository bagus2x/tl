package repository

import (
	"bagus2x/temanlomba/domain"
	"context"
	"database/sql"
	sq "github.com/Masterminds/squirrel"
)

type testimonyRepository struct {
	pq sq.StatementBuilderType
}

func NewPostgresTestimonyRepository(db *sql.DB) domain.TestimonyRepository {
	pq := sq.StatementBuilder.PlaceholderFormat(sq.Dollar).RunWith(db)
	return &testimonyRepository{pq}
}

func (c *testimonyRepository) Create(ctx context.Context, testimony *domain.Testimony) error {
	err := c.pq.
		Insert("Testimony").
		Columns(
			"receiver_id",
			"sender_id",
			"description",
			"rating",
			"created_at",
			"updated_at",
		).
		Values(
			testimony.ReceiverId,
			testimony.SenderId,
			testimony.Description,
			testimony.Rating,
			testimony.CreatedAt,
			testimony.UpdatedAt,
		).
		Suffix("RETURNING id").
		QueryRowContext(ctx).
		Scan(&testimony.ID)
	return err
}

func (c *testimonyRepository) GetBySenderId(ctx context.Context, senderId int64) ([]domain.Testimony, error) {
	rows, err := c.pq.
		Select(
			"id",
			"receiver_id",
			"sender_id",
			"description",
			"rating",
			"created_at",
			"updated_at",
		).
		From("Testimony").
		Where(sq.Eq{"sender_id": senderId}).
		OrderByClause("created_at DESC").
		QueryContext(ctx)
	if err != nil {
		return nil, err
	}

	var testimonies = make([]domain.Testimony, 0)

	for rows.Next() {
		var testimony domain.Testimony

		if err := rows.Scan(
			&testimony.ID,
			&testimony.ReceiverId,
			&testimony.SenderId,
			&testimony.Description,
			&testimony.Rating,
			&testimony.CreatedAt,
			&testimony.UpdatedAt,
		); err != nil {
			return nil, err
		}

		testimonies = append(testimonies, testimony)
	}

	return testimonies, err
}

func (c *testimonyRepository) GetByReceiverId(ctx context.Context, senderId int64) ([]domain.Testimony, error) {
	rows, err := c.pq.
		Select(
			"id",
			"receiver_id",
			"sender_id",
			"description",
			"rating",
			"created_at",
			"updated_at",
		).
		From("Testimony").
		Where(sq.Eq{"receiver_id": senderId}).
		OrderByClause("created_at DESC").
		QueryContext(ctx)
	if err != nil {
		return nil, err
	}

	var testimonies = make([]domain.Testimony, 0)

	for rows.Next() {
		var testimony domain.Testimony

		if err := rows.Scan(
			&testimony.ID,
			&testimony.ReceiverId,
			&testimony.SenderId,
			&testimony.Description,
			&testimony.Rating,
			&testimony.CreatedAt,
			&testimony.UpdatedAt,
		); err != nil {
			return nil, err
		}

		testimonies = append(testimonies, testimony)
	}

	return testimonies, err
}
