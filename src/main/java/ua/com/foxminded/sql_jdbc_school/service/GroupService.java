package ua.com.foxminded.sql_jdbc_school.service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import ua.com.foxminded.sql_jdbc_school.dao.DAOException;
import ua.com.foxminded.sql_jdbc_school.dao.DAOFactory;
import ua.com.foxminded.sql_jdbc_school.dao.GroupDAO;
import ua.com.foxminded.sql_jdbc_school.dao.entities.GroupEntity;
import ua.com.foxminded.sql_jdbc_school.service.dto.GroupDTO;

public class GroupService implements Group<List<GroupDTO>, Integer> {
    private static final String ERROR_CREAT = "The creation of groups is failed.";
    private static final String ERROR_FIND_LESS_OR_EQUALS = "The finding of groups having "
            + "less or equal to the specified number of students is failed. ";
    private Generator generator;

    public GroupService(Generator generator) {
        this.generator = generator;
    }
    
    @Override
    public List<GroupDTO> findGroupsWithLessOrEqualStudents(Integer studentsNumber) 
            throws ServiceException {
        try {
            DAOFactory universityFactory = DAOFactory.getDAOFactory(DAOFactory.POSTGRES);
            GroupDAO groupDAO = universityFactory.getGroupDAO();
            return groupDAO.readGroupsWithLessOrEqualStudents(studentsNumber)
            			   .stream()
            			   .map((groupEntity) -> new GroupDTO(groupEntity.getGroupId(), 
            					   							  groupEntity.getGroupName(), 
            					   							  groupEntity.getStudentQuantity()))
            			   .collect(Collectors.toList());
        } catch (DAOException e) {
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
            DAOFactory postgresFactory = DAOFactory.getDAOFactory(DAOFactory.POSTGRES);
            GroupDAO groupDAO = postgresFactory.getGroupDAO();
            groupDAO.create(groupEntities);
            return groupDAO.readAll()
            			   .stream()
            			   .map((groupEntity) -> new GroupDTO(groupEntity.getGroupId(), 
            					   							  groupEntity.getGroupName()))
            			   .collect(Collectors.toList());
        } catch (DAOException e) {
            throw new ServiceException(ERROR_CREAT, e);
        }
    }
}
