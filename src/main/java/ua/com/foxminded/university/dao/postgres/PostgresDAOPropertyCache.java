package ua.com.foxminded.university.dao.postgres;

import java.io.InputStream;
import java.util.Properties;
import ua.com.foxminded.university.dao.DAOException;
import ua.com.foxminded.university.dao.DAOException.PropertyFileLoadFail;
import ua.com.foxminded.university.dao.DAOPropertyCache;

public class PostgresDAOPropertyCache implements DAOPropertyCache<String> {
    private static final String PROPERTIES_FILE_NAME = "database.properties";
    private static final String ERROR_PROPERTIES_FILE = "The database properties has not loaded.";
    private static final String ERROR_INSTANCE = "The instance of the "
            + "PostgresDAOPropertyCache class has not created.";
    private Properties property = new Properties();

    private PostgresDAOPropertyCache() throws PropertyFileLoadFail {
        try (InputStream input = this.getClass()
                                     .getClassLoader()
                                     .getResourceAsStream(PROPERTIES_FILE_NAME)) {
            property.load(input);
        } catch (Exception e) {
            throw new DAOException.PropertyFileLoadFail(ERROR_PROPERTIES_FILE, e);
        }
    }

    public static PostgresDAOPropertyCache getInstance() throws PropertyFileLoadFail {
        try {
            return LazyHolder.getInstance();
        } catch (PropertyFileLoadFail e) {
            throw new PropertyFileLoadFail(ERROR_INSTANCE, e);
        }
    }
    
    @Override
    public String getProperty(String key) {
        return property.getProperty(key);
    }

    private static class LazyHolder {

        private static final PostgresDAOPropertyCache getInstance() throws PropertyFileLoadFail {
            PostgresDAOPropertyCache propertyCache;
            try {
                propertyCache = new PostgresDAOPropertyCache();
            } catch (PropertyFileLoadFail e) {
                throw new PropertyFileLoadFail(ERROR_INSTANCE, e);
            }
            return propertyCache;
        }
    }
}
