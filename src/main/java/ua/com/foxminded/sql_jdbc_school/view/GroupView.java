package ua.com.foxminded.sql_jdbc_school.view;

import java.util.List;

import ua.com.foxminded.sql_jdbc_school.model.GroupModel;

public interface GroupView extends GenericView {
    
    public void programHasBeenStopped();
    
    public void returnMainMenuOrExit();
    
    public void showStudentQuantityOfGroups(List<GroupModel> groupsList);
    
    public void enterStudentQuantity();
}
