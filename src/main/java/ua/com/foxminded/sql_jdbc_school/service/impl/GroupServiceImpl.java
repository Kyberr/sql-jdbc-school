package ua.com.foxminded.sql_jdbc_school.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import ua.com.foxminded.sql_jdbc_school.dao.DAOConnectionPool;
import ua.com.foxminded.sql_jdbc_school.dao.DAOException;
import ua.com.foxminded.sql_jdbc_school.dao.GroupDAO;
import ua.com.foxminded.sql_jdbc_school.dao.StudentDAO;
import ua.com.foxminded.sql_jdbc_school.entity.GroupEntity;
import ua.com.foxminded.sql_jdbc_school.entity.StudentEntity;
import ua.com.foxminded.sql_jdbc_school.model.GroupModel;
import ua.com.foxminded.sql_jdbc_school.service.Generator;
import ua.com.foxminded.sql_jdbc_school.service.GroupService;
import ua.com.foxminded.sql_jdbc_school.service.ServiceException;

public class GroupServiceImpl implements GroupService<List<GroupModel>, Integer> {
    private static final Logger LOGGER = LogManager.getLogger();
    private static final String CLOSE_CONNECTION_POOL_ERROR = "The closing opreation of connections "
                                                                + "in the pool failed.";
    private static final String ERROR_DELETE_ALL_GROUPS = "The service of groups deletion is failed.";
    private static final String ERROR_CREATE_GROUPS = "The creation of groups is failed.";
    private static final String ERROR_FIND_LESS_OR_EQUALS = "The finding of groups having "
            + "less or equal to the specified number of students is failed. ";
    private final Generator generator;
    private final GroupDAO groupDAO;
    private final StudentDAO studentDAO;
    private final DAOConnectionPool connectionPool;

    public GroupServiceImpl(Generator generator, GroupDAO groupDAO, StudentDAO studentDAO,
            DAOConnectionPool connectionPool) {
        this.generator = generator;
        this.groupDAO = groupDAO;
        this.studentDAO = studentDAO;
        this.connectionPool = connectionPool;
    }

    @Override
    public Integer deleteAllGroups() throws ServiceException {
        int status = 0;
        try {
            status = studentDAO.deleteAll();
            return status;
        } catch (DAOException e) {
            LOGGER.error(ERROR_DELETE_ALL_GROUPS, e);
            throw new ServiceException(ERROR_DELETE_ALL_GROUPS, e);
        } finally {
            closeConnectionPool();
        }
    }

    @Override
    public List<GroupModel> findGroupsWithLessOrEqualStudents(Integer studentQuantity) throws ServiceException {
        try {
            List<GroupEntity> groupEntities = groupDAO.getGroupsHavingLessOrEqualStudents(studentQuantity);
            List<StudentEntity> studentEntities = studentDAO.getAll();
            List<GroupModel> groupModels = new ArrayList<>();
            
            groupEntities.stream().forEach((group) -> {
                long studentsInGroup = studentEntities.stream()
                        .filter((student) -> Objects.nonNull(student.getGroupId()))
                        .filter((student) -> (int) student.getGroupId() == (int) group.getGroupId())
                        .count();
                groupModels.add(new GroupModel(group.getGroupId(), 
                                               group.getGroupName(), 
                                               (int) studentsInGroup));
            });
            return groupModels;
        } catch (DAOException e) {
            LOGGER.error(ERROR_FIND_LESS_OR_EQUALS, e);
            throw new ServiceException(ERROR_FIND_LESS_OR_EQUALS, e);
        } finally {
            closeConnectionPool();
        }
    }

    @Override
    public List<GroupModel> createGroups() throws ServiceException {
        try {
            List<String> groupNames = generator.getGroupName();
            List<GroupEntity> groupEntities = groupNames.stream().map((line) -> new GroupEntity(null, line))
                                                        .collect(Collectors.toList());
            groupDAO.insert(groupEntities);
            return groupDAO.getAll()
                           .stream()
                           .map((groupEntity) -> new GroupModel(groupEntity.getGroupId(), 
                                                                groupEntity.getGroupName()))
                           .collect(Collectors.toList());
        } catch (DAOException e) {
            LOGGER.error(ERROR_CREATE_GROUPS, e);
            throw new ServiceException(ERROR_CREATE_GROUPS, e);
        } finally {
            closeConnectionPool();
        }
    }
    
    private void closeConnectionPool() {
        try {
            connectionPool.closeConnections();
        } catch (DAOException e) {
            LOGGER.error(CLOSE_CONNECTION_POOL_ERROR, e);
        } 
    }
}
