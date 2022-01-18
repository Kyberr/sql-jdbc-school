package ua.com.foxminded.school.dao;

import java.sql.SQLException;

public interface AccountDAO {

    public void createAccountDAO(String superuserName, 
                                 String superuserPass, 
                                 String accountName, 
                                 String accountPassword) throws SQLException;

    public void deleteAccountDAO(String superuserName, 
                                 String superuserPass, 
                                 String deleteName) throws SQLException;
}
