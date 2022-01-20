package ua.com.foxminded.university.dao.university;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import ua.com.foxminded.university.dao.TablesDAO;

public class UniversityTablesDAO implements TablesDAO {
    private static final String MES_TABLES_CREATION = "The tabels has been created.";
    private String role;
    private String password;

    public UniversityTablesDAO(String role, String password) {
        this.role = role;
        this.password = password;
    }

    public void createTables(String sql) throws SQLException {
        try (Connection connection = UniversityDAOFactory.creatConnection(role, password);
             PreparedStatement preparedStatement = connection.prepareStatement(sql);) {
            
            int status = preparedStatement.executeUpdate();
            
            if (status == 0) {
                System.out.println(MES_TABLES_CREATION);
            }
        }
    }
}
