package ua.com.foxminded.university.dao.postgres;

import java.sql.SQLException;

public interface DatabaseDAO {

    public void createDatabase(String databaseName, String ownerDatabase) throws SQLException;

    public void deleteDatabase(String databaseName) throws SQLException;
}
