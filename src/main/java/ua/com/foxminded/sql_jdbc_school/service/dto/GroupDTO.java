package ua.com.foxminded.sql_jdbc_school.service.dto;

import java.util.Objects;

public class GroupDTO {
    private Integer groupId;
    private String groupName;
    private Integer studentsNumber;
    
    public GroupDTO(Integer groupId, String groupName, Integer studentsNumber) {
        this.groupId = groupId;
        this.groupName = groupName;
        this.studentsNumber = studentsNumber;
    }

    public GroupDTO(Integer groupId, String groupName) {
        this.groupId = groupId;
        this.groupName = groupName;
    }
    
    public Integer getStudentsNumber() {
        return studentsNumber;
    }

    public void setStudentsNumber(Integer studentsNumber) {
        this.studentsNumber = studentsNumber;
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

    @Override
    public int hashCode() {
        return Objects.hash(groupId, groupName, studentsNumber);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        GroupDTO other = (GroupDTO) obj;
        return Objects.equals(groupId, other.groupId) && Objects.equals(groupName, other.groupName)
                && Objects.equals(studentsNumber, other.studentsNumber);
    }

    @Override
    public String toString() {
        return "GroupDTO [groupId=" + groupId + ", groupName=" + groupName + ", studentsNumber=" + studentsNumber + "]";
    }
}
