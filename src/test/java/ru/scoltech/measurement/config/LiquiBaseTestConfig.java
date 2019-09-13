package ru.scoltech.measurement.config;

import liquibase.Liquibase;
import liquibase.database.Database;
import liquibase.database.DatabaseFactory;
import liquibase.database.jvm.JdbcConnection;
import liquibase.exception.LiquibaseException;
import liquibase.resource.FileSystemResourceAccessor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.SQLException;

@Configuration
public class LiquiBaseTestConfig {
    @Bean
    public Liquibase liquibase(final DataSource dataSource) throws LiquibaseException, SQLException {
        final Database database = DatabaseFactory.getInstance().findCorrectDatabaseImplementation(new JdbcConnection(dataSource.getConnection()));
        Path path = Paths.get(this.getClass().getResource("/db/changelog-master.xml").getPath());
        return new liquibase.Liquibase(path
                .normalize()
                .toAbsolutePath()
                .toString(), new FileSystemResourceAccessor(), database);
    }
}
