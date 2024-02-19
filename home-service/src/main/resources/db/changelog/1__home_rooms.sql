--liquibase formatted sql

--changeset m.bayzigitov:1 endDelimiter:/

create table homes
(
    id                      int not null generated always as identity,
    name                    text not null,
    address                 text,
    primary key (id)
);

create table rooms
(
    id                      int not null generated always as identity,
    home_id                 int not null references homes,
    name                    text not null,
    primary key (id)
);

