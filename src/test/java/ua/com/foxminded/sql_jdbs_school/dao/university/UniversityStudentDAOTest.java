package ua.com.foxminded.sql_jdbs_school.dao.university;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Properties;
import java.util.stream.Collectors;

import org.junit.jupiter.api.Test;

import ua.com.foxminded.sql_jdbc_school.dao.DAOException;

public class UniversityStudentDAOTest {
	
	private static final String PROPERTIES_FILENAME = "testDb.properites";
	private static final String DB_URL = "TestDatabaseURL";
    private static final String USER_NAME = "TestDatabaseUser";
    private static final String USER_PASS = "TestDatabasePassword";
    private static final String TABLES_SCRIPT_FILENAME = "tables.sql";
    private static final String TEST_DATA_FILENAME = "test-data.sql";
    private static final String END_LINE = "\n";
	
	@Test
	void readStudentsHavingGroupId_Call_CorrectStudnetQuantity() throws SQLException, DAOException, IOException {
		try {
			InputStream input = this.getClass().getClassLoader().getResourceAsStream(PROPERTIES_FILENAME);
			Properties testDbProperties = new Properties();
			testDbProperties.load(input);
			Connection con = DriverManager.getConnection(testDbProperties.getProperty(DB_URL),
														 testDbProperties.getProperty(USER_NAME),
														 testDbProperties.getProperty(USER_PASS));
			
			URL tablesScriptUrl = this.getClass().getClassLoader().getResource(TABLES_SCRIPT_FILENAME);
			String tablesScriptPath = new File(tablesScriptUrl.getFile()).getPath();
			String tablesScript = Files.lines(Paths.get(tablesScriptPath))
							           .collect(Collectors.joining(END_LINE));
			PreparedStatement prStatement = con.prepareStatement(tablesScript);
			prStatement.execute();
			URL testDataScriptUrl = this.getClass().getClassLoader().getResource(TEST_DATA_FILENAME);
			String testDataScriptPath = new File(testDataScriptUrl.getFile()).getAbsolutePath();
			String testDataScript = Files.lines(Paths.get(testDataScriptPath))
										 .collect(Collectors.joining(END_LINE));
			prStatement.execute(testDataScript);
			
		} finally {
			
		}
	}
	

}
