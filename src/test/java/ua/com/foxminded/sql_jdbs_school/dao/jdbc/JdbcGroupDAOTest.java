package ua.com.foxminded.sql_jdbs_school.dao.jdbc;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.stream.Collectors;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import ua.com.foxminded.sql_jdbc_school.dao.DAOConnectionPool;
import ua.com.foxminded.sql_jdbc_school.dao.DAOException;
import ua.com.foxminded.sql_jdbc_school.dao.jdbc.JdbcGroupDAO;
import ua.com.foxminded.sql_jdbc_school.entity.GroupEntity;

@ExtendWith(MockitoExtension.class)
class JdbcGroupDAOTest {
    private static final int TOTAL_GROUP_QUANTITY = 2;
    private static final int STUDENTS_QUANTITY = 3;
    private static final int GROUP_QUANITY = 2;
    private static final String GET_ISERTED_GROUP = "selectInsertedGroup";
    private static final String GROUP_NAME = "group_name";
    private static final String NAME_OF_GROUP = "test";
    private static final String TEST_GROUP_QUERIES_PATH = "D:/repository/SqlJdbcSchool/src/main/"
            + "resource/test-group-queries.properties";
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
    DAOConnectionPool daoConnectionPoolMock;

    @BeforeEach
    void init() throws FileNotFoundException, SQLException, IOException {

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
    void readGroupsWithLessOrEqualStudents_AcceptableArg_RightStudentQuantity()
            throws FileNotFoundException, IOException, SQLException, DAOException {

        try (FileInputStream testDb = new FileInputStream(TEST_DB_PROP_PATH);
             FileInputStream groupQueries = new FileInputStream(GROUP_QUERIES_PATH);) {

            Properties properties = new Properties();
            properties.load(testDb);

            try (Connection con = DriverManager.getConnection(properties.getProperty(DB_URL),
                 properties.getProperty(USER_NAME), properties.getProperty(USER_PASS));) {

                when(daoConnectionPoolMock.getConnection()).thenReturn(con);
                assertEquals(GROUP_QUANITY,
                             universityGroupDao.getGroupsHavingLessOrEqualStudents(STUDENTS_QUANTITY)
                                               .size());
            }
        }
    }

    @Test
    void getAll_GettingAllDatabaseElements_RightGroupsQuantity() throws FileNotFoundException, 
                                                                                  IOException, 
                                                                                  SQLException, 
                                                                                  DAOException {
        
        try (FileInputStream testDbProperties = new FileInputStream(TEST_DB_PROP_PATH);) {

            Properties properties = new Properties();
            properties.load(testDbProperties);

            try (Connection con = DriverManager.getConnection(properties.getProperty(DB_URL),
                    properties.getProperty(USER_NAME), properties.getProperty(USER_PASS));) {

                when(daoConnectionPoolMock.getConnection()).thenReturn(con);
                assertEquals(TOTAL_GROUP_QUANTITY, universityGroupDao.getAll().size());
            }
        }
    }
    
    @Test
    void insert_InsertionOfAcceptableArgument_DatabaseHasArgument() throws FileNotFoundException, 
                                                                IOException, 
                                                                SQLException, 
                                                                DAOException {

        try (FileInputStream testDbProperites = new FileInputStream(TEST_DB_PROP_PATH);
             FileInputStream groupQueries = new FileInputStream(GROUP_QUERIES_PATH);
             FileInputStream testGroupQueries = new FileInputStream(TEST_GROUP_QUERIES_PATH);) {

            Properties properties = new Properties();
            properties.load(testDbProperites);
            properties.load(testGroupQueries);

            try (Connection con = DriverManager.getConnection(properties.getProperty(DB_URL),
                                                              properties.getProperty(USER_NAME), 
                                                              properties.getProperty(USER_PASS));) {

                List<GroupEntity> group = new ArrayList<>();
                group.add(new GroupEntity(NAME_OF_GROUP));
                when(daoConnectionPoolMock.getConnection()).thenReturn(con);
                universityGroupDao.insert(group);
                GroupEntity insertedGroup = null;
                
                try (Statement statement = con.createStatement();
                     ResultSet resultSet = statement.executeQuery(properties
                             .getProperty(GET_ISERTED_GROUP));) {
                    
                    while(resultSet.next()) {
                        insertedGroup = new GroupEntity(resultSet.getString(GROUP_NAME));
                    }
                }
                assertEquals(NAME_OF_GROUP, insertedGroup.getGroupName());
            }
        }
    }
    
    @Test
    void deleteAll_DatabaseAllElementsDeletion_DatabaseHasNoElements () {
        
    }
}
