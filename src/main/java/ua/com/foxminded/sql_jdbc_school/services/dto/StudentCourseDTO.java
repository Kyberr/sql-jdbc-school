package ua.com.foxminded.sql_jdbc_school.services.dto;

import java.util.Objects;

public class StudentCourseDTO {
    Integer studentId;
    Integer groupId;
    String firstName;
    String lastName;
    Integer courseId;
    String courseName;
    String courseDescription;
    
    public StudentCourseDTO(Integer studentId, Integer groupId, String firstName, String lastName, Integer courseId,
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
    public int hashCode() {
        return Objects.hash(courseDescription, courseId, courseName, lastName, firstName, studentId);
    }
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        StudentCourseDTO other = (StudentCourseDTO) obj;
        return Objects.equals(courseDescription, other.courseDescription) && Objects.equals(courseName, other.courseName)
                && Objects.equals(courseId, other.courseId) && Objects.equals(lastName, other.lastName) 
                && Objects.equals(firstName, other.firstName) && Objects.equals(groupId, other.groupId) 
                && Objects.equals(studentId, other.studentId);
                    
    }
}
