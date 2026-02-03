-- liquibase formatted sql

--changeset egrevs:20260203-create-notifications-table
create table notifications (
    id varchar(255) primary key ,
    user_id varchar(255) not null ,
    message varchar(100) not null ,
    status varchar(75) not null ,
    created_at timestamp default current_timestamp not null
);

--changeset egrevs:20260203-add-user-constraint
alter table reviews
    add constraint fk_user_notification foreign key (user_id) references users(id);
