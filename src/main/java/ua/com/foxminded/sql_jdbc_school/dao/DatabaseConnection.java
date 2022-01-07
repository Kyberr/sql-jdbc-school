package ua.com.foxminded.sql_jdbc_school.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;



public class DatabaseConnection {
    private static final String DATABASE_URL = "jdbc:postgresql://localhost:5432/postgres";
    private static final String PASSWORD = "1234";
    private static final String USER_NAME = "postgres";

    public ResultSet getResultSet (String sqlQuery) throws SQLException {
        Connection connection = null;
        Statement statement = null;
        ResultSet resultSet = null;
        
        try {
            connection = DriverManager.getConnection(DATABASE_URL, USER_NAME, PASSWORD);
            statement = connection.createStatement();
            resultSet = statement.executeQuery(sqlQuery);
            return resultSet;
        } catch (SQLException e) {
            System.out.println("Connection is failure" + e);
            throw new SQLException("Connection is failure", e);
        } finally {
            if (resultSet != null) {
                resultSet.close();
            }
            
            if (statement != null) {
                statement.close();
            }
            
            if (connection != null) {
                connection.close();
            }
        }
    }
}
