package ua.com.foxminded.sql_jdbc_school;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.InvalidPathException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import ua.com.foxminded.sql_jdbc_school.services.Reader;

public class Helper {

    public static void main(String[] args) {
        String path = "tablesCreationSQLScript.txt";
        
        URL table = Helper.class.getClassLoader().getResource(path);
        String tablePath = new File(table.getFile()).getPath();
        
        Reader reader = new Reader();
        List<String> list = new ArrayList<>();
        
        try {
            list = reader.toList(tablePath);
        } catch (IOException e) {
            System.out.println("Hello" + e);
        } catch (InvalidPathException e) {
            System.out.println("invalid path exception " + e);
        }
        
        Iterator<String> iter = list.iterator();
        
        while (iter.hasNext()) {
           System.out.println(iter.next());
        }
        
        
       

    }
    
    

}
