package ua.com.foxminded.sql_jdbc_school.services.university;

import java.util.List;
import ua.com.foxminded.sql_jdbc_school.dao.DAOException;
import ua.com.foxminded.sql_jdbc_school.dao.DAOFactory;
import ua.com.foxminded.sql_jdbc_school.dao.GroupDAO;
import ua.com.foxminded.sql_jdbc_school.services.Generator;
import ua.com.foxminded.sql_jdbc_school.services.GroupService;
import ua.com.foxminded.sql_jdbc_school.services.ServicesException;
import ua.com.foxminded.sql_jdbc_school.services.dto.GroupDTO;

public class UniversityGroupService implements GroupService<List<GroupDTO>, Integer> {
    private static final String ERROR_CREAT = "The creation of groups is failed.";
    private static final String ERROR_FIND_LESS_OR_EQUALS = "The finding of groups having "
            + "less or equal to the specified number of students is failed. ";
    private Generator generator;

    public UniversityGroupService(Generator generator) {
        this.generator = generator;
    }
    
    @Override
    public List<GroupDTO> findGroupsWithLessOrEqualStudents(Integer studentsNumber) 
            throws ServicesException.FindGroupsWithLessOrEqualStudentsFailure {
        try {
            DAOFactory universityFactory = DAOFactory.getDAOFactory(DAOFactory.UNIVERSITY);
            GroupDAO groupDAO = universityFactory.getGroupDAO();
            return groupDAO.getGroupsWithLessOrEqualStudents(studentsNumber);
        } catch (DAOException.GetGroupsWithLessOrEqualStudentsFailure e) {
            throw new ServicesException.FindGroupsWithLessOrEqualStudentsFailure (ERROR_FIND_LESS_OR_EQUALS, e); 
        }
    }
    
    @Override
    public List<GroupDTO> createGroups() throws ServicesException.GroupCreationFail {
        try {
            List<String> groupList = generator.getGroupName();
            DAOFactory universityFactory = DAOFactory.getDAOFactory(DAOFactory.UNIVERSITY);
            GroupDAO groupDAO = universityFactory.getGroupDAO();
            groupDAO.insertGroup(groupList);
            return groupDAO.getAllGroups();
        } catch (DAOException.GroupInsertionFail | DAOException.GetAllGroupsFail e) {
            throw new ServicesException.GroupCreationFail(ERROR_CREAT, e);
        }
    }
}
