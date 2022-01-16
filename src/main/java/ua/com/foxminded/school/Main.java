package ua.com.foxminded.school;

import java.io.IOException;
import java.nio.file.InvalidPathException;
import java.sql.SQLException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import ua.com.foxminded.school.dao.AccountDAO;
import ua.com.foxminded.school.dao.DAOFactory;
import ua.com.foxminded.school.dao.DatabaseConmmander;
import ua.com.foxminded.school.dao.DatabaseGenerator;
import ua.com.foxminded.school.dao.PostgresDAOFactory;
import ua.com.foxminded.school.services.Parser;
import ua.com.foxminded.school.services.Reader;

public class Main {
    private static final Logger LOGGER = LogManager.getLogger(Main.class);
    private static final String PASSWORD = "1234";
    private static final String USER_NAME = "postgres";

    public static void main(String[] args) {
        
        LOGGER.info("Hello");
        
        try {
            DAOFactory postgresFactory = DAOFactory.getDAOFactory(DAOFactory.POSTGRES);
            AccountDAO accountDAO = postgresFactory.getAccountDAO(USER_NAME, PASSWORD);
            accountDAO.createAccountDAO("children", "1234");
        } catch (SQLException e) {
            System.out.println("Somthing wrong " + e);
        }
       
        
        
        /*
        String tablesCreationSQLScript = "tablesCreationSQLScript.txt";
        Reader reader = new Reader();
        DatabaseConmmander databaseCreation = new DatabaseConmmander();
        DatabaseGenerator databaseGenerator = new DatabaseGenerator();
        Parser parser = new Parser();
        SqlJdbcSchoolSession session = new SqlJdbcSchoolSession(reader, 
                                                                databaseCreation, 
                                                                databaseGenerator,
                                                                parser);

        try {
            session.boot(tablesCreationSQLScript);
        } catch (InvalidPathException e) {
            System.out.println("The path of the resource is wrong: " + e);
        } catch (IOException e) {
            System.out.println("I/O Error of the resourses:" + e);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        */
    }
}
