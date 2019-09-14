package ru.scoltech.measurement.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AverageMeasurement {
    private final float value;
    private final String buildingId;
    private final String name;
}
