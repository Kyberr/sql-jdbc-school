package ua.com.foxminded.sql_jdbc_school.entity;

import java.util.Objects;

public class GroupEntity {
	private Integer groupId;
    private String groupName;

	public GroupEntity(Integer groupId, String groupName) {
		this.groupId = groupId;
		this.groupName = groupName;
	}

	public GroupEntity(Integer groupId) {
		this.groupId = groupId;
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
		GroupEntity other = (GroupEntity) obj;
		return Objects.equals(groupId, other.groupId) && Objects.equals(groupName, other.groupName);
	}

	@Override
	public String toString() {
		return "GroupEntity [groupId=" + groupId + ", groupName=" + groupName + "]";
	}
}
