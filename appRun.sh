#!/usr/bin/env bash
./gradlew clean
./gradlew dockerCreateDockerfile
docker container prune -f
docker-compose up --build
docker image prune -f