#!/usr/bin/env bash
./gradlew clean
./gradlew dockerCreateDockerfile
./gradlew test
docker container prune -f
docker image prune -f
docker-compose up --build -d
docker container logs measurements -f
