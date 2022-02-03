package ua.com.foxminded.sql_jdbc_school.services.university;

import java.util.List;
import ua.com.foxminded.sql_jdbc_school.dao.DAOException;
import ua.com.foxminded.sql_jdbc_school.dao.DAOFactory;
import ua.com.foxminded.sql_jdbc_school.dao.GroupDAO;
import ua.com.foxminded.sql_jdbc_school.services.Generator;
import ua.com.foxminded.sql_jdbc_school.services.GroupService;
import ua.com.foxminded.sql_jdbc_school.services.ServicesException;
import ua.com.foxminded.sql_jdbc_school.services.dto.GroupDTO;

public class UniversityGroupService implements GroupService<List<GroupDTO>> {
    
    private Generator generator;
    
    public UniversityGroupService (Generator generator) {
        this.generator = generator;
    }
    
    public List<GroupDTO> createGroups() throws ServicesException.GroupCreationFail {
        try {
            List<String> groupList = generator.generateGroups();
            DAOFactory universityFactory = DAOFactory.getDAOFactory(DAOFactory.UNIVERSITY);
            GroupDAO groupDAO = universityFactory.getGroupDAO();
            groupDAO.insertGroup(groupList);
            return groupDAO.getAllGroups();
        } catch (DAOException.GroupInsertionFail |
                 DAOException.GetAllGroupsFail e) {
            throw new ServicesException.GroupCreationFail(e);
        }
    }
}
