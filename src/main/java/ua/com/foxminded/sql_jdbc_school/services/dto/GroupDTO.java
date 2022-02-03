package ua.com.foxminded.sql_jdbc_school.services.dto;

import java.util.Objects;

public class GroupDTO {
    private Integer groupId;
    private String groupName;
    
    public GroupDTO(Integer groupId, String groupName) {
        this.groupId = groupId;
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

    @Override
    public int hashCode() {
        return Objects.hash(groupId, groupName);
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
        return Objects.equals(groupId, other.groupId) && Objects.equals(groupName, other.groupName);
    }

    @Override
    public String toString() {
        return "GroupDTO [groupId=" + groupId + ", groupName=" + groupName + "]";
    }
}
