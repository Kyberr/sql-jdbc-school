package ua.com.foxminded.sql_jdbc_school.services.university;

import java.util.List;
import ua.com.foxminded.sql_jdbc_school.dao.DAOException;
import ua.com.foxminded.sql_jdbc_school.dao.DAOFactory;
import ua.com.foxminded.sql_jdbc_school.dao.StudentDAO;
import ua.com.foxminded.sql_jdbc_school.services.Generator;
import ua.com.foxminded.sql_jdbc_school.services.PropertyCache;
import ua.com.foxminded.sql_jdbc_school.services.Reader;
import ua.com.foxminded.sql_jdbc_school.services.ServicesException;
import ua.com.foxminded.sql_jdbc_school.services.StudentService;
import ua.com.foxminded.sql_jdbc_school.services.dto.StudentDTO;

public class UniversityStudentService implements StudentService<Integer> {
    private static final String FIST_NAME_FILENAME_KEY = "FirstNameFilename";
    private static final String LAST_NAME_FILENAME_KEY = "LastNameFilename";
    private static final String ERROR_INSERT = "The student addition service to the database fails.";
    private Reader reader;
    private Generator studentGenerator;

    public UniversityStudentService(Reader reader, Generator studentGenerator) {
        this.reader = reader;
        this.studentGenerator = studentGenerator;
    }

    public Integer createStudents() throws ServicesException.StudentCreationFail {
        
        try {
            String fistNameFilename = PropertyCache.getInstance().getProperty(FIST_NAME_FILENAME_KEY);
            String lastNameFilename = PropertyCache.getInstance().getProperty(LAST_NAME_FILENAME_KEY);
            List<String> firstNames = reader.toList(fistNameFilename);
            List<String> lastNames = reader.toList(lastNameFilename);
            List<StudentDTO> students = studentGenerator.generateStudents(firstNames, lastNames);
            DAOFactory universityDAOFactory = DAOFactory.getDAOFactory(DAOFactory.UNIVERSITY);
            StudentDAO studentDAO = universityDAOFactory.getStudentDAO();
            return studentDAO.insertStudents(students);
        } catch (DAOException.PropertyFileLoadFail | 
                 ServicesException.ReadFail | 
                 DAOException.StudentInsertionFail e) {
            throw new ServicesException.StudentCreationFail(ERROR_INSERT, e);
        }
    }
}
