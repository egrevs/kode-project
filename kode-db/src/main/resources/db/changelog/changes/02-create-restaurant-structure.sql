-- liquibase formatted sql

--changeset egrevs:20260203-create-cuisine-enum
create type cuisine as enum(
    'ITALIAN',
    'FRENCH',
    'CHINESE',
    'JAPANESE',
    'SPANISH'
);

--changeset egrevs:20260203-create-size-enum
create type size as enum(
    'BIG',
    'MEDIUM',
    'SMALL'
);

--changeset egrevs:20260203-create-restaurant-table
create table restaurants (
    id varchar(255) primary key ,
    name varchar (255) not null ,
    cuisine cuisine not null default 'ITALIAN',
    rating float not null ,
    avg_cooking_time int not null ,
    created_at timestamp default current_timestamp not null ,
    updated_at timestamp default current_timestamp not null
);

--changeset egrevs:20260203-create-menuItems-table
create table menu_items (
    id varchar(255) primary key ,
    name varchar(255) not null ,
    restaurant_id varchar(255) not null ,
    price decimal(10,2) not null ,
    is_available boolean default true not null ,
    created_at timestamp default current_timestamp not null ,
    updated_at timestamp default current_timestamp not null
);

--changeset egrevs:20260203-create-menuItemsVariants-table
create table menu_items_variants (
    id varchar(255) primary key ,
    size size not null default 'MEDIUM',
    price decimal(10,2) not null ,
    preparation_time int not null ,
    menuItems_id varchar(255) not null
);

--changeset egrevs:20260203-add-restaurant_menuItems_constraint
alter table menu_items
    add constraint fk_restaurant_menu_items foreign key (restaurant_id) references restaurants(id);

--changeset egrevs:20260203-add-menuItems_variants_constraint
alter table menu_items_variants
    add constraint fk_variants_menu_items foreign key (menuItems_id) references menu_items(id)
