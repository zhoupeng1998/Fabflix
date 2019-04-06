DROP TABLE IF EXISTS employees;

create table employees(
   email VARCHAR(50) not null,
   password VARCHAR(20) not null,
   fullname VARCHAR(100),
   primary key(email)
);
