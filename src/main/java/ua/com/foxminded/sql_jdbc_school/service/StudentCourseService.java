package ua.com.foxminded.sql_jdbc_school.service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ua.com.foxminded.sql_jdbc_school.dao.CourseDAO;
import ua.com.foxminded.sql_jdbc_school.dao.DAOException;
import ua.com.foxminded.sql_jdbc_school.dao.StudentCourseDAO;
import ua.com.foxminded.sql_jdbc_school.dao.StudentDAO;
import ua.com.foxminded.sql_jdbc_school.dao.entities.CourseEntity;
import ua.com.foxminded.sql_jdbc_school.dao.entities.StudentCourseEntity;
import ua.com.foxminded.sql_jdbc_school.dao.entities.StudentEntity;
import ua.com.foxminded.sql_jdbc_school.service.dto.CourseDTO;
import ua.com.foxminded.sql_jdbc_school.service.dto.StudentCourseDTO;
import ua.com.foxminded.sql_jdbc_school.service.dto.StudentDTO;

public class StudentCourseService implements StudentCourse<List<StudentDTO>, 
                                                           List<CourseDTO>,
                                                           List<StudentCourseDTO>,
                                                           Integer> {
	
	private static final Logger LOGGER = LogManager.getLogger();
    private static final String ERROR_DELETE_STUDENT_FROM_COURSE = "The service of the deletion of a student "
                                                                 + "from the course doesn't work.";
    private static final String ERROR_GET_ALL = "Getting all of the students from the database failed.";
    private static final String ERROR_CREATE_STUDENT_COURSE_RELATION = "The relation creation failed.";
    private static final String ERROR_GET_STUDENTS_OF_COURSE = "The getting students of specified course failed.";
    private static final String ERROR_ADD_STUDENT = "Adding the student to the course failed.";
    private static final int STUDENT_INDEX = 0;
    private static final int COURSE_INDEX = 1;
    private static final int BAD_STATUS = 0;
    private static final int NORMAL_STATUS = 1;
    private final Generator generator;
    private final StudentCourseDAO studentCourseDAO;
    private final StudentDAO studentDAO;
    private final CourseDAO courseDAO;
    
    public StudentCourseService(Generator generator, StudentCourseDAO studentCourseDAO, StudentDAO studentDAO,
			CourseDAO courseDAO) {
		this.generator = generator;
		this.studentCourseDAO = studentCourseDAO;
		this.studentDAO = studentDAO;
		this.courseDAO = courseDAO;
	}
    
    @Override
    public Integer deleteStudentFromCourse(Integer studentId, Integer courseId) throws ServiceException {
        try {
            return studentCourseDAO.deleteStudentFromCourse(studentId, courseId);
        } catch (DAOException e) {
        	LOGGER.error(ERROR_DELETE_STUDENT_FROM_COURSE, e);
            throw new ServiceException(ERROR_DELETE_STUDENT_FROM_COURSE, e);
        }
    }
    
    @Override 
    public List<StudentCourseDTO> getAllStudentCourse() throws ServiceException {
        try {
            return studentCourseDAO.getAll()
            					   .parallelStream()
            					   .map((entity) -> new StudentCourseDTO(entity.getStudentId(), 
            													         entity.getGroupId(), 
            													         entity.getFirstName(), 
            													         entity.getLastName(), 
            													         entity.getCourseId(), 
            													         entity.getCourseName(), 
            													         entity.getCourseDescription()))
            					   .collect(Collectors.toList());
        } catch (Exception e) {
        	LOGGER.error(ERROR_GET_ALL, e);
            throw new ServiceException(ERROR_GET_ALL, e);
        }
    }
    
    @Override 
    public Integer addStudentToCourse(Integer studentId, Integer courseId) 
            throws ServiceException {
        try {
            StudentCourseEntity studentCourseEntity = studentCourseDAO.read(studentId, courseId);
            
            if (studentCourseEntity != null) {
                return BAD_STATUS;
            } else {
                StudentEntity student = studentDAO.read(studentId);
                CourseEntity course = courseDAO.read(courseId);
                List<StudentCourseEntity> studentCourseEntityList = new ArrayList<>();
                studentCourseEntityList.add(new StudentCourseEntity(student.getStudentId(), 
                                                                    student.getGroupId(), 
                                                                    student.getFirstName(),
                                                                    student.getLastName(),
                                                                    course.getCourseId(), 
                                                                    course.getCourseName(), 
                                                                    course.getCourseDescription()));
                studentCourseDAO.insert(studentCourseEntityList);
                return NORMAL_STATUS;
            }
        } catch (DAOException e) {
        	LOGGER.error(ERROR_ADD_STUDENT, e);
            throw new ServiceException (ERROR_ADD_STUDENT, e);
        }
    }
    
    @Override
    public List<StudentCourseDTO> getStudentsOfCourse(Integer courseID) throws ServiceException {
        try {
            return studentCourseDAO.readStudentsOfCourse(courseID)
            					   .stream()
            					   .map((entity) -> new StudentCourseDTO(entity.getStudentId(), 
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
    
    @Override
    public List<StudentCourseDTO> createStudentCourseRelation(List<StudentDTO> studentsHaveGroupId, 
                                                              List<CourseDTO> courses) 
                                                            		  throws ServiceException {
        
    	List<StudentCourseDTO> studentCourseDTOs = assignStudentToCourse(studentsHaveGroupId, courses);
    	List<StudentCourseEntity> studentCourseEntities = studentCourseDTOs.parallelStream()
    			.map((dto) -> new StudentCourseEntity(dto.getStudentId(), 
        											  dto.getGroupId(), 
        											  dto.getFirstName(), 
        											  dto.getLastName(), 
        											  dto.getCourseId(), 
        											  dto.getCourseName(), 
        											  dto.getCourseDescription()))
        		.collect(Collectors.toList());
        
        try {
            studentCourseDAO.insert(studentCourseEntities);
            return studentCourseDTOs;
        } catch (Exception e) {
        	LOGGER.error(ERROR_CREATE_STUDENT_COURSE_RELATION, e);
            throw new ServiceException(ERROR_CREATE_STUDENT_COURSE_RELATION, e);
        }
    }
    
    private List<StudentCourseDTO> assignStudentToCourse(List<StudentDTO> studentsHaveGroupId, 
                                                        List<CourseDTO> courses) {
        List<List<Integer>> studentCourseIndexRelation = generator
                .getStudentCourseIndexRelation(studentsHaveGroupId.size(),courses.size());
        
        try (Stream<List<Integer>> indexRelationStream = studentCourseIndexRelation.stream()) {
            return indexRelationStream.map((indexRelation) -> 
                    new StudentCourseDTO(
                            studentsHaveGroupId.get(indexRelation.get(STUDENT_INDEX)).getStudentId(),
                            studentsHaveGroupId.get(indexRelation.get(STUDENT_INDEX)).getGroupId(),
                            studentsHaveGroupId.get(indexRelation.get(STUDENT_INDEX)).getFirstName(),
                            studentsHaveGroupId.get(indexRelation.get(STUDENT_INDEX)).getLastName(),
                            courses.get(indexRelation.get(COURSE_INDEX)).getCourseId(),
                            courses.get(indexRelation.get(COURSE_INDEX)).getCourseName(),
                            courses.get(indexRelation.get(COURSE_INDEX)).getCourseDescription()))
                    .collect(ArrayList::new, ArrayList::add, ArrayList::addAll);
        }
    }
}
