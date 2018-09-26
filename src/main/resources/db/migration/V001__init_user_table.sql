create table users (
    id varchar(50) not null primary key,
    username varchar(50) not null,
    password varchar(100) not null
);

create table authorities (
    id varchar(50) not null primary key,
    user_id varchar(50) not null,
    role varchar(500) not null -- USER, ADMIN
);

insert into users(id, username, password)values 
('hule-id', 'hule','$2a$04$Brutw80K6C4uLc2t/aJfnOJqcs0MozN9nIz9g3mA5zeXy4c7S3zKG'),
('admin-id', 'admin','$2a$04$Brutw80K6C4uLc2t/aJfnOJqcs0MozN9nIz9g3mA5zeXy4c7S3zKG');

insert into authorities(id, user_id, role) values 
('hule-role-id', 'hule-id', 'USER'),
('admin-role-id', 'admin-id', 'ADMIN');