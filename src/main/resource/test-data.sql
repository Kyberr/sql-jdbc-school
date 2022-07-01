insert into department.groups (group_name) values ('uf-57');
insert into department.groups (group_name) values ('zl-01');
insert into department.students (group_id, last_name, first_name) values (1, 'Smith', 'Oliver');
insert into department.students (group_id, last_name, first_name) values (1, 'Gonzales', 'Lucas');
insert into department.students (group_id, last_name, first_name) values (1, 'Wilson', 'Mary');
insert into department.students (group_id, last_name, first_name) values (2, 'Anderson', 'Linda');
insert into department.students (group_id, last_name, first_name) values (2, 'Harris', 'James');
insert into department.students (last_name, first_name) values ('Thomas', 'Henry');
insert into department.courses (course_name) values ('Math');
insert into department.courses (course_name) values ('Programming');
insert into department.student_course (student_id, group_id, first_name, last_name, course_id) 
    values (1, 2, 'Smith', 'Oliver', 2);
insert into department.student_course (student_id, group_id, first_name, last_name, course_id) 
    values (2, 2, 'Gonzales', 'Lucas', 2);
