
package ua.com.foxminded.sql_jdbc_school.dao;

import java.io.InputStream;
import java.util.Properties;

public class DAOPropertiesCache implements DAOProperties<String> {
	private static final String ERROR_PROPERTIES_FILE = "The property file has not been loaded.";
	private static final String ERROR_INSTANCE = "The instance of the "
            + "DAOCache class has not been created.";
	private Properties property = new Properties();
	
	private DAOPropertiesCache(String fileName) throws DAOException {
		try (InputStream input = this.getClass().getClassLoader().getResourceAsStream(fileName)) {
			property.load(input);
		} catch (Exception e) {
			throw new DAOException(ERROR_PROPERTIES_FILE, e);
		}
	}
	
	public static DAOPropertiesCache getInstance(String fileName) throws DAOException {
		try {
			return LazyHolder.getInstance(fileName);
		} catch (DAOException e) {
			throw new DAOException(ERROR_INSTANCE, e);
		}
	}
	
	@Override
	public String getProperty(String key) throws DAOException {
		return property.getProperty(key);
	}
	
	private static class LazyHolder {
		
		private static final DAOPropertiesCache getInstance(String fileName) throws DAOException {
			DAOPropertiesCache postrgresCache;
			
			try {
				postrgresCache = new DAOPropertiesCache(fileName);
			} catch (DAOException e) {
				throw new DAOException(ERROR_INSTANCE, e);
			}
			return postrgresCache;
		}
	}
}
