package ua.com.foxminded.sql_jdbc_school.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class DatabaseGenerator {
    private static final String DATABASE_URL = "jdbc:postgresql://localhost:5432/postgres";
    private static final String USER = "postgres";
    private static final String PASSWORD = "1234";

    public void generate(String SQLScript) throws SQLException {
        Connection connection = null;
        Statement statement = null;
        ResultSet resultSet = null;

        try {
            connection = DriverManager.getConnection(DATABASE_URL, USER, PASSWORD);
            statement = connection.createStatement();
            statement.executeUpdate(SQLScript);
        } catch (SQLException e) {
            System.out.println("The connection is failure: " + e);
            throw new SQLException("The connection is failure", e);
        } finally {
            try {
                if (resultSet != null) {
                    resultSet.close(); 
                }
                
                if (statement != null) {
                    statement.close();
                }
                
                if (connection != null) {
                    connection.close(); 
                }
            } catch (SQLException exc) {
                System.out.println("The connection is not closed: " + exc);
                throw new SQLException("The connection is not closed", exc);
            }
        }
    }

}
