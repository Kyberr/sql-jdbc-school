package ua.com.foxminded.sql_jdbc_school.dao;

import java.io.InputStream;
import java.util.Properties;

public class UniversityDAOPropertiesCache implements DAOPropertiesCache<String> {
    private static final String PROPERTIES_FILE_NAME = "universityDAO.properties";
    private static final String ERROR_PROPERTIES_FILE = "The database properties has not been loaded.";
    private static final String ERROR_INSTANCE = "The instance of the "
            + "UniversityDAOPropertyCache class has not created.";
    private Properties property = new Properties();

    private UniversityDAOPropertiesCache() throws DAOException.PropertyFileLoadingFail {
        try (InputStream input = this.getClass()
                                     .getClassLoader()
                                     .getResourceAsStream(PROPERTIES_FILE_NAME)) {
            property.load(input);
        } catch (Exception e) {
            throw new DAOException.PropertyFileLoadingFail (ERROR_PROPERTIES_FILE, e);
        }
    }

    public static UniversityDAOPropertiesCache getInstance() throws DAOException.PropertyFileLoadingFail {
        try {
            return LazyHolder.getInstance();
        } catch (DAOException.PropertyFileLoadingFail e) {
            throw new DAOException.PropertyFileLoadingFail(ERROR_INSTANCE, e);
        }
    }
    
    @Override
    public String getProperty(String key) throws DAOException.PropertyFileLoadingFail {
        return property.getProperty(key);
    }

    private static class LazyHolder {

        private static final UniversityDAOPropertiesCache getInstance() throws DAOException.PropertyFileLoadingFail {
            UniversityDAOPropertiesCache propertyCache;
            
            try {
                propertyCache = new UniversityDAOPropertiesCache();
            } catch (DAOException.PropertyFileLoadingFail e) {
                throw new DAOException.PropertyFileLoadingFail(ERROR_INSTANCE, e);
            }
            return propertyCache;
        }
    }
}
