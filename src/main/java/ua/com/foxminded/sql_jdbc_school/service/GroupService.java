package ua.com.foxminded.sql_jdbc_school.service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import ua.com.foxminded.sql_jdbc_school.dao.DAOException;
import ua.com.foxminded.sql_jdbc_school.dao.DAOFactory;
import ua.com.foxminded.sql_jdbc_school.dao.GroupDAO;
import ua.com.foxminded.sql_jdbc_school.dao.StudentDAO;
import ua.com.foxminded.sql_jdbc_school.dao.entities.GroupEntity;
import ua.com.foxminded.sql_jdbc_school.dao.entities.StudentEntity;
import ua.com.foxminded.sql_jdbc_school.service.dto.GroupDTO;

public class GroupService implements Group<List<GroupDTO>, Integer> {
	private static final Logger LOGGER = LogManager.getLogger();
    private static final String ERROR_CREATE_GROUPS = "The creation of groups is failed.";
    private static final String ERROR_FIND_LESS_OR_EQUALS = "The finding of groups having "
            + "less or equal to the specified number of students is failed. ";
    private Generator generator;
    private DAOFactory universityDAOFactory;

    public GroupService(Generator generator, DAOFactory universityDAOFactory) {
        this.generator = generator;
        this.universityDAOFactory = universityDAOFactory;
    }
    
    @Override
    public List<GroupDTO> findGroupsWithLessOrEqualStudents(Integer studentQuantity) 
            throws ServiceException {
        try {
            GroupDAO groupDAO = universityDAOFactory.getGroupDAO();
            List<GroupEntity> groups = groupDAO.readGroupsWithLessOrEqualStudents(studentQuantity);
            StudentDAO studentDAO = universityDAOFactory.getStudentDAO();
            List<StudentEntity> students = studentDAO.getAll();
            List<GroupDTO> groupDTO = new ArrayList<>();
            groups.stream().forEach((group) -> {
            	long studentsInGroup = students.stream()
            			.filter((student) -> student.getGroupId() == group.getGroupId())
            			.count();
            	groupDTO.add(new GroupDTO(group.getGroupId(), 
            						      group.getGroupName(), 
            						      (int) studentsInGroup));
            });
            return groupDTO;		
        } catch (DAOException e) {
        	LOGGER.error(ERROR_FIND_LESS_OR_EQUALS, e);
            throw new ServiceException (ERROR_FIND_LESS_OR_EQUALS, e); 
        }
    }
    
    @Override
    public List<GroupDTO> createGroups() throws ServiceException {
        try {
            List<String> groupNames = generator.getGroupName();
            List<GroupEntity> groupEntities = new ArrayList<>();
            groupEntities = groupNames.stream()
            				       .map((line) -> new GroupEntity(null, line))
            			           .collect(Collectors.toList());
            GroupDAO groupDAO = universityDAOFactory.getGroupDAO();
            groupDAO.insert(groupEntities);
            return groupDAO.getAll()
            			   .stream()
            			   .map((groupEntity) -> new GroupDTO(groupEntity.getGroupId(), 
            					   							  groupEntity.getGroupName()))
            			   .collect(Collectors.toList());
        } catch (DAOException e) {
        	LOGGER.error(ERROR_CREATE_GROUPS, e);
            throw new ServiceException(ERROR_CREATE_GROUPS, e);
        }
    }
}
