-- database creation
-- CREATE USER 'advertcollector'@'localhost' IDENTIFIED BY 'password';
-- CREATE DATABASE adverts;
-- GRANT ALL PRIVILEGES ON adverts.* To 'advertcollector'@'localhost' IDENTIFIED BY 'password';

CREATE TABLE if not exists ADVERTS(
ID int AUTO_INCREMENT NOT NULL,
TITLE varchar(255),
LINK varchar(200),
PRICE DOUBLE,
DESCRIPTION TEXT,
COUNTRY varchar(300),
CITY varchar(300),
STREETS varchar(200),
AREA DOUBLE,
PRICEPERMETER DOUBLE,
COORDINATES varchar(255),
PRIMARY KEY (ID)
);