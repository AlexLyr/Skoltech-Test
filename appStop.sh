#!/usr/bin/env bashdoker
docker container stop measurements
docker container stop database
docker container prune -f