package ua.com.foxminded.sql_jdbc_school.dao.entities;

public class StudentCourseEntity {
	Integer studentId;
    Integer groupId;
    String firstName;
    String lastName;
    Integer courseId;
    String courseName;
    String courseDescription;
	
    public StudentCourseEntity(Integer studentId, Integer groupId, String firstName, String lastName, Integer courseId,
			String courseName, String courseDescription) {
		this.studentId = studentId;
		this.groupId = groupId;
		this.firstName = firstName;
		this.lastName = lastName;
		this.courseId = courseId;
		this.courseName = courseName;
		this.courseDescription = courseDescription;
	}
	public Integer getStudentId() {
		return studentId;
	}
	public void setStudentId(Integer studentId) {
		this.studentId = studentId;
	}
	public Integer getGroupId() {
		return groupId;
	}
	public void setGroupId(Integer groupId) {
		this.groupId = groupId;
	}
	public String getFirstName() {
		return firstName;
	}
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	public String getLastName() {
		return lastName;
	}
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	public Integer getCourseId() {
		return courseId;
	}
	public void setCourseId(Integer courseId) {
		this.courseId = courseId;
	}
	public String getCourseName() {
		return courseName;
	}
	public void setCourseName(String courseName) {
		this.courseName = courseName;
	}
	public String getCourseDescription() {
		return courseDescription;
	}
	public void setCourseDescription(String courseDescription) {
		this.courseDescription = courseDescription;
	}
	@Override
	public String toString() {
		return "StudentCourseEntity [studentId=" + studentId + ", groupId=" + groupId + ", firstName=" + firstName
				+ ", lastName=" + lastName + ", courseId=" + courseId + ", courseName=" + courseName
				+ ", courseDescription=" + courseDescription + "]";
	}
}
