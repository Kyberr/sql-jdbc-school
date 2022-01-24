package ua.com.foxminded.university;

import java.io.IOException;
import java.nio.file.InvalidPathException;
import java.sql.SQLException;
import java.sql.SQLTransientException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import ua.com.foxminded.university.services.Parser;
import ua.com.foxminded.university.services.Reader;
import ua.com.foxminded.university.services.UniversityServices;

public class Main {
    private static final Logger LOGGER = LogManager.getLogger(Main.class);
    private static final String ROLE_NAME = "university";
    private static final String ROLE_PASS = "2345";
    private static final String SQL_SCRIPT_FILE_NAME = "tablesCreationSQLScript.txt";

    public static void main(String[] args) {

        Reader reader = new Reader();
        Parser parser = new Parser();
        UniversityServices services = new UniversityServices(reader, parser);

        try {
            services.createTables(ROLE_NAME, ROLE_PASS, SQL_SCRIPT_FILE_NAME);
            
            
            
            

        } catch (Exception e) {
            LOGGER.error("Error", e);
            /*
            System.out.println(e.getMessage());
            for (int i = 0; i < e.getStackTrace().length; i++) {
                System.out.println(e.getStackTrace()[i]);
            }
            e.printStackTrace();
            */
        }

        // menu.boot(services);

    }
}
