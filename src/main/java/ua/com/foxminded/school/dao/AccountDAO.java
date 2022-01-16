package ua.com.foxminded.school.dao;

import java.sql.SQLException;

public interface AccountDAO {
    public void createAccountDAO (String accountName, String accountPassword) throws SQLException;
    public void deleteAccountDAO(String accountName) throws SQLException;
}
