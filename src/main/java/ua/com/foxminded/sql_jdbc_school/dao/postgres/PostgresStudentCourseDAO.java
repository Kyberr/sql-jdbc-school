package ua.com.foxminded.sql_jdbc_school.dao.postgres;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Savepoint;
import java.util.ArrayList;
import java.util.List;
import ua.com.foxminded.sql_jdbc_school.dao.DAOException;
import ua.com.foxminded.sql_jdbc_school.dao.StudentCourseDAO;
import ua.com.foxminded.sql_jdbc_school.dao.entities.StudentCourseEntity;

public class PostgresStudentCourseDAO implements StudentCourseDAO {
    
    private static final String DELETE_STUDENT_FROM_COURSE = "delete from department.student_course "
            + "where student_id = ? and course_id = ?";
    private static final String SELECT_ALL = "select student_id, group_id, first_name, last_name, course_id, "
            + "course_name, course_description from department.student_course";
    private static final String SELECT_STUDENT_ID_COURSE_ID = "select student_id, group_id, first_name, last_name, "
            + "course_id, course_name, course_description "
            + "from department.student_course "
            + "where student_id = ? and course_id = ?"; 
    private static final String CREATE_TABLE = 
            "drop table if exists department.student_course;"
            + "create table department.student_course ("
            + "student_id integer,"
            + "group_id integer references department.groups(group_id) on delete set null,"
            + "first_name varchar collate pg_catalog.\"default\","
            + "last_name varchar collate pg_catalog.\"default\","
            + "course_id integer,"
            + "course_name varchar collate pg_catalog.\"default\","
            + "course_description varchar collate pg_catalog.\"default\","
            + "foreign key (student_id) references department.students on delete cascade,"
            + "foreign key (course_id) references department.courses on delete cascade,"
            + "primary key (student_id, course_id))"
            + "tablespace pg_default;";
    private static final String SELECT_STUDENTS_OF_COURSE = "select * from department"
                                                          + ".student_course where course_id = %s";
    private static final String INSERT = "insert into department.student_course("
            + "student_id, group_id, first_name, last_name, course_id, course_name, course_description) "
            + "values (?, ?, ?, ?, ?, ?, ?)";
    private static final String ERROR_GET_ALL = "Deleting the student from the course is failed.";
    private static final String ERROR_DEL_STUDENT_FROM_COURSE = "Getting all the student course relations from the database failed.";
    private static final String ERROR_SELECT_STUDENT_ID_COURSE_ID = "Receiving the student-course "
                                                                  + "relation is failed.";
    private static final String ERROR_TABLE_CREATION = "The student_course table creating is failed.";
    private static final String ERROR_INSERT_STUDENTS_COURSE = "The student course hasn't been inserted.";
    private static final String ERROR_GET_STUDENTS_OF_COURSE = "The getting of students related to the "
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
        try (Connection con = PostgresDAOFactory.creatConnection();
             PreparedStatement statement = con.prepareStatement(DELETE_STUDENT_FROM_COURSE)) {

            statement.setInt(1, studentId);
            statement.setInt(2, courseId);
            return statement.executeUpdate();
        } catch (DAOException | SQLException e) {
            throw new DAOException(ERROR_DEL_STUDENT_FROM_COURSE, e);
        }
    }
    
    @Override
    public List<StudentCourseEntity> getAllStudentCourse() throws DAOException {
        try (Connection con = PostgresDAOFactory.creatConnection();
             PreparedStatement statement = con.prepareStatement(SELECT_ALL);
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
            throw new DAOException(ERROR_GET_ALL, e);
        }
    }
    @Override
    public List<StudentCourseEntity> getStudentCourse(int studentId, int courseId) throws DAOException {
        try (Connection con = PostgresDAOFactory.creatConnection();
             PreparedStatement statement = con.prepareStatement(SELECT_STUDENT_ID_COURSE_ID)) {
            
            List<StudentCourseEntity> studentCourse = new ArrayList<>();
            statement.setInt(1, studentId);
            statement.setInt(2, courseId);
            ResultSet resultSet = statement.executeQuery();
            
            while (resultSet.next()) {
                studentCourse.add(new StudentCourseEntity((Integer) resultSet.getObject(STUDENT_ID),
                                                          (Integer) resultSet.getObject(GROUP_ID),
                                                          resultSet.getString(FIRST_NAME),
                                                          resultSet.getString(LAST_NAME),
                                                          (Integer) resultSet.getObject(COURSE_ID),
                                                          resultSet.getString(COURSE_NAME),
                                                          resultSet.getString(COURSE_DESC)));
            }
            resultSet.close();
            return studentCourse;
        } catch (DAOException | SQLException e) {
            throw new DAOException(ERROR_SELECT_STUDENT_ID_COURSE_ID, e);
        }
    }
    
    @Override
    public List<StudentCourseEntity> getStudentsOfCourse(int courseId) throws DAOException {
        try (Connection con = PostgresDAOFactory.creatConnection();
             PreparedStatement statement = con.prepareStatement(String
            		 .format(SELECT_STUDENTS_OF_COURSE, courseId));
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
            throw new DAOException(ERROR_GET_STUDENTS_OF_COURSE, e);
        }
    }
    
    @Override
    public Integer create(List<StudentCourseEntity> studentsCourseEntities) throws DAOException {
        try (Connection con = PostgresDAOFactory.creatConnection();
             PreparedStatement statement = con.prepareStatement(INSERT)) {
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
            throw new DAOException(ERROR_INSERT_STUDENTS_COURSE, e);
        }
    }
    
    @Override
    public int createStudentCourseTable() throws DAOException {
        try (Connection con = PostgresDAOFactory.creatConnection();
             PreparedStatement statement = con.prepareStatement(CREATE_TABLE)) {
            return statement.executeUpdate();
        } catch (DAOException | SQLException e) {
            throw new DAOException(ERROR_TABLE_CREATION, e);
        }
    }
}
