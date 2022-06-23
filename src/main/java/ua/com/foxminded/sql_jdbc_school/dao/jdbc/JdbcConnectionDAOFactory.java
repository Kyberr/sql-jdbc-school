package ua.com.foxminded.sql_jdbc_school.dao.jdbc;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import ua.com.foxminded.sql_jdbc_school.dao.ConnectionDAOFactory;
import ua.com.foxminded.sql_jdbc_school.dao.DAOException;
import ua.com.foxminded.sql_jdbc_school.dao.DAOPropertiesCache;

public class JdbcConnectionDAOFactory implements ConnectionDAOFactory {

    private static final Logger LOGGER = LogManager.getLogger();
    private static final String CREATE_CONNECTION = "The connect creation failed.";
    private static final String DB_PROP_FILENAME = "db.properties";
    private static final String DB_URL = "universityDatabaseURL";
    private static final String USER_NAME = "universityUser";
    private static final String USER_PASS = "universityPassword";

    public Connection createConnection() throws DAOException {
        try {
            return DriverManager.getConnection(DAOPropertiesCache.getInstance(DB_PROP_FILENAME)
                                                                 .getProperty(DB_URL),
                                               DAOPropertiesCache.getInstance(DB_PROP_FILENAME)
                                                                 .getProperty(USER_NAME),
                                               DAOPropertiesCache.getInstance(DB_PROP_FILENAME)
                                                                 .getProperty(USER_PASS));
        } catch (SQLException e) {
            LOGGER.error(CREATE_CONNECTION, e);
            throw new DAOException(CREATE_CONNECTION, e);
        }
    }
}
