package ru.scoltech.measurement;

import liquibase.Contexts;
import liquibase.Liquibase;
import liquibase.exception.LiquibaseException;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.liquibase.LiquibaseAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.function.BodyInserters;
import org.testcontainers.containers.PostgreSQLContainer;
import ru.scoltech.measurement.config.LiquiBaseTestConfig;
import ru.scoltech.measurement.config.TestAppConfiguration;
import ru.scoltech.measurement.model.*;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;


@RunWith(SpringRunner.class)
@TestPropertySource(locations = "classpath:application-test.properties")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, properties = "spring.main.web-application-type=reactive")
@AutoConfigureWebTestClient
@EnableAutoConfiguration(exclude = {LiquibaseAutoConfiguration.class, DataSourceAutoConfiguration.class})
@Import({TestAppConfiguration.class, LiquiBaseTestConfig.class})
public class TestApplication {

    @Autowired
    public PostgreSQLContainer postgreSQLContainer;

    @Autowired
    private Liquibase liquibase;

    @Autowired
    private WebTestClient webClient;

    @Before
    public void before() throws LiquibaseException {
        liquibase.dropAll();
        liquibase.update(new Contexts());
    }

    @Test
    public void shouldReturnFirstBuildingFromLiquibaseScript() throws SQLException {
        Connection conn = DriverManager.getConnection(
                postgreSQLContainer.getJdbcUrl(),
                postgreSQLContainer.getUsername(),
                postgreSQLContainer.getPassword()
        );
        ResultSet resultSet = conn.createStatement().executeQuery("SELECT * from building");
        Building building = new Building();
        resultSet.next();
        String name = resultSet.getString("name");
        String id = resultSet.getString("id");
        building.setName(name);
        building.setId(id);
        Assert.assertEquals(TestData.TEST_BUILDING, building);
    }

    @Test
    public void shouldReturnIdForSavedMeasurement() {
        MeasurementDto resp = webClient.post()
                .uri("/measurements")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .body(BodyInserters.fromObject(TestData.TEST_MEASUREMENT_JSON))
                .exchange()
                .expectStatus().is2xxSuccessful()
                .expectBody(MeasurementDto.class)
                .returnResult()
                .getResponseBody();
        Assert.assertNotNull("Measurement wasn`t saved", resp.getId());
    }

    @Test
    public void shouldReturnBadRequestException() {
        MeasurementDto resp = webClient.post()
                .uri("/measurements")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .body(BodyInserters.fromObject(TestData.NON_VALID_MEASUREMENT_JSON))
                .exchange()
                .expectStatus().isBadRequest()
                .expectBody(MeasurementDto.class)
                .returnResult()
                .getResponseBody();
    }

    @Test
    public void shouldReturnTwoMeasurementsByGauge() {
        webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/measurements/gauge")
                        .queryParam("gauge", "f694eb2a-7e90-43c6-98a0-d1bb9e22e0bd")
                        .queryParam("to", "2016-08-25T01:23:46.931Z")
                        .queryParam("from", "2016-08-13T01:23:46.931Z")
                        .build())
                .exchange()
                .expectStatus().is2xxSuccessful()
                .expectBodyList(Measurement.class)
                .hasSize(2);
    }

    @Test
    public void shouldReturnLastMeasurementForEachGaugeForBuilding() {
        webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/measurements/building")
                        .queryParam("buildingId", "4f319c31-280c-43fe-8a20-fcf43f975954")
                        .build())
                .exchange()
                .expectStatus().is2xxSuccessful()
                .expectBodyList(Measurement.class)
                .hasSize(2);
    }

    @Test
    public void shouldReturnAverageMeasurementsForEachBuilding() {
        List<AverageMeasurement> resp = webClient.get()
                .uri("/measurements/building/average")
                .exchange()
                .expectStatus().is2xxSuccessful()
                .expectBodyList(AverageMeasurement.class)
                .hasSize(2)
                .returnResult().getResponseBody();
        Assert.assertEquals(16.1, resp.get(0).getValue(), 0.1);
        Assert.assertEquals(82.7, resp.get(1).getValue(), 0.1);
    }
}
