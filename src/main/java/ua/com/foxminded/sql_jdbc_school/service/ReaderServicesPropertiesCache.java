package ua.com.foxminded.sql_jdbc_school.service;

import java.io.InputStream;
import java.util.Properties;

public class ReaderServicesPropertiesCache implements ServicePropertyCache<String> {
    private static final String PROPERTIES_FILE_NAME = "readerServices.properties";
    private static final String ERROR_PROPERTIES_FILE = "The services properties has not been loaded.";
    private static final String ERROR_INSTANCE = "The instance of the "
            + "ReaderServicesPropertiesCache class has not created.";
    private Properties property = new Properties();

    private ReaderServicesPropertiesCache() throws ServiceException {
        try (InputStream input = this.getClass()
                                     .getClassLoader()
                                     .getResourceAsStream(PROPERTIES_FILE_NAME)) {
             property.load(input);
        } catch (Exception e) {
            throw new ServiceException (ERROR_PROPERTIES_FILE, e);
        }
    }

    public static ReaderServicesPropertiesCache getInstance() throws ServiceException {
        try {
            return LazyHolder.getInstance();
        } catch (ServiceException e) {
            throw new ServiceException(ERROR_INSTANCE, e);
        }
    }

    @Override
    public String getProperty(String key) throws ServiceException {
        return property.getProperty(key);
    }

    private static class LazyHolder {

        private static final ReaderServicesPropertiesCache getInstance() throws ServiceException{
            ReaderServicesPropertiesCache propertyCache;

            try {
                propertyCache = new ReaderServicesPropertiesCache();
            } catch (ServiceException e) {
                throw new ServiceException(ERROR_INSTANCE, e);
            }
            return propertyCache;
        }
    }
}
