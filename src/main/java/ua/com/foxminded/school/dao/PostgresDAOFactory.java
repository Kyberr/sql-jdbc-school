package ua.com.foxminded.school.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class PostgresDAOFactory extends DAOFactory {
    private static final Logger LOGGER = LogManager.getLogger(PostgresDAOFactory.class);
    private static final String DB_URL = "jdbc:postgresql://localhost:5432/postgres";

    public static Connection createConnection(String username, String password) throws SQLException {
        try {
            Connection connection = DriverManager.getConnection(DB_URL, username, password);
            return connection;
        } catch (SQLException e) {
            LOGGER.error("The connection is failure", e);
            throw new SQLException ("The connection is falure", e); 
        }
    }
    
    @Override
    public AccountDAO getAccountDAO(String user, String password) {
        return new PostgresAccountDAO(user, password);
    }
    /*
    public StudentDAO getStudentDAO() {
        return new PostgresStudentDAO();
    }
    
    public GroupDAO getGroupDAO() {
        return new PostgresGroupDAO();
    }
    
    public CourseDAO getCourseDAO() {
        return new PostgresCourseDAO();
    }
    */
}
