CREATE TABLE Notification
(
    id          BIGSERIAL PRIMARY KEY,
    user_id     BIGINT REFERENCES App_User (id),
    data_id     BIGINT       NULL,
    photo       VARCHAR(512) NULL,
    title       VARCHAR(512) NOT NULL,
    subtitle1   VARCHAR(512) NULL,
    subtitle2   VARCHAR(512) NULL,
    description VARCHAR(512) NOT NULL,
    unread      BOOLEAN      NOT NULL DEFAULT TRUE,
    created_at  timestamptz  NOT NULL
)