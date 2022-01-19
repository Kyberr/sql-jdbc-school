package ua.com.foxminded.university.dao;

import java.sql.SQLException;

public interface AccountDAO {

    public void createAccount(String newAccountName, 
                                 String newAccountPass) throws SQLException;

    public void deleteAccount(String superuserName, 
                                 String superuserPass, 
                                 String deleteAccount) throws SQLException;
}
