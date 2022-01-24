package ua.com.foxminded.university.services;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.InvalidPathException;
import java.sql.SQLException;
import java.util.List;

import org.postgresql.util.PSQLException;

import ua.com.foxminded.university.dao.TablesDAO;
import ua.com.foxminded.university.dao.DAOFactory;

public class UniversityServices {
    private Reader reader;
    private Parser parser;
    
    public UniversityServices(Reader reader, Parser parser) {
        this.reader = reader;
        this.parser = parser;
    }
    
    public void createTables(String role, 
                             String password, 
                             String nameFile) throws Exception {
        URL sqlScriptFile = UniversityServices.class.getClassLoader().getResource(nameFile);
        List<String> sqlScriptList = reader.toList(new File(sqlScriptFile.getFile()).getPath());
        String sqlScript = parser.toStringList(sqlScriptList); 
        DAOFactory universityFactory = DAOFactory.getDAOFactory(DAOFactory.UNIVERSITY);
        TablesDAO tablesDAO = universityFactory.getTablesDAO(role, password);
        tablesDAO.createTables(sqlScript);
    }
}
