package ru.scoltech.measurement.service.emulatereactive;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Scheduler;
import reactor.core.scheduler.Schedulers;
import ru.scoltech.measurement.dao.MeasurementRepositoryFacade;
import ru.scoltech.measurement.model.AverageMeasurement;
import ru.scoltech.measurement.model.Measurement;
import ru.scoltech.measurement.model.MeasurementDto;

import java.util.Map;

@Service
@Slf4j
@RequiredArgsConstructor
public class MeasurementService {
    private final Scheduler jdbcScheduler;
    private final MeasurementRepositoryFacade measurementFacade;

    public Mono<MeasurementDto> saveMeasure(Mono<MeasurementDto> measurementDto) {
        return measurementDto
                .subscribeOn(Schedulers.parallel())
                .publishOn(jdbcScheduler)
                .doOnNext(measurementFacade::save);
    }

    public Flux<Measurement> findAllByGauge(Map<String, String> params) {
        return measurementFacade.findAllByGauge(params)
                .subscribeOn(Schedulers.parallel())
                .publishOn(jdbcScheduler);
    }

    public Flux<Measurement> findLastByBuilding(String buildingId) {
        return measurementFacade.findLastByBuilding(buildingId)
                .subscribeOn(Schedulers.parallel())
                .publishOn(jdbcScheduler);
    }

    public Flux<AverageMeasurement> findAverageFroAllBuilding() {
        return measurementFacade.findAverageForAllBuilding()
                .subscribeOn(Schedulers.parallel())
                .publishOn(jdbcScheduler);
    }
}
