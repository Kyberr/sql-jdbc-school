package ua.com.foxminded.sql_jdbs_school.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.when;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InOrder;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import ua.com.foxminded.sql_jdbc_school.dao.DAOException;
import ua.com.foxminded.sql_jdbc_school.dao.GroupDAO;
import ua.com.foxminded.sql_jdbc_school.dao.StudentDAO;
import ua.com.foxminded.sql_jdbc_school.dao.entities.GroupEntity;
import ua.com.foxminded.sql_jdbc_school.dao.entities.StudentEntity;
import ua.com.foxminded.sql_jdbc_school.service.Generator;
import ua.com.foxminded.sql_jdbc_school.service.GroupService;
import ua.com.foxminded.sql_jdbc_school.service.ServiceException;

@ExtendWith(MockitoExtension.class)
class GroupServiceTest {
	private static final Integer GROUP_ID_1 = 1;
	private static final Integer GROUP_ID_2 = 2;
	private static final Integer GROUP_ID_3 = 3;
	private static final Integer ANY_NUMBER = 10;
	private static final Integer STUDENTS_WITH_GROUP_ID = 2;
	
	@InjectMocks
	GroupService groupService;
	
	@Mock
	GroupDAO groupDaoMock;
	
	@Mock
	StudentDAO studentDaoMock;
	
	@Mock
	Generator generatorMock;
	
	@Test
	void findGroupsWithLessOrEqualStudents_findingGroups_CorrectGroupQuantity () throws DAOException, 
																						ServiceException {
		List<GroupEntity> groups = new ArrayList<>();
		groups.add(new GroupEntity(GROUP_ID_1));
		groups.add(new GroupEntity(GROUP_ID_2));
		List<StudentEntity> students = new ArrayList<>();
		students.add(new StudentEntity(GROUP_ID_1));
		students.add(new StudentEntity(GROUP_ID_2));
		students.add(new StudentEntity(GROUP_ID_3));
		
		when(groupDaoMock.readGroupsWithLessOrEqualStudents(any(Integer.class))).thenReturn(groups);
		when(studentDaoMock.getAll()).thenReturn(students);
		int expectedResult = STUDENTS_WITH_GROUP_ID;
		assertEquals(expectedResult, groupService.findGroupsWithLessOrEqualStudents(ANY_NUMBER).size());
	}
	
	@Test
	void createGroups() throws ServiceException, DAOException {
		List<GroupEntity> groups = new ArrayList<>();
		groupService.createGroups();
		InOrder inOrder = Mockito.inOrder(generatorMock, groupDaoMock);
		inOrder.verify(generatorMock, times(1)).getGroupName();
		inOrder.verify(groupDaoMock, times(1)).insert(groups);
	}
}
