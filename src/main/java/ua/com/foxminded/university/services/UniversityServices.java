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
import ua.com.foxminded.university.dao.DatabaseDAO;
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
                              String newAccountName, 
                              String newAccountPass) throws SQLException {
        
        DAOFactory postgresFactory = DAOFactory.getDAOFactory(DAOFactory.POSTGRES);
        AccountDAO accountDAO = postgresFactory.getAccountDAO(superuserName, superuserPass);
        accountDAO.createAccount(newAccountName, newAccountPass);
    }
    
    public void deleteAccount(String superuserName, 
                              String superuserPass, 
                              String deleteAccountName) throws SQLException {
        DAOFactory postgresFactory = DAOFactory.getDAOFactory(DAOFactory.POSTGRES);
        AccountDAO accountDAO = postgresFactory.getAccountDAO(superuserName, superuserPass);
        accountDAO.deleteAccount(deleteAccountName);
    }
    
    public void createDatabase(String superuserName,
                               String superuserPass, 
                               String ownerName, 
                               String databaseName) throws SQLException {
        DAOFactory postgresFactory = DAOFactory.getDAOFactory(DAOFactory.POSTGRES);
        DatabaseDAO databaseDAO = postgresFactory.getDatabaseDAO(superuserName, superuserPass);
        databaseDAO.createDatabase(databaseName, ownerName);
    }
    
    public void deleteDatabase(String superuserName,
                               String superuserPass, 
                               String databaseName) throws SQLException {
        DAOFactory postgresDAOFactory = DAOFactory.getDAOFactory(DAOFactory.POSTGRES);
        DatabaseDAO databaseDAO = postgresDAOFactory.getDatabaseDAO(superuserName, superuserPass);
        databaseDAO.deleteDatabase(databaseName);
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
