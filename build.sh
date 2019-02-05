#!/bin/sh

cd master/
mvn clean 
mvn install

cd ../validator/
mvn clean
mvn install

sudo docker-compose down
sudo docker-compose up
