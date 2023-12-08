#! /usr/bin/env sh

mvn clean package -DskipTests

docker container prune && docker volume prune

docker volume prune

docker compose up --build