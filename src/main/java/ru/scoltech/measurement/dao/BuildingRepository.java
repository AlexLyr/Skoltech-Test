package ru.scoltech.measurement.dao;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ru.scoltech.measurement.model.Building;

@Repository
public interface BuildingRepository extends CrudRepository<Building, String> {
}
