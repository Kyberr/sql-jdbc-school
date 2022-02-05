package ua.com.foxminded.sql_jdbc_school.services;

public interface StudentCourseService<T, E, S>{
    
    public S createStudentCourseRelation (T students, E courses) 
            throws ServicesException.StudentsCoursesRelationFailure; 

}
