package ru.scoltech.measurement.routers;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;
import ru.scoltech.measurement.handlers.MeasurementHandler;

import static org.springframework.web.reactive.function.server.RequestPredicates.accept;

@Configuration
public class MeasurementRouter {
    @Bean
    public RouterFunction<ServerResponse> route(MeasurementHandler measurementHandler) {
        return RouterFunctions
                .route()
                .path("/measurements", builder -> builder
                        .POST("", accept(MediaType.APPLICATION_JSON_UTF8), measurementHandler::saveMeasurement)
                        .GET("/gauge", measurementHandler::getMeasurementsByGauge)
                        .GET("/building", measurementHandler::getMeasurementsByBuilding)
                        .GET("/building/average", measurementHandler::getAverageValueForEachBuilding)
                )
                .build();
    }
}
