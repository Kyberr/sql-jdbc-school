package ua.com.foxminded.school.services;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.InvalidPathException;
import java.sql.SQLException;
import java.util.List;
import java.util.Scanner;

import ua.com.foxminded.school.dao.AccountDAO;
import ua.com.foxminded.school.dao.DAOFactory;
import ua.com.foxminded.school.dao.PostgresDAOFactory;
import ua.com.foxminded.school.services.Parser;
import ua.com.foxminded.school.services.Reader;

public class SchoolServices {
    private Reader reader;
    private Parser parser;
    
    public SchoolServices(Reader reader, Parser parser) {
        this.reader = reader;
        this.parser = parser;
    }
    
    public void createAccount(String superuserName, 
                              String superuserPass, 
                              String newName, 
                              String newPass) throws SQLException {
        
        DAOFactory postgresDAO = DAOFactory.getDAOFactory(DAOFactory.POSTGRES);
        AccountDAO accountDAO = postgresDAO.getAccountDAO();
        accountDAO.createAccountDAO(superuserName, superuserPass, newName, newPass);
    }
    
    public void deleteAccount(String superuserName, 
                              String superuserPass, 
                              String deleteName) throws SQLException {
        DAOFactory postgresDAO = DAOFactory.getDAOFactory(DAOFactory.POSTGRES);
        AccountDAO accountDAO = postgresDAO.getAccountDAO();
        accountDAO.deleteAccountDAO(superuserName, superuserPass, deleteName);
    }
    
    public void authoriseAccount(String userName, String userPass) {
        
    }
    
    
    

    public void createTables(String nameFile) throws InvalidPathException, 
                                                            IOException, 
                                                            SQLException {
        
        URL SQLScriptFile = SchoolServices.class.getClassLoader().getResource(nameFile);
        List<String> SQLScript = reader.toList(new File(SQLScriptFile.getFile()).getPath());
        
        
        
        
        //databaseGenerator.generate(parser.toStringList(SQLScript));

        //System.out.println(parser.toStringList(SQLScript));
        
        
        
    }
    

}
