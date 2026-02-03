-- liquibase formatted sql

--changeset egrevs:20260203-create-status-enum
create type courier_status as enum(
    'ONLINE',
    'OFFLINE',
    'AVAILABLE',
    'BUSY'
);

--changeset egrevs:20260203-create-courier-table
create table couriers(
    id varchar(255) primary key,
    name varchar(20) not null,
    email varchar(75) not null,
    login varchar(75) not null,
    password varchar(75) not null,
    role varchar(255) not null ,
    status courier_status default 'ONLINE' not null ,
    created_at timestamp default current_timestamp not null,
    updated_at timestamp default current_timestamp not null
);

