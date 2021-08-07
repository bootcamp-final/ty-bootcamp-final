create table users (
   id  serial not null,
   address varchar(255),
   birthday date,
   email_address varchar(255),
   full_name varchar(255),
   gender varchar(255),
   primary key (id)
);
