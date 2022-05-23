
package ua.com.foxminded.sql_jdbc_school.dao.postgres;

import java.io.InputStream;
import java.util.Properties;

import ua.com.foxminded.sql_jdbc_school.dao.DAOException;
import ua.com.foxminded.sql_jdbc_school.dao.DAOPropertiesCache;

public class PostgresCache implements DAOPropertiesCache<String> {
	private static final String ERROR_PROPERTIES_FILE = "The database properties has not been loaded.";
	private static final String ERROR_INSTANCE = "The instance of the "
            + "PostgresCache class has not been created.";
	private Properties property = new Properties();
	//private String fileName;
	
	private PostgresCache(String fileName) throws DAOException {
		try (InputStream input = this.getClass().getClassLoader().getResourceAsStream(fileName)) {
			property.load(input);
		} catch (Exception e) {
			throw new DAOException(ERROR_PROPERTIES_FILE, e);
		}
	}
	
	public static PostgresCache getInstance(String fileName) throws DAOException {
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
		
		private static final PostgresCache getInstance(String fileName) throws DAOException {
			PostgresCache postrgresCache;
			
			try {
				postrgresCache = new PostgresCache(fileName);
			} catch (DAOException e) {
				throw new DAOException(ERROR_INSTANCE, e);
			}
			return postrgresCache;
		}
	}
}
