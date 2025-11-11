--liquibase formatted sql

--changeset kode:20251111-create-users-table

create table USERS (
    id bigserial primary key ,
    name varchar(75) not null ,
    email varchar(255) not null ,
    login varchar(75) not null ,
    password varchar(255) not null ,
    role varchar(50) not null ,
    created_at timestamp not null default current_timestamp ,
    updated_at timestamp default current_timestamp
);


