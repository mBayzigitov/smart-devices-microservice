--liquibase formatted sql

--changeset m.bayzigitov:3 endDelimiter:/

alter table users
add column name varchar(40)