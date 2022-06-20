package ua.com.foxminded.sql_jdbc_school.dao.university;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import ua.com.foxminded.sql_jdbc_school.dao.ConnectionDAOFactory;
import ua.com.foxminded.sql_jdbc_school.dao.DAOPropertiesCache;

public class UniversityConnectionDAOFactory implements ConnectionDAOFactory {
	
	private static final String PROPERTIES_FILE_NAME = "db.properties";
    private static final String DB_URL = "universityDatabaseURL";
    private static final String USER_NAME = "universityUser";
    private static final String USER_PASS = "universityPassword";
    
    public Connection createConnection() throws IOException, SQLException {
        	DAOPropertiesCache dbProperitesCache = DAOPropertiesCache.getInstance(PROPERTIES_FILE_NAME);
            return DriverManager.getConnection(dbProperitesCache.getProperty(DB_URL), 
            								   dbProperitesCache.getProperty(USER_NAME),
            								   dbProperitesCache.getProperty(USER_PASS));
    }
}
