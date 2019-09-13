package ru.scoltech.measurement.containers;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.junit.Assert;
import org.junit.ClassRule;
import org.junit.Test;
import org.testcontainers.containers.GenericContainer;

import java.io.IOException;

public class ClassRuleGenericContainerTest {

    @ClassRule
    public static GenericContainer webServer = new GenericContainer("alpine:3.5")
            .withExposedPorts(80)
            .withCommand("/bin/sh",
                    "-c",
                    "while true; do echo \"HTTP/1.1 200 OK\n\n" +
                            "TestContainers up\" | nc -l -p 80; done");

    @Test
    public void shouldReturnCorrectMessage() throws IOException {
        CloseableHttpClient httpClient = HttpClients.createDefault();
        HttpGet httpGet = new HttpGet("http://" + webServer.getContainerIpAddress() + ":" +webServer.getMappedPort(80));
        HttpResponse response = httpClient.execute(httpGet);
        String greeting = EntityUtils.toString(response.getEntity());
        Assert.assertEquals(greeting, "TestContainers up\n");
    }
}
