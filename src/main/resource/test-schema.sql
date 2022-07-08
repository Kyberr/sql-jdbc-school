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

create table department.student_course
(
student_id integer references department.students(student_id) on delete cascade,
group_id integer references department.groups(group_id) on delete set null,
first_name varchar,
last_name varchar,
course_id integer,
course_name varchar,
course_description varchar,
foreign key (group_id) references department.courses(course_id) on delete set null,
primary key (student_id, course_id)
);