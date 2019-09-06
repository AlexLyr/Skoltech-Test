package ru.scoltech.measurement.dao;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ru.scoltech.measurement.model.Measurement;

@Repository
public interface MeasurementRepository extends CrudRepository<Measurement, String> {
}
