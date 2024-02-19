--liquibase formatted sql

--changeset m.bayzigitov:4 endDelimiter:/

create table outbox_messages
(
    id    int  not null generated always as identity,
    topic text not null,
    data  text,
    primary key (id)
);