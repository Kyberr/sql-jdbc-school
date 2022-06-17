package ua.com.foxminded.sql_jdbc_school.service;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ua.com.foxminded.sql_jdbc_school.dao.DAOException;
import ua.com.foxminded.sql_jdbc_school.dao.StudentDAO;
import ua.com.foxminded.sql_jdbc_school.dao.entities.StudentEntity;
import ua.com.foxminded.sql_jdbc_school.service.dto.GroupDTO;
import ua.com.foxminded.sql_jdbc_school.service.dto.StudentDTO;

public class StudentService implements Student<List<StudentDTO>,
											   List<GroupDTO>, 
                                               String, 
                                               Integer> {
	
	private static final Logger LOGGER = LogManager.getLogger();
    private static final String FIST_NAME_FILENAME = "firstNameList.txt";
    private static final String LAST_NAME_FILENAME = "lastNameList.txt";
    private static final String ERROR_CREATE_STUDENTS = "The student addition service to the database fails.";
    private static final String ERROR_ASSIGN_GROUP = "The assining group to students is failed.";
    private static final String ERROR_ADD_STUDENT = "The student adding to the database is failed.";
    private static final String ERROR_GET_ALL_STUDENT = "Getting students from the database is failed.";
    private static final String ERROR_DELETE_STUDENT = "The deletion of the student from the database is failed.";
    private static final String ERROR_GET_STUDENTS_WITH_GROUP = "Getting students that have group ID is failed.";
    private final Reader reader;
    private final Generator generator;
    private final StudentDAO studentDAO;
    
    public StudentService(Reader reader, Generator generator, StudentDAO studentDAO) {
		this.reader = reader;
		this.generator = generator;
		this.studentDAO = studentDAO;
	}

	@Override 
    public List<StudentDTO> getStudentsHavingGroupId() throws ServiceException {
        
        try {
        return studentDAO.getStudentsHavingGroupId()
        			     .stream()
        			     .map((studentEntity) -> new StudentDTO(studentEntity.getStudentId(), 
        			    		 								studentEntity.getGroupId(), 
        			    		 								studentEntity.getFirstName(), 
        			    		 								studentEntity.getLastName()))
        			     .collect(Collectors.toList());
        } catch (DAOException e) {
        	LOGGER.error(ERROR_GET_STUDENTS_WITH_GROUP, e);
            throw new ServiceException(ERROR_GET_STUDENTS_WITH_GROUP, e);
        }
    }
    
    @Override 
    public Integer deleteStudent(Integer studentId) throws ServiceException {
        try {
            return studentDAO.deleteById(studentId);
        } catch (DAOException e) {
        	LOGGER.error(ERROR_DELETE_STUDENT, e);
            throw new ServiceException(ERROR_DELETE_STUDENT, e);
        }
    }
    
    @Override 
    public List<StudentDTO> getAllStudents() throws ServiceException {
        try {
            return studentDAO.getAll()
            				 .stream()
            				 .map((studentEntity) -> new StudentDTO(studentEntity.getStudentId(),
            						 								studentEntity.getGroupId(),
            						 								studentEntity.getFirstName(),
            						 								studentEntity.getLastName()))
            				 .collect(Collectors.toList());
        } catch (DAOException e) {
        	LOGGER.error(ERROR_GET_ALL_STUDENT, e);
            throw new ServiceException(ERROR_GET_ALL_STUDENT, e);
        }
    }
    
    @Override
    public Integer addStudent(String lastName, String firstName) 
            throws ServiceException {
       try {
           List<StudentDTO> studentDTOs = new ArrayList<>();
           studentDTOs.add(new StudentDTO(firstName, lastName));
           List<StudentEntity> studentEntities = studentDTOs
        		   .stream()
        		   .map((studentDTO) -> new StudentEntity(studentDTO.getFirstName(), 
        				   								  studentDTO.getLastName()))
        		   .collect(Collectors.toList());
           
           return studentDAO.insert(studentEntities);
       } catch (DAOException e) {
    	   LOGGER.error(ERROR_ADD_STUDENT, e);
           throw new ServiceException(ERROR_ADD_STUDENT, e);
       }
    }
    
    @Override
    public List<StudentDTO> assignGroup(List<GroupDTO> groups) throws ServiceException {
        try {
            List<StudentEntity> studentEntities = studentDAO.getAll();
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
        	LOGGER.error(ERROR_ASSIGN_GROUP, e);
            throw new ServiceException(ERROR_ASSIGN_GROUP, e); 
        }
    }
    
    @Override
    public List<StudentDTO> createStudents() throws ServiceException {
        try {
            List<String> firstNames = reader.read(FIST_NAME_FILENAME);
            List<String> lastNames = reader.read(LAST_NAME_FILENAME);
            List<StudentDTO> studentDTOs = generator.getStudentData(firstNames, lastNames);
            List<StudentEntity> studentEntities = studentDTOs.stream()
            		.map((studentDTO) -> new StudentEntity(studentDTO.getFirstName(), 
            											   studentDTO.getLastName()))
            		.collect(Collectors.toList()); 
            studentDAO.insert(studentEntities);
            return studentDTOs;
        } catch (ServiceException | DAOException e) {
        	LOGGER.error(ERROR_CREATE_STUDENTS, e);
            throw new ServiceException(ERROR_CREATE_STUDENTS, e);
        }
    }
}
