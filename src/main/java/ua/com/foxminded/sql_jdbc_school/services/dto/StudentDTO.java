package ua.com.foxminded.sql_jdbc_school.services.dto;

import java.util.Objects;

public class StudentDTO {
    private String studentId;
    private String groupId;
    private String firstName;
    private String lastName;
    
    public StudentDTO(String studentId, String groupId, String firstName, String lastName) {
        this.studentId = studentId;
        this.groupId = groupId;
        this.firstName = firstName;
        this.lastName = lastName;
    }

    public StudentDTO(String firstName, String lastName) {
        this.firstName = firstName;
        this.lastName = lastName;
    }

    public String getStudentId() {
        return studentId;
    }

    public void setStudentId(String studentId) {
        this.studentId = studentId;
    }

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
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
        StudentDTO other = (StudentDTO) obj;
        return Objects.equals(firstName, other.firstName) && Objects.equals(groupId, other.groupId)
                && Objects.equals(lastName, other.lastName) && Objects.equals(studentId, other.studentId);
    }

    @Override
    public String toString() {
        return "StudentDTO [studentId=" + studentId + ", groupId=" + groupId + ", firstName=" + firstName
                + ", lastName=" + lastName + "]";
    }
}
   
