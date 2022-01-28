package ua.com.foxminded.university;

import java.io.InputStream;
import java.util.Properties;
import ua.com.foxminded.university.dao.DAOException;
import ua.com.foxminded.university.dao.DAOException.PropertyFileLoadFail;

public class PropertyCache implements Cache<String> {
    private static final String PROPERTIES_FILE_NAME = "project.properties";
    private static final String ERROR_PROPERTIES_FILE = "The project properties has not been loaded.";
    private static final String ERROR_INSTANCE = "The instance of the "
            + "PostgresDAOPropertyCache class has not created.";
    private Properties property = new Properties();

    private PropertyCache() throws PropertyFileLoadFail {
        try (InputStream input = this.getClass()
                                     .getClassLoader()
                                     .getResourceAsStream(PROPERTIES_FILE_NAME)) {
            property.load(input);
        } catch (Exception e) {
            throw new DAOException.PropertyFileLoadFail(ERROR_PROPERTIES_FILE, e);
        }
    }

    public static PropertyCache getInstance() throws PropertyFileLoadFail {
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

        private static final PropertyCache getInstance() throws PropertyFileLoadFail {
            PropertyCache propertyCache;
            try {
                propertyCache = new PropertyCache();
            } catch (PropertyFileLoadFail e) {
                throw new PropertyFileLoadFail(ERROR_INSTANCE, e);
            }
            return propertyCache;
        }
    }
}
