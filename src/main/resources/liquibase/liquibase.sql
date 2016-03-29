--liquibase formatted sql
--changeset User:2
create table city(
    id bigint primary key,
    name char(40),
    latitude double,
    longitude double
);

create table distance(
    from_city bigint,
    to_city bigint,
    distance double,
    primary key(from_city,to_city),
    foreign key (from_city) references city(id),
    foreign key (to_city) references city(id)
);

insert into city (id, name, latitude, longitude)
            values(1, "city1", 0, 0);

insert into city (id, name, latitude, longitude)
            values(2, "city2", 5, 8);

insert into city (id, name, latitude, longitude)
            values(3, "city3", 6, 2);

insert into distance (from_city, to_city, distance)
            values (1, 2, 3);

insert into distance (from_city, to_city, distance)
            values (2, 3, 7);

insert into distance (from_city, to_city, distance)
            values (3, 1, 4);

insert into distance (from_city, to_city, distance)
            values (1, 3, 10);