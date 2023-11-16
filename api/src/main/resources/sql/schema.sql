drop table if exists users;
drop table if exists images;

CREATE TABLE IF NOT EXISTS users
(
    id       uuid unique primary key DEFAULT gen_random_uuid() NOT NULL,
    username VARCHAR(20)                                       NOT NULL,
    email    VARCHAR(64)                                       NOT NULL,
    CONSTRAINT unique_user UNIQUE (username, email)
);

CREATE TABLE IF NOT EXISTS images
(
    id      uuid unique primary key DEFAULT gen_random_uuid() NOT NULL,
    user_id uuid                                              NOT NULL references users (id),
    path    text                                              NOT NULL
);