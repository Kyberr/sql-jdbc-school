package ua.com.foxminded.sql_jdbc_school.dao.university;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Savepoint;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import ua.com.foxminded.sql_jdbc_school.Main;
import ua.com.foxminded.sql_jdbc_school.dao.CourseDAO;
import ua.com.foxminded.sql_jdbc_school.dao.DAOException;
import ua.com.foxminded.sql_jdbc_school.dao.DAOPropertiesCache;
import ua.com.foxminded.sql_jdbc_school.dao.entities.CourseEntity;

public class UniversityCourseDAO implements CourseDAO {
	private static final Logger LOGGER = LogManager.getLogger(Main.class);
	private static final String QUERIES_FILE_NAME = "courseQueries.properties";
    private static final String SELECT_COURSE = "selectCourse";
    private static final String SELECT_ALL = "selectAll";
    private static final String INSERT = "insert";
    private static final String COURSE_ID = "course_id";
    private static final String COURSE_NAME = "course_name";
    private static final String COURSE_DESC = "course_description";
    private static final String ERROR_GET_COURSE = "The getting of the course from the database is failed.";
    private static final String ERROR_CREATE = "The insertion of the courses to the database is failed.";
    private static final String ERROR_GET_ALL_COURSES = "The getting all data from the database is failed.";
    private static final Integer BAD_STATUS = 0;
    
    @Override
    public CourseEntity read(int courseId) throws DAOException {
        try (Connection con = UniversityDAOFactory.creatConnection();
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
        	LOGGER.error(ERROR_GET_COURSE, e);
            throw new DAOException(ERROR_GET_COURSE, e);
        }
    }
    
    @Override
    public List<CourseEntity> getAll() throws DAOException {
        try (Connection con = UniversityDAOFactory.creatConnection();
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
        	LOGGER.error(ERROR_GET_ALL_COURSES, e);
            throw new DAOException(ERROR_GET_ALL_COURSES, e);
            
        }
    }
    
    @Override
    public Integer insert(List<CourseEntity> courseEntities) throws DAOException {
        try (Connection con = UniversityDAOFactory.creatConnection();
             PreparedStatement statement = con.prepareStatement(DAOPropertiesCache
            		 .getInstance(QUERIES_FILE_NAME).getProperty(INSERT))) {
           
            con.setAutoCommit(false);
            Savepoint save1 = con.setSavepoint();
            int status = BAD_STATUS;

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
        	LOGGER.error(ERROR_CREATE, e);
            throw new DAOException(ERROR_CREATE, e);
        }
    }
}
