package ua.com.foxminded.sql_jdbc_school.services;

public interface TableService<T> {
    
    public T creatTables() throws ServicesException.TableCreationFail;
}
