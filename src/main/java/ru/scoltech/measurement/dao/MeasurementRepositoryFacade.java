package ru.scoltech.measurement.dao;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import ru.scoltech.measurement.model.*;
import ru.scoltech.measurement.util.RequestParams;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Map;
import java.util.Optional;

@Repository
@Slf4j
@RequiredArgsConstructor
public class MeasurementRepositoryFacade {
    private static final int DEFAULT_SIZE = 100;
    private static final int DEFAULT_NUMBER = 0;

    private final BuildingRepository buildingRepository;
    private final GaugeRepository gaugeRepository;
    private final MeasurementRepository measurementRepository;

    @Transactional(isolation = Isolation.READ_UNCOMMITTED)
    public MeasurementDto save(MeasurementDto dto) {
        return Optional.of(dto)
                .map(this::updateGaugesAndBuilding)
                .map(this::mapDtoToEntity)
                .map(measurementRepository::save)
                .map(it -> {
                    dto.setId(it.getEntityId());
                    return dto;
                })
                .get();
    }

    private MeasurementDto updateGaugesAndBuilding(MeasurementDto dto) {
        Building savedBuilding = buildingRepository.save(dto.getBuilding());
        Gauge savedGauge = gaugeRepository.save(dto.getGauge());
        dto.setGauge(savedGauge);
        dto.setBuilding(savedBuilding);
        return dto;
    }

    private Measurement mapDtoToEntity(MeasurementDto dto) {
        Measurement measurement = new Measurement();
        measurement.setValue(dto.getValue());
        measurement.setDateTime(dto.getDateTime());
        measurement.setGaugeId(dto.getGauge().getEntityId());
        measurement.setBuildingId(dto.getBuilding().getEntityId());
        return measurement;
    }

    public Flux<Measurement> findAllByGauge(Map<String, String> params) {
        int size = Optional.ofNullable(params.get(RequestParams.SIZE))
                .map(Integer::parseInt)
                .orElse(DEFAULT_SIZE);
        int number = Optional.ofNullable(params.get(RequestParams.NUMBER))
                .map(Integer::parseInt)
                .orElse(DEFAULT_NUMBER);
        int offset = size * number;
        return Flux.fromStream(() -> measurementRepository
                .findAllByGaugeIdAndDateTimeBetween(params.get(RequestParams.GAUGE_ID),
                        toLocalDateTime(params.get(RequestParams.FROM)),
                        toLocalDateTime(params.get(RequestParams.TO)),
                        size,
                        offset));
    }

    private LocalDateTime toLocalDateTime(String s) {
        return LocalDateTime.ofInstant(Instant.parse(s), ZoneId.of("UTC"));
    }

    public Flux<Measurement> findLastByBuilding(String buildingId) {
        return Flux.fromStream(() -> measurementRepository.findLastByBuildingId(buildingId));
    }

    public Flux<AverageMeasurement> findAverageForAllBuilding() {
        return Flux.fromStream(() -> measurementRepository.findAverageByBuilding());
    }
}
