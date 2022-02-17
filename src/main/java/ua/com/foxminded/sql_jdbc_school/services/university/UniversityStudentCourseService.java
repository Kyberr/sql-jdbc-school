package ua.com.foxminded.sql_jdbc_school.services.university;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;
import ua.com.foxminded.sql_jdbc_school.dao.CourseDAO;
import ua.com.foxminded.sql_jdbc_school.dao.DAOException;
import ua.com.foxminded.sql_jdbc_school.dao.DAOFactory;
import ua.com.foxminded.sql_jdbc_school.dao.StudentCourseDAO;
import ua.com.foxminded.sql_jdbc_school.dao.StudentDAO;
import ua.com.foxminded.sql_jdbc_school.services.Generator;
import ua.com.foxminded.sql_jdbc_school.services.ServicesException;
import ua.com.foxminded.sql_jdbc_school.services.StudentCourseService;
import ua.com.foxminded.sql_jdbc_school.services.dto.CourseDTO;
import ua.com.foxminded.sql_jdbc_school.services.dto.StudentCourseDTO;
import ua.com.foxminded.sql_jdbc_school.services.dto.StudentDTO;

public class UniversityStudentCourseService implements StudentCourseService<List<StudentDTO>, 
                                                                            List<CourseDTO>,
                                                                            List<StudentCourseDTO>,
                                                                            Integer> {
    
    private static final String ERROR_ADD_STUDENT = "The addition of the student to the database is failed.";
    private static final String ERROR_CREATE_RELATION = "The relation creation is failed.";
    private static final String ERROR_GET_STUDENTS_OF_COURSE = "The getting students of specified course is failed.";
    private static final int STUDENT_INDEX = 0;
    private static final int COURSE_INDEX = 1;
    private static final int BAD_STATUS = 0;
    private static final int NORMAL_STATUS = 1;
    private Generator generator;
    
    public UniversityStudentCourseService(Generator generator) {
        this.generator = generator;
    }
    
    @Override 
    public Integer addStudentToCourse(Integer studentId, Integer courseId) 
            throws ServicesException.AddNewStudentFailure {
        try {
            DAOFactory universityDAOFactory = DAOFactory.getDAOFactory(DAOFactory.UNIVERSITY);
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
        } catch (DAOException.GetStudentFailure 
                | DAOException.GetCourseFailure
                | DAOException.StudentCourseInsertionFailure e) {
            throw new ServicesException.AddNewStudentFailure(ERROR_ADD_STUDENT, e);
        }
    }
    
    @Override
    public List<StudentCourseDTO> getStudentsOfCourse(Integer courseID) 
            throws ServicesException.GetStudentsRelatedToCourseFaluer {
        try {
            DAOFactory universityDAOFactory = DAOFactory.getDAOFactory(DAOFactory.UNIVERSITY);
            StudentCourseDAO studentDAO = universityDAOFactory.getStudentCourseDAO();
            return studentDAO.getStudentsOfCourse(courseID);
        } catch (DAOException.GetStudentRelatedToCourseFailure e) {
            throw new ServicesException.GetStudentsRelatedToCourseFaluer(ERROR_GET_STUDENTS_OF_COURSE, e);
        }
    }
    
    @Override
    public List<StudentCourseDTO> createStudentCourseRelation(List<StudentDTO> studentsHaveGroupId, 
                                                              List<CourseDTO> courses)
            throws ServicesException.StudentsCoursesRelationFailure {
        List<StudentCourseDTO> studentCourse = assignCrourseToStudent(studentsHaveGroupId, courses);
        
        try {
            DAOFactory universityDAOFacotry = DAOFactory.getDAOFactory(DAOFactory.UNIVERSITY);
            StudentCourseDAO studentCourseDAO = universityDAOFacotry.getStudentCourseDAO();
            studentCourseDAO.createStudentCourseTable();
            studentCourseDAO.insertStudentCourse(studentCourse);
            return studentCourse;
        } catch (Exception e) {
            throw new ServicesException.StudentsCoursesRelationFailure(ERROR_CREATE_RELATION, e);
        }
    }
    
    public List<StudentCourseDTO> assignCrourseToStudent(List<StudentDTO> studentsHaveGroupId, 
                                                          List<CourseDTO> courses) {
        List<StudentDTO> students = studentsHaveGroupId;
        List<List<Integer>> studentCourseIndexRelation = generator
                .getStudentCourseIndexRelation(students.size(),courses.size());
        
        try (Stream<List<Integer>> indexRelationStream = studentCourseIndexRelation.stream()) {
             return indexRelationStream.map((indexRelation) ->
                    new StudentCourseDTO(students.get(indexRelation.get(STUDENT_INDEX)).getStudentId(),
                                         students.get(indexRelation.get(STUDENT_INDEX)).getGroupId(),
                                         students.get(indexRelation.get(STUDENT_INDEX)).getFirstName(),
                                         students.get(indexRelation.get(STUDENT_INDEX)).getLastName(),
                                         courses.get(indexRelation.get(COURSE_INDEX)).getCourseId(),
                                         courses.get(indexRelation.get(COURSE_INDEX)).getCourseName(),
                                         courses.get(indexRelation.get(COURSE_INDEX)).getCourseDescription()))
                    .collect(ArrayList::new, ArrayList::add, ArrayList::addAll);
        }
    }
}
