package ua.com.foxminded.sql_jdbc_school.view;

import java.util.List;
import java.util.Scanner;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import ua.com.foxminded.sql_jdbc_school.dao.DAOConnectionPool;
import ua.com.foxminded.sql_jdbc_school.dao.DAOException;
import ua.com.foxminded.sql_jdbc_school.model.GroupModel;
import ua.com.foxminded.sql_jdbc_school.service.GroupService;
import ua.com.foxminded.sql_jdbc_school.service.ServiceException;

public class GroupView {
    private static final Logger LOGGER = LogManager.getLogger();
    private static final String WORD_EXIT = "exit";
    private static final int NORMAL_DELETE_STATUS = 0;
    private static final String EMPTY_STRING = "";
    private static final String CLOSE_CONNECTION_POOL_ERROR = "Closing connections of the pool failed.";
    
    ViewProcessor viewProcessor;
    GroupService groupService;
    DAOConnectionPool daoConnectionPool;
    
    public GroupView(ViewProcessor viewProcessor,GroupService groupService, DAOConnectionPool daoConnectionPool) {
        this.viewProcessor = viewProcessor;
        this.groupService = groupService;
        this.daoConnectionPool = daoConnectionPool;
    }
    
    public List<GroupModel> createGroups() throws ServiceException {
        List<GroupModel> groupsWithoutId = groupService.createWithoutId();
        return groupService.assignIdAndAddToDatabase(groupsWithoutId);
    }
    
    public void deleteAllGroups() throws ServiceException {
        groupService.deleteAll();
    }

    public void findGroupsWithLessOrEqualStudents(Scanner scanner) throws ServiceException {
        viewProcessor.enterStudentQuantity();
        int studentQuantity = scanOnlyIntInput(scanner);
        List<GroupModel> groups = groupService.findGroupsWithLessOrEqualStudents(studentQuantity);
        viewProcessor.showStudentQuantityOfGroups(groups);
        exitOrReturnMainMenu(scanner);
    }
    
    private void exitOrReturnMainMenu(Scanner scanner) {
        viewProcessor.returnMainMenuOrExit();

        while (scanner.hasNextLine()) {
            String input = scanner.nextLine();

            if (input.equals(WORD_EXIT)) {
                closeConnectionPool();
                viewProcessor.programHasBeenStopped();
                System.exit(NORMAL_DELETE_STATUS);
            } else if (input.equals(EMPTY_STRING)) {
                break;
            } else {
                viewProcessor.returnMainMenuOrExit();
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
            viewProcessor.showIncorrectInputWarning();
        }
        int input = scanner.nextInt();
        scanner.nextLine(); // it is used to clean the buffer from the empty string
        return input;
    }
}
