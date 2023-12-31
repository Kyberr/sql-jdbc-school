package ua.com.foxminded.sql_jdbc_school.model;

import java.util.Objects;

public class GroupModel {
    
    private Integer groupId;
    private String groupName;
    private Integer studentQuantity;
    
    public GroupModel(Integer groupId) {
        this.groupId = groupId;
    }

    public GroupModel(Integer groupId, String groupName, Integer studentQuantity) {
        this.groupId = groupId;
        this.groupName = groupName;
        this.studentQuantity = studentQuantity;
    }

    public GroupModel(Integer groupId, String groupName) {
        this.groupId = groupId;
        this.groupName = groupName;
    }
    
    public GroupModel(String groupName) {
        this.groupName = groupName;
    }

    public Integer getGroupId() {
        return groupId;
    }

    public void setGroupId(Integer groupId) {
        this.groupId = groupId;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public Integer getStudentQuantity() {
        return studentQuantity;
    }

    public void setStudentQuantity(Integer studentQuantity) {
        this.studentQuantity = studentQuantity;
    }

    @Override
    public int hashCode() {
        return Objects.hash(groupId, groupName, studentQuantity);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        GroupModel other = (GroupModel) obj;
        return Objects.equals(groupId, other.groupId) && Objects.equals(groupName, other.groupName)
                && Objects.equals(studentQuantity, other.studentQuantity);
    }

    @Override
    public String toString() {
        return "GroupDTO [groupId=" + groupId + ", groupName=" + groupName + ", studentQuantity=" + studentQuantity
                + "]";
    }
}
