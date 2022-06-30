package ua.com.foxminded.sql_jdbs_school.dao.jdbc;

import javax.sql.DataSource;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;

@Configuration
public class TestConfig {
    
    @Bean
    public DataSource dataSource() {
        return new EmbeddedDatabaseBuilder().generateUniqueName(true)
                                            .setType(EmbeddedDatabaseType.H2)
                                            .setScriptEncoding("UTF-8")
                                            .addScript("/test-schema.sql")
                                            .addScript("/test-data.sql")
                                            .build();
    }
    
    @Bean
    public JdbcTemplate jdbcTemplate() {
        return new JdbcTemplate(dataSource());
    }
}
