package repository

import (
	"bagus2x/temanlomba/domain"
	"context"
	"database/sql"
	sq "github.com/Masterminds/squirrel"
	"github.com/pkg/errors"
)

type postgresFavUserRepository struct {
	pq sq.StatementBuilderType
}

func NewPostgresFavUserRepository(db *sql.DB) domain.FavUserRepository {
	pq := sq.StatementBuilder.PlaceholderFormat(sq.Dollar).RunWith(db)
	return &postgresFavUserRepository{pq}
}

func (p *postgresFavUserRepository) Create(ctx context.Context, favUser domain.FavUser) error {
	_, err := p.pq.
		Insert("Favorite_User").
		Columns("liked_id", "liker_id", "created_at").
		Values(favUser.LikedId, favUser.LikerId, favUser.CreatedAt).
		ExecContext(ctx)
	if err != nil {
		return err
	}

	return err
}

func (p *postgresFavUserRepository) GetByLikerId(ctx context.Context, userId int64) ([]domain.FavUser, error) {
	rows, err := p.pq.
		Select("liked_id", "liker_id", "created_at").
		From("Favorite_User").
		Where(sq.Eq{"liker_id": userId}).
		OrderByClause("created_at DESC").
		QueryContext(ctx)
	if err != nil {
		return nil, err
	}

	favorites := make([]domain.FavUser, 0)

	for rows.Next() {
		var fav domain.FavUser

		err := rows.Scan(&fav.LikedId, &fav.LikerId, &fav.CreatedAt)
		if err != nil {
			return nil, err
		}

		favorites = append(favorites, fav)
	}

	return favorites, nil
}

func (p *postgresFavUserRepository) GetByLikedIdAndLikerId(ctx context.Context, userId, competitionId int64) (domain.FavUser, error) {
	row := p.pq.
		Select("liked_id", "liker_id", "created_at").
		From("Favorite_User").
		Where(sq.Eq{"liked_id": userId, "liker_id": competitionId}).
		QueryRowContext(ctx)

	var fav domain.FavUser

	err := row.Scan(&fav.LikedId, &fav.LikerId, &fav.CreatedAt)
	if err != nil {
		return domain.FavUser{}, err
	}

	return fav, nil
}

func (p *postgresFavUserRepository) Delete(ctx context.Context, userId, competitionId int64) error {
	result, err := p.pq.
		Delete("Favorite_User").
		Where(sq.Eq{"liked_id": userId, "liker_id": competitionId}).
		ExecContext(ctx)
	if err != nil {
		return err
	}

	if affected, err := result.RowsAffected(); affected == 0 {
		if err != nil {
			return err
		}
		return errors.WithMessage(domain.ErrNotFound, "Cannot delete favorite user")
	}

	return nil
}
