package ua.com.foxminded.school;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class Helper3 {

    public static void main(String[] args) {
        
        List<String> result = Helper3.getBooks();
        Iterator itr = result.iterator();
        
        while (itr.hasNext()) {
            System.out.println(itr.next());
        }

    }
    
    public static List<String> getBooks() {
        List<String> result = new ArrayList<>();

        try (Connection connection = DriverManager.getConnection("jdbc:postgresql://localhost:5432/postgres",
                "postgres", "1234")) {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT * FROM books.books");

            while (resultSet.next()) {
                result.add(resultSet.getString("name"));
            }
        } catch (SQLException e) {
            System.out.println("Connection failute");
            e.printStackTrace();
        }
        return result;
    }
}
