package ua.com.foxminded.school;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class Helper2 {
    private static final String DATABASE_URL = "jdbc:postgresql://localhost:5432/postgres"; 
    
    private static final String PASSWORD = "1234";
    private static final String USER_NAME = "postgres";

    public static void main(String[] args) {
        
        try {
            Helper2.getConnection();
        } catch (SQLException e) {
            System.out.println("Connection is fail" + e);
        }
    }
    
    public static void getConnection () throws SQLException {
        String sqlQuery = "SELECT * FROM books.books";

        Connection connection = null;
        Statement statement = null;
        ResultSet resultSet = null;

        try {
            connection = DriverManager.getConnection(DATABASE_URL, USER_NAME, PASSWORD);
            statement = connection.createStatement();
            resultSet = statement.executeQuery(sqlQuery);

            
            while (resultSet.next()) {
                System.out.println(resultSet.getString("name"));
            }
            
        } catch (SQLException e) {
            System.out.println("Connection is failure" + e);
        } finally {
           // if (resultSet != null) {
                resultSet.close();
           // }

            if (statement != null) {
                statement.close();
            }

            if (connection != null) {
                connection.close();
            }
        }
        
    }
}
