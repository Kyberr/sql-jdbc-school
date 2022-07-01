package ua.com.foxminded.sql_jdbs_school.dao.jdbc;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import ua.com.foxminded.sql_jdbc_school.dao.DAOConnectionPool;
import ua.com.foxminded.sql_jdbc_school.dao.DAOException;
import ua.com.foxminded.sql_jdbc_school.dao.jdbc.JdbcCourseDAO;
import ua.com.foxminded.sql_jdbc_school.entity.CourseEntity;

@ExtendWith(MockitoExtension.class)
@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = TestConfig.class)
@DirtiesContext(classMode = ClassMode.AFTER_EACH_TEST_METHOD)
public class JdbcCourseDAOTest {
    
    private static Properties PROPERTIES = null;
    private static final int COURSE_QUANTITY_OF_STUDENT = 1;
    private static final int COURSE_QUANTITY = 2;
    private static final int INSERTED_COURSE_QUANTITY = 1;
    private static final int TEST_STUDENT_ID = 1;
    private static final int TEST_COURSE_ID = 2;
    private static final String GET_ALL_COURSES = "getAllCourses";
    private static final String GET_STUDENT_OF_COURSE_BY_ID = "getStudentOfCourseById";
    private static final String GET_COURSE_HAVING_NAME_TEST = "getCourseHavingNameTest";
    private static final String TEST_COURSE_NAME = "test";
    private static final String TEST_QUERIES = "test-queries.properties";

    @Autowired
    JdbcTemplate jdbcTemplate;
    
    @InjectMocks
    JdbcCourseDAO jdbcCourseDao;
    
    @Mock
    DAOConnectionPool daoConnectionPoolMock;
    
    @BeforeAll
    static void init() throws IOException {
        try (InputStream testQueries = Thread.currentThread().getContextClassLoader()
                                                             .getResourceAsStream(TEST_QUERIES);) {
            PROPERTIES = new Properties();
            PROPERTIES.load(testQueries);
        }
    }
    
    @Test
    void deleteAll() throws DAOException, SQLException {
        try (Connection connection = jdbcTemplate.getDataSource().getConnection();) {
            when(daoConnectionPoolMock.getConnection()).thenReturn(connection);
            jdbcCourseDao.deleteAll();
            
            try (Statement statement = connection.createStatement();
                 ResultSet resultSet = statement.executeQuery(PROPERTIES.getProperty(GET_ALL_COURSES));) {
                assertFalse(resultSet.next());
            }
        }
    }
    
    @Test
    void getCoursesOfStudentById_GettingCoursesOfStudent_RightCourseQuantity() throws SQLException, 
                                                                                      DAOException {
        try (Connection connection = jdbcTemplate.getDataSource().getConnection();) {
            when(daoConnectionPoolMock.getConnection()).thenReturn(connection);
            List<CourseEntity> courses = jdbcCourseDao.getCoursesOfStudentById(TEST_STUDENT_ID);
            assertEquals(COURSE_QUANTITY_OF_STUDENT, courses.size());
        }
    }
    
    @Test
    void deleteStudentFromCourseById_DeletingStudentFromCourse_DatabaseHasNoSuchRelation() 
            throws SQLException, DAOException {
        try (Connection connection = jdbcTemplate.getDataSource().getConnection();) {
            when(daoConnectionPoolMock.getConnection()).thenReturn(connection);
            jdbcCourseDao.deleteStudentFromCourseById(TEST_STUDENT_ID, TEST_COURSE_ID);
            
            try (Statement statement = connection.createStatement();
                 ResultSet resultSet = statement.executeQuery(PROPERTIES
                         .getProperty(GET_STUDENT_OF_COURSE_BY_ID));) {
                
                assertFalse(resultSet.next());
            }
        }
    }
    
    @Test
    void getCourseById_GettingCourseById_RightIdOfGettedCourse() throws SQLException, DAOException {
        try (Connection connection = jdbcTemplate.getDataSource().getConnection();) {
            when(daoConnectionPoolMock.getConnection()).thenReturn(connection);
            CourseEntity course = jdbcCourseDao.getCourseById(TEST_COURSE_ID);
            assertEquals(TEST_COURSE_ID, course.getCourseId());
        }
    }
    
    @Test
    void getAll_GettingAllCourses_RightCoursQuantity() throws SQLException, DAOException {
        try (Connection connection = jdbcTemplate.getDataSource().getConnection();) {
            when(daoConnectionPoolMock.getConnection()).thenReturn(connection);
            List<CourseEntity> courses = jdbcCourseDao.getAll();
            assertEquals(COURSE_QUANTITY, courses.size());
        }
    }
    
    @Test
    void insert_InsertionOfCourses_DatabaseHasCourses() throws SQLException, DAOException {
        try (Connection connection = jdbcTemplate.getDataSource().getConnection();) {
            CourseEntity course = new CourseEntity(TEST_COURSE_ID, TEST_COURSE_NAME);
            List<CourseEntity> courseList = new ArrayList<>();
            courseList.add(course);
            when(daoConnectionPoolMock.getConnection()).thenReturn(connection);
            jdbcCourseDao.insert(courseList);
            List<Map<String, Object>> insertedCourse = jdbcTemplate.queryForList(PROPERTIES
                    .getProperty(GET_COURSE_HAVING_NAME_TEST));
            assertEquals(INSERTED_COURSE_QUANTITY, insertedCourse.size());
        }
    }
}
