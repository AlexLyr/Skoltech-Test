package ru.scoltech.measurement.dao;

import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.scoltech.measurement.model.AverageMeasurement;
import ru.scoltech.measurement.model.Measurement;

import java.time.LocalDateTime;
import java.util.stream.Stream;

@Repository
@Transactional
public interface MeasurementRepository extends CrudRepository<Measurement, String> {

    @Transactional(readOnly = true)
    @Query("SELECT * FROM measurement WHERE gauge_id = :gaugeId and (date_time >= :from and date_time <= :to) ORDER BY date_time DESC NULLS LAST LIMIT :size OFFSET :number")
    Stream<Measurement> findAllByGaugeIdAndDateTimeBetween(@Param("gaugeId") String gaugeId,
                                                           @Param("from") LocalDateTime from,
                                                           @Param("to") LocalDateTime to,
                                                           @Param("size") int size,
                                                           @Param("number") int number);

    @Transactional(readOnly = true)
    @Query("SELECT id, gauge_id, building_id, \"value\", date_time\n" +
            "FROM (\n" +
            "       SELECT *,\n" +
            "              rank() OVER (\n" +
            "                PARTITION BY gauge_id\n" +
            "                ORDER BY date_time\n" +
            "                  DESC\n" +
            "                )\n" +
            "       FROM measurement) s\n" +
            "WHERE s.rank = 1 AND building_id = :buildingId")
    Stream<Measurement> findLastByBuildingId(@Param("buildingId") String buildingId);

    @Transactional(readOnly = true)
    @Query("select name, building_id, \"value\"\n" +
            "from (\n" +
            "       SELECT building_id,\n" +
            "              rank() over (partition by building_id order by date_time DESC) as rank,\n" +
            "              AVG(\"value\") OVER (PARTITION BY building_id)              AS value\n" +
            "       from measurement) s JOIN building ON building.id = s.building_id\n" +
            "where s.rank = 1;")
    Stream<AverageMeasurement> findAverageByBuilding();
}