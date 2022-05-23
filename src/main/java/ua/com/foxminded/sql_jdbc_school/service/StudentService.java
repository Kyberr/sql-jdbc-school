package ua.com.foxminded.sql_jdbc_school.service;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import ua.com.foxminded.sql_jdbc_school.dao.DAOException;
import ua.com.foxminded.sql_jdbc_school.dao.DAOFactory;
import ua.com.foxminded.sql_jdbc_school.dao.StudentDAO;
import ua.com.foxminded.sql_jdbc_school.dao.entities.StudentEntity;
import ua.com.foxminded.sql_jdbc_school.service.dto.GroupDTO;
import ua.com.foxminded.sql_jdbc_school.service.dto.StudentDTO;

public class StudentService implements Student<List<StudentDTO>,
											   List<GroupDTO>, 
                                               String, 
                                               Integer> {
    private static final String FIST_NAME_FILENAME_KEY = "FirstNameFilename";
    private static final String LAST_NAME_FILENAME_KEY = "LastNameFilename";
    private static final String ERROR_INSERT = "The student addition service to the database fails.";
    private static final String ERROR_ASSIGN_GROUP = "The assining group to students is failed.";
    private static final String ERROR_ADD_STUDENT = "The student adding to the database is failed.";
    private static final String ERROR_GET_ALL_STUDENT = "Getting students from the database is failed.";
    private static final String ERROR_DELETE_STUDENT = "The deletion of the student from the database is failed.";
    private static final String ERROR_GET_STUDENTS_WITH_GROUP = "Getting students that have group ID is failed.";
    
    private Reader reader;
    private Generator generator;

    public StudentService(Reader reader, Generator generator) {
        this.reader = reader;
        this.generator = generator;
    }
    
    @Override 
    public List<StudentDTO> getStudentsWithGroupId() throws ServiceException {
        
        try {
        DAOFactory universityDAOFactory = DAOFactory.getDAOFactory(DAOFactory.POSTGRES);
        StudentDAO studentDAO = universityDAOFactory.getStudentDAO();
        return studentDAO.readStudentsWithGroupId()
        			     .stream()
        			     .map((studentEntity) -> new StudentDTO(studentEntity.getStudentId(), 
        			    		 								studentEntity.getGroupId(), 
        			    		 								studentEntity.getFirstName(), 
        			    		 								studentEntity.getLastName()))
        			     .collect(Collectors.toList());
        } catch (DAOException e) {
            throw new ServiceException(ERROR_GET_STUDENTS_WITH_GROUP, e);
        }
    }
    
    @Override 
    public Integer deleteStudent(Integer studentId) throws ServiceException {
        try {
            DAOFactory universityDAOFactory = DAOFactory.getDAOFactory(DAOFactory.POSTGRES);
            StudentDAO studentDAO = universityDAOFactory.getStudentDAO();
            return studentDAO.delete(studentId);
        } catch (DAOException e) {
            throw new ServiceException(ERROR_DELETE_STUDENT, e);
        }
    }
    
    @Override 
    public List<StudentDTO> getAllStudents() throws ServiceException {
        try {
            DAOFactory universityDAOFactory = DAOFactory.getDAOFactory(DAOFactory.POSTGRES);
            StudentDAO studentDAO = universityDAOFactory.getStudentDAO();
            return studentDAO.readAll()
            				 .stream()
            				 .map((studentEntity) -> new StudentDTO(studentEntity.getStudentId(),
            						 								studentEntity.getGroupId(),
            						 								studentEntity.getFirstName(),
            						 								studentEntity.getLastName()))
            				 .collect(Collectors.toList());
        } catch (DAOException e) {
            throw new ServiceException(ERROR_GET_ALL_STUDENT, e);
        }
        
    }
    
    @Override
    public Integer addStudent(String lastName, String firstName) 
            throws ServiceException {
       try {
           List<StudentDTO> studentDTOs = new ArrayList<>();
           studentDTOs.add(new StudentDTO(firstName, lastName));
           DAOFactory universityDAOFactory = DAOFactory.getDAOFactory(DAOFactory.POSTGRES);
           StudentDAO studentDAO = universityDAOFactory.getStudentDAO();
           List<StudentEntity> studentEntities = studentDTOs
        		   .stream()
        		   .map((studentDTO) -> new StudentEntity(studentDTO.getFirstName(), 
        				   								  studentDTO.getLastName()))
        		   .collect(Collectors.toList());
           
           return studentDAO.create(studentEntities);
       } catch (DAOException e) {
           throw new ServiceException(ERROR_ADD_STUDENT, e);
       }
    }
    
    @Override
    public List<StudentDTO> assignGroup(List<GroupDTO> groups) throws ServiceException {
        try {
            DAOFactory universityDAOFactory = DAOFactory.getDAOFactory(DAOFactory.POSTGRES);
            StudentDAO studentDAO = universityDAOFactory.getStudentDAO();
            List<StudentEntity> studentEntities = studentDAO.readAll();
            List<Integer> groupSize = generator.getNumberOfStudentsInGroup(studentEntities.size(), 
                                                                           groups.size());
            List<StudentEntity> studentsHaveGroupId = new ArrayList<>();
            
            AtomicInteger atomicInteger = new AtomicInteger();
            IntStream.range(0, groupSize.size())
                     .forEach((groupIndex) -> IntStream.range(0, groupSize.get(groupIndex))
                    		 						   .forEach((index) -> {
                    		 							   int studentIndex = atomicInteger.getAndIncrement();
                    studentsHaveGroupId.add(new StudentEntity(studentEntities.get(studentIndex).getStudentId(),
                                                              groups.get(groupIndex).getGroupId(),
                                                              studentEntities.get(studentIndex).getFirstName(),
                                                              studentEntities.get(studentIndex).getLastName()));
                             }));
            studentDAO.update(studentsHaveGroupId);
            return studentsHaveGroupId.stream()
            						  .map((studentEntity) -> new StudentDTO(studentEntity.getStudentId(),
            																 studentEntity.getGroupId(),
            																 studentEntity.getFirstName(),
            																 studentEntity.getLastName()))
            						  .collect(Collectors.toList());
        } catch (DAOException e) {
            throw new ServiceException(ERROR_ASSIGN_GROUP, e); 
        }
    }
    
    @Override
    public List<StudentDTO> createStudents() throws ServiceException {
        try {
            String fistNameFilename = ReaderServicesPropertiesCache.getInstance()
                                                                   .getProperty(FIST_NAME_FILENAME_KEY);
            String lastNameFilename = ReaderServicesPropertiesCache.getInstance()
                                                                   .getProperty(LAST_NAME_FILENAME_KEY);
            List<String> firstNames = reader.toList(fistNameFilename);
            List<String> lastNames = reader.toList(lastNameFilename);
            List<StudentDTO> studentDTOs = generator.getStudentData(firstNames, lastNames);
            DAOFactory universityDAOFactory = DAOFactory.getDAOFactory(DAOFactory.POSTGRES);
            StudentDAO studentDAO = universityDAOFactory.getStudentDAO();
            List<StudentEntity> studentEntities = studentDTOs.stream()
            		.map((studentDTO) -> new StudentEntity(studentDTO.getFirstName(), 
            											   studentDTO.getLastName()))
            		.collect(Collectors.toList()); 
            studentDAO.create(studentEntities);
            return studentDTOs;
        } catch (ServiceException | DAOException e) {
            throw new ServiceException(ERROR_INSERT, e);
        }
    }
}
