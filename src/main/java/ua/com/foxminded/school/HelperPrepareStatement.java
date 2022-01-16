package ua.com.foxminded.school;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class HelperPrepareStatement {
    private static final Logger LOGGER = LogManager.getLogger();
    private static final String DATABASE_URL = "jdbc:postgresql://localhost:5432/postgres";
    private static final String PASSWORD = "1234";
    private static final String USER_NAME = "postgres";

    public static void main(String[] args) {

        try {
            HelperPrepareStatement.getConnection();
        } catch (SQLException e) {
            System.out.println("Connection is fail" + e);
        }
    }

    public static void getConnection() throws SQLException {
        String sqlQuery = "create role $1";
        //String selectQuery = "select rolname from pg_roles where rolname = ?";
        
        String role = "'seven'";
        String password = "1234";
        
        

        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        try {
            connection = DriverManager.getConnection(DATABASE_URL, USER_NAME, PASSWORD);
            preparedStatement = connection.prepareStatement(sqlQuery);
            preparedStatement.setString(1, role);
            //preparedStatement.setString(2, password);
            
            resultSet = preparedStatement.executeQuery();
            
            while (resultSet.next()) {
                LOGGER.info(resultSet.getString("rolname"));
            }

        } catch (SQLException e) {
            LOGGER.error("Connection is failure. SQL state: {}\n{}", e.getSQLState(), e.getMessage());
        } finally {
            if (resultSet != null) {
                resultSet.close();
            }

            if (preparedStatement != null) {
                preparedStatement.close();
            }

            if (connection != null) {
                connection.close();
            }
        }

    }

}
