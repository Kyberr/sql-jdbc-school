package ua.com.foxminded.sql_jdbc_school.dao;

import java.util.List;

import ua.com.foxminded.sql_jdbc_school.service.dto.CourseDTO;

public interface CourseDAO {
    
    public CourseDTO getCourse(int courseId) throws DAOException;
    public int insertCourse(List<String> courseNameList) throws DAOException;
    public List<CourseDTO> getAllCourses() throws DAOException;
}
