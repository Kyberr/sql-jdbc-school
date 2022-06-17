package ua.com.foxminded.sql_jdbc_school.service.dto;

import java.util.Objects;

public class CourseDto {
    private Integer courseId;
    private String courseName;
    private String courseDescription;
    
    public CourseDto(Integer courseId, String courseName, String courseDescription) {
        this.courseId = courseId;
        this.courseName = courseName;
        this.courseDescription = courseDescription;
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
        return Objects.hash(courseDescription, courseId, courseName);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        CourseDto other = (CourseDto) obj;
        return Objects.equals(courseDescription, other.courseDescription) && Objects.equals(courseId, other.courseId)
                && Objects.equals(courseName, other.courseName);
    }

    @Override
    public String toString() {
        return "CourseDTO [courseId=" + courseId + ", courseName=" + courseName + ", courseDescription="
                + courseDescription + "]";
    }
}
