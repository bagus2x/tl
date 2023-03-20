package repository

import (
	"bagus2x/temanlomba/domain"
	"context"
	"database/sql"
	sq "github.com/Masterminds/squirrel"
)

type announcementRepository struct {
	pq sq.StatementBuilderType
}

func NewPostgresAnnouncementRepository(db *sql.DB) domain.AnnouncementRepository {
	pq := sq.StatementBuilder.PlaceholderFormat(sq.Dollar).RunWith(db)
	return &announcementRepository{pq}
}

func (a *announcementRepository) Create(ctx context.Context, announcement *domain.Announcement) error {
	err := a.pq.
		Insert("Announcement").
		Columns("author_id", "description", "file", "created_at", "updated_at").
		Values(
			announcement.AuthorId,
			announcement.Description,
			announcement.File,
			announcement.CreatedAt,
			announcement.UpdatedAt,
		).
		Suffix("RETURNING id").
		QueryRowContext(ctx).
		Scan(&announcement.ID)
	return err
}

func (a *announcementRepository) Get(ctx context.Context, authorId int64) ([]domain.Announcement, error) {
	query := a.pq.
		Select("id", "author_id", "description", "file", "created_at", "updated_at").
		From("Announcement")

	if authorId != 0 {
		query = query.Where(sq.Eq{"author_id": authorId})
	}

	rows, err := query.
		OrderByClause("created_at DESC").
		QueryContext(ctx)
	if err != nil {
		return nil, err
	}

	announcements := make([]domain.Announcement, 0)

	for rows.Next() {
		var announcement domain.Announcement

		err := rows.Scan(
			&announcement.ID,
			&announcement.AuthorId,
			&announcement.Description,
			&announcement.File,
			&announcement.CreatedAt,
			&announcement.UpdatedAt,
		)
		if err != nil {
			return nil, err
		}

		announcements = append(announcements, announcement)
	}

	return announcements, nil
}

func (a *announcementRepository) GetById(ctx context.Context, announcementId int64) (domain.Announcement, error) {
	var announcement domain.Announcement

	err := a.pq.
		Select("id", "author_id", "description", "file", "created_at", "updated_at").
		From("Announcement").
		Where(sq.Eq{"id": announcementId}).
		QueryRowContext(ctx).
		Scan(
			&announcement.ID,
			&announcement.AuthorId,
			&announcement.Description,
			&announcement.File,
			&announcement.CreatedAt,
			&announcement.UpdatedAt,
		)

	return domain.Announcement{}, err
}
