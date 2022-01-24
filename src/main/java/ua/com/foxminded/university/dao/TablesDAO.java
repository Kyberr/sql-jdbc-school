package ua.com.foxminded.university.dao;

import java.sql.SQLException;

import ua.com.foxminded.university.dao.DAOException.DatabaseConnectionFail;
import ua.com.foxminded.university.dao.DAOException.TableCreationFail;

public interface TablesDAO {
    
    public int createTables(String sql) throws DatabaseConnectionFail, TableCreationFail;
}
