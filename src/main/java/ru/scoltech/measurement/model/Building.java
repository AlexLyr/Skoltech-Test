package ru.scoltech.measurement.model;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.HashSet;
import java.util.Set;

@EqualsAndHashCode(callSuper = true)
@Data
public class Building extends BaseEntity {
    private String name;
    private Set<Gauge> gauges = new HashSet<>();
}
