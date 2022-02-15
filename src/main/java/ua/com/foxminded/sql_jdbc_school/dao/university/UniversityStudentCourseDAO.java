package ua.com.foxminded.sql_jdbc_school.dao.university;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Savepoint;
import java.util.ArrayList;
import java.util.List;
import ua.com.foxminded.sql_jdbc_school.dao.DAOException;
import ua.com.foxminded.sql_jdbc_school.dao.StudentCourseDAO;
import ua.com.foxminded.sql_jdbc_school.services.dto.StudentCourseDTO;

public class UniversityStudentCourseDAO implements StudentCourseDAO {
    private static final String SQL_CREATE_TABLE = "create table department.student_course ("
            + "student_id integer references department.students(student_id) on delete cascade,"
            + "group_id integer references department.groups(group_id) on delete set null,"
            + "first_name varchar collate pg_catalog.\"default\","
            + "last_name varchar collate pg_catalog.\"default\","
            + "course_id integer references department.courses(course_id) on delete set null,"
            + "course_name varchar collate pg_catalog.\"default\","
            + "course_description varchar collate pg_catalog.\"default\")"
            + "tablespace pg_default";
    private static final String SQL_STUDENTS_OF_COURSE = "select * from department"
            + ".student_course where course_id = %s";
    private static final String SQL_INSERT = "insert into department.student_course("
            + "student_id, group_id, first_name, last_name, course_id, course_name, course_description) "
            + "values (?, ?, ?, ?, ?, ?, ?)";
    private static final String ERROR_TABLE_CREATION = "The student course view creating is failed.";
    private static final String ERROR_GET_STUDENTS_OF_COURSE = "The getting of students related to the "
            + "specified course are failed.";
    private static final String COLUMN_NAME_STUDENT_ID = "student_id";
    private static final String COLUMN_NAME_GROUP_ID = "group_id";
    private static final String COLUMN_NAME_FIRST_NAME = "first_name";
    private static final String COLUMN_NAME_LAST_NAME = "last_name";
    private static final String COLUMN_NAME_COURSE_ID = "course_id";
    private static final String COLUMN_NAME_COURSE_NAME = "course_name";
    private static final String COLUMN_NAME_COURSE_DESC = "course_description";
    
    @Override
    public List<StudentCourseDTO> getStudentsOfCourse(int courseID) 
            throws DAOException.GetStudentRelatedToCourseFailure {
        try (Connection con = UniversityDAOFactory.creatConnection();
             PreparedStatement statement = con.prepareStatement(String.format(SQL_STUDENTS_OF_COURSE, courseID));
             ResultSet resultSet = statement.executeQuery();) {
            List<StudentCourseDTO> studentCourse = new ArrayList<>();
            
            while (resultSet.next()) {
                studentCourse.add(new StudentCourseDTO((Integer) resultSet.getObject(COLUMN_NAME_STUDENT_ID),
                                                       (Integer) resultSet.getObject(COLUMN_NAME_GROUP_ID),
                                                       resultSet.getString(COLUMN_NAME_FIRST_NAME), 
                                                       resultSet.getString(COLUMN_NAME_LAST_NAME),
                                                       (Integer) resultSet.getObject(COLUMN_NAME_COURSE_ID),
                                                       resultSet.getString(COLUMN_NAME_COURSE_NAME),
                                                       resultSet.getString(COLUMN_NAME_COURSE_DESC)));
            }
            return studentCourse;
        } catch (DAOException.DatabaseConnectionFail 
                | SQLException e) {
            throw new DAOException.GetStudentRelatedToCourseFailure(ERROR_GET_STUDENTS_OF_COURSE, e);
        }
    }
    
    @Override
    public int insertStudentCourse(List<StudentCourseDTO> studentsCourses) 
            throws DAOException.StudentCourseInsertionFailure {
        try (Connection con = UniversityDAOFactory.creatConnection();
             PreparedStatement statement = con.prepareStatement(SQL_INSERT)) {
            con.setAutoCommit(false);
            Savepoint save1 = con.setSavepoint();
            int status = 0;
            
            try {
                for (StudentCourseDTO studentCourse : studentsCourses) {
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
        } catch (DAOException.DatabaseConnectionFail | SQLException e) {
            throw new DAOException.StudentCourseInsertionFailure(null, e);
        }
    }
    
    @Override
    public int createStudentCourseTable() throws DAOException.CreatingStudentCourseTableFailure {
        try (Connection con = UniversityDAOFactory.creatConnection();
             PreparedStatement statement = con.prepareStatement(SQL_CREATE_TABLE)) {
            return statement.executeUpdate();
        } catch (DAOException.DatabaseConnectionFail 
                | SQLException e) {
            throw new DAOException.CreatingStudentCourseTableFailure(ERROR_TABLE_CREATION, e);
        }
    }
}
