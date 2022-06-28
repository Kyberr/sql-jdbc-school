package ua.com.foxminded.sql_jdbc_school.menu;

import java.util.List;
import java.util.Scanner;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import ua.com.foxminded.sql_jdbc_school.dao.DAOConnectionPool;
import ua.com.foxminded.sql_jdbc_school.dao.DAOException;
import ua.com.foxminded.sql_jdbc_school.model.GroupModel;
import ua.com.foxminded.sql_jdbc_school.service.GroupService;
import ua.com.foxminded.sql_jdbc_school.service.ServiceException;
import ua.com.foxminded.sql_jdbc_school.view.GroupView;

public class GroupMenu {
    private static final Logger LOGGER = LogManager.getLogger();
    private static final String WORD_EXIT = "exit";
    private static final int NORMAL_DELETE_STATUS = 0;
    private static final String EMPTY_STRING = "";
    private static final String CLOSE_CONNECTION_POOL_ERROR = "Closing connections of the pool failed.";
    
    GroupView<List<GroupModel>> groupMenuView;
    GroupService<List<GroupModel>, Integer> groupService;
    DAOConnectionPool daoConnectionPool;
    
    public GroupMenu(GroupView<List<GroupModel>> groupMenuView,
                     GroupService<List<GroupModel>, Integer> groupService, 
                     DAOConnectionPool daoConnectionPool) {
        this.groupMenuView = groupMenuView;
        this.groupService = groupService;
        this.daoConnectionPool = daoConnectionPool;
    }
    
    public List<GroupModel> createGroups() throws ServiceException {
        return groupService.createGroups();
    }
    
    public void deleteAllGroups() throws ServiceException {
        groupService.deleteAllGroups();
    }

    public void findGroupsWithLessOrEqualStudents(Scanner studentsNumberScanner) throws ServiceException {
        groupMenuView.enterNumberOfStudents();
        int studentQuantity = scanOnlyIntInput(studentsNumberScanner);
        List<GroupModel> groups = groupService.findGroupsWithLessOrEqualStudents(studentQuantity);
        groupMenuView.showNumberOfStudentsInGroups(groups);
        exitOrReturnMainMenu(studentsNumberScanner);
    }
    
    private void exitOrReturnMainMenu(Scanner scanner) {
        groupMenuView.returnMainMenuOrExit();

        while (scanner.hasNextLine()) {
            String input = scanner.nextLine();

            if (input.equals(WORD_EXIT)) {
                closeConnectionPool();
                groupMenuView.executionHasBeenStopped();
                System.exit(NORMAL_DELETE_STATUS);
            } else if (input.equals(EMPTY_STRING)) {
                break;
            } else {
                groupMenuView.returnMainMenuOrExit();
            }
        }
    }
    
    private void closeConnectionPool() {
        try {
            daoConnectionPool.closeConnections();
        } catch (DAOException e) {
            LOGGER.error(CLOSE_CONNECTION_POOL_ERROR, e);
        } 
    }
    
    private int scanOnlyIntInput(Scanner scanner) {
        while (!scanner.hasNextInt()) {
            scanner.nextLine();
            groupMenuView.showIncorrectInputWarning();
        }
        int input = scanner.nextInt();
        scanner.nextLine(); // it is used to clean the buffer from the empty string
        return input;
    }
}
