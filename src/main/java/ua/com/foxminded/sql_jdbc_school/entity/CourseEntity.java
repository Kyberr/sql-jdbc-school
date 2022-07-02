package ua.com.foxminded.sql_jdbc_school.entity;

import java.util.Objects;

public class CourseEntity {
    private Integer courseId;
    private String courseName;
    private String courseDescription;

    public CourseEntity() {
    }
    
    public CourseEntity(Integer courseId, String courseName) {
        this.courseId = courseId;
        this.courseName = courseName;
    }

    public CourseEntity(Integer courseId, String courseName, String courseDescription) {
        this.courseId = courseId;
        this.courseName = courseName;
        this.courseDescription = courseDescription;
    }
    
    public CourseEntity(String courseName, String courseDescription) {
        this.courseName = courseName;
        this.courseDescription = courseDescription;
    }

    public CourseEntity(String courseName) {
        this.courseName = courseName;
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
        CourseEntity other = (CourseEntity) obj;
        return Objects.equals(courseDescription, other.courseDescription) && Objects.equals(courseId, other.courseId)
                && Objects.equals(courseName, other.courseName);
    }

    @Override
    public String toString() {
        return "CourseEntity [courseId=" + courseId + ", courseName=" + courseName + ", courseDescription="
                + courseDescription + "]";
    }
}
