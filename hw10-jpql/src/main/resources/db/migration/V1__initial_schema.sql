-- Для @GeneratedValue(strategy = GenerationType.IDENTITY)
--/*
--create table clients
--(
--    id   bigserial not null primary key,
--    name varchar(50)
--);
--
-- */

-- Для @GeneratedValue(strategy = GenerationType.SEQUENCE)
create sequence hibernate_sequence start with 1 increment by 1;

create table clients
(
    id   bigint not null primary key,
    name varchar(50),
    address_id bigint
);
create table address
(
    id   bigserial not null primary key,
    street varchar(250)
);

create table phones
(
    id   bigserial not null primary key,
    number varchar(20),
    client_id bigint
);
