package ua.com.foxminded.sql_jdbc_school.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ua.com.foxminded.sql_jdbc_school.dao.CourseDAO;
import ua.com.foxminded.sql_jdbc_school.dao.DAOException;
import ua.com.foxminded.sql_jdbc_school.dao.StudentCourseDAO;
import ua.com.foxminded.sql_jdbc_school.dao.StudentDAO;
import ua.com.foxminded.sql_jdbc_school.dao.entities.CourseEntity;
import ua.com.foxminded.sql_jdbc_school.dao.entities.StudentCourseEntity;
import ua.com.foxminded.sql_jdbc_school.dao.entities.StudentEntity;
import ua.com.foxminded.sql_jdbc_school.service.ServiceException;
import ua.com.foxminded.sql_jdbc_school.service.StudentCourseService;
import ua.com.foxminded.sql_jdbc_school.service.dto.CourseDto;
import ua.com.foxminded.sql_jdbc_school.service.dto.StudentDto;

public class StudentCourseServiceImpl implements StudentCourseService<List<StudentDto>, 
                                                           List<CourseDto>,
                                                           List<StudentDto>,
                                                           Integer> {
	
	private static final Logger LOGGER = LogManager.getLogger();
    private static final String ERROR_GET_STUDENTS_OF_COURSE = "The getting students of specified course failed.";
    private final StudentCourseDAO studentCourseDAO;
    private final StudentDAO studentDAO;
    private final CourseDAO courseDAO;
    
    public StudentCourseServiceImpl(StudentCourseDAO studentCourseDAO, StudentDAO studentDAO,
			CourseDAO courseDAO) {
		this.studentCourseDAO = studentCourseDAO;
		this.studentDAO = studentDAO;
		this.courseDAO = courseDAO;
	}
    
    @Override
    public List<StudentDto> getStudentsOfCourse(Integer courseID) throws ServiceException {
        try {
            return studentCourseDAO.readStudentsOfCourse(courseID)
            					   .stream()
            					   .map((entity) -> new StudentDto(entity.getStudentId(), 
            							   								 entity.getGroupId(), 
            							   								 entity.getFirstName(), 
            							   								 entity.getLastName(), 
            							   								 entity.getCourseId(), 
            							   								 entity.getCourseName(), 
            							   								 entity.getCourseDescription()))
            					   .collect(Collectors.toList());
        } catch (DAOException e) {
        	LOGGER.error(ERROR_GET_STUDENTS_OF_COURSE, e);
            throw new ServiceException(ERROR_GET_STUDENTS_OF_COURSE, e);
        }
    }
}
