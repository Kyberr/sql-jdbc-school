package ua.com.foxminded.sql_jdbs_school.dao.jdbc;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import ua.com.foxminded.sql_jdbc_school.dao.DAOConnectionPool;
import ua.com.foxminded.sql_jdbc_school.dao.DAOException;
import ua.com.foxminded.sql_jdbc_school.dao.jdbc.JdbcStudentDAO;

@ExtendWith(SpringExtension.class)
@ExtendWith(MockitoExtension.class)
@ContextConfiguration(classes = TestConfig.class)
@Sql({"/test-schema.sql", "/test-data.sql"})
class JdbcStudentDAOTest {
    private static final Logger LOGGER = LogManager.getLogger();
    
    @InjectMocks
    JdbcStudentDAO jdbcStudentDao;
    
    @Autowired
    private JdbcTemplate jdbcTemplate;
    
    @Mock
    DAOConnectionPool daoConnectionPoolMock;
    
    @Test
    void getAll() throws SQLException, DAOException {
        /*
        List<Map<String, Object>> students = jdbcTemplate.queryForList("select * from department.students");
        assertEquals(6, students.size());
        */
        
       // try {
        Connection connection = jdbcTemplate.getDataSource().getConnection();
        
            when(daoConnectionPoolMock.getConnection()).thenReturn(connection);
    //        jdbcStudentDao.getAll();
            assertEquals(6, jdbcStudentDao.getAll().size());
       // } catch (DAOException | SQLException e) {
         //   LOGGER.error("Error", e);
      //  }
        
     //   assertTrue(jdbcTemplate.getDataSource().getConnection() != null);
        
        
    }
    
    
	
    /*
    private static final String TEST_DB_PROP_PATH = "D:/repository/SqlJdbcSchool/"
												  + "src/main/resource/test-db.properties";
	private static final String TEST_TABLES_SCRIPT_PATH = "D:/repository/SqlJdbcSchool/"
														+ "src/main/resource/test-tables.sql";
	private static final String TEST_DATA_PATH = "D:/repository/SqlJdbcSchool/"
											   + "src/main/resource/test-data.sql";
	private static final String DB_URL = "databaseURL";
    private static final String USER_NAME = "databaseUser";
    private static final String USER_PASS = "databaseUserPassword";
    private static final String END_LINE = "\n";
    private static final Integer STUDENT_QUANTITY_HAVING_GROUP_ID = 5;
    
    @InjectMocks
    JdbcStudentDAO universityStudentDao;
    
    @Mock
    DAOConnectionFactory universityConnectionDaoFactory;
	
	@BeforeAll
	static void init() throws SQLException, DAOException, IOException {
		Connection con = null;
		Statement statement = null;
		PreparedStatement prStatement = null;
		
		try {
			FileInputStream testDbInput = new FileInputStream(TEST_DB_PROP_PATH);
			Properties testDbProperties = new Properties();
			testDbProperties.load(testDbInput);
			con = DriverManager.getConnection(testDbProperties.getProperty(DB_URL),
											  testDbProperties.getProperty(USER_NAME),
											  testDbProperties.getProperty(USER_PASS));
			
			Path tablesScriptPath = Paths.get(TEST_TABLES_SCRIPT_PATH);
			String tablesScript = Files.lines(tablesScriptPath)
									   .collect(Collectors.joining(END_LINE));
			statement = con.createStatement();
			statement.execute(tablesScript);
			Path testDataScriptPath = Paths.get(TEST_DATA_PATH);
			String testDataScript = Files.lines(testDataScriptPath)
										 .collect(Collectors.joining(END_LINE));
			prStatement = con.prepareStatement(testDataScript);
			prStatement.execute();
		}  finally {
			con.close();
			statement.close();
			prStatement.close();
		}
	}
	
	@Test
	void readStudentsHavingGroupId_Call_CorrectStudnetQuantity() throws IOException, 
																		SQLException, 
																		DAOException {
		Connection con = null;
		
		try {
			FileInputStream testDbInput = new FileInputStream(TEST_DB_PROP_PATH);
			Properties testDbProperties = new Properties();
			testDbProperties.load(testDbInput);
			con = DriverManager.getConnection(testDbProperties.getProperty(DB_URL),
											  testDbProperties.getProperty(USER_NAME),
											  testDbProperties.getProperty(USER_PASS));
			when(universityConnectionDaoFactory.createConnection()).thenReturn(con);
			assertEquals(STUDENT_QUANTITY_HAVING_GROUP_ID, 
					     universityStudentDao.getStudentsHavingGroupId().size());
		} finally {
			con.close();
		}
	}
	/*
	@Test
	void getById() throws SQLException, IOException, DAOException {
		Connection con = null;
		
		try {
			FileInputStream testDbInput = new FileInputStream(TEST_DB_PROP_PATH);
			Properties testDbProperties = new Properties();
			testDbProperties.load(testDbInput);
			con = DriverManager.getConnection(testDbProperties.getProperty(DB_URL),
											  testDbProperties.getProperty(USER_NAME),
											  testDbProperties.getProperty(USER_PASS));
			when(universityConnectionDaoFactory.createConnection()).thenReturn(con);
			assertEquals(STUDENT_QUANTITY_HAVING_GROUP_ID, 
					     universityStudentDao.().size());
		} finally {
			con.close();
		}
		
	}
	*/
}
