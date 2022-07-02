package ua.com.foxminded.sql_jdbs_school.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InOrder;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import ua.com.foxminded.sql_jdbc_school.dao.DAOException;
import ua.com.foxminded.sql_jdbc_school.dao.GroupDAO;
import ua.com.foxminded.sql_jdbc_school.dao.StudentDAO;
import ua.com.foxminded.sql_jdbc_school.entity.GroupEntity;
import ua.com.foxminded.sql_jdbc_school.entity.StudentEntity;
import ua.com.foxminded.sql_jdbc_school.model.GroupModel;
import ua.com.foxminded.sql_jdbc_school.service.ServiceException;
import ua.com.foxminded.sql_jdbc_school.service.impl.GroupServiceImpl;

@ExtendWith(MockitoExtension.class)
class GroupServiceImplTest {
    private static final String GROUP_NAME_REGEX = "[a-z][a-z]-\\d\\d";
    private static final int GROUP_QUANTITY = 10;
	private static final int GROUP_ID_1 = 1;
	private static final int GROUP_ID_2 = 2;
	private static final int GROUP_ID_3 = 3;
	private static final int ANY_NUMBER = 10;
	private static final int STUDENTS_WITH_GROUP_ID = 2;
	
	@InjectMocks
	GroupServiceImpl groupService;
	
	@Mock
	GroupDAO groupDaoMock;
	
	@Mock
	StudentDAO studentDaoMock;
	
	@Test
	void deleteAll_DeletingAllGroups_CorectNumberOfCalls() throws ServiceException, DAOException {
	    groupService.deleteAll();
	    verify(groupDaoMock, times(1)).deleteAll();
	}
	
	@Test
	void findGroupsWithLessOrEqualStudents_FindingGroups_CorrectGroupQuantity () throws DAOException, 
																						ServiceException {
		List<GroupEntity> groups = new ArrayList<>();
		groups.add(new GroupEntity(GROUP_ID_1));
		groups.add(new GroupEntity(GROUP_ID_2));
		List<StudentEntity> students = new ArrayList<>();
		students.add(new StudentEntity(GROUP_ID_1));
		students.add(new StudentEntity(GROUP_ID_2));
		students.add(new StudentEntity(GROUP_ID_3));
		
		when(groupDaoMock.getGroupsHavingLessOrEqualStudents(any(Integer.class))).thenReturn(groups);
		when(studentDaoMock.getAll()).thenReturn(students);
		int expectedResult = STUDENTS_WITH_GROUP_ID;
		assertEquals(expectedResult, groupService.findGroupsWithLessOrEqualStudents(ANY_NUMBER).size());
	}
	
	@Test
    void createWithoutId_CreationOfGroups_CorrectNumberOfGroups() {
	    List<GroupModel> groups = groupService.createWithoutId();
	    int groupQuantity = (int) groups.stream().count();
	    assertEquals(GROUP_QUANTITY, groupQuantity);
	}
	
	@Test
    void createWithoutId_GenerationOfGroupNames_CorrectGroupNames() {
	    List<GroupModel> groups = groupService.createWithoutId();
	    groups.stream().forEach((group) -> assertTrue(group.getGroupName().matches(GROUP_NAME_REGEX)));
    }
	
	@Test
	void assignIdAndAddToDatabase_AssigningAndAddingGroupsToDatabase_CorrectOrderAndNumberOfCalls() 
	        throws ServiceException, DAOException {
	    List<GroupModel> groups = new ArrayList<>();
	    groupService.assignIdAndAddToDatabase(groups);
	    InOrder inOrder = Mockito.inOrder(groupDaoMock);
	    inOrder.verify(groupDaoMock, times(1)).insert(ArgumentMatchers.<GroupEntity>anyList());
	    inOrder.verify(groupDaoMock, times(1)).getAll();
	}
}
