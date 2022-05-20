package ua.com.foxminded.sql_jdbc_school.dao.entities;

import java.util.Objects;

public class GroupEntity {
	private Integer groupId;
    private String groupName;
    private Integer studentsNumber;
	
    public GroupEntity(Integer groupId, String groupName, Integer studentsNumber) {
		super();
		this.groupId = groupId;
		this.groupName = groupName;
		this.studentsNumber = studentsNumber;
	}

	public GroupEntity(Integer groupId, String groupName) {
		super();
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

	public Integer getStudentsNumber() {
		return studentsNumber;
	}

	public void setStudentsNumber(Integer studentsNumber) {
		this.studentsNumber = studentsNumber;
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
		GroupEntity other = (GroupEntity) obj;
		return Objects.equals(groupId, other.groupId) && Objects.equals(groupName, other.groupName)
				&& Objects.equals(studentsNumber, other.studentsNumber);
	}

	@Override
	public String toString() {
		return "GroupEntity [groupId=" + groupId + ", groupName=" + groupName + ", studentsNumber=" + studentsNumber
				+ "]";
	}
}
