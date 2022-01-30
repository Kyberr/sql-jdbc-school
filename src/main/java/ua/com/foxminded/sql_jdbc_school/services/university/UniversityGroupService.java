package ua.com.foxminded.sql_jdbc_school.services.university;

import java.util.List;
import ua.com.foxminded.sql_jdbc_school.dao.DAOException;
import ua.com.foxminded.sql_jdbc_school.dao.DAOFactory;
import ua.com.foxminded.sql_jdbc_school.dao.GroupDAO;
import ua.com.foxminded.sql_jdbc_school.services.Generator;
import ua.com.foxminded.sql_jdbc_school.services.GroupService;
import ua.com.foxminded.sql_jdbc_school.services.ServicesException;

public class UniversityGroupService implements GroupService<Integer> {
    
    private Generator generator;
    
    public UniversityGroupService (Generator generator) {
        this.generator = generator;
    }
    
    public Integer createGroups() throws ServicesException.GroupCreationFail {
        try {
            List<String> groupList = generator.generateGroups();
            DAOFactory universityFactory = DAOFactory.getDAOFactory(DAOFactory.UNIVERSITY);
            GroupDAO groupDAO = universityFactory.getGroupDAO();
            return groupDAO.insertGroup(groupList);
        } catch (DAOException.GroupInsertionFail e) {
            throw new ServicesException.GroupCreationFail(e);
        }
    }
}
