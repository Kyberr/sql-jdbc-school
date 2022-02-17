drop schema if exists department cascade;
create schema department authorization university;

create sequence department.groups_group_id_seq as integer;
create table department.groups 
(
group_id integer not null default nextval('department.groups_group_id_seq'::regclass) primary key,
group_name varchar collate pg_catalog."default" not null unique 
)
tablespace pg_default;
alter sequence department.groups_group_id_seq owned by department.groups.group_id;

create sequence department.students_student_id_seq as integer;
create table department.students
(
student_id integer not null default nextval('department.students_student_id_seq'::regclass) primary key,
group_id integer references department.groups (group_id) on delete set null,
first_name varchar collate pg_catalog."default" not null,
last_name varchar collate pg_catalog."default" not null
)
tablespace pg_default;
alter sequence department.students_student_id_seq owned by department.students.student_id;

create sequence department.courses_course_id_seq as integer;
create table department.courses
(
course_id integer not null default nextval('department.courses_course_id_seq'::regclass) primary key,
course_name varchar collate pg_catalog."default" not null,
course_description varchar collate pg_catalog."default"
)
tablespace pg_default;
alter sequence department.courses_course_id_seq owned by department.courses.course_id;