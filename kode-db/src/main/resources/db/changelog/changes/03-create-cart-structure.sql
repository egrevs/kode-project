-- liquibase formatted sql

--changeset egrevs:20260203-create-cart-table
create table carts (
    id varchar(255) primary key ,
    user_id varchar(255) not null ,
    quantity int not null ,
    total_price decimal(10,2) not null ,
    created_at timestamp default current_timestamp not null ,
    updated_at timestamp default current_timestamp not null
);

--changeset egrevs:20260203-create-cartItems-table
create table cart_items (
    id varchar(255) primary key ,
    cart_id varchar(255) not null ,
    menu_items_id varchar(255) not null ,
    menu_item_price decimal(10,2) not null ,
    menu_item_name varchar(75) not null ,
    total_price decimal(10,2) not null ,
    quantity int not null ,
    created_at timestamp default current_timestamp not null
);

--changeset egrevs:20260203-add-user_constraint
alter table carts
    add constraint fk_user_cart foreign key (user_id) references users(id);

--changeset egrevs:20260203-add-cart_constraint
alter table cart_items
    add constraint fk_cart_cartItems foreign key (cart_id) references carts(id);

--changeset egrevs:20260203-add-menuItems_constraint
alter table cart_items
    add constraint fk_cart_menu_items foreign key (menu_items_id) references menu_items(id);