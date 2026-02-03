-- liquibase formatted sql

--changeset egrevs:20260203-create-payment-table
create table payments (
    id varchar(255) primary key ,
    order_id varchar(255) not null ,
    total_amount decimal(10,2) not null ,
    payment_status varchar(20) default 'CREATED' not null ,
    payment_method varchar(20) default 'CARD' not null ,
    created_at timestamp default current_timestamp not null ,
    updated_at timestamp default current_timestamp not null
);

--changeset egrevs:20260203-create_split-payments-table
create table split_payments (
    id varchar(255) primary key ,
    payment_id varchar(255) not null ,
    restaurant_id varchar(255) not null ,
    payment_status varchar(20) default 'CREATED' not null ,
    payment_method varchar(20) default 'CARD' not null ,
    price decimal(10,2) not null ,
    created_at timestamp default current_timestamp not null ,
    updated_at timestamp default current_timestamp not null
);

--changeset egrevs:20260203-add-order-constraint
alter table payments
    add constraint fk_order_payment foreign key (order_id) references "order"(id);

--changeset egrevs:20260203-add-payment-constraint
alter table split_payments
    add constraint fk_payment_split_payments foreign key (payment_id) references payments(id);

--changeset egrevs:20260203-add-restaurant-constraint
alter table split_payments
    add constraint fk_restaurant_split_payments foreign key (restaurant_id) references restaurants(id);

