package ua.com.foxminded.sql_jdbc_school.dao.postgres;

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
import ua.com.foxminded.sql_jdbc_school.dao.DAOPropertiesCache;
import ua.com.foxminded.sql_jdbc_school.dao.entities.CourseEntity;

public class PostgresCourseDAO implements CourseDAO {
	public static final String QUERIES_FILE_NAME = "courseQueries.properties";
    public static final String SELECT_COURSE = "selectCourse";
    public static final String SELECT_ALL = "selectAll";
    public static final String INSERT = "insert";
    public static final String COURSE_ID = "course_id";
    public static final String COURSE_NAME = "course_name";
    public static final String COURSE_DESC = "course_description";
    public static final String ERROR_GET_COURSE = "The getting of the course from the database is failed.";
    public static final String ERROR_CREATE = "The insertion of the courses to the database is failed.";
    public static final String ERROR_GET_ALL_COURSES = "The getting all data from the database is failed.";
    
    @Override
    public CourseEntity read(int courseId) throws DAOException {
        try (Connection con = PostgresDAOFactory.creatConnection();
             PreparedStatement statement = con.prepareStatement(DAOPropertiesCache
            		 .getInstance(QUERIES_FILE_NAME).getProperty(SELECT_COURSE));) {
            
            CourseEntity course = null;
            statement.setInt(1, courseId);
            ResultSet resultSet = statement.executeQuery();
            
            while (resultSet.next()) {
                course = new CourseEntity(resultSet.getInt(COURSE_ID), 
                                          resultSet.getString(COURSE_NAME),
                                          resultSet.getString(COURSE_DESC));
            }
            resultSet.close();
            return course;
        } catch (DAOException | SQLException e) {
            throw new DAOException(ERROR_GET_COURSE, e);
        }
    }
    
    @Override
    public List<CourseEntity> readAll() throws DAOException {
        try (Connection con = PostgresDAOFactory.creatConnection();
             Statement statement = con.createStatement();
             ResultSet resultSet = statement.executeQuery(DAOPropertiesCache
            		 .getInstance(QUERIES_FILE_NAME).getProperty(SELECT_ALL))) {

            List<CourseEntity> courseEntities = new ArrayList<>();
            
            while (resultSet.next()) {
                courseEntities.add(new CourseEntity(resultSet.getInt(COURSE_ID),
                                          			resultSet.getString(COURSE_NAME),
                                          			resultSet.getString(COURSE_DESC)));
            }
            return courseEntities;
        } catch (DAOException | SQLException e) {
            throw new DAOException(ERROR_GET_ALL_COURSES, e);
            
        }
    }
    
    @Override
    public Integer create(List<CourseEntity> courseEntities) throws DAOException {
        try (Connection con = PostgresDAOFactory.creatConnection();
             PreparedStatement statement = con.prepareStatement(DAOPropertiesCache
            		 .getInstance(QUERIES_FILE_NAME).getProperty(INSERT))) {
           
            con.setAutoCommit(false);
            Savepoint save1 = con.setSavepoint();
            int status = 0;

            try {
                for (CourseEntity courseEntity : courseEntities) {
                    statement.setString(1, courseEntity.getCourseName());
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
        } catch (DAOException | SQLException e) {
            throw new DAOException(ERROR_CREATE, e);
        }
    }
}
