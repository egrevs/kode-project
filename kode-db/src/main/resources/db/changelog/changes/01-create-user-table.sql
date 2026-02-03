-- liquibase formatted sql

--changeset egrevs:20260203-create-user-table
create table users (
    id varchar(255) primary key ,
    name varchar(20) not null,
    email varchar(75) not null ,
    login varchar(75) not null ,
    password varchar(75) not null ,
    created_at timestamp default current_timestamp not null ,
    updated_at timestamp default current_timestamp not null
);

--changeset egrevs:20260203-user-role-enum
create type user_role as enum (
    'USER',
    'COURIER'
);

--changeset egrevs:20260203-add-user-role-column
alter table users
    add column role user_role not null default 'USER';

--changeset egrevs:20260203-create-user-history-table
create table user_history (
    id varchar(255) primary key ,
    user_id varchar(255) not null ,
    name varchar(20) not null ,
    email varchar(75) not null ,
    login varchar(75) not null ,
    role user_role not null default 'USER',
    valid_to timestamp default current_timestamp not null ,
    valid_from timestamp default current_timestamp not null
);

--changeset egrevs:20260203-add-userHistory-constraint
alter table user_history
    add constraint fk_user_userHistory foreign key (user_id) references users(id);