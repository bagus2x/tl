package repository

import (
	"bagus2x/temanlomba/domain"
	"context"
	"database/sql"
	sq "github.com/Masterminds/squirrel"
)

type competitionRepository struct {
	pq sq.StatementBuilderType
}

func NewPostgresCompetitionRepository(db *sql.DB) domain.CompetitionRepository {
	pq := sq.StatementBuilder.PlaceholderFormat(sq.Dollar).RunWith(db)
	return &competitionRepository{pq}
}

func (c *competitionRepository) Create(ctx context.Context, competition *domain.Competition) error {
	err := c.pq.
		Insert("Competition").
		Columns(
			"author_id",
			"poster",
			"title",
			"description",
			"theme",
			"city",
			"country",
			"deadline",
			"minimum_fee",
			"maximum_fee",
			"category",
			"organizer",
			"organizer_name",
			"url_link",
			"created_at",
			"updated_at",
		).
		Values(
			competition.AuthorId,
			competition.Poster,
			competition.Title,
			competition.Description,
			competition.Theme,
			competition.City,
			competition.Country,
			competition.Deadline,
			competition.MinimumFee,
			competition.MaximumFee,
			competition.Category,
			competition.Organizer,
			competition.OrganizerName,
			competition.UrlLink,
			competition.CreatedAt,
			competition.UpdatedAt,
		).
		Suffix("RETURNING id").
		QueryRowContext(ctx).
		Scan(&competition.ID)
	return err
}

func (c *competitionRepository) Get(ctx context.Context) ([]domain.Competition, error) {
	rows, err := c.pq.
		Select(
			"id",
			"author_id",
			"poster",
			"title",
			"description",
			"theme",
			"city",
			"country",
			"deadline",
			"minimum_fee",
			"maximum_fee",
			"category",
			"organizer",
			"organizer_name",
			"url_link",
			"created_at",
			"updated_at",
		).
		From("Competition").
		OrderByClause("created_at DESC").
		QueryContext(ctx)
	if err != nil {
		return nil, err
	}

	var competitions = make([]domain.Competition, 0)

	for rows.Next() {
		var competition domain.Competition

		if err := rows.Scan(
			&competition.ID,
			&competition.AuthorId,
			&competition.Poster,
			&competition.Title,
			&competition.Description,
			&competition.Theme,
			&competition.City,
			&competition.Country,
			&competition.Deadline,
			&competition.MinimumFee,
			&competition.MaximumFee,
			&competition.Category,
			&competition.Organizer,
			&competition.OrganizerName,
			&competition.UrlLink,
			&competition.CreatedAt,
			&competition.UpdatedAt,
		); err != nil {
			return nil, err
		}

		competitions = append(competitions, competition)
	}

	return competitions, err
}

func (c *competitionRepository) GetById(ctx context.Context, competitionId int64) (domain.Competition, error) {
	row := c.pq.
		Select(
			"id",
			"author_id",
			"poster",
			"title",
			"description",
			"theme",
			"city",
			"country",
			"deadline",
			"minimum_fee",
			"maximum_fee",
			"category",
			"organizer",
			"organizer_name",
			"url_link",
			"created_at",
			"updated_at",
		).
		From("Competition").
		Where(sq.Eq{"id": competitionId}).
		QueryRowContext(ctx)

	var competition domain.Competition

	if err := row.Scan(
		&competition.ID,
		&competition.AuthorId,
		&competition.Poster,
		&competition.Title,
		&competition.Description,
		&competition.Theme,
		&competition.City,
		&competition.Country,
		&competition.Deadline,
		&competition.MinimumFee,
		&competition.MaximumFee,
		&competition.Category,
		&competition.Organizer,
		&competition.OrganizerName,
		&competition.UrlLink,
		&competition.CreatedAt,
		&competition.UpdatedAt,
	); err != nil {
		return domain.Competition{}, err
	}

	return competition, nil
}
