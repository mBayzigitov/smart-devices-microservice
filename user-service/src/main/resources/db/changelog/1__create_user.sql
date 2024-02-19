--liquibase formatted sql

--changeset m.bayzigitov:1 endDelimiter:/

create table users
(
    id                      int not null generated always as identity,
    username                text not null unique,
    password                text,
    primary key (id)
);