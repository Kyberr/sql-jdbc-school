package ua.com.foxminded.university.services.postgres;

import java.util.List;
import java.util.Map;

import ua.com.foxminded.university.dao.DAOException;
import ua.com.foxminded.university.dao.postgres.PostgresDAOPropertyCache;
import ua.com.foxminded.university.generator.StudentGenerator;
import ua.com.foxminded.university.services.Parser;
import ua.com.foxminded.university.services.Reader;
import ua.com.foxminded.university.services.StudentService;

public class PostgresStudentService implements StudentService {
    private static final String FIST_NAME_FILENAME_KEY = "FirstNameFilename";
    private static final String LAST_NAME_FILENAME_KEY = "LastNameFilename";
    private Reader reader;
    private Parser parser;
    private StudentGenerator studentGenerator;
    
    public PostgresStudentService(Reader reader, Parser parser, StudentGenerator studentGenerator) {
        this.reader = reader;
        this.parser = parser;
        this.studentGenerator = studentGenerator;
    }
    
    public int insertStudents() {
        try {
            String fistNameFilename = PostgresDAOPropertyCache.getInstance()
                                                              .getProperty(FIST_NAME_FILENAME_KEY);
            String lastNameFilename = PostgresDAOPropertyCache.getInstance()
                                                              .getProperty(LAST_NAME_FILENAME_KEY);
            List<String> firstNames = reader.toList(fistNameFilename);
            List<String> lastNames = reader.toList(lastNameFilename);
            Map<Integer, Map<String, String>> students = studentGenerator.  
            
            
        } catch (DAOException.PropertyFileLoadFail | ReadFail e) {
            
        }
        
        
        
    }
}
