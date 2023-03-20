package domain

import (
	"context"
	"gopkg.in/guregu/null.v4"
	"mime/multipart"
	"time"
)

type User struct {
	ID               int64       `json:"id"`
	Name             string      `json:"name"`
	Email            string      `json:"email"`
	Verified         bool        `json:"verified"`
	VerificationCode string      `json:"verificationCode"`
	Password         string      `json:"password"`
	Photo            null.String `json:"photo"`
	University       null.String `json:"university"`
	Faculty          null.String `json:"faculty"`
	StudyProgram     null.String `json:"studyProgram"`
	Department       null.String `json:"department"`
	Stream           null.String `json:"stream"`
	Batch            null.Int    `json:"batch"`
	Gender           null.String `json:"gender"`
	Age              null.Int    `json:"age"`
	Bio              null.String `json:"bio"`
	Achievements     []string    `json:"achievements"`
	Certifications   []string    `json:"certifications"`
	SKills           []string    `json:"skills"`
	Invitable        bool        `json:"invitable"`
	Location         null.String `json:"location"`
	TotalRating      float32     `json:"total_rating"`
	Votes            int32       `json:"votes"`
	Likes            int32       `json:"likes"`
	Friends          int32       `json:"friends"`
	UpdatedAt        time.Time   `json:"updatedAt"`
	CreatedAt        time.Time   `json:"createdAt"`
}

type UpdateUserRequest struct {
	ID             int64                 `json:"id"`
	Name           string                `json:"name"`
	Photo          *multipart.FileHeader `json:"photo"`
	University     string                `json:"university"`
	Faculty        string                `json:"faculty"`
	StudyProgram   string                `json:"studyProgram"`
	Department     string                `json:"department"`
	Stream         string                `json:"stream"`
	Batch          null.Int              `json:"batch"`
	Gender         string                `json:"gender"`
	Age            null.Int              `json:"age"`
	Bio            string                `json:"bio"`
	Achievements   []string              `json:"achievements"`
	Certifications []string              `json:"certifications"`
	SKills         []string              `json:"skills"`
	Invitable      bool                  `json:"invitable"`
	Location       null.String           `json:"location"`
}

type UserResponse struct {
	ID               int64       `json:"id"`
	Name             string      `json:"name"`
	Email            string      `json:"email"`
	Photo            null.String `json:"photo"`
	University       null.String `json:"university"`
	Faculty          null.String `json:"faculty"`
	Department       null.String `json:"department"`
	StudyProgram     null.String `json:"studyProgram"`
	Stream           null.String `json:"stream"`
	Batch            null.Int    `json:"batch"`
	Gender           null.String `json:"gender"`
	Age              null.Int    `json:"age"`
	Bio              null.String `json:"bio"`
	Achievements     []string    `json:"achievements"`
	Certifications   []string    `json:"certifications"`
	SKills           []string    `json:"skills"`
	Invitable        bool        `json:"invitable"`
	Location         null.String `json:"location"`
	FriendshipStatus null.String `json:"friendshipStatus"`
	Favorite         bool        `json:"favorite"`
	Rating           float32     `json:"rating"`
	Votes            int32       `json:"votes"`
	Likes            int32       `json:"likes"`
	Friends          int32       `json:"friends"`
	UpdatedAt        int64       `json:"updatedAt"`
	CreatedAt        int64       `json:"createdAt"`
}

type UserRepository interface {
	Create(ctx context.Context, user *User) error
	Get(ctx context.Context) ([]User, error)
	GetById(ctx context.Context, userId int64) (User, error)
	GetByEmail(ctx context.Context, email string) (User, error)
	Update(ctx context.Context, user *User) error
	IncrementFriends(ctx context.Context, userId int64) error
	DecrementFriends(ctx context.Context, userId int64) error
	IncrementLikes(ctx context.Context, userId int64) error
	DecrementLikes(ctx context.Context, userId int64) error
}

type UserUseCase interface {
	GetUser(ctx context.Context, userId int64) (UserResponse, error)
	GetUsers(ctx context.Context) ([]UserResponse, error)
	Update(ctx context.Context, req *UpdateUserRequest) (UserResponse, error)
}

func UserToUserResponse(user User) (res UserResponse) {
	res.ID = user.ID
	res.Name = user.Name
	res.Email = user.Email
	res.Photo = user.Photo
	res.University = user.University
	res.Faculty = user.Faculty
	res.Department = user.Department
	res.StudyProgram = user.StudyProgram
	res.Stream = user.Stream
	res.Batch = user.Batch
	res.Gender = user.Gender
	res.Age = user.Age
	res.Bio = user.Bio
	res.Achievements = user.Achievements
	res.Certifications = user.Certifications
	res.SKills = user.SKills
	res.Invitable = user.Invitable
	res.Location = user.Location
	res.Rating = user.TotalRating
	res.Votes = user.Votes
	res.Likes = user.Likes
	res.Friends = user.Friends
	res.UpdatedAt = user.UpdatedAt.UnixMilli()
	res.CreatedAt = user.CreatedAt.UnixMilli()
	return
}
