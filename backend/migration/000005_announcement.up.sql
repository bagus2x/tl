CREATE TABLE Announcement
(
    id          BIGSERIAL PRIMARY KEY,
    author_id   BIGINT       NOT NULL REFERENCES app_user (id),
    description TEXT         NOT NULL,
    file        VARCHAR(512) NULL,
    created_at  timestamptz  NOT NULL,
    updated_at  timestamptz  NOT NULL
)