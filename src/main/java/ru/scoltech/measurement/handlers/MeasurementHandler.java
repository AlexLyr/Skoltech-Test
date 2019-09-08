package ru.scoltech.measurement.handlers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;
import ru.scoltech.measurement.model.Measurement;
import ru.scoltech.measurement.service.ReactiveMeasurementService;

@Component
@Slf4j
public class MeasurementHandler {

    @Autowired
    private ReactiveMeasurementService service;

    public Mono<ServerResponse> saveMeasurement(ServerRequest request) {
        Mono<Measurement> measurement = request.bodyToMono(Measurement.class)
                .onErrorMap(it -> {
                    log.error(it.getMessage());
                    return it;
                });
        return service.saveMeasure(measurement)
                .flatMap(it -> ServerResponse.ok()
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
                        .body(BodyInserters.fromObject(it)))
                .switchIfEmpty(ServerResponse.status(HttpStatus.INTERNAL_SERVER_ERROR).build());
    }
}
