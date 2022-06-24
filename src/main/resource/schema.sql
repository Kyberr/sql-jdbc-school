-- create database university owner university
drop schema if exists department cascade;
create schema department authorization university;

create sequence department.groups_group_id_seq as integer;
create table department.groups 
(
group_id integer default nextval('department.groups_group_id_seq'::regclass) primary key,
group_name varchar collate pg_catalog."default" not null unique 
)
tablespace pg_default;
alter sequence department.groups_group_id_seq owned by department.groups.group_id;

create sequence department.students_student_id_seq as integer;
create table department.students
(
student_id integer default nextval('department.students_student_id_seq'::regclass) primary key,
group_id integer references department.groups (group_id) on delete set null,
first_name varchar collate pg_catalog."default" not null,
last_name varchar collate pg_catalog."default" not null
)
tablespace pg_default;
alter sequence department.students_student_id_seq owned by department.students.student_id;

create sequence department.courses_course_id_seq as integer;
create table department.courses
(
course_id integer default nextval('department.courses_course_id_seq'::regclass) primary key,
course_name varchar collate pg_catalog."default" not null,
course_description varchar collate pg_catalog."default"
)
tablespace pg_default;
alter sequence department.courses_course_id_seq owned by department.courses.course_id;

drop table if exists department.student_course;
create table department.student_course 
(
student_id integer,
group_id integer,
first_name varchar,
last_name varchar,
course_id integer references department.courses(course_id) on delete set null,
course_name varchar,
course_description varchar,
foreign key (student_id) references department.students(student_id) on delete cascade,
foreign key (group_id) references department.groups(group_id) on delete set null,
primary key (student_id, course_id)
)
tablespace pg_default;
	   