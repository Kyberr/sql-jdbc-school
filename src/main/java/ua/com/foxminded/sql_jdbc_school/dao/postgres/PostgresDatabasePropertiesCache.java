package ua.com.foxminded.sql_jdbc_school.dao.postgres;

import java.io.InputStream;
import java.util.Properties;

import ua.com.foxminded.sql_jdbc_school.dao.DAOException;
import ua.com.foxminded.sql_jdbc_school.dao.DAOPropertiesCache;

public class PostgresDatabasePropertiesCache implements DAOPropertiesCache<String> {
    private static final String PROPERTIES_FILE_NAME = "postgresDatabase.properties";
    private static final String ERROR_PROPERTIES_FILE = "The database properties has not been loaded.";
    private static final String ERROR_INSTANCE = "The instance of the "
            + "UniversityDAOPropertyCache class has not been created.";
    private Properties property = new Properties();

    private PostgresDatabasePropertiesCache() throws DAOException {
        try (InputStream input = this.getClass()
                                     .getClassLoader()
                                     .getResourceAsStream(PROPERTIES_FILE_NAME)) {
            property.load(input);
        } catch (Exception e) {
            throw new DAOException (ERROR_PROPERTIES_FILE, e);
        }
    }

    public static PostgresDatabasePropertiesCache getInstance() throws DAOException {
        try {
            return LazyHolder.getInstance();
        } catch (DAOException e) {
            throw new DAOException(ERROR_INSTANCE, e);
        }
    }
    
    @Override
    public String getProperty(String key) throws DAOException {
        return property.getProperty(key);
    }

    private static class LazyHolder {

        private static final PostgresDatabasePropertiesCache getInstance() throws DAOException {
            PostgresDatabasePropertiesCache propertyCache;
            
            try {
                propertyCache = new PostgresDatabasePropertiesCache();
            } catch (DAOException e) {
                throw new DAOException(ERROR_INSTANCE, e);
            }
            return propertyCache;
        }
    }
}
