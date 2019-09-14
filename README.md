# Getting started

### Reference document
This is test application for skolkovo. Was written on Webflux with jdbc driver,
cause R2DBC is experimental feature, and i was exhausted try to get it work right.

Run application without IntellijIdea stater run ```./gradlew runApplication```
or just run in bash ```sh appRun.sh``` to stop app and purge all containers run ```sh appStop.sh```

Integration tests are using docker testcontainers and boot with ```./gradlew test``` command.

Or you can just run the app and try following cURL commands for testing:

* Save measurement: ```curl -X POST \
    http://localhost:8080/measurements \
    -H 'Content-Length: 256' \
    -H 'Content-Type: application/json' \
    -d '{
      "building": {
          "id": "7fb07925-e3f1-436f-8c6f-454a715740cc",
          "name": "Горный университет"
      },
      "gauge": {
          "id": "99b5e225-36a9-48a9-8d9c-ad4bc33a41ab",
          "type": "TEMPERATURE"
      },
      "value": 13.58,
      "dateTime": "2019-02-11T22:09:28.987Z"
  }'```
  
* Get measurements fro gauge by date range: ```curl -X GET \
    'http://localhost:8080/measurements/gauge?gauge=f694eb2a-7e90-43c6-98a0-d1bb9e22e0bd&from=2019-02-01T22:08:28.987Z&to=2019-02-09T22:08:28.987Z&size=100&number=0' \
    -H 'Postman-Token: 7c8c550e-0ec6-4371-9fd7-93669e0a021d' \
    -H 'cache-control: no-cache'```
    
* Get last measurements for each gauge for building: ```curl -X GET \
                                                          'http://localhost:8080/measurements/building?buildingId=4f319c31-280c-43fe-8a20-fcf43f975954' \
                                                          -H 'Postman-Token: a9aa2aa2-08f6-4185-a976-66da1a3aed7c' \
                                                          -H 'cache-control: no-cache'```

* Get average values from gauge for each building: ```curl -X GET \
                                                        http://localhost:8080/measurements/building/average \
                                                        -H 'Postman-Token: 3460ac4d-0112-4b4f-8c34-8b6ba1a95405' \
                                                        -H 'cache-control: no-cache'```



