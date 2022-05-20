package ua.com.foxminded.sql_jdbc_school.dao.postgres;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Savepoint;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import ua.com.foxminded.sql_jdbc_school.dao.DAOException;
import ua.com.foxminded.sql_jdbc_school.dao.StudentDAO;
import ua.com.foxminded.sql_jdbc_school.service.dto.StudentDTO;

public class PostgresStudentDAO implements StudentDAO {
    
    private static final String SELECT_STUDENTS_WITH_GROUP_ID = "select * from department.students "
                                                             + "where group_id is not null";
    private static final String SELECT_STUDENT = "select * from department.students where student_id = ?";
    private static final String INSERT_STUDENTS = "insert into department.students (group_id, "
                                                + "first_name, last_name) values (?, ?, ?)";
    private static final String SELECT_ALL = "select * from department.students";
    private static final String UPDATE = "update department.students set group_id=?, "
                                       + "first_name=?, last_name=? where student_id=?";
    private static final String DELETE_STUDENT = "delete from department.students where student_id = ?";
    private static final String STUDENT_ID = "student_id";
    private static final String GROUP_ID = "group_id";
    private static final String FIRST_NAME = "first_name";
    private static final String LAST_NAME = "last_name";
    private static final String ERROR_GET_ALL = "The getting of all the students failed.";
    private static final String ERROR_INSERT = "The inserting of the students failed.";
    private static final String ERROR_UDATE = "The updating of the students infurmation failed.";
    private static final String ERROR_DELETE = "The deletion of the student data failed.";
    private static final String ERROR_GET_STUDENT = "Getting the student data failed.";
    private static final String ERROR_GET_STUDENTS_WITHOUT_GROUP = "Getting the student data, that have no "
                                                                + "group ID failed.";
    
    @Override
    public List<StudentDTO> getStudentsWithGroupId() throws DAOException {
        try (Connection con = PostgresDAOFactory.creatConnection();
             PreparedStatement statement = con.prepareStatement(SELECT_STUDENTS_WITH_GROUP_ID);
             ResultSet resultSet = statement.executeQuery();) {
            
            List<StudentDTO> studentsHaveGroupId = new ArrayList<>();
            
            while (resultSet.next()) {
                studentsHaveGroupId.add(new StudentDTO(resultSet.getInt(STUDENT_ID),
                                                       resultSet.getInt(GROUP_ID),
                                                       resultSet.getString(FIRST_NAME),
                                                       resultSet.getString(LAST_NAME)));
            }
            return studentsHaveGroupId;
        } catch (DAOException | SQLException e) {
            throw new DAOException(ERROR_GET_STUDENTS_WITHOUT_GROUP, e);
        }
        
    }
    
    @Override
    public StudentDTO getStudent(int studentId) throws DAOException {
        try (Connection con = PostgresDAOFactory.creatConnection();
             PreparedStatement statement = con.prepareStatement(SELECT_STUDENT);) {

            StudentDTO student = null;
            statement.setInt(1, studentId);
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                student = new StudentDTO(resultSet.getInt(STUDENT_ID), 
                                         (Integer) resultSet.getObject(GROUP_ID),
                                         resultSet.getString(FIRST_NAME), 
                                         resultSet.getString(LAST_NAME));
            }
            resultSet.close();
            return student;
        } catch (DAOException | SQLException e) {
            throw new DAOException(ERROR_GET_STUDENT, e);
        }
    }
    
    @Override
    public int deleteStudent(int studentId) throws DAOException {
        try (Connection con = PostgresDAOFactory.creatConnection();
             PreparedStatement statement = con.prepareStatement(DELETE_STUDENT)) {
            
            statement.setInt(1, studentId);
            return statement.executeUpdate();
        } catch (DAOException | SQLException e) {
            throw new DAOException(ERROR_DELETE, e);
        }
    }
    
    @Override
    public int insertStudent(List<StudentDTO> students) throws DAOException {
        try(Connection con = PostgresDAOFactory.creatConnection();
            PreparedStatement statement = con.prepareStatement(INSERT_STUDENTS);) {
            con.setAutoCommit(false);
            Savepoint save1 = con.setSavepoint();
            
            try {
                int status = 0;
                
                for (StudentDTO student : students) {
                    statement.setObject(1, student.getGroupId());
                    statement.setString(2, student.getFirstName());
                    statement.setString(3, student.getLastName());
                    status = statement.executeUpdate();
                }
                
                con.commit();
                return status;
            } catch (SQLException ex) {
                if (con != null) {
                    try {
                        con.rollback(save1);
                    } catch (SQLException exp) {
                        throw new SQLException(exp);
                    }
                }
                
                throw new SQLException(ex);
            }
        } catch (DAOException | SQLException e) {
            throw new DAOException(ERROR_INSERT, e);
        } 
    }
    
    @Override
    public List<StudentDTO> getAllStudents() throws DAOException {
        try(Connection con = PostgresDAOFactory.creatConnection();
            Statement statement = con.createStatement();
            ResultSet resultSet = statement.executeQuery(SELECT_ALL);) {
            List<StudentDTO> students = new ArrayList<>();
            
            while(resultSet.next()) {
                students.add(new StudentDTO((Integer) resultSet.getObject(STUDENT_ID),
                                            (Integer) resultSet.getObject(GROUP_ID),
                                            resultSet.getString(FIRST_NAME),
                                            resultSet.getString(LAST_NAME)));
            }
            return students;
        } catch (DAOException | SQLException e) {
            throw new DAOException(ERROR_GET_ALL, e);
        }
    }
    
    @Override
    public int updateStudent(List<StudentDTO> students) throws DAOException {
        try(Connection con = PostgresDAOFactory.creatConnection();
            PreparedStatement statement = con.prepareStatement(UPDATE)) {
            con.setAutoCommit(false);
            Savepoint save1 = con.setSavepoint();
            int status = 0;
            
            try {
                for (StudentDTO student : students) {
                    statement.setInt(4, student.getStudentId());
                    statement.setObject(1, student.getGroupId());
                    statement.setString(2, student.getFirstName());
                    statement.setString(3, student.getLastName());
                    status += statement.executeUpdate();
                }
                
                con.commit();
                return status;
            } catch (SQLException ex) {
                if (con != null) {
                    try {
                        con.rollback(save1);
                    } catch (SQLException exp) {
                        throw new SQLException(exp);
                    }
                }
                
                throw new SQLException(ex);
            }
        } catch (DAOException | SQLException e) {
            throw new DAOException(ERROR_UDATE, e);
        }
    }
}
