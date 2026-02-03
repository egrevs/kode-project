-- liquibase formatted sql

--changeset egrevs:20260203-create-order-status-enum
create type status as enum(
    'PENDING',
    'COLLECTING',
    'DELIVERING',
    'DELIVERED',
    'READY'
);

--changeset egrevs:20260203-create-orders-table
create table "order" (
    id varchar(255) primary key ,
    user_id varchar(255) not null ,
    status status not null default 'PENDING',
    total_price decimal(10,2) not null ,
    courier_id varchar(255) not null ,
    created_at timestamp default current_timestamp not null
);

--changeset egrevs:20260203-create-orderItems-table
create table order_items (
    id varchar(255) primary key ,
    menu_item_id varchar(255) not null ,
    menu_item_name varchar(75) not null ,
    quantity int not null ,
    menu_item_price decimal(10,2) not null ,
    total_price decimal(10,2) not null ,
    created_at timestamp default current_timestamp not null ,
    order_id varchar(255) not null
);

--changeset egrevs:20260203-add-user-order-constraint
alter table "order"
        add constraint fk_user_orders foreign key(user_id) references users(id);

--changeset egrevs:20260203-add-orderItems-order-constraint
alter table order_items
    add constraint fk_order_orderItems foreign key(order_id) references "order"(id);

--changeset egrevs:20260203-add-courier-column-constraint
alter table "order"
    add constraint fk_order_courier foreign key(courier_id) references couriers(id);

