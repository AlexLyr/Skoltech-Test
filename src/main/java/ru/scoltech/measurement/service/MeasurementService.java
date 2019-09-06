package ru.scoltech.measurement.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.support.TransactionTemplate;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Scheduler;
import ru.scoltech.measurement.dao.MeasurementRepository;
import ru.scoltech.measurement.model.Measurement;

import java.util.UUID;

@Service
@Slf4j
public class MeasurementService extends AppService {
    private final MeasurementRepository repository;

    public MeasurementService(Scheduler jdbcScheduler,
                              TransactionTemplate transactionTemplate,
                              MeasurementRepository repository) {
        super(jdbcScheduler, transactionTemplate);
        this.repository = repository;
    }

    public Mono<Measurement> saveMeasure(Mono<Measurement> measurement) {
        return measurement
                .map(it -> {
                    it.setId(UUID.randomUUID().toString());
                    return it;
                })
                .doOnNext(it -> asyncCallable(() -> repository.save(it)))
                .doOnError(msg -> log.error(msg.getMessage()))
                .defaultIfEmpty(null);
    }
}
