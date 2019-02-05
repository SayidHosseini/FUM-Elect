#!/bin/sh

clear

cd master/
echo "cleaning master"
mvn clean
echo "installing master"
mvn install

cd ../validator/
echo "cleaning validator"
mvn clean
echo "installing validator"
mvn install

cd ../electionportal/
echo "cleaning electionportal"
mvn clean
echo "installing electionportal"
mvn install

echo "docker-compose down"
sudo docker-compose down
echo "docker-compose build"
sudo docker-compose build
echo "docker-compose up"
sudo docker-compose up
