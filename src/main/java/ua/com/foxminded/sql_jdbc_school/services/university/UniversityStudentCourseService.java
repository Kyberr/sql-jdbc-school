package ua.com.foxminded.sql_jdbc_school.services.university;

import java.util.ArrayList;
import java.util.List;
import ua.com.foxminded.sql_jdbc_school.dao.DAOFactory;
import ua.com.foxminded.sql_jdbc_school.dao.StudentCourseDAO;
import ua.com.foxminded.sql_jdbc_school.services.ServicesException;
import ua.com.foxminded.sql_jdbc_school.services.StudentCourseService;
import ua.com.foxminded.sql_jdbc_school.services.dto.CourseDTO;
import ua.com.foxminded.sql_jdbc_school.services.dto.StudentCourseDTO;
import ua.com.foxminded.sql_jdbc_school.services.dto.StudentDTO;

public class UniversityStudentCourseService implements StudentCourseService<List<StudentDTO>, 
                                                                            List<CourseDTO>,
                                                                            List<StudentCourseDTO>> {
    private static final String ERROR_CREATE_RELATION = "The relation creation is failed.";
    
    public List<StudentCourseDTO> createStudentCourseRelation(List<StudentDTO> students, 
           List<CourseDTO> courses) throws ServicesException.StudentsCoursesRelationFailure {
        try {
            DAOFactory universityDAOFacotry = DAOFactory.getDAOFactory(DAOFactory.UNIVERSITY);
            StudentCourseDAO studentCourseDAO = universityDAOFacotry.getStudentCourseDAO();
            studentCourseDAO.createStudentCourseView();
            
            //  needs Generator
            
            
        } catch (Exception e) {
            throw new ServicesException.StudentsCoursesRelationFailure (ERROR_CREATE_RELATION, e);
        }
    }

}
