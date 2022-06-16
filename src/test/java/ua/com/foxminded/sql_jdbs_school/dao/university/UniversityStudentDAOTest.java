package ua.com.foxminded.sql_jdbs_school.dao.university;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;
import java.util.stream.Collectors;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import ua.com.foxminded.sql_jdbc_school.dao.ConnectionDAOFactory;
import ua.com.foxminded.sql_jdbc_school.dao.DAOException;
import ua.com.foxminded.sql_jdbc_school.dao.university.UniversityStudentDAO;

@ExtendWith(MockitoExtension.class)
class UniversityStudentDAOTest {
	private static final String TEST_DB_PROP_PATH = "D:/repository/SqlJdbcSchool/src/main/resource/test-db.properties";
	private static final String TEST_TABLES_SCRIPT_PATH = "D:/repository/SqlJdbcSchool/src/main/resource/test-tables.sql";
	private static final String TEST_DATA_PATH = "D:/repository/SqlJdbcSchool/src/main/resource/test-data.sql";
	private static final String DB_URL = "databaseURL";
    private static final String USER_NAME = "databaseUser";
    private static final String USER_PASS = "databaseUserPassword";
    private static final String END_LINE = "\n";
    private static final Integer STUDENT_QUANTITY_HAVING_GROUP_ID = 5;
    
    @InjectMocks
    UniversityStudentDAO universityStudentDao;
    
    @Mock
    ConnectionDAOFactory universityConnectionDaoFactory;
	
	@Test
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
}
