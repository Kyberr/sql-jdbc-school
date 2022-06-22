drop schema if exists department cascade;
create schema department authorization sa;

create table department.groups 
(
group_id integer not null primary key,
group_name varchar not null unique 
);

create table department.students
(
student_id integer not null primary key,
group_id integer references department.groups (group_id) on delete set null,
first_name varchar not null,
last_name varchar not null
);

create table department.courses
(
course_id integer not null primary key,
course_name varchar not null,
course_description varchar
);