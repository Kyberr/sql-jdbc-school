package ua.com.foxminded.sql_jdbc_school.service;

import java.util.List;

import ua.com.foxminded.sql_jdbc_school.model.CourseModel;

public interface CourseService extends GenericService<List<CourseModel>> {
    
    public List<CourseModel> createWithoutId(String courseNameListFilename) throws ServiceException;
    
    public int deleteStudentFromCourseById(int studentId, int courseId) throws ServiceException;

    public List<CourseModel> getAllCourses() throws ServiceException;
}
