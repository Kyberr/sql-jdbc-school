package ua.com.foxminded.sql_jdbc_school;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URL;
import java.nio.file.Path;
import java.util.Enumeration;
import java.util.Properties;
import java.util.stream.Collectors;

public class Helper {
    
    public void print() throws IOException {
      /*
       InputStream in  = Thread.currentThread()
                               .getContextClassLoader()
                               .getResourceAsStream("test-group-queries.properties");
       */
       /*
       Helper instance = new Helper();
       InputStream in = instance.getClass()
                                .getClassLoader()
                                .getResourceAsStream("test-group-queries.properties");
       
       Properties properties = new Properties();
       properties.load(in);
       String element = properties.getProperty("selectInsertedGroup");
       System.out.println(element);
       */
        
        InputStream in =this.getClass().getClassLoader().getResourceAsStream("test-group-queries.properties");
        String path = new BufferedReader(new InputStreamReader(in))
                .lines().collect(Collectors.joining("\n"));
        System.out.println(path);
    }
}
