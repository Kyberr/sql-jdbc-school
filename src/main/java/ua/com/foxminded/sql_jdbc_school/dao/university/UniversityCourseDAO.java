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
    
    public static final String SELECT_COURSE = "select * from department.courses where course_id = ?";
    public static final String INSERT = "insert into department.courses(course_name) values (?)";
    public static final String SELECT_ALL = "select * from department.courses";
    public static final String COURSE_ID = "course_id";
    public static final String COURSE_NAME = "course_name";
    public static final String COURSE_DESC = "course_description";
    public static final String ERROR_GET_COURSE = "The getting of the course from the database is failed.";
    public static final String ERROR_INSERT = "The insertion of the courses to the database is failed.";
    public static final String ERROR_GET_ALL_COURSES = "The getting all data from the database is failed.";
    
    @Override
    public CourseDTO getCourse(int courseId) throws DAOException.GetCourseFailure {
        try (Connection con = UniversityDAOFactory.creatConnection();
             PreparedStatement statement = con.prepareStatement(SELECT_COURSE);) {
            
            CourseDTO course = null;
            statement.setInt(1, courseId);
            ResultSet resultSet = statement.executeQuery();
            
            while (resultSet.next()) {
                course = new CourseDTO(resultSet.getInt(COURSE_ID), 
                                       resultSet.getString(COURSE_NAME),
                                       resultSet.getString(COURSE_DESC));
            }
            resultSet.close();
            return course;
        } catch (DAOException.DatabaseConnectionFail
                | SQLException e) {
            throw new DAOException.GetCourseFailure(ERROR_GET_COURSE, e);
        }
    }
    
    @Override
    public List<CourseDTO> getAllCourses() throws DAOException.GetAllCoursesFail {
        try (Connection con = UniversityDAOFactory.creatConnection();
             Statement statement = con.createStatement();
             ResultSet resultSet = statement.executeQuery(SELECT_ALL)) {

            List<CourseDTO> courses = new ArrayList<>();
            
            while (resultSet.next()) {
                courses.add(new CourseDTO(resultSet.getInt(COURSE_ID),
                                          resultSet.getString(COURSE_NAME),
                                          resultSet.getString(COURSE_DESC)));
            }
            return courses;
        } catch (DAOException.DatabaseConnectionFail | SQLException e) {
            throw new DAOException.GetAllCoursesFail(ERROR_GET_ALL_COURSES, e);
            
        }
    }
    
    @Override
    public int insertCourse(List<String> courseNameList) throws DAOException.CourseInsertionFail {
        try (Connection con = UniversityDAOFactory.creatConnection();
             PreparedStatement statement = con.prepareStatement(INSERT)) {
           
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
