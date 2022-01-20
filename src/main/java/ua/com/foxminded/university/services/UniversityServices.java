package ua.com.foxminded.university.services;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.InvalidPathException;
import java.sql.SQLException;
import java.util.List;
import java.util.Scanner;

import ua.com.foxminded.university.dao.RoleDAO;
import ua.com.foxminded.university.dao.TablesDAO;
import ua.com.foxminded.university.dao.postgres.DatabaseDAO;
import ua.com.foxminded.university.dao.postgres.PostgresDAOFactory;
import ua.com.foxminded.university.dao.DAOFactory;
import ua.com.foxminded.university.services.Parser;
import ua.com.foxminded.university.services.Reader;

public class UniversityServices {
    private Reader reader;
    private Parser parser;
    
    public UniversityServices(Reader reader, Parser parser) {
        this.reader = reader;
        this.parser = parser;
    }
    
    public void createRole(String superuserName, 
                           String superuserPass, 
                           String newRoleName, 
                           String newRolePass) throws SQLException {
        
        DAOFactory postgresFactory = DAOFactory.getDAOFactory(DAOFactory.POSTGRES);
        RoleDAO roleDAO = postgresFactory.getRoleDAO(superuserName, superuserPass);
        roleDAO.createRole(newRoleName, newRolePass);
    }
    
    public void deleteRole(String superuserName, 
                           String superuserPass, 
                           String deleteRoleName) throws SQLException {
        DAOFactory postgresFactory = DAOFactory.getDAOFactory(DAOFactory.POSTGRES);
        RoleDAO roleDAO = postgresFactory.getRoleDAO(superuserName, superuserPass);
        roleDAO.deleteRole(deleteRoleName);
    }
    
    public void createDatabase(String superuserName,
                               String superuserPass, 
                               String ownerDatabase, 
                               String databaseName) throws SQLException {
        DAOFactory postgresFactory = DAOFactory.getDAOFactory(DAOFactory.POSTGRES);
        DatabaseDAO databaseDAO = postgresFactory.getDatabaseDAO(superuserName, superuserPass);
        databaseDAO.createDatabase(databaseName, ownerDatabase);
    }
    
    public void deleteDatabase(String superuserName,
                               String superuserPass, 
                               String databaseName) throws SQLException {
        DAOFactory postgresDAOFactory = DAOFactory.getDAOFactory(DAOFactory.POSTGRES);
        DatabaseDAO databaseDAO = postgresDAOFactory.getDatabaseDAO(superuserName, superuserPass);
        databaseDAO.deleteDatabase(databaseName);
    }

    public void createTables(String role, 
                             String password, 
                             String nameFile) throws InvalidPathException, 
                                                     IOException, 
                                                     SQLException {
        
        URL sqlScriptFile = UniversityServices.class.getClassLoader().getResource(nameFile);
        List<String> sqlScriptList = reader.toList(new File(sqlScriptFile.getFile()).getPath());
        String sqlScript = parser.toStringList(sqlScriptList); 
        DAOFactory universityFactory = DAOFactory.getDAOFactory(DAOFactory.UNIVERSITY);
        TablesDAO tablesDAO = universityFactory.getTablesDAO(role, password);
        tablesDAO.createTables(sqlScript);
    }
}
