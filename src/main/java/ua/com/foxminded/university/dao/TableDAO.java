package ua.com.foxminded.university.dao;

public interface TableDAO {
    
    public int createTables(String sql) throws DAOException.TableCreationFail;
}
