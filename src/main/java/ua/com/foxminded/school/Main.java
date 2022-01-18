package ua.com.foxminded.school;

import java.io.IOException;
import java.nio.file.InvalidPathException;
import java.sql.SQLException;
import java.util.Scanner;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import ua.com.foxminded.school.dao.DAOFactory;
import ua.com.foxminded.school.dao.PostgresDAOFactory;
import ua.com.foxminded.school.services.Menu;
import ua.com.foxminded.school.services.Parser;
import ua.com.foxminded.school.services.Reader;
import ua.com.foxminded.school.services.SchoolServices;

public class Main {
    private static final Logger LOGGER = LogManager.getLogger(Main.class);
    private static final String SUPERUSER_NAME = "postgres";
    private static final String SUPERUSER_PASS = "1234";
    private static final String SQL_SCRIPT_FILE_NAME = "tablesCreationSQLScript.txt";

    public static void main(String[] args) {
        
        Reader reader = new Reader();
        Parser parser = new Parser();
        SchoolServices services = new SchoolServices(reader, parser);
        
        try {
            //services.createAccount(SUPERUSER_NAME, SUPERUSER_PASS, "oner", "2345");
            services.deleteAccount(SUPERUSER_NAME, SUPERUSER_PASS, "school");
            
        } catch (SQLException e) {
            LOGGER.error("Error");
        }
        
        
        
       // menu.boot(services);
        
        
        
        
    }
}
