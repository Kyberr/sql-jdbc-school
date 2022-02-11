package ua.com.foxminded.sql_jdbc_school.dao.university;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Savepoint;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import ua.com.foxminded.sql_jdbc_school.dao.CourseDAO;
import ua.com.foxminded.sql_jdbc_school.dao.DAOException;
import ua.com.foxminded.sql_jdbc_school.services.dto.CourseDTO;

public class UniversityCourseDAO implements CourseDAO {
    public static final String SQL_INSERT = "insert into department.courses(course_name) values (?)";
    public static final String SQL_SELECT_ALL = "select * from department.courses";
    public static final String SQL_COLUMN_NAME_COURSE_ID = "course_id";
    public static final String SQL_COLUMN_NAME_COURSE_NAME = "course_name";
    public static final String SQL_COLUMN_NAME_COURSE_DESC = "course_description";
    public static final String ERROR_INSERT = "The insertion of the courses to the database is failed.";
    public static final String ERROR_GET_ALL_COURSES = "The getting all data from the courses table is failed.";
    
    @Override
    public List<CourseDTO> getAllCourses() throws DAOException.GetAllCoursesFail {
        try (Connection con = UniversityDAOFactory.creatConnection();
             Statement statement = con.createStatement();
             ResultSet resultSet = statement.executeQuery(SQL_SELECT_ALL)) {

            List<CourseDTO> courses = new ArrayList<>();
            
            while (resultSet.next()) {
                courses.add(new CourseDTO(resultSet.getInt(SQL_COLUMN_NAME_COURSE_ID),
                                          resultSet.getString(SQL_COLUMN_NAME_COURSE_NAME),
                                          resultSet.getString(SQL_COLUMN_NAME_COURSE_DESC)));
            }
            return courses;
        } catch (DAOException.DatabaseConnectionFail | SQLException e) {
            throw new DAOException.GetAllCoursesFail(ERROR_GET_ALL_COURSES, e);
            
        }
    }
    
    @Override
    public int insertCourse(List<String> courseNameList) throws DAOException.CourseInsertionFail {
        try (Connection con = UniversityDAOFactory.creatConnection();
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
        } catch (DAOException.DatabaseConnectionFail | SQLException e) {
            throw new DAOException.CourseInsertionFail(ERROR_INSERT, e);
        }
    }
}
