package ua.com.university.university;

import java.sql.SQLException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import ua.com.foxminded.university.services.Parser;
import ua.com.foxminded.university.services.Reader;
import ua.com.foxminded.university.services.UniversityServices;

public class Main {
    private static final Logger LOGGER = LogManager.getLogger(Main.class);
    private static final String SUPERUSER_NAME = "postgres";
    private static final String SUPERUSER_PASS = "1234";
    private static final String NEW_USER_NAME = "teacher";
    private static final String NEW_USER_PASS = "2345";
    private static final String SQL_SCRIPT_FILE_NAME = "tablesCreationSQLScript.txt";

    public static void main(String[] args) {
        
        Reader reader = new Reader();
        Parser parser = new Parser();
        UniversityServices services = new UniversityServices(reader, parser);
        
        try {
            services.createAccount(SUPERUSER_NAME, SUPERUSER_PASS, 
                                   NEW_USER_NAME, NEW_USER_PASS);
            //services.deleteAccount(SUPERUSER_NAME, SUPERUSER_PASS, "school");
            services.createDatabase();
            
            
            
        } catch (SQLException e) {
            LOGGER.error("Error");
        }
        
        
        
       // menu.boot(services);
        
        
        
        
    }
}
