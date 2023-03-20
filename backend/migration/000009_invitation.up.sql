CREATE TABLE Invitation
(
    id          BIGSERIAL PRIMARY KEY,
    inviter_id  BIGINT REFERENCES App_User (id),
    invitee_id  BIGINT REFERENCES App_User (id),
    description TEXT         NOT NULL,
    file        VARCHAR(512) NULL,
    response    TEXT         NOT NULL,
    status      VARCHAR(50)  NOT NULL,
    created_at  timestamptz  NOT NULL,
    updated_at  timestamptz  NOT NULL
)