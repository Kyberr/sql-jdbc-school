package ua.com.foxminded.university;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import ua.com.foxminded.university.dao.postgres.PostgresDAOPropertyCache;
import ua.com.foxminded.university.services.Menu;
import ua.com.foxminded.university.services.Parser;
import ua.com.foxminded.university.services.Reader;
import ua.com.foxminded.university.services.TableService;
import ua.com.foxminded.university.services.postgres.PostgresTableService;

public class Main {
    private static final Logger LOGGER = LogManager.getLogger(Main.class);
    
    

    public static void main(String[] args) {

        Reader reader = new Reader();
        Parser parser = new Parser();
        TableService services = new PostgresTableService(reader, parser);
        //Menu menu = new Menu();

        try {
           // menu.boot(services);
            
            
            System.out.println(services.createTables());
            //System.out.println( PostgresDAOPropertyCache.getInstance().getProperty("Password"));
            
            
            

        } catch (Exception e) {
            LOGGER.error("Error", e);
        }
    }
}
