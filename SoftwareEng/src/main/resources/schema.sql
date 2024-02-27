CREATE TABLE if not exists user
(
    uId varchar(255) NOT NULL PRIMARY KEY,
    username varchar(255),
    gender bool,
    role varchar(255),
    password varchar(255)
);