package ua.com.foxminded.sql_jdbc_school.dao.university;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Savepoint;
import java.util.List;
import ua.com.foxminded.sql_jdbc_school.dao.CourseDAO;
import ua.com.foxminded.sql_jdbc_school.dao.DAOException;

public class UniversityCourseDAO implements CourseDAO {
    public static final String SQL_INSERT = "insert into department.courses(course_name) values (?)";
    public static final String ERROR_INSERT = "The insertion of the courses to the database is failed.";

    public int insertCourse(List<String> courseNameList) throws DAOException.CourseInsertionFail {
        try(Connection con = UniversityDAOFactory.creatConnection();
            PreparedStatement statement = con.prepareStatement(SQL_INSERT)) {
            
            con.setAutoCommit(false);
            Savepoint save1 = con.setSavepoint();
            int status = 0;
            
            try {
                for (String courseName : courseNameList) {
                    statement.setString(1, courseName);
                    status = statement.executeUpdate();
                }
                
                con.commit();
                return status;
            } catch (SQLException e) {
                if (con != null) {
                    try {
                        con.rollback(save1);
                    } catch (SQLException exc) {
                        throw new SQLException(exc);
                    }
                }
                
                throw new SQLException(e);
            }
        } catch (DAOException.DatabaseConnectionFail |
                 SQLException e) {
            throw new DAOException.CourseInsertionFail(ERROR_INSERT, e);
        }
    }
}
