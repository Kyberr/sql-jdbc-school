package ua.com.foxminded.sql_jdbc_school.services;

import java.io.InputStream;
import java.util.Properties;

public class ReaderServicesPropertiesCache implements ServicesPropertiesCache<String> {
    private static final String PROPERTIES_FILE_NAME = "readerServices.properties";
    private static final String ERROR_PROPERTIES_FILE = "The services properties has not been loaded.";
    private static final String ERROR_INSTANCE = "The instance of the "
            + "ReaderServicesPropertiesCache class has not created.";
    private Properties property = new Properties();

    private ReaderServicesPropertiesCache() throws ServicesException.PropertyFileLoadingFail {
        try (InputStream input = this.getClass()
                                     .getClassLoader()
                                     .getResourceAsStream(PROPERTIES_FILE_NAME)) {
            property.load(input);
        } catch (Exception e) {
            throw new ServicesException.PropertyFileLoadingFail (ERROR_PROPERTIES_FILE, e);
        }
    }

    public static ReaderServicesPropertiesCache getInstance() throws ServicesException.PropertyFileLoadingFail {
        try {
            return LazyHolder.getInstance();
        } catch (ServicesException.PropertyFileLoadingFail e) {
            throw new ServicesException.PropertyFileLoadingFail(ERROR_INSTANCE, e);
        }
    }

    @Override
    public String getProperty(String key) throws ServicesException.PropertyFileLoadingFail {
        return property.getProperty(key);
    }

    private static class LazyHolder {

        private static final ReaderServicesPropertiesCache getInstance() throws ServicesException
                                                                                   .PropertyFileLoadingFail {
            ReaderServicesPropertiesCache propertyCache;

            try {
                propertyCache = new ReaderServicesPropertiesCache();
            } catch (ServicesException.PropertyFileLoadingFail e) {
                throw new ServicesException.PropertyFileLoadingFail(ERROR_INSTANCE, e);
            }
            return propertyCache;
        }
    }
}
