package ua.com.foxminded.sql_jdbc_school.dao.university;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import ua.com.foxminded.sql_jdbc_school.dao.ConnectionDAOFactory;
import ua.com.foxminded.sql_jdbc_school.dao.DAOException;
import ua.com.foxminded.sql_jdbc_school.dao.DAOPropertiesCache;

public class UniversityConnectionDAOFactory implements ConnectionDAOFactory {
	
	private static final Logger LOGGER = LogManager.getLogger();
	private static final String CREATE_CONNECTION = "The connect creation failed.";
	private static final String PROPERTIES_FILE_NAME = "db.properties";
    private static final String DB_URL = "universityDatabaseURL";
    private static final String USER_NAME = "universityUser";
    private static final String USER_PASS = "universityPassword";
    
    public Connection createConnection() throws DAOException {
    	Connection con = null;
    	
    	try {
            con = DriverManager.getConnection(DAOPropertiesCache.getInstance(PROPERTIES_FILE_NAME)
            													 .getProperty(DB_URL), 
            								   DAOPropertiesCache.getInstance(PROPERTIES_FILE_NAME)
            								   					 .getProperty(USER_NAME),
            								   DAOPropertiesCache.getInstance(PROPERTIES_FILE_NAME)
            								   					 .getProperty(USER_PASS));
    	} catch (SQLException | DAOException e) {
    		LOGGER.error(CREATE_CONNECTION, e);
    		throw new DAOException(CREATE_CONNECTION, e);
    	}
    	return con;
    }
}
