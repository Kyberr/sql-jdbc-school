package ua.com.foxminded.school.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

public class DatabaseConmmander {

    public List<ResultSet> send(List<String> SQLScript) throws SQLException {
        Stream<ResultSet> databaseCommander = Stream.empty();
        DatabaseConnection databaseConnection = new DatabaseConnection();
        List<ResultSet> result = new ArrayList<>();

        try {
            SQLScript.stream().forEachOrdered((line) -> {
                try {
                    ResultSet resultSet = databaseConnection.getResultSet(line);
                    result.add(resultSet);
                } catch (SQLException e) {
                    System.out.println("Connection is failure" + e);
                }
            });
            return result;
        } finally {
            databaseCommander.close();
        } 
       // System.out.println("The database has been created.");
    }

}
