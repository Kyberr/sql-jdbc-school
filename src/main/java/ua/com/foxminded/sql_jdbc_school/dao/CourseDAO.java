package ua.com.foxminded.sql_jdbc_school.dao;

import java.util.List;

public interface CourseDAO {
    
    public int insertCourse(List<String> courseNameList) throws DAOException.CourseInsertionFail;
}
