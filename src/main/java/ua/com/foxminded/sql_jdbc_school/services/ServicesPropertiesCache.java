package ua.com.foxminded.sql_jdbc_school.services;

public interface ServicesPropertiesCache<T> {
    
    public T getProperty(T key) throws ServicesException.PropertyFileLoadingFail;
}
