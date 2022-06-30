package ua.com.foxminded.sql_jdbc_school.entity;

import java.util.Objects;

public class StudentEntity {
    private Integer studentId;
    private Integer groupId;
    private String firstName;
    private String lastName;

    public StudentEntity(Integer studentId, Integer groupId, String firstName, String lastName) {
        this.studentId = studentId;
        this.groupId = groupId;
        this.firstName = firstName;
        this.lastName = lastName;
    }
    
    public StudentEntity(Integer groupId, String firstName, String lastName) {
        this.groupId = groupId;
        this.firstName = firstName;
        this.lastName = lastName;
    }

    public StudentEntity(String firstName, String lastName) {
        this.firstName = firstName;
        this.lastName = lastName;
    }

    public StudentEntity(Integer groupId) {
        this.groupId = groupId;
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

    @Override
    public int hashCode() {
        return Objects.hash(firstName, groupId, lastName, studentId);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        StudentEntity other = (StudentEntity) obj;
        return Objects.equals(firstName, other.firstName) && Objects.equals(groupId, other.groupId)
                && Objects.equals(lastName, other.lastName) && Objects.equals(studentId, other.studentId);
    }

    @Override
    public String toString() {
        return "StudentEntity [studentId=" + studentId + ", groupId=" + groupId + ", firstName=" + firstName
                + ", lastName=" + lastName + "]";
    }
}
