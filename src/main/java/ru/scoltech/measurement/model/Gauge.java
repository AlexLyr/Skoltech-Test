package ru.scoltech.measurement.model;

import lombok.Data;
import org.springframework.data.annotation.Id;

@Data
public class Gauge {
    @Id
    private String id;
    private GaugeType type;
}
