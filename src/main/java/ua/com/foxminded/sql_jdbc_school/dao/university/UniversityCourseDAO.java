package ua.com.foxminded.sql_jdbc_school.dao.university;

import java.io.IOException;
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

import ua.com.foxminded.sql_jdbc_school.dao.ConnectionDAOFactory;
import ua.com.foxminded.sql_jdbc_school.dao.ConnectionPool;
import ua.com.foxminded.sql_jdbc_school.dao.CourseDAO;
import ua.com.foxminded.sql_jdbc_school.dao.DAOException;
import ua.com.foxminded.sql_jdbc_school.dao.DAOPropertiesCache;
import ua.com.foxminded.sql_jdbc_school.dao.entities.CourseEntity;

public class UniversityCourseDAO extends UniversityGenericDAO<CourseEntity> implements CourseDAO {
	
	private static final Logger LOGGER = LogManager.getLogger();
	private static final String ERROR_GET_COURSES_OF_STUDENT = "Get courses of a student is failed.";
	private static final String GET_COURSES_OF_STUDENT = "getCoursesOfStudentById";
	private static final String ERROR_DELETE_STUDENT_FROM_COURSE = "Getting all the student course "
			 													 + "relations from the database failed.";
	private static final String DELETE_STUDENT_FROM_COURSE = "deleteStudentFromCourse";
	private static final String SQL_QUERIES_FILENAME = "course-queries.properties";
    private static final String SELECT_COURSE = "selectCourse";
    private static final String SELECT_ALL = "selectAll";
    private static final String INSERT = "insert";
    private static final String COURSE_ID = "course_id";
    private static final String COURSE_NAME = "course_name";
    private static final String COURSE_DESC = "course_description";
    private static final String ERROR_RESULTSET = "ResultSet is not closed.";
    private static final String ERROR_GET_COURSE = "The getting of the course from the database is failed.";
    private static final String ERROR_CREATE = "The insertion of the courses to the database is failed.";
    private static final String ERROR_GET_ALL_COURSES = "The getting all data from the database is failed.";
    private static final Integer BAD_STATUS = 0;
    private ConnectionPool connectionPool;
    
    public UniversityCourseDAO(ConnectionDAOFactory universityConnectionDAOFacotry, 
    						   ConnectionPool connectionPool) {
    	super(universityConnectionDAOFacotry);
    	this.connectionPool = connectionPool;
    }
    
	@Override
    public List<CourseEntity> getCoursesOfStudentById(int studentId) throws DAOException {
		Connection con = null;
		ResultSet resultSet = null;
    	List<CourseEntity> coursesOfstudent = new ArrayList<>();
    	
		try {
    		con = connectionPool.getConnection();
    		
    		try (PreparedStatement prStatement = con.prepareStatement(DAOPropertiesCache
    					.getInstance(SQL_QUERIES_FILENAME)
    					.getProperty(GET_COURSES_OF_STUDENT));) {
    			
    			prStatement.setInt(1, studentId);
        		resultSet = prStatement.executeQuery();
        		
        		while(resultSet.next()) {
        			coursesOfstudent.add(new CourseEntity(resultSet.getInt(COURSE_ID),
        												  resultSet.getString(COURSE_NAME),
        												  resultSet.getString(COURSE_DESC)));
        		}
    		}
    		
    		connectionPool.releaseConnection(con);
    		return coursesOfstudent;
    	} catch (SQLException | IOException e) {
    		LOGGER.error(ERROR_GET_COURSES_OF_STUDENT, e);
    		throw new DAOException(ERROR_GET_COURSES_OF_STUDENT, e);
    	} finally {
    		try {
    			if (resultSet != null) {
    				resultSet.close();
    			}
    		} catch (SQLException e) {
    			LOGGER.error(ERROR_RESULTSET, e);
    		}
		} 
	}
    
    @Override
    public int deleteStudentFromCourse(int studentId, int courseId) throws DAOException {
        try (Connection con = universityConnectionDAOFactory.createConnection();
             PreparedStatement statement = con.prepareStatement(DAOPropertiesCache
            		 .getInstance(SQL_QUERIES_FILENAME)
            		 .getProperty(DELETE_STUDENT_FROM_COURSE))) {

            statement.setInt(1, studentId);
            statement.setInt(2, courseId);
            return statement.executeUpdate();
        } catch (SQLException | IOException e) {
        	LOGGER.error(ERROR_DELETE_STUDENT_FROM_COURSE, e);
            throw new DAOException(ERROR_DELETE_STUDENT_FROM_COURSE, e);
        }
    }

	@Override
    public CourseEntity getCourseById(int courseId) throws DAOException {
		ResultSet resultSet = null;
		
		try (Connection con = universityConnectionDAOFactory.createConnection();
             PreparedStatement statement = con.prepareStatement(DAOPropertiesCache
            		 .getInstance(SQL_QUERIES_FILENAME).getProperty(SELECT_COURSE));) {
            
            CourseEntity course = null;
            statement.setInt(1, courseId);
            resultSet = statement.executeQuery();
            
            while (resultSet.next()) {
                course = new CourseEntity(resultSet.getInt(COURSE_ID), 
                                          resultSet.getString(COURSE_NAME),
                                          resultSet.getString(COURSE_DESC));
            }
            
            return course;
        } catch (SQLException | IOException e) {
        	LOGGER.error(ERROR_GET_COURSE, e);
            throw new DAOException(ERROR_GET_COURSE, e);
        } finally {
        	try {
    			if (resultSet != null) {
    				resultSet.close();
    			}
    		} catch (SQLException e) {
    			LOGGER.error(ERROR_RESULTSET, e);
    		}
        }
    }
    
    @Override
    public List<CourseEntity> getAll() throws DAOException {
        try (Connection con = universityConnectionDAOFactory.createConnection();
             Statement statement = con.createStatement();
             ResultSet resultSet = statement.executeQuery(DAOPropertiesCache
            		 .getInstance(SQL_QUERIES_FILENAME).getProperty(SELECT_ALL))) {

            List<CourseEntity> courseEntities = new ArrayList<>();
            
            while (resultSet.next()) {
                courseEntities.add(new CourseEntity(resultSet.getInt(COURSE_ID),
                                          			resultSet.getString(COURSE_NAME),
                                          			resultSet.getString(COURSE_DESC)));
            }
            return courseEntities;
        } catch (SQLException | IOException e) {
        	LOGGER.error(ERROR_GET_ALL_COURSES, e);
            throw new DAOException(ERROR_GET_ALL_COURSES, e);
            
        }
    }
    
    @Override
    public Integer insert(List<CourseEntity> courseEntities) throws DAOException {
        try (Connection con = universityConnectionDAOFactory.createConnection();
             PreparedStatement statement = con.prepareStatement(DAOPropertiesCache
            		 .getInstance(SQL_QUERIES_FILENAME).getProperty(INSERT))) {
           
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
        } catch (SQLException | IOException e) {
        	LOGGER.error(ERROR_CREATE, e);
            throw new DAOException(ERROR_CREATE, e);
        }
    }
}
