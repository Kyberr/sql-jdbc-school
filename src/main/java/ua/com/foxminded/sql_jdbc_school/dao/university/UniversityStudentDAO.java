package ua.com.foxminded.sql_jdbc_school.dao.university;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Savepoint;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import ua.com.foxminded.sql_jdbc_school.dao.ConnectionDAOFactory;
import ua.com.foxminded.sql_jdbc_school.dao.DAOException;
import ua.com.foxminded.sql_jdbc_school.dao.DAOPropertiesCache;
import ua.com.foxminded.sql_jdbc_school.dao.StudentDAO;
import ua.com.foxminded.sql_jdbc_school.dao.entities.StudentEntity;

public class UniversityStudentDAO implements StudentDAO {
    
	private static final Logger LOGGER = LogManager.getLogger();
	private static final String QUERIES_FILE_NAME = "student-queries.properties";
	private static final String SELECT_STUDENTS_WITH_GROUP = "selectStudentsWithGroup";
	private static final String SELECT_STUDENT = "selectStudent";
	private static final String DELETE_STUDENT = "deleteStudent";
	private static final String INSERT_STUDENTS = "insertStudent";
	private static final String SELECT_ALL = "selectAll";
    private static final String UPDATE = "update";
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
    private final ConnectionDAOFactory universityConnectionDAOFactory;
    
    public UniversityStudentDAO(ConnectionDAOFactory universityConnectionDAOFactory) {
		this.universityConnectionDAOFactory = universityConnectionDAOFactory;
	}

	@Override
    public List<StudentEntity> getStudentsHavingGroupId() throws DAOException {
        try (Connection con = universityConnectionDAOFactory.createConnection();
             PreparedStatement statement = con.prepareStatement(DAOPropertiesCache
            		 .getInstance(QUERIES_FILE_NAME).getProperty(SELECT_STUDENTS_WITH_GROUP));
             ResultSet resultSet = statement.executeQuery();) {
            
            List<StudentEntity> studentsHavingGroupId = new ArrayList<>();
            
            while (resultSet.next()) {
                studentsHavingGroupId.add(new StudentEntity(resultSet.getInt(STUDENT_ID),
                                                            resultSet.getInt(GROUP_ID),
                                                            resultSet.getString(FIRST_NAME),
                                                            resultSet.getString(LAST_NAME)));
            }
            
            return studentsHavingGroupId;
        } catch (DAOException | SQLException e) {
        	LOGGER.error(ERROR_GET_STUDENTS_WITHOUT_GROUP, e);
            throw new DAOException(ERROR_GET_STUDENTS_WITHOUT_GROUP, e);
        }
    }
    
    @Override
    public StudentEntity getById(int studentId) throws DAOException {
        try (Connection con = universityConnectionDAOFactory.createConnection();
             PreparedStatement statement = con.prepareStatement(DAOPropertiesCache
            		 .getInstance(QUERIES_FILE_NAME).getProperty(SELECT_STUDENT));) {

            StudentEntity student = null;
            statement.setInt(1, studentId);
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                student = new StudentEntity(resultSet.getInt(STUDENT_ID), 
                                         (Integer) resultSet.getObject(GROUP_ID),
                                         resultSet.getString(FIRST_NAME), 
                                         resultSet.getString(LAST_NAME));
            }
            resultSet.close();
            return student;
        } catch (DAOException | SQLException e) {
        	LOGGER.error(ERROR_GET_STUDENT, e);
            throw new DAOException(ERROR_GET_STUDENT, e);
        }
    }
    
    @Override
    public int deleteById(int studentId) throws DAOException {
        try (Connection con = universityConnectionDAOFactory.createConnection();
             PreparedStatement statement = con.prepareStatement(DAOPropertiesCache
            		 .getInstance(QUERIES_FILE_NAME).getProperty(DELETE_STUDENT))) {
            
            statement.setInt(1, studentId);
            return statement.executeUpdate();
        } catch (DAOException | SQLException e) {
        	LOGGER.error(ERROR_DELETE, e);
            throw new DAOException(ERROR_DELETE, e);
        }
    }
    
    @Override
    public Integer insert(List<StudentEntity> studentEntities) throws DAOException {
        try(Connection con = universityConnectionDAOFactory.createConnection();
            PreparedStatement statement = con.prepareStatement(DAOPropertiesCache
            		.getInstance(QUERIES_FILE_NAME).getProperty(INSERT_STUDENTS));) {
            con.setAutoCommit(false);
            Savepoint save1 = con.setSavepoint();
            
            try {
                int status = 0;
                
                for (StudentEntity student : studentEntities) {
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
        	LOGGER.error(ERROR_INSERT, e);
            throw new DAOException(ERROR_INSERT, e);
        } 
    }
    
    @Override
    public List<StudentEntity> getAll() throws DAOException {
        try(Connection con = universityConnectionDAOFactory.createConnection();
            Statement statement = con.createStatement();
            ResultSet resultSet = statement.executeQuery(DAOPropertiesCache
            		.getInstance(QUERIES_FILE_NAME).getProperty(SELECT_ALL));) {
            List<StudentEntity> students = new ArrayList<>();
            
            while(resultSet.next()) {
                students.add(new StudentEntity((Integer) resultSet.getObject(STUDENT_ID),
                                            (Integer) resultSet.getObject(GROUP_ID),
                                            resultSet.getString(FIRST_NAME),
                                            resultSet.getString(LAST_NAME)));
            }
            return students;
        } catch (DAOException | SQLException e) {
        	LOGGER.error(ERROR_GET_ALL, e);
            throw new DAOException(ERROR_GET_ALL, e);
        }
    }
    
    @Override
    public int update(List<StudentEntity> students) throws DAOException {
        try(Connection con = universityConnectionDAOFactory.createConnection();
            PreparedStatement statement = con.prepareStatement(DAOPropertiesCache
            		.getInstance(QUERIES_FILE_NAME).getProperty(UPDATE))) {
            con.setAutoCommit(false);
            Savepoint save1 = con.setSavepoint();
            int status = 0;
            
            try {
                for (StudentEntity student : students) {
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
        	LOGGER.error(ERROR_UDATE, e);
            throw new DAOException(ERROR_UDATE, e);
        }
    }

}
