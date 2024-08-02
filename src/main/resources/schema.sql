--CREATE SCHEMA IF NOT EXISTS rishabh;
--SET SCHEMA rishabh;

drop table if exists USER_ROLE;
drop table if exists ROLES;
drop table if exists USERS;

create table USERS(
  USER_ID int not null AUTO_INCREMENT,
  FIRST_NAME varchar(100) not null,
  LAST_NAME varchar(100) not null,
  LOGIN varchar(100) not null,
  PASSWORD varchar(100) not null,
  ENABLED varchar(1),
  PRIMARY KEY ( USER_ID )
);

create table ROLES(
  ROLE_ID int not null AUTO_INCREMENT,
  ROLE_NAME varchar(100) not null,
  PRIMARY KEY ( ROLE_ID )
);

create table USER_ROLE(
  ID int not null AUTO_INCREMENT,
  USER_ID int,
  ROLE_ID int,
  foreign key (USER_ID) references USERS(USER_ID),
  foreign key (ROLE_ID) references ROLES(ROLE_ID)
)
