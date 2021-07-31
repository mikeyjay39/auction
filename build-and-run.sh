#!/usr/bin/bash

./shutdown-containers.sh
mvn clean install -DskipTests=true
docker-compose up