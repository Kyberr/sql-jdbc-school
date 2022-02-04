package ua.com.foxminded.sql_jdbc_school.services.university;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.IntStream;
import ua.com.foxminded.sql_jdbc_school.dao.DAOException;
import ua.com.foxminded.sql_jdbc_school.dao.DAOFactory;
import ua.com.foxminded.sql_jdbc_school.dao.StudentDAO;
import ua.com.foxminded.sql_jdbc_school.services.Generator;
import ua.com.foxminded.sql_jdbc_school.services.Reader;
import ua.com.foxminded.sql_jdbc_school.services.ReaderServicesPropertiesCache;
import ua.com.foxminded.sql_jdbc_school.services.ServicesException;
import ua.com.foxminded.sql_jdbc_school.services.StudentService;
import ua.com.foxminded.sql_jdbc_school.services.dto.GroupDTO;
import ua.com.foxminded.sql_jdbc_school.services.dto.StudentDTO;

public class UniversityStudentService implements StudentService<List<StudentDTO>, List<GroupDTO>> {
    private static final String FIST_NAME_FILENAME_KEY = "FirstNameFilename";
    private static final String LAST_NAME_FILENAME_KEY = "LastNameFilename";
    private static final String ERROR_INSERT = "The student addition service to the database fails.";
    private static final String ERROR_ASSIGN_GROUP = "The assining group to students is failed.";
    private static final int ZERO = 0;
    
    private Reader reader;
    private Generator generator;

    public UniversityStudentService(Reader reader, Generator generator) {
        this.reader = reader;
        this.generator = generator;
    }
    
    public List<StudentDTO> assignGroup(List<GroupDTO> groups) throws ServicesException.AssignGgoupToStudentsFail {
        try {
            DAOFactory universityDAOFactory = DAOFactory.getDAOFactory(DAOFactory.UNIVERSITY);
            StudentDAO studentDAO = universityDAOFactory.getStudentDAO();
            List<StudentDTO> students = studentDAO.getAllStudents();
            List<Integer> studentsNumberInGroup = generator.generateStudentNumber(students.size(), 
                                                                                  groups.size());
            AtomicInteger atomicInteger = new AtomicInteger();
            IntStream.range(ZERO, groups.size())
                     .parallel()
                     .forEach((groupIndex) -> IntStream.range(ZERO, studentsNumberInGroup.get(groupIndex))
                                                  .parallel()
                                                  .forEach((studentsNumber) -> {
                                                           students.get(atomicInteger.getAndIncrement())
                                                                   .setGroupId(groups.get(groupIndex)
                                                                                     .getGroupId());
                                                           }));
            studentDAO.updateStudent(students);
            return students;
        } catch (DAOException.GetAllSutudentsFail
                | DAOException.StudentUptatingFail e) {
            throw new ServicesException.AssignGgoupToStudentsFail(ERROR_ASSIGN_GROUP, e); 
        }
    }

    public List<StudentDTO> createStudents() throws ServicesException.StudentCreationFail {
        try {
            String fistNameFilename = ReaderServicesPropertiesCache.getInstance()
                                                                   .getProperty(FIST_NAME_FILENAME_KEY);
            String lastNameFilename = ReaderServicesPropertiesCache.getInstance()
                                                                   .getProperty(LAST_NAME_FILENAME_KEY);
            List<String> firstNames = reader.toList(fistNameFilename);
            List<String> lastNames = reader.toList(lastNameFilename);
            List<StudentDTO> students = generator.generateStudents(firstNames, lastNames);
            DAOFactory universityDAOFactory = DAOFactory.getDAOFactory(DAOFactory.UNIVERSITY);
            StudentDAO studentDAO = universityDAOFactory.getStudentDAO();
            studentDAO.insertStudent(students);
            return studentDAO.getAllStudents();
        } catch (ServicesException.PropertyFileLoadingFail 
                | ServicesException.ReadFail 
                | DAOException.StudentInsertionFail 
                | DAOException.GetAllSutudentsFail e) {
            throw new ServicesException.StudentCreationFail(ERROR_INSERT, e);
        }
    }
}
