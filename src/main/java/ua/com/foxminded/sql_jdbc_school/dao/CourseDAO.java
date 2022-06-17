package ua.com.foxminded.sql_jdbc_school.dao;

import ua.com.foxminded.sql_jdbc_school.dao.entities.CourseEntity;

public interface CourseDAO extends GenericDAO<CourseEntity, Integer> {
    
	public int deleteStudentFromCourse(int studentId, int courseId) throws DAOException;
	public CourseEntity read(int courseId) throws DAOException;
}
