package ua.com.foxminded.sql_jdbc_school.dao.university;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Savepoint;
import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import ua.com.foxminded.sql_jdbc_school.dao.DAOException;
import ua.com.foxminded.sql_jdbc_school.dao.DAOPropertiesCache;
import ua.com.foxminded.sql_jdbc_school.dao.StudentCourseDAO;
import ua.com.foxminded.sql_jdbc_school.dao.entities.StudentCourseEntity;

public class UniversityStudentCourseDAO implements StudentCourseDAO {
	private static final Logger LOGGER = LogManager.getLogger();
	private static final String QUERIES_FILENAME = "studentCourseQueries.properties";
    private static final String DELETE_STUDENT_FROM_COURSE = "deleteStudentFromCourse";
    private static final String SELECT_ALL = "selectAll";
    private static final String SELECT_ENTITY = "selectEntity";
    private static final String SELECT_STUDENTS_OF_COURSE = "selectStudentsOfCourse";
    private static final String INSERT = "insert";
    
    
    
    private static final String ERROR_READ_ALL = "Deleting the student from the course is failed.";
    private static final String ERROR_DELETE_STUDENT_FROM_COURSE = "Getting all the student course "
    															 + "relations from the database failed.";
    private static final String ERROR_READ = "Receiving the student-course "
                                           + "relation is failed.";
    private static final String ERROR_CREATE = "The student course hasn't been inserted.";
    private static final String ERROR_READ_STUDENTS_OF_COURSE = "The getting of students related to the "
                                                             + "specified course are failed.";
    private static final String STUDENT_ID = "student_id";
    private static final String GROUP_ID = "group_id";
    private static final String FIRST_NAME = "first_name";
    private static final String LAST_NAME = "last_name";
    private static final String COURSE_ID = "course_id";
    private static final String COURSE_NAME = "course_name";
    private static final String COURSE_DESC = "course_description";
    
    @Override
    public int deleteStudentFromCourse(int studentId, int courseId) throws DAOException {
        try (Connection con = UniversityDAOFactory.creatConnection();
             PreparedStatement statement = con.prepareStatement(DAOPropertiesCache
            		 .getInstance(QUERIES_FILENAME)
            		 .getProperty(DELETE_STUDENT_FROM_COURSE))) {

            statement.setInt(1, studentId);
            statement.setInt(2, courseId);
            return statement.executeUpdate();
        } catch (DAOException | SQLException e) {
        	LOGGER.error(ERROR_DELETE_STUDENT_FROM_COURSE, e);
            throw new DAOException(ERROR_DELETE_STUDENT_FROM_COURSE, e);
        }
    }
    
    @Override
    public List<StudentCourseEntity> getAll() throws DAOException {
        try (Connection con = UniversityDAOFactory.creatConnection();
             PreparedStatement statement = con.prepareStatement(DAOPropertiesCache
            		 .getInstance(QUERIES_FILENAME)
            		 .getProperty(SELECT_ALL));
             ResultSet resultSet = statement.executeQuery();) {
            
            List<StudentCourseEntity> studentCourse = new ArrayList<>();
            
            while (resultSet.next()) {
                studentCourse.add(new StudentCourseEntity((Integer) resultSet.getObject(STUDENT_ID),
                                                          (Integer) resultSet.getObject(GROUP_ID),
                                                          resultSet.getString(FIRST_NAME),
                                                          resultSet.getString(LAST_NAME),
                                                          (Integer) resultSet.getObject(COURSE_ID),
                                                          resultSet.getString(COURSE_NAME),
                                                          resultSet.getString(COURSE_DESC)));
            }
            return studentCourse;
        } catch (DAOException | SQLException e) {
        	LOGGER.error(ERROR_READ_ALL, e);
            throw new DAOException(ERROR_READ_ALL, e);
        }
    }
    @Override
    public StudentCourseEntity read(int studentId, int courseId) throws DAOException {
        try (Connection con = UniversityDAOFactory.creatConnection();
             PreparedStatement statement = con.prepareStatement(DAOPropertiesCache
            		 .getInstance(QUERIES_FILENAME)
            		 .getProperty(SELECT_ENTITY))) {
            
            StudentCourseEntity studentCourse = null;
            statement.setInt(1, studentId);
            statement.setInt(2, courseId);
            ResultSet resultSet = statement.executeQuery();
            
            while (resultSet.next()) {
            	studentCourse = new StudentCourseEntity((Integer) resultSet.getObject(STUDENT_ID),
                                                        (Integer) resultSet.getObject(GROUP_ID),
                                                        resultSet.getString(FIRST_NAME),
                                                        resultSet.getString(LAST_NAME),
                                                        (Integer) resultSet.getObject(COURSE_ID),
                                                        resultSet.getString(COURSE_NAME),
                                                        resultSet.getString(COURSE_DESC));
            }
            resultSet.close();
            return studentCourse;
        } catch (DAOException | SQLException e) {
        	LOGGER.error(ERROR_READ, e);
            throw new DAOException(ERROR_READ, e);
        }
    }
    
    @Override
    public List<StudentCourseEntity> readStudentsOfCourse(int courseId) throws DAOException {
        try (Connection con = UniversityDAOFactory.creatConnection();
             PreparedStatement statement = con.prepareStatement(String.format(DAOPropertiesCache
            		 .getInstance(QUERIES_FILENAME)
            		 .getProperty(SELECT_STUDENTS_OF_COURSE), courseId));
             ResultSet resultSet = statement.executeQuery();) {
            
        	List<StudentCourseEntity> studentCourse = new ArrayList<>();
            
            while (resultSet.next()) {
                studentCourse.add(new StudentCourseEntity((Integer) resultSet.getObject(STUDENT_ID),
                                                          (Integer) resultSet.getObject(GROUP_ID),
                                                          resultSet.getString(FIRST_NAME), 
                                                          resultSet.getString(LAST_NAME),
                                                          (Integer) resultSet.getObject(COURSE_ID),
                                                          resultSet.getString(COURSE_NAME),
                                                          resultSet.getString(COURSE_DESC)));
            }
            
            return studentCourse;
        } catch (DAOException | SQLException e) {
        	LOGGER.error(ERROR_READ_STUDENTS_OF_COURSE, e);
            throw new DAOException(ERROR_READ_STUDENTS_OF_COURSE, e);
        }
    }
    
    @Override
    public Integer insert(List<StudentCourseEntity> studentsCourseEntities) throws DAOException {
        try (Connection con = UniversityDAOFactory.creatConnection();
             PreparedStatement statement = con.prepareStatement(DAOPropertiesCache
            		 .getInstance(QUERIES_FILENAME)
            		 .getProperty(INSERT))) {
            con.setAutoCommit(false);
            Savepoint save1 = con.setSavepoint();
            int status = 0;
            
            try {
                for (StudentCourseEntity studentCourse : studentsCourseEntities) {
                     statement.setObject(1, studentCourse.getStudentId());
                     statement.setObject(2, studentCourse.getGroupId());
                     statement.setString(3, studentCourse.getFirstName());
                     statement.setString(4, studentCourse.getLastName());
                     statement.setObject(5, studentCourse.getCourseId());
                     statement.setObject(6, studentCourse.getCourseName());
                     statement.setString(7, studentCourse.getCourseDescription());
                     status += statement.executeUpdate();
                }
                con.commit();
                return status;
            } catch (SQLException e) {
                if (con != null) {
                    try {
                        con.rollback(save1);
                    } catch (SQLException ex) {
                        throw new SQLException(ex);
                    }
                }
                throw new SQLException (e);
            }
        } catch (DAOException | SQLException e) {
        	LOGGER.error(ERROR_CREATE, e);
            throw new DAOException(ERROR_CREATE, e);
        }
    }
}
