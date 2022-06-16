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
    private static final String TABLE_CRIETION_SCRIPT_FILENAME = "tableCreationScript.sql";
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
			
			URL tableCreationScriptUrl = this.getClass().getClassLoader().getResource(TABLE_CRIETION_SCRIPT_FILENAME);
			String tableCreationScriptPath = new File(tableCreationScriptUrl.getFile()).getPath();
			String tableCreationScript = Files.lines(Paths.get(tableCreationScriptPath))
							        		  .collect(Collectors.joining(END_LINE));
			PreparedStatement prStatement = con.prepareStatement(tableCreationScript);
			prStatement.execute();
			URL testDataAddingScriptUrl = this.getClass().getClassLoader().getResourse(sqlScript);
			
		} finally {
			
		}
	}
	

}
