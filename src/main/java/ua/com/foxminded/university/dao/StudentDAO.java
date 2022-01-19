package ua.com.foxminded.university.dao;

public interface StudentDAO {
    public int addStudent(String firstName, String lastName);
    public boolean deleteStudent(String studentId);
    public String findLessOrEqualStudentNumberGroups (String studentNumber);
    public String findCourseStudents(String courseName);
    public int addStudentToCourse(String studentId, String courseName);
}
