CREATE TABLE Message
(
    id          BIGSERIAL PRIMARY KEY,
    sender_id   BIGINT REFERENCES App_User (id) NOT NULL,
    receiver_id BIGINT REFERENCES App_User (id) NOT NULL,
    description TEXT                            NOT NULL,
    file        VARCHAR(512),
    created_at  timestamptz                     NOT NULL DEFAULT NOW(),
    unread      BOOLEAN                         NOT NULL DEFAULT true
)