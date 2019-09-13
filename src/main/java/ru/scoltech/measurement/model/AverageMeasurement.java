package ru.scoltech.measurement.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AverageMeasurement {
    private float value;
    private String buildingId;
    private String name;
}
