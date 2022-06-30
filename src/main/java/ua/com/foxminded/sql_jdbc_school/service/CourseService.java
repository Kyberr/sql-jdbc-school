package ua.com.foxminded.sql_jdbc_school.service;

import java.util.List;

import ua.com.foxminded.sql_jdbc_school.model.CourseModel;

public interface CourseService extends GenericService<List<CourseModel>, Integer> {

    public Integer deleteStudentFromCourseById(Integer studentId, Integer courseId) throws ServiceException;

    public List<CourseModel> getAllCourses() throws ServiceException;
}
