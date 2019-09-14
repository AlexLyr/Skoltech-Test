package ru.scoltech.measurement.handlers;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.scoltech.measurement.model.AverageMeasurement;
import ru.scoltech.measurement.model.Measurement;
import ru.scoltech.measurement.model.MeasurementDto;
import ru.scoltech.measurement.routers.MeasurementValidator;
import ru.scoltech.measurement.service.emulatereactive.MeasurementService;

import java.util.Map;
import java.util.Optional;

import static ru.scoltech.measurement.util.RequestParams.*;

@Component
@Slf4j
@RequiredArgsConstructor
public class MeasurementHandler {

    private final MeasurementService service;
    private final MeasurementValidator validator;

    public Mono<ServerResponse> saveMeasurement(ServerRequest request) {
        Mono<MeasurementDto> measurement = request.bodyToMono(MeasurementDto.class)
                .doOnNext(validator::validateBody);
        return service.saveMeasure(measurement)
                .flatMap(it -> ServerResponse.ok()
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
                        .body(BodyInserters.fromObject(it)))
                .switchIfEmpty(ServerResponse.status(HttpStatus.INTERNAL_SERVER_ERROR).build());
    }

    public Mono<ServerResponse> getMeasurementsByGauge(ServerRequest request) {
        Flux<Measurement> measurement = service.findAllByGauge(removeNotAllowedParams(request));
        return ServerResponse.ok()
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .body(measurement, Measurement.class)
                .switchIfEmpty(ServerResponse.noContent().build());
    }

    public Mono<ServerResponse> getMeasurementsByBuilding(ServerRequest request) {
        Optional<String> buildingId = request.queryParam("buildingId");
        return ServerResponse.ok()
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .body(buildingId
                        .map(service::findLastByBuilding)
                        .orElse(Flux.empty()), Measurement.class)
                .switchIfEmpty(ServerResponse.noContent().build());
    }

    public Mono<ServerResponse> getAverageValueForEachBuilding(ServerRequest request) {
        Flux<AverageMeasurement> measurement = service.findAverageFroAllBuilding();
        return ServerResponse.ok()
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .body(measurement, AverageMeasurement.class)
                .switchIfEmpty(ServerResponse.noContent().build());
    }

    private Map<String, String> removeNotAllowedParams(ServerRequest request) {
        request.queryParams()
                .toSingleValueMap()
                .keySet()
                .removeIf((isFrom
                        .or(isTo)
                        .or(isGaugeId)
                        .or(isNumber)
                        .or(isSize)
                        .negate()));
        return request.queryParams().toSingleValueMap();
    }
}
