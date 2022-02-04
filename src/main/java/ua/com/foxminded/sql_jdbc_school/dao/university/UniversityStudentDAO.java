package ua.com.foxminded.sql_jdbc_school.dao.university;

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
import ua.com.foxminded.sql_jdbc_school.dao.DAOException.DatabaseConnectionFail;
import ua.com.foxminded.sql_jdbc_school.services.dto.StudentDTO;

public class UniversityStudentDAO implements StudentDAO {
    private static final String SQL_INSERT = "insert into department.students (group_id, "
                                           + "first_name, last_name) values (?, ?, ?)";
    private static final String SQL_SELECT_ALL = "select * from department.students";
    private static final String SQL_UPDATE = "update department.students set group_id=?, "
                                           + "first_name=?, last_name=? where student_id=?";
    private static final String COLUMN_NAME_STUDENT_ID = "student_id";
    private static final String COLUMN_NAME_GROUP_ID = "group_id";
    private static final String COLUMN_NAME_FIRST_NAME = "first_name";
    private static final String COLUMN_NAME_LAST_NAME = "last_name";
    private static final String ERROR_GET_ALL = "The getting of all the students is failed.";
    private static final String ERROR_INSERT = "The inserting of the students is failed.";
    private static final String ERROR_UDATE = "The updating of the students infurmation is failed.";
    
    public int insertStudent(List<StudentDTO> students) throws DAOException.StudentInsertionFail  {
        try(Connection con = UniversityDAOFactory.creatConnection();
            PreparedStatement statement = con.prepareStatement(SQL_INSERT);) {
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
            } catch (SQLException e) {
                if (con != null) {
                    try {
                        con.rollback(save1);
                    } catch (SQLException exc) {
                        throw new SQLException(exc);
                    }
                }
                
                throw new SQLException(e);
            }
        } catch (DatabaseConnectionFail | SQLException e) {
            throw new DAOException.StudentInsertionFail(ERROR_INSERT, e);
        } 
    }
    
    public List<StudentDTO> getAllStudents() throws DAOException.GetAllSutudentsFail {
        try(Connection con = UniversityDAOFactory.creatConnection();
            Statement statement = con.createStatement();
            ResultSet resultSet = statement.executeQuery(SQL_SELECT_ALL);) {
            List<StudentDTO> students = new ArrayList<>();
            
            while(resultSet.next()) {
                students.add(new StudentDTO((Integer) resultSet.getObject(COLUMN_NAME_STUDENT_ID),
                                            (Integer) resultSet.getObject(COLUMN_NAME_GROUP_ID),
                                            resultSet.getString(COLUMN_NAME_FIRST_NAME),
                                            resultSet.getString(COLUMN_NAME_LAST_NAME)));
            }
            
            return students;
        } catch (DAOException.DatabaseConnectionFail | SQLException e) {
            throw new DAOException.GetAllSutudentsFail(ERROR_GET_ALL, e);
        }
    }
    
    public int updateStudent(List<StudentDTO> students) throws DAOException.StudentUptatingFail {
        try(Connection con = UniversityDAOFactory.creatConnection();
            PreparedStatement statement = con.prepareStatement(SQL_UPDATE)) {
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
            } catch (SQLException e) {
                if (con != null) {
                    try {
                        con.rollback(save1);
                    } catch (SQLException exc) {
                        throw new SQLException(exc);
                    }
                }
                
                throw new SQLException(e);
            }
        } catch (DAOException.DatabaseConnectionFail | SQLException e) {
            throw new DAOException.StudentUptatingFail(ERROR_UDATE, e);
        }
    }
    
    public static void printSQlException(SQLException ex) {
        for (Throwable e : ex) {
            e.printStackTrace(System.err);
            System.err.println("SQLState: " + ((SQLException) e).getSQLState());
            System.err.println("Error Code: " + ((SQLException) e).getErrorCode());
            System.err.println("Message: " + e.getMessage());

            Throwable t = ex.getCause();

            while (t != null) {
                System.out.println("Cause: " + t);
            }
        }
    }
}
