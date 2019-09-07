package ru.scoltech.measurement.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Measurement extends BaseEntity{
    @NotNull
    private Building building;
    @NotNull
    private Gauge gauge;
    @NotNull
    private float value;
    @NotNull
    private LocalDateTime dateTime;
}
