package ua.com.foxminded.sql_jdbc_school.dao;

import java.util.List;

import ua.com.foxminded.sql_jdbc_school.entity.CourseEntity;

public interface CourseDAO extends GenericDAO<CourseEntity> {

    public List<CourseEntity> getCoursesOfStudentById(int studentId) throws DAOException;

    public int deleteStudentFromCourseById(int studentId, int courseId) throws DAOException;

    public CourseEntity getCourseById(int courseId) throws DAOException;
}
