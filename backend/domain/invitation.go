package domain

import (
	"context"
	"gopkg.in/guregu/null.v4"
	"mime/multipart"
	"time"
)

type Invitation struct {
	ID          int64
	InviterId   int64
	InviteeId   int64
	Description string
	File        null.String
	Response    string
	Status      InvitationStatus
	CreatedAt   time.Time
	UpdatedAt   time.Time
}

type InvitationResponse struct {
	ID          int64            `json:"id"`
	Inviter     UserResponse     `json:"inviter"`
	Invitee     UserResponse     `json:"invitee"`
	Description string           `json:"description"`
	File        null.String      `json:"file"`
	Response    string           `json:"response"`
	Status      InvitationStatus `json:"status"`
	CreatedAt   int64            `json:"createdAt"`
}

type InvitationStatus string

func (i InvitationStatus) Valid() bool {
	return i == InvitationStatusRequested || i == InvitationStatusAccepted || i == InvitationStatusDeclined
}

const (
	InvitationStatusRequested = InvitationStatus("invitation_requested")
	InvitationStatusAccepted  = InvitationStatus("invitation_accepted")
	InvitationStatusDeclined  = InvitationStatus("invitation_declined")
)

type InvitationRepository interface {
	Create(ctx context.Context, invitation *Invitation) error
	GetById(ctx context.Context, invitationId int64) (Invitation, error)
	Update(ctx context.Context, invitation *Invitation) error
}

type InvitationUseCase interface {
	Invite(ctx context.Context, req *InviteRequest) error
	Respond(ctx context.Context, req *RespondRequest) error
	GetInvitation(ctx context.Context, invitationId int64) (InvitationResponse, error)
}

type InviteRequest struct {
	InviterId   int64                 `json:"inviterId"`
	InviteeId   int64                 `json:"inviteeId"`
	Description string                `json:"description"`
	File        *multipart.FileHeader `json:"file"`
}

type RespondRequest struct {
	ID        int64            `json:"ID"`
	InviteeId int64            `json:"inviteeId"`
	Response  string           `json:"response"`
	Status    InvitationStatus `json:"status"`
}
