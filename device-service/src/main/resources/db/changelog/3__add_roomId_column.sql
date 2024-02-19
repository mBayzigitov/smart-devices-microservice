--liquibase formatted sql

--changeset m.bayzigitov:3 endDelimiter:/

alter table devices
    add column room_id int