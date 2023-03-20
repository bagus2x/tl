package repository

import (
	"bagus2x/temanlomba/domain"
	"context"
	"database/sql"
	sq "github.com/Masterminds/squirrel"
)

type friendshipPostgresRepository struct {
	pq sq.StatementBuilderType
}

func NewFriendshipPostgresRepository(db *sql.DB) domain.FriendshipRepository {
	pq := sq.StatementBuilder.PlaceholderFormat(sq.Dollar).RunWith(db)
	return &friendshipPostgresRepository{pq}
}

func (f *friendshipPostgresRepository) CreateFriendship(ctx context.Context, friendship domain.Friendship) error {
	if _, err := f.pq.
		Insert("Friendship").
		Columns("requester_id", "addressee_id", "accepted", "created_at").
		Values(friendship.RequesterId, friendship.AddresseeId, friendship.Accepted, friendship.CreatedAt).
		ExecContext(ctx); err != nil {
		return err
	}

	return nil
}

func (f *friendshipPostgresRepository) GetFriendship(ctx context.Context, requesterId, addresseeId int64) (domain.Friendship, error) {
	row := f.pq.
		Select("requester_id", "addressee_id", "accepted", "created_at").
		From("Friendship").
		Where(sq.Eq{"requester_id": requesterId, "addressee_id": addresseeId}).
		QueryRowContext(ctx)

	var friendship domain.Friendship

	if err := row.Scan(
		&friendship.RequesterId,
		&friendship.AddresseeId,
		&friendship.Accepted,
		&friendship.CreatedAt,
	); err != nil {
		return domain.Friendship{}, err
	}

	return friendship, nil
}

func (f *friendshipPostgresRepository) GetFriendships(ctx context.Context, userId int64, accepted bool) ([]domain.Friendship, error) {
	rows, err := f.pq.
		Select("requester_id, addressee_id, accepted, created_at").
		From("Friendship").
		Where(
			sq.And{
				sq.Eq{"accepted": accepted},
				sq.Or{
					sq.Eq{"requester_id": userId},
					sq.Eq{"addressee_id": userId},
				},
			},
		).
		OrderByClause("created_at DESC").
		QueryContext(ctx)
	if err != nil {
		return nil, err
	}

	friendships := make([]domain.Friendship, 0)

	for rows.Next() {
		var friendship domain.Friendship

		if err := rows.Scan(
			&friendship.RequesterId,
			&friendship.AddresseeId,
			&friendship.Accepted,
			&friendship.CreatedAt,
		); err != nil {
			return nil, err
		}

		friendships = append(friendships, friendship)
	}

	return friendships, nil
}

func (f *friendshipPostgresRepository) UpdateFriendship(ctx context.Context, friendship domain.Friendship) error {
	if _, err := f.pq.
		Update("Friendship").
		Set("accepted", friendship.Accepted).
		Where(sq.Eq{"requester_id": friendship.RequesterId, "addressee_id": friendship.AddresseeId}).
		ExecContext(ctx); err != nil {
		return err
	}

	return nil
}

func (f *friendshipPostgresRepository) DeleteFriendship(ctx context.Context, userId1, userId2 int64) error {
	if _, err := f.pq.
		Delete("Friendship").
		Where(
			sq.Or{
				sq.Eq{"requester_id": userId1, "addressee_id": userId2},
				sq.Eq{"requester_id": userId2, "addressee_id": userId1},
			},
		).
		ExecContext(ctx); err != nil {
		return err
	}

	return nil
}
