package ua.com.foxminded.sql_jdbc_school;

import java.util.ArrayList;
import java.util.List;

import ua.com.foxminded.sql_jdbc_school.entity.GroupEntity;
import ua.com.foxminded.sql_jdbc_school.entity.StudentEntity;
import ua.com.foxminded.sql_jdbc_school.model.GroupModel;

public class Helper {

    public static void main(String[] args) {
        
        try {
            List<GroupEntity> groupEntities = new ArrayList<>();
            groupEntities.add(new GroupEntity(1, "FirstGroup"));
            groupEntities.add(new GroupEntity(2, "SecondGroup"));
            
            List<StudentEntity> studentEntities = new ArrayList<>();
            studentEntities.add(new StudentEntity(1));
            studentEntities.add(new StudentEntity("firstName", "lastName"));
            List<GroupModel> groupModels = new ArrayList<>();
            
            
            groupEntities.stream().forEach((group) -> {
                long studentsInGroup = studentEntities.stream()
                        .filter((student) -> (int)student.getGroupId() == (int) group.getGroupId())
                        .count();
                groupModels.add(new GroupModel(group.getGroupId(), 
                                               group.getGroupName(), 
                                               (int) studentsInGroup));
            });
            
            System.out.println(groupModels.toString());
        } catch (Exception e) {
            e.printStackTrace();;
        }
    }
}
