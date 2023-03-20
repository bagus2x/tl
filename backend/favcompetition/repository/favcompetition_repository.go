package repository

import (
	"bagus2x/temanlomba/domain"
	"context"
	"database/sql"
	sq "github.com/Masterminds/squirrel"
	"github.com/pkg/errors"
)

type postgresFavCompetitionRepository struct {
	pq sq.StatementBuilderType
}

func NewPostgresFavCompetitionRepository(db *sql.DB) domain.FavCompetitionRepository {
	pq := sq.StatementBuilder.PlaceholderFormat(sq.Dollar).RunWith(db)
	return &postgresFavCompetitionRepository{pq}
}

func (p *postgresFavCompetitionRepository) Create(ctx context.Context, favCompetition domain.FavCompetition) error {
	_, err := p.pq.
		Insert("Favorite_Competition").
		Columns("user_id", "competition_id", "created_at").
		Values(favCompetition.UserId, favCompetition.CompetitionId, favCompetition.CreatedAt).
		ExecContext(ctx)
	if err != nil {
		return err
	}

	return err
}

func (p *postgresFavCompetitionRepository) GetByUserId(ctx context.Context, userId int64) ([]domain.FavCompetition, error) {
	rows, err := p.pq.
		Select("user_id", "competition_id", "created_at").
		From("Favorite_Competition").
		Where(sq.Eq{"user_id": userId}).
		OrderByClause("created_at DESC").
		QueryContext(ctx)
	if err != nil {
		return nil, err
	}

	favorites := make([]domain.FavCompetition, 0)

	for rows.Next() {
		var fav domain.FavCompetition

		err := rows.Scan(&fav.UserId, &fav.CompetitionId, &fav.CreatedAt)
		if err != nil {
			return nil, err
		}

		favorites = append(favorites, fav)
	}

	return favorites, nil
}

func (p *postgresFavCompetitionRepository) GetByUserIdAndCompId(ctx context.Context, userId, competitionId int64) (domain.FavCompetition, error) {
	row := p.pq.
		Select("user_id", "competition_id", "created_at").
		From("Favorite_Competition").
		Where(sq.Eq{"user_id": userId, "competition_id": competitionId}).
		QueryRowContext(ctx)

	var fav domain.FavCompetition

	err := row.Scan(&fav.UserId, &fav.CompetitionId, &fav.CreatedAt)
	if err != nil {
		return domain.FavCompetition{}, err
	}

	return fav, nil
}

func (p *postgresFavCompetitionRepository) Delete(ctx context.Context, userId, competitionId int64) error {
	result, err := p.pq.
		Delete("Favorite_Competition").
		Where(sq.Eq{"user_id": userId, "competition_id": competitionId}).
		ExecContext(ctx)

	if affected, err := result.RowsAffected(); affected == 0 {
		if err != nil {
			return err
		}
		return errors.WithMessage(domain.ErrNotFound, "Cannot delete favorite competition")
	}

	return err
}
