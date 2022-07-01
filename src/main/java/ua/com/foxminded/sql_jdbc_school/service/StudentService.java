package ua.com.foxminded.sql_jdbc_school.service;

import java.util.List;

import ua.com.foxminded.sql_jdbc_school.model.CourseModel;
import ua.com.foxminded.sql_jdbc_school.model.GroupModel;
import ua.com.foxminded.sql_jdbc_school.model.StudentModel;

public interface StudentService extends GenericService<List<StudentModel>> {

    public List<StudentModel> getAllStudents() throws ServiceException;

    public List<StudentModel> getStudentsOfCourseById(int courseId) throws ServiceException;

    public int addStudentToCourseById(int studentId, int courseId) throws ServiceException;

    public List<StudentModel> getAllStudentsHavingCourse() throws ServiceException;

    public List<StudentModel> assignCourseToStudent(List<StudentModel> students, 
                                                    List<CourseModel> courses) throws ServiceException;

    public List<StudentModel> getStudentsHavingGroupId() throws ServiceException;

    public int deleteStudent(int studentId) throws ServiceException;

    public int addStudent(String lastName, String firstName) throws ServiceException;

    public List<StudentModel> assignGroupToStudent(List<GroupModel> groups) throws ServiceException;
}
