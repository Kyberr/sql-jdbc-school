package ua.com.foxminded.sql_jdbc_school.services.university;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.IntStream;

import ua.com.foxminded.sql_jdbc_school.dao.DAOException;
import ua.com.foxminded.sql_jdbc_school.dao.DAOFactory;
import ua.com.foxminded.sql_jdbc_school.dao.GroupDAO;
import ua.com.foxminded.sql_jdbc_school.dao.StudentDAO;
import ua.com.foxminded.sql_jdbc_school.services.Generator;
import ua.com.foxminded.sql_jdbc_school.services.Reader;
import ua.com.foxminded.sql_jdbc_school.services.ReaderServicesPropertiesCache;
import ua.com.foxminded.sql_jdbc_school.services.ServicesException;
import ua.com.foxminded.sql_jdbc_school.services.StudentService;
import ua.com.foxminded.sql_jdbc_school.services.dto.GroupDTO;
import ua.com.foxminded.sql_jdbc_school.services.dto.StudentDTO;

public class UniversityStudentService implements StudentService {
    private static final String FIST_NAME_FILENAME_KEY = "FirstNameFilename";
    private static final String LAST_NAME_FILENAME_KEY = "LastNameFilename";
    private static final String ERROR_INSERT = "The student addition service to the database fails.";
    private static final String ERROR_ASSIGN_GROUP = "The assining group to students is failed.";
    
    private Reader reader;
    private Generator generator;

    public UniversityStudentService(Reader reader, Generator generator) {
        this.reader = reader;
        this.generator = generator;
    }
    
    public List<StudentDTO> assignGroup() throws ServicesException.AssignGgoupToStudentsFail {
        try {
            DAOFactory universityDAOFactory = DAOFactory.getDAOFactory(DAOFactory.UNIVERSITY);
            StudentDAO studentDAO = universityDAOFactory.getStudentDAO();
            List<StudentDTO> students = studentDAO.getAllStudents();
            GroupDAO groupDAO = universityDAOFactory.getGroupDAO();
            List<GroupDTO> groups = groupDAO.getAllGroups();
            
            
            //
            IntStream.range(0, groups.size()).forEach((group) -> System.out.println(groups.get(group)));
            System.out.println();
            
            
            
            List<Integer> studentsNumberInGroups = generator.generateStudentNumber(students.size(), 
                                                                                   groups.size());
            
            //
            IntStream.range(0, studentsNumberInGroups.size()).forEach((index) -> System.out.println(studentsNumberInGroups.get(index)));
            System.out.println();
            
            AtomicInteger atomicInteger = new AtomicInteger();
            
            IntStream.range(0, groups.size())
                     .forEach((groupIndex) -> {
                IntStream.range(0, studentsNumberInGroups.get(groupIndex))
                         .forEach((studentIndex) -> {
                              
                    students.get(atomicInteger.get()).setGroupId(groups.get(groupIndex).getGroupId());
                    atomicInteger.incrementAndGet();
                });
            });
            
            IntStream.range(0, students.size()).forEach((student) -> System.out.println(students.get(student)));
            
            return students;
        } catch (DAOException.GetAllSutudentsFail | DAOException.GetAllGroupsFail e) {
            throw new ServicesException.AssignGgoupToStudentsFail(ERROR_ASSIGN_GROUP, e); 
        }
    }

    public Integer createStudents() throws ServicesException.StudentCreationFail {
        
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
            return studentDAO.insertStudent(students);
        } catch (ServicesException.PropertyFileLoadingFail | 
                 ServicesException.ReadFail | 
                 DAOException.StudentInsertionFail e) {
            throw new ServicesException.StudentCreationFail(ERROR_INSERT, e);
        }
    }
}
