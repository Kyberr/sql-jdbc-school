package ua.com.foxminded.sql_jdbc_school;

import java.io.IOException;
import java.nio.file.InvalidPathException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import ua.com.foxminded.sql_jdbc_school.services.Reader;

public class Main {

    public static void main(String[] args) {
        Logger logger = LogManager.getLogger();
        String tablesCreationSQLScript = "tablesCreationSQLScript.txt";
        Reader reader = new Reader();
        SqlJdbcSchoolSession session = new SqlJdbcSchoolSession(reader);

        try {
            session.boot(tablesCreationSQLScript);
        } catch (InvalidPathException e) {
            logger.error("The path of the resource is wrong:", e);
        } catch (IOException e) {
            logger.error("I/O Error of the resourses:", e);
        }
    }
}
