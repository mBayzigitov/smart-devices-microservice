--liquibase formatted sql

--changeset m.bayzigitov:2 endDelimiter:/

create table refresh_token
(
    id                      uuid,
    access_token_id         uuid not null,
    expires_at              TIMESTAMP,
    primary key (id)
);