package ua.com.foxminded.sql_jdbs_school.dao.jdbc;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
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
    private static final String TEST_DATA = "test-data.sql";
    private static final String TEST_DB_PROPERTIES = "test-db.properties";
    private static final String TEST_QUERIES = "test-queries.properties";
    private static final String TEST_SCHEMA = "test-schema.sql";
    private static final String END_LINE = "\n";
    private static final String DB_URL = "databaseURL";
    private static final String USER_NAME = "databaseUser";
    private static final String USER_PASS = "databaseUserPassword";
    private static final boolean RESULTSET_IS_EMPTY = false;

    @InjectMocks
    JdbcGroupDAO jdbcGroupDao;

    @Mock
    DAOConnectionPool daoConnectionPoolMock;

    @BeforeEach
    void init() throws FileNotFoundException, SQLException, IOException {
        

        try (InputStream testDbProperitesIn = Thread.currentThread()
                                             .getContextClassLoader()
                                             .getResourceAsStream(TEST_DB_PROPERTIES)) {

            Properties testDbPoperites = new Properties();
            testDbPoperites.load(testDbProperitesIn);

            try (InputStream schemaInput = Thread.currentThread()
                                                 .getContextClassLoader()
                                                 .getResourceAsStream(TEST_SCHEMA);
                 InputStream testDataInput = Thread.currentThread()
                                                   .getContextClassLoader()
                                                   .getResourceAsStream(TEST_DATA);
                 Connection con = DriverManager.getConnection(testDbPoperites.getProperty(DB_URL),
                                                              testDbPoperites.getProperty(USER_NAME), 
                                                              testDbPoperites.getProperty(USER_PASS));
                 Statement statement = con.createStatement();) {
                
                String schema = new BufferedReader(new InputStreamReader(schemaInput)).lines()
                        .collect(Collectors.joining(END_LINE));
                String testData = new BufferedReader(new InputStreamReader(testDataInput)).lines()
                        .collect(Collectors.joining(END_LINE));
                statement.execute(schema);
                statement.execute(testData);
            }
        }
    }

    @Test
    void readGroupsWithLessOrEqualStudents_AcceptableArg_RightStudentQuantity()
            throws FileNotFoundException, IOException, SQLException, DAOException {

        try (InputStream testDbPropertiesIn = this.getClass()
                                                  .getClassLoader()
                                                  .getResourceAsStream(TEST_DB_PROPERTIES);) {

            Properties dbProp = new Properties();
            dbProp.load(testDbPropertiesIn);

            try (Connection connection = DriverManager.getConnection(dbProp.getProperty(DB_URL),
                                                                     dbProp.getProperty(USER_NAME), 
                                                                     dbProp.getProperty(USER_PASS));) {

                when(daoConnectionPoolMock.getConnection()).thenReturn(connection);
                assertEquals(GROUP_QUANITY,
                             jdbcGroupDao.getGroupsHavingLessOrEqualStudents(STUDENTS_QUANTITY)
                                               .size());
            }
        }
    }

    @Test
    void getAll_GettingAllDatabaseElements_RightGroupsQuantity() throws FileNotFoundException, 
                                                                                  IOException, 
                                                                                  SQLException, 
                                                                                  DAOException {
        
        try (InputStream testDbPropertiesIn = this.getClass().getClassLoader()
                                                             .getResourceAsStream(TEST_DB_PROPERTIES);) {

            Properties properties = new Properties();
            properties.load(testDbPropertiesIn);

            try (Connection con = DriverManager.getConnection(properties.getProperty(DB_URL),
                                                              properties.getProperty(USER_NAME), 
                                                              properties.getProperty(USER_PASS));) {

                when(daoConnectionPoolMock.getConnection()).thenReturn(con);
                assertEquals(TOTAL_GROUP_QUANTITY, jdbcGroupDao.getAll().size());
            }
        }
    }
    
    @Test
    void insert_InsertionOfAcceptableArgument_DatabaseHasArgument() throws FileNotFoundException, 
                                                                IOException, 
                                                                SQLException, 
                                                                DAOException {

        try (InputStream testDbPropertiesIn = this.getClass().getClassLoader()
                                                             .getResourceAsStream(TEST_DB_PROPERTIES);
             InputStream testQueriesIn = this.getClass().getClassLoader()
                                                        .getResourceAsStream(TEST_QUERIES);) {

            Properties properties = new Properties();
            properties.load(testDbPropertiesIn);
            properties.load(testQueriesIn);

            try (Connection con = DriverManager.getConnection(properties.getProperty(DB_URL),
                                                              properties.getProperty(USER_NAME), 
                                                              properties.getProperty(USER_PASS));) {

                List<GroupEntity> group = new ArrayList<>();
                group.add(new GroupEntity(NAME_OF_GROUP));
                when(daoConnectionPoolMock.getConnection()).thenReturn(con);
                jdbcGroupDao.insert(group);
                GroupEntity insertedGroup = null;
                
                try (Statement statement = con.createStatement();
                     ResultSet resultSet = statement.executeQuery(
                             properties.getProperty(GET_ISERTED_GROUP));) {
                    
                    while(resultSet.next()) {
                        insertedGroup = new GroupEntity(resultSet.getString(GROUP_NAME));
                    }
                }
                assertEquals(NAME_OF_GROUP, insertedGroup.getGroupName());
            }
        }
    }
    
    @Test
    void deleteAll_AllDatabaseElementsDeletion_DatabaseHasNoElements () throws IOException, 
                                                                               SQLException, 
                                                                               DAOException {
        try (InputStream testDbPropertiesIn = this.getClass().getClassLoader()
                                                             .getResourceAsStream(TEST_DB_PROPERTIES);) {
            Properties properties = new Properties();
            properties.load(testDbPropertiesIn);
            
            try (Connection con = DriverManager.getConnection(properties.getProperty(DB_URL), 
                                                              properties.getProperty(USER_NAME), 
                                                              properties.getProperty(USER_PASS));
                 PreparedStatement prStatement = con.prepareStatement("select * from department.groups");
                 InputStream testQueries = this.getClass().getClassLoader()
                                                          .getResourceAsStream(TEST_QUERIES);) {
                
                when(daoConnectionPoolMock.getConnection()).thenReturn(con);
                jdbcGroupDao.deleteAll();
                
                try (ResultSet resultSet = prStatement.executeQuery();) {
                    
                    assertEquals(RESULTSET_IS_EMPTY , resultSet.next());
                }
            }
        }
    }
}
