package ua.com.foxminded.sql_jdbc_school.service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;
import ua.com.foxminded.sql_jdbc_school.dao.CourseDAO;
import ua.com.foxminded.sql_jdbc_school.dao.DAOException;
import ua.com.foxminded.sql_jdbc_school.dao.DAOFactory;
import ua.com.foxminded.sql_jdbc_school.dao.StudentCourseDAO;
import ua.com.foxminded.sql_jdbc_school.dao.StudentDAO;
import ua.com.foxminded.sql_jdbc_school.service.dto.CourseDTO;
import ua.com.foxminded.sql_jdbc_school.service.dto.StudentCourseDTO;
import ua.com.foxminded.sql_jdbc_school.service.dto.StudentDTO;

public class StudentCourseService implements StudentCourse<List<StudentDTO>, 
                                                                            List<CourseDTO>,
                                                                            List<StudentCourseDTO>,
                                                                            Integer> {
    
    private static final String ERROR_DELETE_STUDENT_FROM_COURSE = "The service of deletion of a student "
                                                                 + "from the course doesn't work.";
    private static final String ERROR_GET_ALL = "Getting all of the students from the database failed.";
    private static final String ERROR_CREATE_RELATION = "The relation creation failed.";
    private static final String ERROR_GET_STUDENTS_OF_COURSE = "The getting students of specified course failed.";
    private static final String ERROR_ADD_STUDENT = "Adding the student to the course failed.";
    private static final int STUDENT_INDEX = 0;
    private static final int COURSE_INDEX = 1;
    private static final int BAD_STATUS = 0;
    private static final int NORMAL_STATUS = 1;
    private Generator generator;
    
    public StudentCourseService(Generator generator) {
        this.generator = generator;
    }
    
    @Override
    public Integer deleteStudentFromCourse(Integer studentId, Integer courseId) throws ServiceException {
        try {
            DAOFactory universityDAOFactory = DAOFactory.getDAOFactory(DAOFactory.POSTGRES);
            StudentCourseDAO studentCourse = universityDAOFactory.getStudentCourseDAO();
            return studentCourse.deleteStudentFromCourse(studentId, courseId);
        } catch (DAOException e) {
            throw new ServiceException(ERROR_DELETE_STUDENT_FROM_COURSE, e);
        }
    }
    
    @Override 
    public List<StudentCourseDTO> getAllStudentCourse() throws ServiceException {
        try {
            DAOFactory universityDAOFactory = DAOFactory.getDAOFactory(DAOFactory.POSTGRES);
            StudentCourseDAO studentCourse = universityDAOFactory.getStudentCourseDAO();
            return studentCourse.getAllStudentCourse();
        } catch (Exception e) {
            throw new ServiceException(ERROR_GET_ALL, e);
        }
    }
    
    @Override 
    public Integer addStudentToCourse(Integer studentId, Integer courseId) 
            throws ServiceException {
        try {
            DAOFactory universityDAOFactory = DAOFactory.getDAOFactory(DAOFactory.POSTGRES);
            StudentCourseDAO studentCourseDAO = universityDAOFactory.getStudentCourseDAO();
            List<StudentCourseDTO> studentCourse = studentCourseDAO.getStudentCourse(studentId, courseId);
            
            if (!studentCourse.isEmpty()) {
                return BAD_STATUS;
            } else {
                StudentDAO studentDAO = universityDAOFactory.getStudentDAO();
                CourseDAO courseDAO = universityDAOFactory.getCourseDAO();
                StudentDTO student = studentDAO.getStudent(studentId);
                CourseDTO course = courseDAO.getCourse(courseId);
                studentCourse.add(new StudentCourseDTO(student.getStudentId(), 
                                                       student.getGroupId(), 
                                                       student.getFirstName(),
                                                       student.getLastName(),
                                                       course.getCourseId(), 
                                                       course.getCourseName(), 
                                                       course.getCourseDescription()));
                studentCourseDAO.insertStudentCourse(studentCourse);
                return NORMAL_STATUS;
            }
        } catch (DAOException e) {
            throw new ServiceException (ERROR_ADD_STUDENT, e);
        }
    }
    
    @Override
    public List<StudentCourseDTO> getStudentsOfCourse(Integer courseID) throws ServiceException {
        try {
            DAOFactory universityDAOFactory = DAOFactory.getDAOFactory(DAOFactory.POSTGRES);
            StudentCourseDAO studentDAO = universityDAOFactory.getStudentCourseDAO();
            return studentDAO.getStudentsOfCourse(courseID);
        } catch (DAOException e) {
            throw new ServiceException(ERROR_GET_STUDENTS_OF_COURSE, e);
        }
    }
    
    @Override
    public List<StudentCourseDTO> createStudentCourseRelation(List<StudentDTO> studentsHaveGroupId, 
                                                              List<CourseDTO> courses) 
                                                            		  throws ServiceException {
        List<StudentCourseDTO> studentCourse = assignCrourseToStudent(studentsHaveGroupId, 
        															  courses);
        
        try {
            DAOFactory universityDAOFacotry = DAOFactory.getDAOFactory(DAOFactory.POSTGRES);
            StudentCourseDAO studentCourseDAO = universityDAOFacotry.getStudentCourseDAO();
            studentCourseDAO.createStudentCourseTable();
            studentCourseDAO.insertStudentCourse(studentCourse);
            return studentCourse;
        } catch (Exception e) {
            throw new ServiceException(ERROR_CREATE_RELATION, e);
        }
    }
    
    public List<StudentCourseDTO> assignCrourseToStudent(List<StudentDTO> studentsHaveGroupId, 
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
