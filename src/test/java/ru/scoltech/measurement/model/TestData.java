package ru.scoltech.measurement.model;

public class TestData {

    public static Building TEST_BUILDING;

    static {
        TEST_BUILDING = new Building();
        TEST_BUILDING.setId("4f319c31-280c-43fe-8a20-fcf43f975954");
        TEST_BUILDING.setName("БЦ Плаза");
    }

    public static final String TEST_MEASUREMENT_JSON = "{\n" +
            "\t\"building\": {\"id\":\"7fb07925-e3f1-436f-8c6f-454a715740cc\", \n" +
            "\t\t\"name\": \"Горный университет\"\n" +
            "\t},\n" +
            "\t\"gauge\": {\"id\": \"99b5e225-36a9-48a9-8d9c-ad4bc33a41ab\",\n" +
            "\t\t\"type\": \"TEMPERATURE\"\n" +
            "\t},\n" +
            "\t\"value\": 13.58,\n" +
            "\t\"dateTime\":\"2019-02-11T22:09:28.987Z\"\n" +
            "}";

    public static final String NON_VALID_MEASUREMENT_JSON = "{\n" +
            "\t\"building\": {\"id\":\"7fb07925-e3f1-436f-8c6f-454a715740cc\", \n" +
            "\t\t\"name\": \"Горный университет\"\n" +
            "\t},\n" +
            "\t\"gauge\": {\"id\": \"99b5e225-36a9-48a9-8d9c-ad4bc33a41ab\",\n" +
            "\t\t\"type\": \"TEMPERATURE\"\n" +
            "\t},\n" +
            "\t\"value\": 13.58,\n" +
            "}";
}
