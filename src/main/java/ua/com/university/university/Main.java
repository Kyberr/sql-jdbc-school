package ua.com.university.university;

import java.io.IOException;
import java.nio.file.InvalidPathException;
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
    private static final String NEW_ACCOUNT_NAME = "university";
    private static final String NEW_DATABASE_NAME = "university";
    private static final String NEW_ACCOUNT_PASS = "2345";
    private static final String SQL_SCRIPT_FILE_NAME = "tablesCreationSQLScript.txt";

    public static void main(String[] args) {

        Reader reader = new Reader();
        Parser parser = new Parser();
        UniversityServices services = new UniversityServices(reader, parser);

        try {
            
            services.createRole(SUPERUSER_NAME, SUPERUSER_PASS, NEW_ACCOUNT_NAME, NEW_ACCOUNT_PASS);
            services.createDatabase(SUPERUSER_NAME, SUPERUSER_PASS, NEW_ACCOUNT_NAME, NEW_DATABASE_NAME);
            services.createTables(NEW_ACCOUNT_NAME, NEW_ACCOUNT_PASS, SQL_SCRIPT_FILE_NAME);
            

            /*
            services.deleteDatabase(SUPERUSER_NAME, SUPERUSER_PASS, NEW_DATABASE_NAME);
            services.deleteRole(SUPERUSER_NAME, SUPERUSER_PASS, NEW_ACCOUNT_NAME);
            */

        } catch (SQLException | InvalidPathException | IOException e) {
            LOGGER.error("Error", e);
        }

        // menu.boot(services);

    }
}
