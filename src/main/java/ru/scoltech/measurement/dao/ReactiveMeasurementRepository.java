package ru.scoltech.measurement.dao;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import ru.scoltech.measurement.model.Measurement;

@Repository
public interface ReactiveMeasurementRepository extends ReactiveCrudRepository<Measurement, String> {
}
