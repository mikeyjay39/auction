#!/usr/bin/bash

./shutdown-containers.sh
mvn -f backend/pom.xml clean install -DskipTests=true
docker-compose up --build