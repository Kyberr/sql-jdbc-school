package ua.com.foxminded.university.dao;

import java.sql.SQLException;

public interface RoleDAO {

    public void createRole(String newAccountName, 
                              String newAccountPass) throws SQLException;

    public void deleteRole(String accountName) throws SQLException;
}
