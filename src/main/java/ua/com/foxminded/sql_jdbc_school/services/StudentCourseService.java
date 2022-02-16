package ua.com.foxminded.sql_jdbc_school.services;

public interface StudentCourseService<T, E, S, F> {
    
    F addStudentToCourse(F studentId, F courseId) throws ServicesException.AddNewStudentFailure;
    public S getStudentsOfCourse(F courseID) throws ServicesException.GetStudentsRelatedToCourseFaluer;
    public S createStudentCourseRelation (T students, E courses) 
            throws ServicesException.StudentsCoursesRelationFailure; 
}
