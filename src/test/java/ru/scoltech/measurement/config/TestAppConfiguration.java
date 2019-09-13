package ru.scoltech.measurement.config;

import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.testcontainers.containers.PostgreSQLContainer;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

@Configuration
public class TestAppConfiguration {
    @Bean
    public PostgreSQLContainer postgreSQLContainer() throws SQLException {
        final PostgreSQLContainer postgreSQLContainer = new PostgreSQLContainer();
        postgreSQLContainer.start();
        Connection conn = DriverManager.getConnection(
                postgreSQLContainer.getJdbcUrl(),
                postgreSQLContainer.getUsername(),
                postgreSQLContainer.getPassword()
        );
        conn.createStatement().execute("create extension \"uuid-ossp\"");
        return postgreSQLContainer;
    }

    @Bean
    public DataSource dataSource(final PostgreSQLContainer postgreSQLContainer) {
        return DataSourceBuilder.create()
                .password(postgreSQLContainer.getPassword())
                .username(postgreSQLContainer.getUsername())
                .url(postgreSQLContainer.getJdbcUrl())
                .driverClassName(postgreSQLContainer.getDriverClassName())
                .build();
    }
}
