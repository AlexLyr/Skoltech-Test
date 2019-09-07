package ru.scoltech.measurement.model;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.HashSet;
import java.util.Set;

@EqualsAndHashCode(callSuper = true)
@Data
public class Gauge extends BaseEntity {
    private GaugeType type;
    private Set<Measurement> measurements = new HashSet<>();
}
