package ua.com.foxminded.university.services.university;

import java.util.List;
import ua.com.foxminded.university.PropertyCache;
import ua.com.foxminded.university.dao.DAOException;
import ua.com.foxminded.university.dao.DAOFactory;
import ua.com.foxminded.university.dao.StudentDAO;
import ua.com.foxminded.university.dto.StudentData;
import ua.com.foxminded.university.generator.Generator;
import ua.com.foxminded.university.services.Reader;
import ua.com.foxminded.university.services.ServicesException;
import ua.com.foxminded.university.services.StudentService;

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

    public Integer insertStudents() throws ServicesException.StudentInsertionFail {
        
        try {
            String fistNameFilename = PropertyCache.getInstance().getProperty(FIST_NAME_FILENAME_KEY);
            String lastNameFilename = PropertyCache.getInstance().getProperty(LAST_NAME_FILENAME_KEY);
            List<String> firstNames = reader.toList(fistNameFilename);
            List<String> lastNames = reader.toList(lastNameFilename);
            List<StudentData> students = studentGenerator.generateStudents(firstNames, lastNames);
            DAOFactory universityDAOFactory = DAOFactory.getDAOFactory(DAOFactory.UNIVERSITY);
            StudentDAO studentDAO = universityDAOFactory.getStudentDAO();
            return studentDAO.insertStudents(students);
        } catch (DAOException.PropertyFileLoadFail | 
                 ServicesException.ReadFail | 
                 DAOException.StudentInsertionFail e) {
            throw new ServicesException.StudentInsertionFail(ERROR_INSERT, e);
        }
    }
}
