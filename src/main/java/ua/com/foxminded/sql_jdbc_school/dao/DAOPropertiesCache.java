
package ua.com.foxminded.sql_jdbc_school.dao;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class DAOPropertiesCache implements DAOProperties<String> {
    private static final Logger LOGGER = LogManager.getLogger();
    private static final String GET_INSTANCE_ERROR = "The creating of the DAOPropertiesCache instance failed.";
    private Properties property = new Properties();

    private DAOPropertiesCache(String fileName) throws IOException {
        try (InputStream input = this.getClass().getClassLoader().getResourceAsStream(fileName)) {
            property.load(input);
        }
    }

    public static DAOPropertiesCache getInstance(String fileName) throws DAOException {
        try {
            return LazyHolder.getInstance(fileName);
        } catch (IOException e) {
            LOGGER.error(GET_INSTANCE_ERROR, e);
            throw new DAOException(GET_INSTANCE_ERROR, e);
        }
    }

    @Override
    public String getProperty(String key) {
        return property.getProperty(key);
    }

    private static class LazyHolder {

        private static final DAOPropertiesCache getInstance(String fileName) throws IOException {
            DAOPropertiesCache propetiesCache;
            propetiesCache = new DAOPropertiesCache(fileName);
            return propetiesCache;
        }
    }
}
