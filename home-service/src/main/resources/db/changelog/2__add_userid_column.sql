--liquibase formatted sql

--changeset m.bayzigitov:2 endDelimiter:/

alter table homes
add column user_id int