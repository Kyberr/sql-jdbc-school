package ua.com.foxminded.sql_jdbc_school.dao.jdbc;

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

import ua.com.foxminded.sql_jdbc_school.dao.CourseDAO;
import ua.com.foxminded.sql_jdbc_school.dao.DAOConnectionPool;
import ua.com.foxminded.sql_jdbc_school.dao.DAOException;
import ua.com.foxminded.sql_jdbc_school.dao.DAOPropertiesCache;
import ua.com.foxminded.sql_jdbc_school.entity.CourseEntity;

public class JdbcCourseDAO implements CourseDAO {

    private static final Logger LOGGER = LogManager.getLogger();
    private static final String DELETE_ALL_ERROR = "The deletion operation of all the courses faled.";
    private static final String DELETE_ALL = "deleteAll";
    private static final String GET_COURSES_OF_STUDENT_ERROR = "Get courses of a student is failed.";
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
    private static final String RESULTSET_ERROR = "ResultSet is not closed.";
    private static final String GET_COURSE_ERROR = "The getting of the course from the database is failed.";
    private static final String INSERT__ERROR = "The insertion of the courses to the database is failed.";
    private static final String GET_ALL_COURSES_ERROR = "The getting all data from the database is failed.";
    private static final Integer BAD_STATUS = 0;
    
    private DAOConnectionPool jdbcDaoConnectionPool;
    
    public JdbcCourseDAO(DAOConnectionPool jdbcDaoConnectionPool) {
        this.jdbcDaoConnectionPool = jdbcDaoConnectionPool;
    }

    @Override
    public Integer deleteAll() throws DAOException {
        try {
            int status = 0;
            Connection con = jdbcDaoConnectionPool.getConnection();
             
            try (PreparedStatement prStatement = con.prepareStatement(DAOPropertiesCache
                    .getInstance(SQL_QUERIES_FILENAME)
                    .getProperty(DELETE_ALL));) {
                
                status = prStatement.executeUpdate();
            }
            jdbcDaoConnectionPool.releaseConnection(con);
            return status;
        } catch (SQLException e) {
            LOGGER.error(DELETE_ALL_ERROR, e);
            throw new DAOException(DELETE_ALL_ERROR, e);
        }
    }

    @Override
    public List<CourseEntity> getCoursesOfStudentById(int studentId) throws DAOException {
        Connection con = null;
        ResultSet resultSet = null;
        List<CourseEntity> coursesOfstudent = new ArrayList<>();

        try {
            con = jdbcDaoConnectionPool.getConnection();

            try (PreparedStatement prStatement = con.prepareStatement(DAOPropertiesCache
                    .getInstance(SQL_QUERIES_FILENAME)
                    .getProperty(GET_COURSES_OF_STUDENT));) {

                prStatement.setInt(1, studentId);
                resultSet = prStatement.executeQuery();

                while (resultSet.next()) {
                    coursesOfstudent.add(new CourseEntity(resultSet.getInt(COURSE_ID), 
                                                          resultSet.getString(COURSE_NAME),
                                                          resultSet.getString(COURSE_DESC)));
                }
            }
            jdbcDaoConnectionPool.releaseConnection(con);
            return coursesOfstudent;
        } catch (SQLException e) {
            LOGGER.error(GET_COURSES_OF_STUDENT_ERROR, e);
            throw new DAOException(GET_COURSES_OF_STUDENT_ERROR, e);
        } finally {
            try {
                if (resultSet != null) {
                    resultSet.close();
                }
            } catch (SQLException e) {
                LOGGER.error(RESULTSET_ERROR, e);
            }
        }
    }

    @Override
    public int deleteStudentFromCourse(int studentId, int courseId) throws DAOException {
        try {
            Connection con = jdbcDaoConnectionPool.getConnection();
            
            try (PreparedStatement prStatement = con.prepareStatement(DAOPropertiesCache
                    .getInstance(SQL_QUERIES_FILENAME)
                    .getProperty(DELETE_STUDENT_FROM_COURSE)); ) {
                
                prStatement.setInt(1, studentId);
                prStatement.setInt(2, courseId);
                jdbcDaoConnectionPool.releaseConnection(con);
                return prStatement.executeUpdate();
            }
        } catch (SQLException e) {
            LOGGER.error(ERROR_DELETE_STUDENT_FROM_COURSE, e);
            throw new DAOException(ERROR_DELETE_STUDENT_FROM_COURSE, e);
        }
    }

    @Override
    public CourseEntity getCourseById(int courseId) throws DAOException {
        ResultSet resultSet = null;
        CourseEntity course = null;
        
        try {
            Connection con = jdbcDaoConnectionPool.getConnection();
            
            try (PreparedStatement prStatement = con.prepareStatement(DAOPropertiesCache
                    .getInstance(SQL_QUERIES_FILENAME)
                    .getProperty(SELECT_COURSE));) {
                
                prStatement.setInt(1, courseId);
                resultSet = prStatement.executeQuery();

                while (resultSet.next()) {
                    course = new CourseEntity(resultSet.getInt(COURSE_ID), 
                                              resultSet.getString(COURSE_NAME),
                                              resultSet.getString(COURSE_DESC));
                }
            }
            jdbcDaoConnectionPool.releaseConnection(con);
            return course;
        } catch (SQLException e) {
            LOGGER.error(GET_COURSE_ERROR, e);
            throw new DAOException(GET_COURSE_ERROR, e);
        } finally {
            try {
                if (resultSet != null) {
                    resultSet.close();
                }
            } catch (SQLException e) {
                LOGGER.error(RESULTSET_ERROR, e);
            }
        }
    }

    @Override
    public List<CourseEntity> getAll() throws DAOException {
        try {
            Connection con = jdbcDaoConnectionPool.getConnection();
            List<CourseEntity> courseEntities = new ArrayList<>();
            
            try (Statement statement = con.createStatement();
                 ResultSet resultSet = statement.executeQuery(DAOPropertiesCache
                         .getInstance(SQL_QUERIES_FILENAME)
                         .getProperty(SELECT_ALL))) {

                while (resultSet.next()) {
                    courseEntities.add(new CourseEntity(resultSet.getInt(COURSE_ID), 
                                                        resultSet.getString(COURSE_NAME),
                                                        resultSet.getString(COURSE_DESC)));
                }
                jdbcDaoConnectionPool.releaseConnection(con);
            }
            return courseEntities;
        } catch (SQLException e) {
            LOGGER.error(GET_ALL_COURSES_ERROR, e);
            throw new DAOException(GET_ALL_COURSES_ERROR, e);
        }
    }

    @Override
    public Integer insert(List<CourseEntity> courseEntities) throws DAOException {
        try {
            Connection con = jdbcDaoConnectionPool.getConnection();
            int status = BAD_STATUS;
            
            try (PreparedStatement statement = con.prepareStatement(DAOPropertiesCache
                    .getInstance(SQL_QUERIES_FILENAME)
                    .getProperty(INSERT));) {
                
                con.setAutoCommit(false);
                Savepoint save = con.setSavepoint();

                try {
                    for (CourseEntity courseEntity : courseEntities) {
                        statement.setString(1, courseEntity.getCourseName());
                        status = statement.executeUpdate();
                    }
                    con.commit();
                } catch (SQLException e) {
                    if (con != null) {
                        try {
                            con.rollback(save);
                        } catch (SQLException exc) {
                            throw new SQLException(exc);
                        }
                    }
                    throw new SQLException(e);
                }
            }
            jdbcDaoConnectionPool.releaseConnection(con);
            return status;
        } catch (SQLException e) {
            LOGGER.error(INSERT__ERROR, e);
            throw new DAOException(INSERT__ERROR, e);
        }
    }
}
