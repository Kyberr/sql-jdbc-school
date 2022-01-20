package ua.com.foxminded.university.dao;

import java.sql.SQLException;

public interface TablesDAO {
    
    public void createTables(String sql) throws SQLException;
}
