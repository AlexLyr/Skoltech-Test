package ru.scoltech.measurement.model;

import lombok.Data;
import org.springframework.data.annotation.Id;

@Data
public class Building {
    @Id
    private String id;
    private String name;
}
