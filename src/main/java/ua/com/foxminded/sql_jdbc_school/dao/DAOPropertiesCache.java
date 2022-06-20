
package ua.com.foxminded.sql_jdbc_school.dao;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class DAOPropertiesCache implements DAOProperties<String> {
	private Properties property = new Properties();
	
	private DAOPropertiesCache(String fileName) throws IOException {
		try (InputStream input = this.getClass().getClassLoader().getResourceAsStream(fileName)) {
			property.load(input);
		}
	}
	
	public static DAOPropertiesCache getInstance(String fileName) throws IOException {
			return LazyHolder.getInstance(fileName);
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
