package ua.com.foxminded.sql_jdbs_school.dao.university;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.acl.Group;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.stream.Collectors;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import ua.com.foxminded.sql_jdbc_school.dao.ConnectionDAOFactory;
import ua.com.foxminded.sql_jdbc_school.dao.DAOConnectionPool;
import ua.com.foxminded.sql_jdbc_school.dao.DAOException;
import ua.com.foxminded.sql_jdbc_school.dao.jdbc.JdbcGroupDAO;
import ua.com.foxminded.sql_jdbc_school.entity.GroupEntity;

@ExtendWith(MockitoExtension.class)
class JdbcGroupDAOTest {
    private static final Logger LOGGER = LogManager.getLogger();
    private static final int INDEX_OF_ELEMENT = 0;
    private static final int TOTAL_GROUP_QUANTITY = 2;
    private static final int STUDENTS_QUANTITY = 3;
    private static final int GROUP_QUANITY = 2;
    private static final int GROUP_ID = 10;
    private static final String GROUP_NAME_COLUMN = "group_name";
    private static final String GROUP_NAME = "test";
    private static final String SELECT_QUERY = "iselect * from department.groups where group_name = 'test';";
    private static final String GROUP_QUERIES_PATH = "D:/repository/SqlJdbcSchool/src/main/"
            + "resource/group-queries.properties";
    private static final String TEST_DATA_PATH = "D:/repository/SqlJdbcSchool/src/main/resource/test-data.sql";
    private static final String END_LINE = "\n";
    private static final String TEST_TABLES_SCRIPT_PATH = "D:/repository/SqlJdbcSchool/"
            + "src/main/resource/test-schema.sql";
    private static final String TEST_DB_PROP_PATH = "D:/repository/SqlJdbcSchool/"
            + "src/main/resource/test-db.properties";
    private static final String DB_URL = "databaseURL";
    private static final String USER_NAME = "databaseUser";
    private static final String USER_PASS = "databaseUserPassword";

    @InjectMocks
    JdbcGroupDAO universityGroupDao;

    @Mock
    DAOConnectionPool connectionPoolMock;

    @BeforeAll
    static void init() throws FileNotFoundException, SQLException, IOException {

        try (FileInputStream testDbInput = new FileInputStream(TEST_DB_PROP_PATH);) {

            Properties testDbPoperites = new Properties();
            testDbPoperites.load(testDbInput);

            try (Connection con = DriverManager.getConnection(testDbPoperites.getProperty(DB_URL),
                                                              testDbPoperites.getProperty(USER_NAME), 
                                                              testDbPoperites.getProperty(USER_PASS));
                 Statement statement = con.createStatement();) {

                Path tableScriptPath = Paths.get(TEST_TABLES_SCRIPT_PATH);
                String tablesScript = Files.lines(tableScriptPath).collect(Collectors.joining(END_LINE));
                statement.execute(tablesScript);

                Path testDataScriptPath = Paths.get(TEST_DATA_PATH);
                String testDataScrip = Files.lines(testDataScriptPath).collect(Collectors.joining(END_LINE));
                statement.execute(testDataScrip);
            }
        }
    }

    @Test
    void readGroupsWithLessOrEqualStudents_AnyAcceptableArg_RightStudentQuantity()
            throws FileNotFoundException, IOException, SQLException, DAOException {

        try (FileInputStream testDb = new FileInputStream(TEST_DB_PROP_PATH);
             FileInputStream groupQueries = new FileInputStream(GROUP_QUERIES_PATH);) {

            Properties properties = new Properties();
            properties.load(testDb);

            try (Connection con = DriverManager.getConnection(properties.getProperty(DB_URL),
                 properties.getProperty(USER_NAME), properties.getProperty(USER_PASS));) {

                when(connectionPoolMock.getConnection()).thenReturn(con);
                assertEquals(GROUP_QUANITY,
                             universityGroupDao.getGroupsHavingLessOrEqualStudents(STUDENTS_QUANTITY)
                                               .size());
            }
        }
    }

    @Test
    void getAll_Call_RightGroupsQuantity() throws FileNotFoundException, IOException, SQLException, DAOException {
        try (FileInputStream testDbProperties = new FileInputStream(TEST_DB_PROP_PATH);) {

            Properties properties = new Properties();
            properties.load(testDbProperties);

            try (Connection con = DriverManager.getConnection(properties.getProperty(DB_URL),
                    properties.getProperty(USER_NAME), properties.getProperty(USER_PASS));) {

                when(connectionPoolMock.getConnection()).thenReturn(con);
                assertEquals(TOTAL_GROUP_QUANTITY, universityGroupDao.getAll().size());
            }
        }
    }
    
    @Test
    void insert_AcceptableArgument_Success() throws FileNotFoundException, 
                                                    IOException, 
                                                    SQLException, 
                                                    DAOException {

        try (FileInputStream testDbProperites = new FileInputStream(TEST_DB_PROP_PATH);
                FileInputStream groupQueries = new FileInputStream(GROUP_QUERIES_PATH);) {

            Properties properties = new Properties();
            properties.load(testDbProperites);

            try (Connection con = DriverManager.getConnection(properties.getProperty(DB_URL),
                    properties.getProperty(USER_NAME), properties.getProperty(USER_PASS));
            // PreparedStatement prStatement =
            // con.prepareStatement(properties.getProperty(SELECT_QUERY));
            // ResultSet resultSet = prStatement.executeQuery();
            ) {

                List<GroupEntity> group = new ArrayList<>();
                group.add(new GroupEntity(GROUP_ID, GROUP_NAME));

                when(universityConnectionDaoFactory.createConnection()).thenReturn(con);
                // universityGroupDao.insert(group);
                assertEquals(1, universityGroupDao.insert(group));

                /*
                 * List<GroupEntity> result = new ArrayList<>(); ResultSet resultSet =
                 * prStatement.executeQuery();
                 * 
                 * while (resultSet.next()) { result.add(new GroupEntity(GROUP_ID,
                 * resultSet.getString(GROUP_NAME_COLUMN))); } LOGGER.error(result.toString());
                 * assertEquals(result.get(INDEX_OF_ELEMENT).getGroupName(),
                 * group.get(INDEX_OF_ELEMENT).getGroupName());
                 */
            }
        }
    }
    
}
