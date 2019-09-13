package ru.scoltech.measurement.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.scoltech.measurement.config.json.LocalDateTimeDeserializer;
import ru.scoltech.measurement.config.json.LocalDateTimeSerializer;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class MeasurementDto {
    private String id;
    @NotNull(message = "building id must be not null")
    private Building building;
    @NotNull(message = "gauge id must be not null")
    private Gauge gauge;
    @NotNull(message = "value must be not null")
    private float value;
    @NotNull(message = "Date time must be not null")
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    private LocalDateTime dateTime;
}
