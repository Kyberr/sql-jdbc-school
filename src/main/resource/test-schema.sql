drop schema if exists department cascade;
create schema department authorization sa;

create sequence department.group_id_seq; 
create table department.groups 
(
group_id integer default nextval('department.group_id_seq') primary key,
group_name varchar not null unique 
);

create sequence department.student_id_seq;
create table department.students
(
student_id integer default nextval('department.student_id_seq') primary key,
group_id integer references department.groups (group_id) on delete set null,
first_name varchar not null,
last_name varchar not null
);

create sequence department.course_id_seq;
create table department.courses
(
course_id integer default nextval('department.course_id_seq') primary key,
course_name varchar not null,
course_description varchar
);