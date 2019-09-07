package ru.scoltech.measurement.service.emulatereactive;

import org.springframework.transaction.support.TransactionTemplate;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Scheduler;
import reactor.core.scheduler.Schedulers;

import java.util.concurrent.Callable;

/**
 * Spring reactive jdbc driver is experimental feature
 * so if you dont want to use it, you can do smth like this
 */

public class AppService {

    private final Scheduler jdbcScheduler;
    private final TransactionTemplate transactionTemplate;

    public AppService(Scheduler jdbcScheduler, TransactionTemplate transactionTemplate) {
        this.jdbcScheduler = jdbcScheduler;
        this.transactionTemplate = transactionTemplate;
    }

    protected <T> Mono<T> asyncCallable(Callable<T> callable) {
        return Mono.fromCallable(callable)
                .subscribeOn(Schedulers.parallel())
                .publishOn(jdbcScheduler);
    }
}
