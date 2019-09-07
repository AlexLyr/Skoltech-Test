package ru.scoltech.measurement.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jdbc.repository.config.EnableJdbcRepositories;
import org.springframework.data.jdbc.repository.config.JdbcConfiguration;
import org.springframework.data.relational.core.mapping.event.BeforeSaveEvent;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.support.TransactionTemplate;
import reactor.core.scheduler.Scheduler;
import reactor.core.scheduler.Schedulers;
import ru.scoltech.measurement.model.BaseEntity;

import javax.sql.DataSource;
import java.util.UUID;
import java.util.concurrent.Executors;

@Configuration
@EnableJdbcRepositories(value = "ru.scoltech.measurement.dao")
public class DataConfig extends JdbcConfiguration {
    @Value("${spring.datasource.maximum-pool-size:100}")
    private int connectionPoolSize;

    private final DataSource dataSource;

    public DataConfig(DataSource dataSource) {this.dataSource = dataSource;}

    @Bean
    public Scheduler jdbcScheduler() {
        return Schedulers.fromExecutor(Executors.newFixedThreadPool(connectionPoolSize));
    }

    @Bean
    public TransactionTemplate transactionTemplate(PlatformTransactionManager transactionManager) {
        return new TransactionTemplate(transactionManager);
    }

    @Bean
    public PlatformTransactionManager transactionManager() {
        return new DataSourceTransactionManager(dataSource);
    }

    @Bean
    public NamedParameterJdbcOperations operations() {
        return new NamedParameterJdbcTemplate(dataSource);
    }

    @Bean
    public ApplicationListener<BeforeSaveEvent> idSetting() {
        return event -> {
            if (event.getEntity() instanceof BaseEntity) {
                BaseEntity entity = (BaseEntity) event.getEntity();
                if (entity.isNew()) {
                    entity.setId(UUID.randomUUID().toString());
                }
            }
        };
    }
}
