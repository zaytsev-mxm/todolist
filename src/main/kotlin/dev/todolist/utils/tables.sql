create table users
(
    id       varchar(60) primary key not null,
    login    varchar(60) not null ,
    password varchar(60) not null,
    username varchar(30),
    email    varchar(30)
);

create table tokens
(
    id      varchar(60) primary key,
    user_id varchar(60) not null,
    token   varchar(60) not null
);

create table lists
(
    id          varchar(60) primary key,
    title       varchar(150) not null,
    description varchar(500),
    user_id     varchar(60) not null,
    foreign key (user_id) references users(id)
);

create table todos
(
    id          varchar(60) primary key,
    title       varchar(150) not null,
    description varchar(500),
    done        boolean,
    list_id     varchar(60),
    foreign key (list_id) references lists(id)
);