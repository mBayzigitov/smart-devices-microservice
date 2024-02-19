--liquibase formatted sql

--changeset m.bayzigitov:2 endDelimiter:/

alter table devices
    add column home_id int not null default 1