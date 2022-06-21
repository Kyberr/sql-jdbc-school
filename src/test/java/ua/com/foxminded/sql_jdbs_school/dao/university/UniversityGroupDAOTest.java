package ua.com.foxminded.sql_jdbs_school.dao.university;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;
import java.util.stream.Collectors;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import ua.com.foxminded.sql_jdbc_school.dao.ConnectionDAOFactory;
import ua.com.foxminded.sql_jdbc_school.dao.DAOException;
import ua.com.foxminded.sql_jdbc_school.dao.university.UniversityGroupDAO;

@ExtendWith(MockitoExtension.class)
class UniversityGroupDAOTest {
	private static final int STUDENTS_QUANTITY = 3;
	private static final int RIGHT_GROUP_QUANITY = 2;
	private static final String GROUP_QUERIES = "D:/repository/SqlJdbcSchool/src/main/"
											  + "resource/group-queries.properties";
	private static final String SELECT_LESS_OR_EQUAL_STUDENTS = "getGroupsHavingLessOrEqualStudents";
	private static final String TEST_DATA_PATH = "D:/repository/SqlJdbcSchool/src/main/resource/test-data.sql";
	private static final String END_LINE = "\n";
	private static final String TEST_TABLES_SCRIPT_PATH = "D:/repository/SqlJdbcSchool/src/main/resource/test-tables.sql";
	private static final String TEST_DB_PROP_PATH = "D:/repository/SqlJdbcSchool/src/main/resource/test-db.properties";
	private static final String DB_URL = "databaseURL";
    private static final String USER_NAME = "databaseUser";
    private static final String USER_PASS = "databaseUserPassword";
    
	@InjectMocks
	UniversityGroupDAO universityGroupDao;
	
	@Mock
	ConnectionDAOFactory universityConnectionDaoFactory;
	
	@BeforeAll
	static void init() throws FileNotFoundException, SQLException, IOException {
		
		try (FileInputStream testDbInput = new FileInputStream(TEST_DB_PROP_PATH);) {
			
			Properties testDbPoperites = new Properties();
			testDbPoperites.load(testDbInput);
			
			try (Connection con = DriverManager.getConnection(testDbPoperites.getProperty(DB_URL),
					testDbPoperites.getProperty(USER_NAME), testDbPoperites.getProperty(USER_PASS));
					Statement statement = con.createStatement();) {

				Path tableScriptPath = Paths.get(TEST_TABLES_SCRIPT_PATH);
				String tablesScript = Files.lines(tableScriptPath).collect(Collectors.joining(END_LINE));
				statement.execute(tablesScript);

				Path testDataScriptPath = Paths.get(TEST_DATA_PATH);
				String testDataScrip = Files.lines(testDataScriptPath).collect(Collectors.joining(END_LINE));
				statement.execute(testDataScrip);
			}
		}
	}
	
	@Test
	void readGroupsWithLessOrEqualStudents_AnyAcceptableArg_RightStudentQuantity() throws FileNotFoundException, 
																						  IOException, 
																						  SQLException, 
																						  DAOException {
		
		try (FileInputStream testDb = new FileInputStream(TEST_DB_PROP_PATH);
			 FileInputStream groupQueries = new FileInputStream(GROUP_QUERIES);) {
			
			Properties properties = new Properties();
			properties.load(testDb);
			properties.load(groupQueries);
			
			try (Connection con = DriverManager.getConnection(properties.getProperty(DB_URL), 
															  properties.getProperty(USER_NAME), 
															  properties.getProperty(USER_PASS));
				 PreparedStatement prStatement = con.prepareStatement(String.format(properties
						 .getProperty(SELECT_LESS_OR_EQUAL_STUDENTS), STUDENTS_QUANTITY));
				 ResultSet resultSet = prStatement.executeQuery()) {
				
				when(universityConnectionDaoFactory.createConnection()).thenReturn(con);
				assertEquals(RIGHT_GROUP_QUANITY, 
							 universityGroupDao.getGroupsHavingLessOrEqualStudents(STUDENTS_QUANTITY).size());
			}
		}
	}
}
