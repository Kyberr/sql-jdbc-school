package ua.com.foxminded.university.services;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.InvalidPathException;
import java.sql.SQLException;
import java.util.List;
import java.util.Scanner;

import ua.com.foxminded.university.dao.AccountDAO;
import ua.com.foxminded.university.dao.DAOFactory;
import ua.com.foxminded.university.dao.PostgresDAOFactory;
import ua.com.foxminded.university.services.Parser;
import ua.com.foxminded.university.services.Reader;

public class UniversityServices {
    private Reader reader;
    private Parser parser;
    
    public UniversityServices(Reader reader, Parser parser) {
        this.reader = reader;
        this.parser = parser;
    }
    
    public void createAccount(String superuserName, 
                              String superuserPass, 
                              String newUserName, 
                              String newUserPass) throws SQLException {
        
        DAOFactory postgresDAO = DAOFactory.getDAOFactory(DAOFactory.POSTGRES);
        AccountDAO accountDAO = postgresDAO.getAccountDAO();
        accountDAO.createAccountDAO(superuserName, superuserPass, newUserName, newUserPass);
    }
    
    public void deleteAccount(String superuserName, 
                              String superuserPass, 
                              String deleteAccountName) throws SQLException {
        DAOFactory postgresDAO = DAOFactory.getDAOFactory(DAOFactory.POSTGRES);
        AccountDAO accountDAO = postgresDAO.getAccountDAO();
        accountDAO.deleteAccount(superuserName, superuserPass, deleteAccountName);
    }
    
    public void createDatabase() {
        
    }
    
    
    

    public void createTables(String nameFile) throws InvalidPathException, 
                                                            IOException, 
                                                            SQLException {
        
        URL SQLScriptFile = UniversityServices.class.getClassLoader().getResource(nameFile);
        List<String> SQLScript = reader.toList(new File(SQLScriptFile.getFile()).getPath());
        
        
        
        
        //databaseGenerator.generate(parser.toStringList(SQLScript));

        //System.out.println(parser.toStringList(SQLScript));
        
        
        
    }
    

}
