package ru.scoltech.measurement.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor

public class Measurement {
    @Id
    private String id;
    @NotNull
    private Building building;
    @NotNull
    private Gauge gauge;
    @NotNull
    private float value;
    @NotNull
    private LocalDateTime dateTime;
}
