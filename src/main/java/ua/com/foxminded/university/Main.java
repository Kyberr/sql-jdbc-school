package ua.com.foxminded.university;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import ua.com.foxminded.university.generator.Generator;
import ua.com.foxminded.university.services.Menu;
import ua.com.foxminded.university.services.Parser;
import ua.com.foxminded.university.services.Reader;
import ua.com.foxminded.university.services.StudentService;
import ua.com.foxminded.university.services.TableService;
import ua.com.foxminded.university.services.university.UniversityStudentService;
import ua.com.foxminded.university.services.university.UniversityTableService;

public class Main {
    private static final Logger LOGGER = LogManager.getLogger(Main.class);
    
    

    public static void main(String[] args) {

        Reader reader = new Reader();
        Parser parser = new Parser();
        Generator generator = new Generator();
        TableService<Integer> tableServices = new UniversityTableService(reader, parser);
        StudentService<Integer> studentServices = new UniversityStudentService(reader, generator);
        
        
        
        //Menu menu = new Menu();
        
        

        try {
           // menu.boot(services);
            System.out.println(tableServices.createTables());
            System.out.println(studentServices.insertStudents());
            
            //System.out.println( PostgresDAOPropertyCache.getInstance().getProperty("Password"));
            
            
            

        } catch (Exception e) {
            LOGGER.error("Error", e);
        }
    }
}
