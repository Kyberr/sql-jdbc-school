package ua.com.foxminded.sql_jdbc_school.dao.university;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import ua.com.foxminded.sql_jdbc_school.dao.DAOException;
import ua.com.foxminded.sql_jdbc_school.dao.StudentCourseDAO;

public class UniversityStudentCourseDAO implements StudentCourseDAO {
    private static final String SQL_CREATE_VIEW = "create view department.student_course as "
            + "select * from department.students natural join department.courses "
            + "where students.student_id in (null) "
            + "and courses.course_id in (null) "
            + "order by students.student_id, courses.course_id";
    private static final String ERROR_CREATE = "The student course view creating is failed.";
    
    
    
    public int createStudentCourseView() throws DAOException.CreatingStudentCourseViewFailure {
        try (Connection con = UniversityDAOFactory.creatConnection();
             PreparedStatement statement = con.prepareStatement(SQL_CREATE_VIEW)) {
            return statement.executeUpdate();
        } catch (DAOException.DatabaseConnectionFail 
                | SQLException e) {
            throw new DAOException.CreatingStudentCourseViewFailure(ERROR_CREATE, e);
        }
    }
}
