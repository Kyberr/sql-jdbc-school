package ua.com.foxminded.sql_jdbc_school.dao;

import java.util.List;

import ua.com.foxminded.sql_jdbc_school.service.dto.StudentDTO;

public interface StudentDAO {
    
    public List<StudentDTO> getStudentsWithGroupId() throws DAOException;
    public StudentDTO getStudent(int studentId) throws DAOException;
    public int deleteStudent(int studentId) throws DAOException;
    public int insertStudent(List<StudentDTO> students) throws DAOException;
    public List<StudentDTO> getAllStudents() throws DAOException;  
    public int updateStudent(List<StudentDTO> students) throws DAOException;
}
