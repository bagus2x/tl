package repository

import (
	"bagus2x/temanlomba/domain"
	"context"
	"database/sql"
	sq "github.com/Masterminds/squirrel"
)

type postgresInvitationRepository struct {
	pq sq.StatementBuilderType
}

func NewInvitationPostgresRepository(db *sql.DB) domain.InvitationRepository {
	pq := sq.StatementBuilder.PlaceholderFormat(sq.Dollar).RunWith(db)
	return &postgresInvitationRepository{pq}
}

func (p *postgresInvitationRepository) Create(ctx context.Context, invitation *domain.Invitation) error {
	err := p.pq.
		Insert("Invitation").
		Columns(
			"inviter_id",
			"invitee_id",
			"description",
			"file",
			"response",
			"status",
			"created_at",
			"updated_at",
		).
		Values(
			invitation.InviterId,
			invitation.InviteeId,
			invitation.Description,
			invitation.File,
			invitation.Response,
			invitation.Status,
			invitation.CreatedAt,
			invitation.UpdatedAt,
		).
		Suffix("RETURNING id").
		QueryRowContext(ctx).
		Scan(&invitation.ID)
	return err
}

func (p *postgresInvitationRepository) GetById(ctx context.Context, invitationId int64) (domain.Invitation, error) {
	var invitation domain.Invitation
	err := p.pq.
		Select(
			"id",
			"inviter_id",
			"invitee_id",
			"description",
			"file",
			"response",
			"status",
			"created_at",
			"updated_at",
		).
		From("Invitation").
		Where(sq.Eq{"id": invitationId}).
		QueryRowContext(ctx).
		Scan(
			&invitation.ID,
			&invitation.InviterId,
			&invitation.InviteeId,
			&invitation.Description,
			&invitation.File,
			&invitation.Response,
			&invitation.Status,
			&invitation.CreatedAt,
			&invitation.UpdatedAt,
		)
	if err != nil {
		return domain.Invitation{}, err
	}

	return invitation, nil
}

func (p *postgresInvitationRepository) Update(ctx context.Context, invitation *domain.Invitation) error {
	_, err := p.pq.
		Update("Invitation").
		Set("inviter_id", invitation.InviterId).
		Set("invitee_id", invitation.InviteeId).
		Set("description", invitation.Description).
		Set("file", invitation.File).
		Set("response", invitation.Response).
		Set("status", invitation.Status).
		Set("created_at", invitation.CreatedAt).
		Set("updated_at", invitation.UpdatedAt).
		Where(sq.Eq{"id": invitation.ID}).
		ExecContext(ctx)
	return err
}
