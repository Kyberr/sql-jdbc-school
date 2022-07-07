package ua.com.foxminded.sql_jdbs_school.dao.jdbc;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.sql.Connection;
import java.sql.SQLException;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import ua.com.foxminded.sql_jdbc_school.dao.DAOConnectionFactory;
import ua.com.foxminded.sql_jdbc_school.dao.DAOException;
import ua.com.foxminded.sql_jdbc_school.dao.jdbc.JdbcDAOConnectionPool;

@ExtendWith(SpringExtension.class)
@ExtendWith(MockitoExtension.class)
@ContextConfiguration(classes = TestConfig.class)
class JdbcDAOConnectionPoolTest {
    private static final int MORE_MAX_POOL_SIZE = 21;
    private static final int MAX_POOL_SIZE = 20;
    
    @Autowired
    JdbcTemplate jdbcTemplate;
    
    @InjectMocks
    JdbcDAOConnectionPool jdbcDaoConnectionPool;
    
    @Mock
    DAOConnectionFactory jdbcDaoConnectionFactoryMock;
    
    
    @Test
    void getConnection_ExceedingPoolSize_ThrowingException() throws SQLException, 
                                                                    DAOException {
        try (Connection connection = jdbcTemplate.getDataSource().getConnection();) {
            when(jdbcDaoConnectionFactoryMock.createConnection()).thenReturn(connection);

            assertThrows(DAOException.class, () -> {
                for (int i = 0; i < MORE_MAX_POOL_SIZE; i++) {
                    jdbcDaoConnectionPool.getConnection();
                }
            });
        }
    }
    
    @Test
    void getConnection_UsingAllConnectionsOfPool_CorrectCreatedConnectionsQuantity() throws SQLException, 
                                                                                            DAOException {
        try (Connection connection = jdbcTemplate.getDataSource().getConnection();) {
            when(jdbcDaoConnectionFactoryMock.createConnection()).thenReturn(connection);
            
            Connection invokedConnection = null;
            
            for (int i = 0; i < MAX_POOL_SIZE; i++) {
                invokedConnection = jdbcDaoConnectionPool.getConnection();
            }
            
            for (int i = 0; i < MAX_POOL_SIZE; i++) {
                jdbcDaoConnectionPool.releaseConnection(invokedConnection);
            }
            verify(jdbcDaoConnectionFactoryMock, times(20)).createConnection();
        }
    }
    
    @Test
    void getConnection_GettingConnectionFromPool_GettedConnection() throws DAOException, 
                                                                           SQLException {
        try (Connection connection = jdbcTemplate.getDataSource().getConnection();) {
            when(jdbcDaoConnectionFactoryMock.createConnection()).thenReturn(connection);
            jdbcDaoConnectionPool.getConnection();
            jdbcDaoConnectionPool.releaseConnection(connection);
            jdbcDaoConnectionPool.getConnection();
            verify(jdbcDaoConnectionFactoryMock, times(1)).createConnection();
        }
    }
    
    @Test
    void getConnection_CreatingConnection_CreatedConnection() throws DAOException {
        jdbcDaoConnectionPool.getConnection();
        verify(jdbcDaoConnectionFactoryMock, times(1)).createConnection();
    }
}
