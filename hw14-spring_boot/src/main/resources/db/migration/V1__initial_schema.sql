create table clients
(
    id   bigserial primary key,
    name varchar(250)
);
create table address
(
    id   bigserial primary key,
    street varchar(250),
    client_id bigint references clients(id)
);

create table phones
(
    id   bigserial primary key,
    number varchar(20),
    client_id bigint references clients(id)
);