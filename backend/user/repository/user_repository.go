package repository

import (
	"bagus2x/temanlomba/domain"
	"context"
	"database/sql"
	sq "github.com/Masterminds/squirrel"
	"github.com/lib/pq"
)

type postgresUserRepository struct {
	pq sq.StatementBuilderType
}

func NewPostgresUserRepository(db *sql.DB) domain.UserRepository {
	st := sq.StatementBuilder.PlaceholderFormat(sq.Dollar).RunWith(db)
	return &postgresUserRepository{st}
}

func (p *postgresUserRepository) Create(ctx context.Context, user *domain.User) error {
	user.Certifications = make([]string, 0) // Default value
	user.Achievements = make([]string, 0)
	user.SKills = make([]string, 0)

	err := p.pq.
		Insert("App_User").
		Columns(
			"name",
			"email",
			"verified",
			"verification_code",
			"password",
			"photo",
			"university",
			"faculty",
			"studyProgram",
			"department",
			"stream",
			"batch",
			"gender",
			"age",
			"bio",
			"achievements",
			"certifications",
			"skills",
			"invitable",
			"location",
			"total_rating",
			"votes",
			"likes",
			"friends",
			"created_at",
			"updated_at",
		).Values(
		user.Name,
		user.Email,
		user.Verified,
		user.VerificationCode,
		user.Password,
		user.Photo,
		user.University,
		user.Faculty,
		user.StudyProgram,
		user.Department,
		user.Stream,
		user.Batch,
		user.Gender,
		user.Age,
		user.Bio,
		pq.Array(user.Achievements),
		pq.Array(user.Certifications),
		pq.Array(user.SKills),
		user.Invitable,
		user.Location,
		user.TotalRating,
		user.Votes,
		user.Likes,
		user.Friends,
		user.CreatedAt,
		user.UpdatedAt,
	).
		Suffix("RETURNING id").
		QueryRowContext(ctx).Scan(&user.ID)
	if err != nil {
		return err
	}

	return nil
}

func (p *postgresUserRepository) Get(ctx context.Context) ([]domain.User, error) {
	rows, err := p.pq.
		Select(
			"id",
			"name",
			"email",
			"verified",
			"verification_code",
			"password",
			"photo",
			"university",
			"faculty",
			"studyProgram",
			"department",
			"stream",
			"batch",
			"gender",
			"age",
			"bio",
			"achievements",
			"certifications",
			"skills",
			"invitable",
			"location",
			"total_rating",
			"votes",
			"likes",
			"friends",
			"created_at",
			"updated_at",
		).
		From("App_User").
		OrderByClause("created_at DESC").
		QueryContext(ctx)
	if err != nil {
		return nil, err
	}

	users := make([]domain.User, 0)
	for rows.Next() {
		var user domain.User

		achievements := make([]string, 0)
		certifications := make([]string, 0)
		skills := make([]string, 0)

		err := rows.Scan(
			&user.ID,
			&user.Name,
			&user.Email,
			&user.Verified,
			&user.VerificationCode,
			&user.Password,
			&user.Photo,
			&user.University,
			&user.Faculty,
			&user.StudyProgram,
			&user.Department,
			&user.Stream,
			&user.Batch,
			&user.Gender,
			&user.Age,
			&user.Bio,
			pq.Array(&achievements),
			pq.Array(&certifications),
			pq.Array(&skills),
			&user.Invitable,
			&user.Location,
			&user.TotalRating,
			&user.Votes,
			&user.Likes,
			&user.Friends,
			&user.CreatedAt,
			&user.UpdatedAt,
		)
		if err != nil {
			return nil, err
		}

		user.Achievements = achievements
		user.Certifications = certifications
		user.SKills = skills

		users = append(users, user)
	}

	return users, nil
}

func (p *postgresUserRepository) GetById(ctx context.Context, userId int64) (domain.User, error) {
	var user domain.User

	achievements := make([]string, 0)
	certifications := make([]string, 0)
	skills := make([]string, 0)
	err := p.pq.
		Select(
			"id",
			"name",
			"email",
			"verified",
			"verification_code",
			"password",
			"photo",
			"university",
			"faculty",
			"studyProgram",
			"department",
			"stream",
			"batch",
			"gender",
			"age",
			"bio",
			"achievements",
			"certifications",
			"skills",
			"invitable",
			"location",
			"total_rating",
			"votes",
			"likes",
			"friends",
			"created_at",
			"updated_at",
		).
		From("App_User").
		Where(sq.Eq{"id": userId}).
		QueryRowContext(ctx).
		Scan(
			&user.ID,
			&user.Name,
			&user.Email,
			&user.Verified,
			&user.VerificationCode,
			&user.Password,
			&user.Photo,
			&user.University,
			&user.Faculty,
			&user.StudyProgram,
			&user.Department,
			&user.Stream,
			&user.Batch,
			&user.Gender,
			&user.Age,
			&user.Bio,
			pq.Array(&achievements),
			pq.Array(&certifications),
			pq.Array(&skills),
			&user.Invitable,
			&user.Location,
			&user.TotalRating,
			&user.Votes,
			&user.Likes,
			&user.Friends,
			&user.CreatedAt,
			&user.UpdatedAt,
		)
	if err != nil {
		return domain.User{}, err
	}

	user.Certifications = certifications
	user.Achievements = achievements
	user.SKills = skills

	return user, nil
}

func (p *postgresUserRepository) GetByEmail(ctx context.Context, email string) (domain.User, error) {
	var user domain.User

	achievements := make([]string, 0)
	certifications := make([]string, 0)
	skills := make([]string, 0)
	err := p.pq.
		Select(
			"id",
			"name",
			"email",
			"verified",
			"verification_code",
			"password",
			"photo",
			"university",
			"faculty",
			"studyProgram",
			"department",
			"stream",
			"batch",
			"gender",
			"age",
			"bio",
			"achievements",
			"certifications",
			"skills",
			"invitable",
			"location",
			"total_rating",
			"votes",
			"likes",
			"friends",
			"created_at",
			"updated_at",
		).
		From("App_User").
		Where(sq.Eq{"email": email}).
		QueryRowContext(ctx).
		Scan(
			&user.ID,
			&user.Name,
			&user.Email,
			&user.Verified,
			&user.VerificationCode,
			&user.Password,
			&user.Photo,
			&user.University,
			&user.Faculty,
			&user.StudyProgram,
			&user.Department,
			&user.Stream,
			&user.Batch,
			&user.Gender,
			&user.Age,
			&user.Bio,
			pq.Array(&achievements),
			pq.Array(&certifications),
			pq.Array(&skills),
			&user.Invitable,
			&user.Location,
			&user.TotalRating,
			&user.Votes,
			&user.Likes,
			&user.Friends,
			&user.CreatedAt,
			&user.UpdatedAt,
		)
	if err != nil {
		return domain.User{}, err
	}

	user.Certifications = certifications
	user.Achievements = achievements
	user.SKills = skills

	return user, nil
}

func (p *postgresUserRepository) Update(ctx context.Context, user *domain.User) error {
	_, err := p.pq.
		Update("App_User").
		Set("name", user.Name).
		Set("email", user.Email).
		Set("verified", user.Verified).
		Set("verification_code", user.VerificationCode).
		Set("password", user.Password).
		Set("photo", user.Photo).
		Set("university", user.University).
		Set("faculty", user.Faculty).
		Set("studyProgram", user.StudyProgram).
		Set("department", user.Department).
		Set("stream", user.Stream).
		Set("batch", user.Batch).
		Set("gender", user.Gender).
		Set("age", user.Age).
		Set("bio", user.Bio).
		Set("achievements", pq.Array(user.Achievements)).
		Set("certifications", pq.Array(user.Certifications)).
		Set("skills", pq.Array(user.SKills)).
		Set("invitable", user.Invitable).
		Set("location", user.Location).
		Set("total_rating", user.TotalRating).
		Set("votes", user.Votes).
		Set("likes", user.Likes).
		Set("friends", user.Friends).
		Set("created_at", user.CreatedAt).
		Set("updated_at", user.UpdatedAt).
		Where(sq.Eq{"id": user.ID}).
		ExecContext(ctx)
	if err != nil {
		return err
	}

	return nil
}

func (p *postgresUserRepository) DecrementFriends(ctx context.Context, userId int64) error {
	_, err := p.pq.
		Update("App_User").
		Set("friends", sq.Expr("friends - 1")).
		Where(sq.Eq{"id": userId}).
		ExecContext(ctx)
	if err != nil {
		return err
	}

	return nil
}

func (p *postgresUserRepository) IncrementLikes(ctx context.Context, userId int64) error {
	_, err := p.pq.
		Update("App_User").
		Set("likes", sq.Expr("likes + 1")).
		Where(sq.Eq{"id": userId}).
		ExecContext(ctx)
	if err != nil {
		return err
	}

	return nil
}

func (p *postgresUserRepository) DecrementLikes(ctx context.Context, userId int64) error {
	_, err := p.pq.
		Update("App_User").
		Set("likes", sq.Expr("likes - 1")).
		Where(sq.Eq{"id": userId}).
		ExecContext(ctx)
	if err != nil {
		return err
	}

	return nil
}

func (p *postgresUserRepository) IncrementFriends(ctx context.Context, userId int64) error {
	_, err := p.pq.
		Update("App_User").
		Set("friends", sq.Expr("friends + 1")).
		Where(sq.Eq{"id": userId}).
		ExecContext(ctx)
	if err != nil {
		return err
	}

	return nil
}
