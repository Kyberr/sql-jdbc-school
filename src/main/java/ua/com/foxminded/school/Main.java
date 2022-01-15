package ua.com.foxminded.school;

import java.io.IOException;
import java.nio.file.InvalidPathException;
import java.sql.SQLException;

import ua.com.foxminded.school.dao.DatabaseConmmander;
import ua.com.foxminded.school.dao.DatabaseGenerator;
import ua.com.foxminded.school.services.Parser;
import ua.com.foxminded.school.services.Reader;

public class Main {

    public static void main(String[] args) {
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
    }
}
