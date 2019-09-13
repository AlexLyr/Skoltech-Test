package ru.scoltech.measurement.dao;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ru.scoltech.measurement.model.Gauge;

@Repository
public interface GaugeRepository extends CrudRepository<Gauge, String> {
}
