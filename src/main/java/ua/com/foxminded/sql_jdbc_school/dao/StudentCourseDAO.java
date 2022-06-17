package ua.com.foxminded.sql_jdbc_school.dao;

import java.util.List;
import ua.com.foxminded.sql_jdbc_school.dao.entities.StudentCourseEntity;

public interface StudentCourseDAO extends GenericDAO<StudentCourseEntity, Integer>{
    
    public StudentCourseEntity read(int studentId, int courseId) throws DAOException;
    public List<StudentCourseEntity> readStudentsOfCourse(int courseID) throws DAOException;
}
