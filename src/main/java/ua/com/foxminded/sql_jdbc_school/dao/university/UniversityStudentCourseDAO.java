package ua.com.foxminded.sql_jdbc_school.dao.university;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Savepoint;
import java.util.List;
import ua.com.foxminded.sql_jdbc_school.dao.DAOException;
import ua.com.foxminded.sql_jdbc_school.dao.StudentCourseDAO;
import ua.com.foxminded.sql_jdbc_school.services.dto.StudentCourseDTO;

public class UniversityStudentCourseDAO implements StudentCourseDAO {
    private static final String SQL_CREATE_TABLE = "create table department.student_course ("
            + "student_id integer references department.students(student_id),"
            + "group_id integer references department.groups(group_id),"
            + "first_name varchar collate pg_catalog.\"default\" not null,"
            + "last_name varchar collate pg_catalog.\"default\" not null,"
            + "course_id integer references department.courses(course_id),"
            + "course_name varchar collate pg_catalog.\"default\","
            + "course_description varchar collate pg_catalog.\"default\")"
            + "tablespace pg_default";
    private static final String SQL_INSERT = "insert into department.student_course("
            + "student_id, group_id, first_name, last_name, course_id, course_name, course_description) "
            + "values (?, ?, ?, ?, ?, ?, ?)";
    private static final String ERROR_TABLE_CREATION = "The student course view creating is failed.";
    
    @Override
    public int insertStudentCourse(List<StudentCourseDTO> studentsCourses) 
            throws DAOException.StudentCourseInsertionFailure {
        try (Connection con = UniversityDAOFactory.creatConnection();
             PreparedStatement statement = con.prepareStatement(SQL_INSERT)) {
            con.setAutoCommit(false);
            Savepoint save1 = con.setSavepoint();
            int status = 0;
            
            try {
                for (StudentCourseDTO studentCourse : studentsCourses) {
                    statement.setObject(1, studentCourse.getStudentId());
                    statement.setObject(2, studentCourse.getGroupId());
                    statement.setString(3, studentCourse.getFirstName());
                    statement.setString(4, studentCourse.getLastName());
                    statement.setObject(5, studentCourse.getCourseId());
                    statement.setObject(6, studentCourse.getCourseName());
                    statement.setString(7, studentCourse.getCourseDescription());
                    status += statement.executeUpdate();
                }
                con.commit();
                return status;
            } catch (SQLException e) {
                if (con != null) {
                    try {
                        con.rollback(save1);
                    } catch (SQLException ex) {
                        throw new SQLException(ex);
                    }
                }
                
                throw new SQLException (e);
            }
        } catch (DAOException.DatabaseConnectionFail | SQLException e) {
            throw new DAOException.StudentCourseInsertionFailure(null, e);
        }
    }
    
    @Override
    public int createStudentCourseTable() throws DAOException.CreatingStudentCourseTableFailure {
        try (Connection con = UniversityDAOFactory.creatConnection();
             PreparedStatement statement = con.prepareStatement(SQL_CREATE_TABLE)) {
            return statement.executeUpdate();
        } catch (DAOException.DatabaseConnectionFail 
                | SQLException e) {
            throw new DAOException.CreatingStudentCourseTableFailure(ERROR_TABLE_CREATION, e);
        }
    }
}
