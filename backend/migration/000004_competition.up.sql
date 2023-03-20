CREATE TABLE Competition
(
    id             BIGSERIAL PRIMARY KEY,
    author_id      BIGINT REFERENCES app_user (id),
    poster         VARCHAR(512) NOT NULL,
    title          VARCHAR(512) NOT NULL,
    description    TEXT         NOT NULL,
    theme          VARCHAR(255) NOT NULL,
    city           VARCHAR(255) NOT NULL,
    country        VARCHAR(255) NOT NULL,
    deadline       timestamptz  NOT NULL,
    minimum_fee    BIGINT       NOT NULL,
    maximum_fee    BIGINT       NOT NULL,
    category       VARCHAR(255) NOT NULL,
    organizer      VARCHAR(255) NOT NULL,
    organizer_name VARCHAR(255) NOT NULL,
    url_link       VARCHAR(512) NOT NULL,
    created_at     timestamptz  NOT NULL,
    updated_at     timestamptz  NOT NULL
)