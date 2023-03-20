CREATE TABLE Favorite_User
(
    liked_id        BIGINT      NOT NULL REFERENCES App_User (id),
    liker_id BIGINT      NOT NULL REFERENCES App_User (id),
    created_at     timestamptz NOT NULL,
    PRIMARY KEY(liked_id, liker_id)
)