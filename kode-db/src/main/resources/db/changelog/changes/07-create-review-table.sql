-- liquibase formatted sql

--changeset egrevs:20260203-create-reviews-table
create table reviews (
    id varchar(255) primary key ,
    user_id varchar(255) not null ,
    restaurant_id varchar(255) not null ,
    text varchar(100),
    rating float not null ,
    created_at timestamp default current_timestamp not null ,
    updated_at timestamp default current_timestamp not null
);

--changeset egrevs:20260203-add-user-constraint
alter table reviews
    add constraint fk_user_review foreign key (user_id) references users(id);

--changeset egrevs:20260203-add-restaurant-constraint
alter table reviews
    add constraint fk_restaurant_review foreign key (restaurant_id) references restaurants(id);