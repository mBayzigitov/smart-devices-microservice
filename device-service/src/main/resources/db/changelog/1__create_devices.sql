--liquibase formatted sql

--changeset m.bayzigitov:1 endDelimiter:/

create table devices
(
    id                      int generated always as identity,
    tuya_device_id          text not null,
    name                    text not null,
    category                text not null,
    primary key (id)
);

