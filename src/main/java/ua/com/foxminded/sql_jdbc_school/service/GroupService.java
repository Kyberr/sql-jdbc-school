package ua.com.foxminded.sql_jdbc_school.service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import ua.com.foxminded.sql_jdbc_school.dao.DAOException;
import ua.com.foxminded.sql_jdbc_school.dao.DAOFactory;
import ua.com.foxminded.sql_jdbc_school.dao.GroupDAO;
import ua.com.foxminded.sql_jdbc_school.dao.entities.GroupEntity;
import ua.com.foxminded.sql_jdbc_school.service.dto.GroupDTO;

public class GroupService implements Group<List<GroupDTO>, Integer> {
	private static final Logger LOGGER = LogManager.getLogger();
    private static final String ERROR_CREATE_GROUPS = "The creation of groups is failed.";
    private static final String ERROR_FIND_LESS_OR_EQUALS = "The finding of groups having "
            + "less or equal to the specified number of students is failed. ";
    private Generator generator;

    public GroupService(Generator generator) {
        this.generator = generator;
    }
    
    @Override
    public List<GroupDTO> findGroupsWithLessOrEqualStudents(Integer studentQuantity) 
            throws ServiceException {
        try {
            DAOFactory universityDAOFactory = DAOFactory.getDAOFactory(DAOFactory.UNIVERSITY);
            GroupDAO groupDAO = universityDAOFactory.getGroupDAO();
            return groupDAO.readGroupsWithLessOrEqualStudents(studentQuantity)
            			   .stream()
            			   .map((groupEntity) -> new GroupDTO(groupEntity.getGroupId(), 
            					   							  groupEntity.getGroupName(), 
            					   							  groupEntity.getStudentQuantity()))
            			   .collect(Collectors.toList());
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
            DAOFactory universityDAOFactory = DAOFactory.getDAOFactory(DAOFactory.UNIVERSITY);
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
