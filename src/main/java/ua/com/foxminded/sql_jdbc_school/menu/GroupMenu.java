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
import ua.com.foxminded.sql_jdbc_school.view.GroupMenuView;

public class GroupMenu {
    private static final Logger LOGGER = LogManager.getLogger();
    private static final String WORD_EXIT = "exit";
    private static final int NORMAL_DELETE_STATUS = 0;
    private static final String EMPTY_STRING = "";
    private static final String CLOSE_CONNECTION_POOL_ERROR = "Closing connections of the pool failed.";
    
    GroupMenuView<List<GroupModel>> groupServiceMenuView;
    GroupService<List<GroupModel>, Integer> groupService;
    DAOConnectionPool daoConnectionPool;
    
    public GroupMenu(GroupMenuView<List<GroupModel>> groupServiceMenuView, 
                            GroupService<List<GroupModel>, Integer>  groupService, 
                            DAOConnectionPool daoConnectionPool) {
        this.groupServiceMenuView = groupServiceMenuView;
        this.groupService = groupService;
        this.daoConnectionPool = daoConnectionPool;
    }

    public void findGroupsWithLessOrEqualStudents(Scanner studentsNumberScanner) throws ServiceException {
        groupServiceMenuView.enterNumberOfStudents();
        int studentQuantity = scanOnlyIntInput(studentsNumberScanner);
        List<GroupModel> groups = groupService.findGroupsWithLessOrEqualStudents(studentQuantity);
        groupServiceMenuView.showNumberOfStudentsInGroups(groups);
        exitOrReturnMainMenu(studentsNumberScanner);
    }
    
    private void exitOrReturnMainMenu(Scanner scanner) {
        groupServiceMenuView.returnMainMenuOrExit();

        while (scanner.hasNextLine()) {
            String input = scanner.nextLine();

            if (input.equals(WORD_EXIT)) {
                closeConnectionPool();
                groupServiceMenuView.executionHasBeenStopped();
                System.exit(NORMAL_DELETE_STATUS);
            } else if (input.equals(EMPTY_STRING)) {
                break;
            } else {
                groupServiceMenuView.returnMainMenuOrExit();
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
            groupServiceMenuView.showIncorrectInputWarning();
        }
        int input = scanner.nextInt();
        scanner.nextLine(); // it is used to clean the buffer from the empty string
        return input;
    }
}
