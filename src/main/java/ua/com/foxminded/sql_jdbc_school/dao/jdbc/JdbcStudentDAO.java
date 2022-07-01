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

import ua.com.foxminded.sql_jdbc_school.dao.DAOConnectionPool;
import ua.com.foxminded.sql_jdbc_school.dao.DAOException;
import ua.com.foxminded.sql_jdbc_school.dao.DAOPropertiesCache;
import ua.com.foxminded.sql_jdbc_school.dao.StudentDAO;
import ua.com.foxminded.sql_jdbc_school.entity.CourseEntity;
import ua.com.foxminded.sql_jdbc_school.entity.StudentEntity;

public class JdbcStudentDAO implements StudentDAO {

    private static final Logger LOGGER = LogManager.getLogger();
    private static final int BAD_STATUS = 0;
    private static final String DELETE_ALL_ERROR = "Deletion of all students failed.";
    private static final String DELETE_ALL = "deleteAll";
    private static final String GET_STUDENTS_OF_COURS_BY_ID = "getStudentsOfCourseByID";
    private static final String ERROR_GET_STUDENTS_OF_COURS_BY_ID = "Getting students of the course by "
                                                                  + "Id is failed.";
    private static final String GET_STUDENT_OF_COURSE_BY_ID = "getStudentOfCourseById";
    private static final String ERROR_GET_STUDENT_OF_COURSE_BY_ID = "Getting the student from "
                                                                  + "the course by ID is failed.";
    private static final String ERROR_GET_STUDENTS_HAVING_COURSE = "Receiving students have been added "
                                                                 + "to courses is failed.";
    private static final String GET_STUDENTS_HAVING_COURSE = "getStudentsHavingCouse";
    private static final String ERROR_ADD_STUDENT_TO_COURSE = "The student has not been added to the course";
    private static final String ADD_STUDENT_TO_COURSE = "addStudentToCourse";
    private static final String SQL_QUERIES_FILENAME = "student-queries.properties";
    private static final String SELECT_STUDENTS_WITH_GROUP = "selectStudentsWithGroup";
    private static final String SELECT_STUDENT = "selectStudent";
    private static final String DELETE_STUDENT = "deleteStudent";
    private static final String INSERT_STUDENTS = "insertStudent";
    private static final String SELECT_ALL = "selectAll";
    private static final String UPDATE = "update";
    private static final String STUDENT_ID = "student_id";
    private static final String GROUP_ID = "group_id";
    private static final String FIRST_NAME = "first_name";
    private static final String LAST_NAME = "last_name";
    private static final String ERROR_GET_ALL = "The getting of all the students failed.";
    private static final String ERROR_INSERT = "The inserting of the students failed.";
    private static final String ERROR_UDATE = "The updating of the students infurmation failed.";
    private static final String ERROR_DELETE = "The deletion of the student data failed.";
    private static final String ERROR_GET_STUDENT = "Getting the student data failed.";
    private static final String ERROR_GET_STUDENTS_WITHOUT_GROUP = "Getting the student data, that have no "
                                                                 + "group ID failed.";
    private DAOConnectionPool jdbcDaoConnectionPool;

    public JdbcStudentDAO(DAOConnectionPool connectionPool) {
        this.jdbcDaoConnectionPool = connectionPool;
    }
    
    @Override
    public int deleteAll() throws DAOException {
        try {
            int status = BAD_STATUS;
            Connection connection = jdbcDaoConnectionPool.getConnection();
             
            try (PreparedStatement preparedStatement = connection.prepareStatement(DAOPropertiesCache
                    .getInstance(SQL_QUERIES_FILENAME)
                    .getProperty(DELETE_ALL));) {
                
                status = preparedStatement.executeUpdate();
            }
            jdbcDaoConnectionPool.releaseConnection(connection);
            return status;
        } catch (SQLException e) {
            LOGGER.error(DELETE_ALL_ERROR, e);
            throw new DAOException(DELETE_ALL_ERROR, e);
        }
    }

    @Override
    public List<StudentEntity> getStudensOfCourseById(int courseId) throws DAOException {
        try {
            StudentEntity studentHavingCourse = null;
            List<StudentEntity> studentsHavingCourse = new ArrayList<>();
            Connection connection = jdbcDaoConnectionPool.getConnection();
            
            try (PreparedStatement preparedStatement = connection.prepareStatement(DAOPropertiesCache
                    .getInstance(SQL_QUERIES_FILENAME)
                    .getProperty(GET_STUDENTS_OF_COURS_BY_ID));) {
                
                preparedStatement.setInt(1, courseId);
                
                try (ResultSet resultSet = preparedStatement.executeQuery();) {
                    while (resultSet.next()) {
                        studentHavingCourse = new StudentEntity(resultSet.getInt(STUDENT_ID), 
                                                                resultSet.getInt(GROUP_ID),
                                                                resultSet.getString(FIRST_NAME), 
                                                                resultSet.getString(LAST_NAME));
                        studentsHavingCourse.add(studentHavingCourse);
                    }
                }
            } 
            jdbcDaoConnectionPool.releaseConnection(connection);
            return studentsHavingCourse;
        } catch (SQLException e) {
        LOGGER.error(ERROR_GET_STUDENTS_OF_COURS_BY_ID, e);
        throw new DAOException(ERROR_GET_STUDENTS_OF_COURS_BY_ID, e);
        } 
    }

    @Override
    public StudentEntity getStudentOfCourseById(int studentId, 
                                                int courseId) throws DAOException {
        try {
            StudentEntity studentHavingCourse = null;
            Connection connection = jdbcDaoConnectionPool.getConnection();

            try (PreparedStatement preparedStatement = connection.prepareStatement(DAOPropertiesCache
                    .getInstance(SQL_QUERIES_FILENAME)
                    .getProperty(GET_STUDENT_OF_COURSE_BY_ID));) {

                preparedStatement.setInt(1, studentId);
                preparedStatement.setInt(2, courseId);
                
                try (ResultSet resultSet = preparedStatement.executeQuery();) {
                    while (resultSet.next()) {
                        studentHavingCourse = new StudentEntity(resultSet.getInt(STUDENT_ID), 
                                                                resultSet.getInt(GROUP_ID),
                                                                resultSet.getString(FIRST_NAME), 
                                                                resultSet.getString(LAST_NAME));
                    }
                }
                
            }
            jdbcDaoConnectionPool.releaseConnection(connection);
            return studentHavingCourse;
        } catch (SQLException e) {
            LOGGER.error(ERROR_GET_STUDENT_OF_COURSE_BY_ID, e);
            throw new DAOException(ERROR_GET_STUDENT_OF_COURSE_BY_ID, e);
        } 
    }

    @Override
    public List<StudentEntity> getAllStudentsHavingCouse() throws DAOException {
        try {
            Connection connection = jdbcDaoConnectionPool.getConnection();
            StudentEntity studentHavingCourse = null;
            List<StudentEntity> studentsHavingCourse = new ArrayList<>();

            try (PreparedStatement preparedStatement = connection.prepareStatement(DAOPropertiesCache
                    .getInstance(SQL_QUERIES_FILENAME)
                    .getProperty(GET_STUDENTS_HAVING_COURSE));
                 ResultSet resultSet = preparedStatement.executeQuery();) {

                while (resultSet.next()) {
                    studentHavingCourse = new StudentEntity(resultSet.getInt(STUDENT_ID),
                                                            resultSet.getInt(GROUP_ID), 
                                                            resultSet.getString(FIRST_NAME),
                                                            resultSet.getString(LAST_NAME));
                    studentsHavingCourse.add(studentHavingCourse);
                }
            }
            jdbcDaoConnectionPool.releaseConnection(connection);
            return studentsHavingCourse;
        } catch (SQLException e) {
            LOGGER.error(ERROR_GET_STUDENTS_HAVING_COURSE, e);
            throw new DAOException(ERROR_GET_STUDENTS_HAVING_COURSE, e);
        }
    }

    @Override
    public int addStudentToCourse(StudentEntity student, CourseEntity course) throws DAOException {
        try {
            Connection connection = jdbcDaoConnectionPool.getConnection();
            int status = 0;

            try (PreparedStatement preparedStatement = connection.prepareStatement(DAOPropertiesCache
                    .getInstance(SQL_QUERIES_FILENAME)
                    .getProperty(ADD_STUDENT_TO_COURSE));) {

                connection.setAutoCommit(false);
                Savepoint save = connection.setSavepoint();

                try {
                    preparedStatement.setObject(1, student.getStudentId());
                    preparedStatement.setObject(2, student.getGroupId());
                    preparedStatement.setObject(3, student.getFirstName());
                    preparedStatement.setObject(4, student.getLastName());
                    preparedStatement.setObject(5, course.getCourseId());
                    preparedStatement.setObject(6, course.getCourseName());
                    preparedStatement.setObject(7, course.getCourseDescription());
                    status = preparedStatement.executeUpdate();
                    connection.commit();
                    jdbcDaoConnectionPool.releaseConnection(connection);
                } catch (SQLException e) {
                    if (connection != null) {
                        connection.rollback(save);
                    }
                    throw new SQLException(e);
                }
            }
            return status;
        } catch (SQLException e) {
            LOGGER.error(ERROR_ADD_STUDENT_TO_COURSE, e);
            throw new DAOException(ERROR_ADD_STUDENT_TO_COURSE, e);
        }
    }

    @Override
    public List<StudentEntity> getStudentsHavingGroupId() throws DAOException {
        try {
            Connection connection = jdbcDaoConnectionPool.getConnection();
            StudentEntity studentHavingGroupId = null;
            List<StudentEntity> studentsHavingGroupId = new ArrayList<>();

            try (PreparedStatement preparedStatement = connection.prepareStatement(DAOPropertiesCache
                    .getInstance(SQL_QUERIES_FILENAME)
                    .getProperty(SELECT_STUDENTS_WITH_GROUP));
                 ResultSet resultSet = preparedStatement.executeQuery();) {

                while (resultSet.next()) {
                    studentHavingGroupId = new StudentEntity(resultSet.getInt(STUDENT_ID), 
                                                             resultSet.getInt(GROUP_ID),
                                                             resultSet.getString(FIRST_NAME), 
                                                             resultSet.getString(LAST_NAME));
                    studentsHavingGroupId.add(studentHavingGroupId);
                }
            }
            jdbcDaoConnectionPool.releaseConnection(connection);
            return studentsHavingGroupId;
        } catch (SQLException e) {
            LOGGER.error(ERROR_GET_STUDENTS_WITHOUT_GROUP, e);
            throw new DAOException(ERROR_GET_STUDENTS_WITHOUT_GROUP, e);
        }
    }

    @Override
    public StudentEntity getStudentById(int studentId) throws DAOException {
        
        try {
            StudentEntity student = null;
            Connection connection = jdbcDaoConnectionPool.getConnection();

            try (PreparedStatement preparedStatement = connection.prepareStatement(DAOPropertiesCache
                    .getInstance(SQL_QUERIES_FILENAME)
                    .getProperty(SELECT_STUDENT));) {
                
                preparedStatement.setInt(1, studentId);
                
                try (ResultSet resultSet = preparedStatement.executeQuery();) {
                    while (resultSet.next()) {
                        student = new StudentEntity(resultSet.getInt(STUDENT_ID), 
                                                    resultSet.getInt(GROUP_ID),
                                                    resultSet.getString(FIRST_NAME), 
                                                    resultSet.getString(LAST_NAME));
                    }
                }
            }
            jdbcDaoConnectionPool.releaseConnection(connection);
            return student;
        } catch (SQLException e) {
            LOGGER.error(ERROR_GET_STUDENT, e);
            throw new DAOException(ERROR_GET_STUDENT, e);
        } 
    }

    @Override
    public int deleteStudentById(int studentId) throws DAOException {
        try {
            Connection connection = jdbcDaoConnectionPool.getConnection();
            
            try (PreparedStatement preparedStatement = connection.prepareStatement(DAOPropertiesCache
                    .getInstance(SQL_QUERIES_FILENAME)
                    .getProperty(DELETE_STUDENT));) {
                
                preparedStatement.setInt(1, studentId);
                jdbcDaoConnectionPool.releaseConnection(connection);
                return preparedStatement.executeUpdate();
            }
        } catch (SQLException e) {
            LOGGER.error(ERROR_DELETE, e);
            throw new DAOException(ERROR_DELETE, e);
        }
    }

    @Override
    public int insert(List<StudentEntity> studentEntities) throws DAOException {
        try {
            Connection connection = jdbcDaoConnectionPool.getConnection();
            int status = BAD_STATUS;
            
            try (PreparedStatement preparedStatement = connection.prepareStatement(DAOPropertiesCache
                    .getInstance(SQL_QUERIES_FILENAME)
                    .getProperty(INSERT_STUDENTS));) {
                
                connection.setAutoCommit(false);
                Savepoint save = connection.setSavepoint();
                
                try {
                    for (StudentEntity student : studentEntities) {
                        preparedStatement.setObject(1, student.getGroupId());
                        preparedStatement.setString(2, student.getFirstName());
                        preparedStatement.setString(3, student.getLastName());
                        status = preparedStatement.executeUpdate();
                    }
                    connection.commit();
                } catch (SQLException e) {
                    if (connection != null) {
                        connection.rollback(save);
                    }
                    throw new SQLException(e);
                }
            }
            jdbcDaoConnectionPool.releaseConnection(connection);
            return status;
        } catch (SQLException e) {
            LOGGER.error(ERROR_INSERT, e);
            throw new DAOException(ERROR_INSERT, e);
        }
    }

    @Override
    public List<StudentEntity> getAll() throws DAOException {
        try {
            Connection connection = jdbcDaoConnectionPool.getConnection();
            StudentEntity student = null;
            List<StudentEntity> students = new ArrayList<>();
            
            try (Statement statement = connection.createStatement();
                 ResultSet resultSet = statement.executeQuery(DAOPropertiesCache
                         .getInstance(SQL_QUERIES_FILENAME)
                         .getProperty(SELECT_ALL));) {

                while (resultSet.next()) {
                    student = new StudentEntity(resultSet.getInt(STUDENT_ID),
                                                resultSet.getInt(GROUP_ID), 
                                                resultSet.getString(FIRST_NAME),
                                                resultSet.getString(LAST_NAME));
                    students.add(student);
                }
            }
            jdbcDaoConnectionPool.releaseConnection(connection);
            return students;
        } catch (SQLException e) {
            LOGGER.error(ERROR_GET_ALL, e);
            throw new DAOException(ERROR_GET_ALL, e);
        }
    }

    @Override
    public int update(List<StudentEntity> students) throws DAOException {
        try {
            Connection connection = jdbcDaoConnectionPool.getConnection();
            int status = BAD_STATUS;
            
            try (PreparedStatement preparedStatement = connection.prepareStatement(DAOPropertiesCache
                        .getInstance(SQL_QUERIES_FILENAME)
                        .getProperty(UPDATE));) {
                
                connection.setAutoCommit(false);
                Savepoint save = connection.setSavepoint();
                
                try {
                    for (StudentEntity student : students) {
                        preparedStatement.setInt(4, student.getStudentId());
                        preparedStatement.setInt(1, student.getGroupId());
                        preparedStatement.setString(2, student.getFirstName());
                        preparedStatement.setString(3, student.getLastName());
                        status = preparedStatement.executeUpdate();
                    }
                    connection.commit();
                } catch (SQLException e) {
                    if (connection != null) {
                        connection.rollback(save);
                    }
                    throw new SQLException(e);
                }
            }
            jdbcDaoConnectionPool.releaseConnection(connection);
            return status;
        } catch (SQLException e) {
            LOGGER.error(ERROR_UDATE, e);
            throw new DAOException(ERROR_UDATE, e);
        }
    }
}
