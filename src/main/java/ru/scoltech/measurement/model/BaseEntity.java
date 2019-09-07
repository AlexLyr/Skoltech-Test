package ru.scoltech.measurement.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.domain.Persistable;

import java.io.Serializable;

@Data
public class BaseEntity implements Persistable, Serializable {

    @Id
    private String id;

    @Override
    public Object getId() {
        return id;
    }

    @Override
    @JsonIgnore
    public boolean isNew() {
        return id == null;
    }
}
