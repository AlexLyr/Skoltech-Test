package ru.scoltech.measurement.config;

import io.r2dbc.postgresql.PostgresqlConnectionConfiguration;
import io.r2dbc.postgresql.PostgresqlConnectionFactory;
import io.r2dbc.spi.ConnectionFactory;
import liquibase.Contexts;
import liquibase.LabelExpression;
import liquibase.Liquibase;
import liquibase.database.Database;
import liquibase.database.DatabaseFactory;
import liquibase.database.jvm.JdbcConnection;
import liquibase.exception.LiquibaseException;
import liquibase.resource.FileSystemResourceAccessor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.r2dbc.config.AbstractR2dbcConfiguration;
import org.springframework.data.r2dbc.repository.config.EnableR2dbcRepositories;

import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Objects;

@Configuration
@EnableR2dbcRepositories(value = "ru.scoltech.measurement.dao")
@Slf4j
public class ReactiveDataConfig extends AbstractR2dbcConfiguration {

    @Value("${spring.r2dbc.host}")
    private String host;
    @Value("${spring.r2dbc.port}")
    private int port;
    @Value("${spring.r2dbc.username}")
    private String user;
    @Value("${spring.r2dbc.database}")
    private String database;
    @Value("${spring.r2dbc.password}")
    private String password;
    @Value("${spring.datasource.url}")
    private String url;
    @Value("${path.liquibase.change-log}")
    private String changelogResource;

    @Override
    public ConnectionFactory connectionFactory() {
        runLiquiBase();
        return new PostgresqlConnectionFactory(
                PostgresqlConnectionConfiguration.builder()
                        .host(host)
                        .port(port)
                        .username(user)
                        .password(password)
                        .database(database)
                        .build());
    }

    public void runLiquiBase() {
        java.sql.Connection connection = null;
        try {
            connection = DriverManager.getConnection(url, user, password);
            String changelog = Objects.requireNonNull(this.getClass().getClassLoader().getResource(changelogResource)).getPath();
            Database database = DatabaseFactory.getInstance().findCorrectDatabaseImplementation(new JdbcConnection(connection));
            Liquibase liquibase = new Liquibase(changelog, new FileSystemResourceAccessor(), database);
            liquibase.update(new Contexts(), new LabelExpression());
        } catch (SQLException | LiquibaseException e) {
            log.error(e.getMessage());
        } finally {
            if (connection != null) {
                try {
                    connection.rollback();
                    connection.close();
                } catch (SQLException ignored) {
                }
            }
        }
    }
}
