package ua.com.foxminded.sql_jdbc_school.service;

import java.util.List;
import ua.com.foxminded.sql_jdbc_school.dao.DAOException;
import ua.com.foxminded.sql_jdbc_school.dao.DAOFactory;
import ua.com.foxminded.sql_jdbc_school.dao.GroupDAO;
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
            return groupDAO.getGroupsWithLessOrEqualStudents(studentsNumber);
        } catch (DAOException e) {
            throw new ServiceException (ERROR_FIND_LESS_OR_EQUALS, e); 
        }
    }
    
    @Override
    public List<GroupDTO> createGroups() throws ServiceException {
        try {
            List<String> groupList = generator.getGroupName();
            DAOFactory universityFactory = DAOFactory.getDAOFactory(DAOFactory.POSTGRES);
            GroupDAO groupDAO = universityFactory.getGroupDAO();
            groupDAO.insertGroup(groupList);
            return groupDAO.getAllGroups();
        } catch (DAOException e) {
            throw new ServiceException(ERROR_CREAT, e);
        }
    }
}
