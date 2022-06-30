package ua.com.foxminded.sql_jdbs_school.dao.jdbc;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyInt;
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

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.opentest4j.AssertionFailedError;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;
import org.springframework.test.annotation.DirtiesContext.MethodMode;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import ua.com.foxminded.sql_jdbc_school.dao.DAOConnectionPool;
import ua.com.foxminded.sql_jdbc_school.dao.DAOException;
import ua.com.foxminded.sql_jdbc_school.dao.jdbc.JdbcStudentDAO;
import ua.com.foxminded.sql_jdbc_school.entity.StudentEntity;

@ExtendWith(SpringExtension.class)
@ExtendWith(MockitoExtension.class)
@ContextConfiguration(classes = TestConfig.class)
@DirtiesContext(classMode = ClassMode.AFTER_EACH_TEST_METHOD)
class JdbcStudentDAOTest {
    
    private static final Logger LOGGER = LogManager.getLogger();
    private static Properties PROPERTIES = null;
    private static final String STUDENT_ID = "student_id";
    private static final String GROUP_ID = "group_id";
    private static final String FIRST_NAME = "first_name";
    private static final String LAST_NAME = "last_name";
    private static final String TEST_QUERIES = "test-queries.properties";
    private static final String STUDENT_LAST_NAME = "test";
    private static final String STUDENT_FIRST_NAME = "test";
    private static final String GET_STUDENT_HAVING_TEST_LAST_NAME = "getStudentHavingTestLastName";
    private static final String GET_STUDENT_HAVING_ID_ONE = "getStudentHavingIdOne";
    private static final int STUDENT_GROUP_ID = 2;
    private static final int TEST_STUDENT_ID = 1;
    
    @InjectMocks
    JdbcStudentDAO jdbcStudentDao;
    
    @Autowired
    private JdbcTemplate jdbcTemplate;
    
    @Mock
    DAOConnectionPool daoConnectionPoolMock;
    
    @BeforeAll
    static void init() throws IOException {
        try (InputStream queries = Thread.currentThread().getContextClassLoader()
                                                         .getResourceAsStream(TEST_QUERIES);) {
            PROPERTIES = new Properties();
            PROPERTIES.load(queries);
        }
    }
    
    @Test
    void deleteById() throws SQLException, DAOException {
        try (Connection connection = jdbcTemplate.getDataSource().getConnection();) {
            when(daoConnectionPoolMock.getConnection()).thenReturn(connection);
            jdbcStudentDao.deleteStudentById(TEST_STUDENT_ID);
            Statement statement = jdbcTemplate.getDataSource().getConnection().createStatement();
            ResultSet resultSet = statement.executeQuery(PROPERTIES.getProperty(GET_STUDENT_HAVING_ID_ONE));
            assertFalse(resultSet.next());
        }
    }
    
    @Test
    void insert_InsertionOfStudent_DatabaseHasStudent() throws SQLException, DAOException, IOException {
        
        try (Connection connection = jdbcTemplate.getDataSource().getConnection()) {
            when(daoConnectionPoolMock.getConnection()).thenReturn(connection);
            StudentEntity student = new StudentEntity(STUDENT_GROUP_ID, STUDENT_FIRST_NAME, 
                                                      STUDENT_LAST_NAME);
            List<StudentEntity> studentList = new ArrayList<>();
            studentList.add(student);
            jdbcStudentDao.insert(studentList);
            Map<String, Object> insertedStudent = jdbcTemplate.queryForMap(PROPERTIES
                    .getProperty(GET_STUDENT_HAVING_TEST_LAST_NAME));
            
            assertEquals(STUDENT_LAST_NAME, insertedStudent.get(LAST_NAME));
            assertEquals(STUDENT_FIRST_NAME, insertedStudent.get(FIRST_NAME));
            assertEquals(STUDENT_GROUP_ID, insertedStudent.get(GROUP_ID));
        }
    }
    
    @Test
    void getAll_GettingAllStudents_RightStudentQuantity() throws SQLException, DAOException {
        
        try (Connection connection = jdbcTemplate.getDataSource().getConnection();) {
            when(daoConnectionPoolMock.getConnection()).thenReturn(connection);
            assertEquals(6, jdbcStudentDao.getAll().size());
        }
    }
    
    @Test
    void udate_UdatingStudentData_DatabaseHasUpdatedData() throws SQLException, DAOException, IOException {
        
        try (Connection connection = jdbcTemplate.getDataSource().getConnection();) {
            
            when(daoConnectionPoolMock.getConnection()).thenReturn(connection);
            StudentEntity student = new StudentEntity(TEST_STUDENT_ID, STUDENT_GROUP_ID, STUDENT_FIRST_NAME, 
                                                      STUDENT_LAST_NAME);
            List<StudentEntity> studentList = new ArrayList<>();
            studentList.add(student);
            jdbcStudentDao.update(studentList);
            Map<String, Object> studentFirstName = jdbcTemplate.queryForMap(
                    PROPERTIES.getProperty(GET_STUDENT_HAVING_ID_ONE));
            assertEquals(STUDENT_LAST_NAME, studentFirstName.get(LAST_NAME));
            assertEquals(STUDENT_FIRST_NAME, studentFirstName.get(FIRST_NAME));
            assertEquals(STUDENT_GROUP_ID, studentFirstName.get(GROUP_ID));
            assertEquals(TEST_STUDENT_ID, studentFirstName.get(STUDENT_ID));
        }
    }
}
