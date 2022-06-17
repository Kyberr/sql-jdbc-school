package ua.com.foxminded.sql_jdbc_school.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ua.com.foxminded.sql_jdbc_school.dao.DAOException;
import ua.com.foxminded.sql_jdbc_school.dao.GroupDAO;
import ua.com.foxminded.sql_jdbc_school.dao.StudentDAO;
import ua.com.foxminded.sql_jdbc_school.dao.entities.GroupEntity;
import ua.com.foxminded.sql_jdbc_school.dao.entities.StudentEntity;
import ua.com.foxminded.sql_jdbc_school.service.Generator;
import ua.com.foxminded.sql_jdbc_school.service.GroupUniversity;
import ua.com.foxminded.sql_jdbc_school.service.ServiceException;
import ua.com.foxminded.sql_jdbc_school.service.dto.GroupDTO;

public class GroupServiceImpl implements GroupUniversity<List<GroupDTO>, Integer> {
	private static final Logger LOGGER = LogManager.getLogger();
    private static final String ERROR_CREATE_GROUPS = "The creation of groups is failed.";
    private static final String ERROR_FIND_LESS_OR_EQUALS = "The finding of groups having "
            + "less or equal to the specified number of students is failed. ";
    private final Generator generator;
    private final GroupDAO groupDAO;
    private final StudentDAO studentDAO;

    public GroupServiceImpl(Generator generator, GroupDAO groupDAO, StudentDAO studentDAO) {
        this.generator = generator;
        this.groupDAO = groupDAO;
        this.studentDAO = studentDAO;
    }
    
    @Override
    public List<GroupDTO> findGroupsWithLessOrEqualStudents(Integer studentQuantity) 
            throws ServiceException {
        try {
            List<GroupEntity> groups = groupDAO.readGroupsWithLessOrEqualStudents(studentQuantity);
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
            List<GroupEntity> groupEntities = groupNames.stream()
            				          					.map((line) -> new GroupEntity(null, line))
            				          					.collect(Collectors.toList());
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
