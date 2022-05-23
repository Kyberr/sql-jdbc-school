package ua.com.foxminded.sql_jdbc_school.dao;

import ua.com.foxminded.sql_jdbc_school.dao.entities.CourseEntity;

public interface CourseDAO extends GenericDAO<CourseEntity, Integer> {
    
    public CourseEntity read(int courseId) throws DAOException;
}
