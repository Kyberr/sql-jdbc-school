package ua.com.foxminded.university.dao;

import ua.com.foxminded.university.dao.DAOException.DatabaseConnectionFail;
import ua.com.foxminded.university.dao.DAOException.TableCreationFail;

public interface TableDAO {
    
    public int createTables(String sql) throws DatabaseConnectionFail, TableCreationFail;
}
