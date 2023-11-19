drop table if exists images cascade;
drop table if exists users cascade;

CREATE TABLE IF NOT EXISTS users
(
    id       uuid unique primary key DEFAULT gen_random_uuid() NOT NULL,
    username VARCHAR(20)                                       NOT NULL,
    email    VARCHAR(64)                                       NOT NULL,
    CONSTRAINT unique_user UNIQUE (username, email)
);

CREATE TABLE IF NOT EXISTS audit_logs
(
    id               uuid unique primary key DEFAULT gen_random_uuid() NOT NULL,
    user_id          uuid                                               NOT NULL references users (id),
    operation        text                                               NOT NULL,
    status           text                                               NOT NULL,
    message          text                                               NOT NULL,
    create_timestamp timestamp with time zone DEFAULT now()             NOT NULL
);

CREATE TABLE IF NOT EXISTS images
(
    id                uuid unique primary key  DEFAULT gen_random_uuid() NOT NULL,
    user_id           uuid                                               NOT NULL references users (id),
    original_image_id uuid,
    path              text                                               NOT NULL,
    status            text                                               NOT NULL,
    type              text                                               NOT NULL,
    file_size         bigint                                             NOT NULL,
    width             integer,
    height            integer,
    create_timestamp  timestamp with time zone DEFAULT now()             NOT NULL
);

CREATE TABLE IF NOT EXISTS operations
(
    id          uuid unique primary key DEFAULT gen_random_uuid() NOT NULL,
    name        text                                              NOT NULL,
    description text                                              NOT NULL
);

CREATE TABLE IF NOT EXISTS image_operations
(
    id               uuid unique primary key  DEFAULT gen_random_uuid() NOT NULL,
    image_id         uuid                                               NOT NULL references images (id),
    operation_id     uuid                                               NOT NULL references operations (id),
    batch_id         uuid                                               NOT NULL references operation_batches (id),
    parameters       jsonb                                              NOT NULL,
    result_path      text                                               NOT NULL,
    status           text                                               NOT NULL,
    error_message    text,
    create_timestamp timestamp with time zone DEFAULT now()             NOT NULL
);

CREATE TABLE IF NOT EXISTS operation_batches
(
    id               uuid unique primary key  DEFAULT gen_random_uuid() NOT NULL,
    status           text                                               NOT NULL,
    create_timestamp timestamp with time zone DEFAULT now()             NOT NULL
);