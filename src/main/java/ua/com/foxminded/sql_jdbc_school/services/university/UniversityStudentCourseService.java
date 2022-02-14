package ua.com.foxminded.sql_jdbc_school.services.university;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;
import ua.com.foxminded.sql_jdbc_school.dao.DAOException;
import ua.com.foxminded.sql_jdbc_school.dao.DAOFactory;
import ua.com.foxminded.sql_jdbc_school.dao.StudentCourseDAO;
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
    private static final String ERROR_CREATE_RELATION = "The relation creation is failed.";
    private static final String ERROR_GET_STUDENTS_OF_COURSE = "The getting students of specified course is failed.";
    private static final int STUDENT_INDEX = 0;
    private static final int COURSE_INDEX = 1;
    private Generator generator;
    
    public UniversityStudentCourseService(Generator generator) {
        this.generator = generator;
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
    public List<StudentCourseDTO> createStudentCourseRelation(List<StudentDTO> students, 
                                                              List<CourseDTO> courses)
            throws ServicesException.StudentsCoursesRelationFailure {
        List<StudentCourseDTO> studentCourseList = getOrdedStudentCourseRelation(students, courses);
        
        try {
            DAOFactory universityDAOFacotry = DAOFactory.getDAOFactory(DAOFactory.UNIVERSITY);
            StudentCourseDAO studentCourseDAO = universityDAOFacotry.getStudentCourseDAO();
            studentCourseDAO.createStudentCourseTable();
            studentCourseDAO.insertStudentCourse(studentCourseList);
            return studentCourseList;
        } catch (Exception e) {
            throw new ServicesException.StudentsCoursesRelationFailure(ERROR_CREATE_RELATION, e);
        }
    }
    
    private List<StudentCourseDTO> getOrdedStudentCourseRelation (List<StudentDTO> students, 
                                                                  List<CourseDTO> courses) {
        List<StudentCourseDTO> studentCourseHasGroupId = getStudentCourseHasGroupId(students, courses);
        List<StudentCourseDTO> studentsNoGroupId = getStudentCourseNoGroupId(students);
        studentCourseHasGroupId.addAll(studentsNoGroupId);
        return studentCourseHasGroupId;
    }
    
    private List<StudentCourseDTO> getStudentCourseNoGroupId(List<StudentDTO> students) {
        List<StudentDTO> studentNoGroupId = getStudentsNoGroupId(students);
        
        try (Stream<StudentDTO> studentNoGropuIdStream = studentNoGroupId.parallelStream()) {
            return studentNoGropuIdStream.map((student) -> new StudentCourseDTO(student.getStudentId(),
                                                                                student.getGroupId(),
                                                                                student.getFirstName(),
                                                                                student.getLastName()))
                    .collect(ArrayList::new, ArrayList::add, ArrayList::addAll);
        }
    }
    
    private List<StudentCourseDTO> getStudentCourseHasGroupId(List<StudentDTO> students, 
                                                              List<CourseDTO> courses) {
        List<StudentDTO> studentHasGroupId = getStudentHasGroupId(students);
        List<List<Integer>> coursePerStudent = generator.getCoursePerStudent(studentHasGroupId.size(), 
                                                                             courses.size());
        
        try (Stream<List<Integer>> coursePerStudentStream = coursePerStudent.parallelStream()) {
            return coursePerStudentStream.map((index) ->
            new StudentCourseDTO(studentHasGroupId.get(index.get(STUDENT_INDEX)).getStudentId(),
                                 studentHasGroupId.get(index.get(STUDENT_INDEX)).getGroupId(),
                                 studentHasGroupId.get(index.get(STUDENT_INDEX)).getFirstName(),
                                 studentHasGroupId.get(index.get(STUDENT_INDEX)).getLastName(),
                                 courses.get(index.get(COURSE_INDEX)).getCourseId(),
                                 courses.get(index.get(COURSE_INDEX)).getCourseName(),
                                 courses.get(index.get(COURSE_INDEX)).getCourseDescription()))
            .collect(ArrayList::new, ArrayList::add, ArrayList::addAll);
        }
    }
    
    private List<StudentDTO> getStudentHasGroupId(List<StudentDTO> studentList) {
        try (Stream<StudentDTO> students = studentList.parallelStream()) {
            return students.filter((student) -> student.getGroupId() != null)
                           .collect(ArrayList::new, ArrayList::add, ArrayList::addAll);
        }
    }
    
    private List<StudentDTO> getStudentsNoGroupId(List<StudentDTO> studentList) {
        try (Stream<StudentDTO> students = studentList.parallelStream()) {
            return students.filter((student) -> student.getGroupId() == null)
                           .collect(ArrayList::new, ArrayList::add, ArrayList::addAll);
        }
    }
}
