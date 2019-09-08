package ru.scoltech.measurement.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import ru.scoltech.measurement.dao.ReactiveMeasurementRepository;
import ru.scoltech.measurement.model.Measurement;

@Service
@Slf4j
public class ReactiveMeasurementService {

    private final ReactiveMeasurementRepository repository;

    public ReactiveMeasurementService(ReactiveMeasurementRepository repository) {this.repository = repository;}

    public Mono<Measurement> saveMeasure(Mono<Measurement> measurement) {
        return measurement
                .doOnNext(repository::save)
                .doOnError(msg -> log.error(msg.getMessage()));
    }
}
