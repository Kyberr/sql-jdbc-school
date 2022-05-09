package ua.com.foxminded.sql_jdbc_school.dao;

public interface TableDAO {
    
    public int createTables(String sqlScript) throws DAOException;
}
