package ua.com.foxminded.sql_jdbc_school.dao;

import java.util.List;

import ua.com.foxminded.sql_jdbc_school.services.dto.CourseDTO;

public interface CourseDAO {
    
    public int insertCourse(List<String> courseNameList) throws DAOException.CourseInsertionFail;
    
    public List<CourseDTO> getAllCourses() throws DAOException.GetAllCoursesFail;
}
