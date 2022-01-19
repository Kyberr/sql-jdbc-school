package ua.com.foxminded.university.dao;

import java.sql.SQLException;

public interface DatabaseDAO {

    public void createDatabase(String databaseName, String ownerAccount) throws SQLException;

    public void deleteDatabase(String databaseName) throws SQLException;
}
