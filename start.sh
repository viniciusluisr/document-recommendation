#!/bin/bash -l

echo -------------------------------------------------
echo Installing OpenJDK-8 and Maven
echo -------------------------------------------------
add-apt-repository ppa:openjdk-r/ppa -y
apt-get update
apt-get install openjdk-8-jdk
apt-get install maven -y

echo
echo -------------------------------------------------
echo Stopping and removing existing docker containers
echo -------------------------------------------------

docker stop $(docker ps -a -q)
docker rm $(docker ps -a -q)

echo
echo -------------------------------------------------
echo Starting services
echo -------------------------------------------------

docker-compose up -d
mvn clean install -DskipTests

echo
echo -------------------------------------------------
echo Scaling Elasticsearch
echo -------------------------------------------------
docker-compose scale master=3

echo
echo -------------------------------------------------
echo Starting application
echo -------------------------------------------------

cd target/
java -jar document-recommendation-1.0-SNAPSHOT.jar
