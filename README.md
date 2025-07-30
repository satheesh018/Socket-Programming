# Socket-Programming

The folder TCP project is having the source code and the  folder Jar and docker files have the jar files of the project and the docker files to run those jars.

## How to run the project

Download the file Jar and docker files

open a terminal inside that file and run the following commands one by one(make sure the ports 8080 and 9090 of your system is not occupied):

1.docker build -f Dockerfile-server -t proxy-server .

2.docker build -f Dockerfile-client -t proxy-client .

3.docker run -d --name proxy-server -p 9090:9090 proxy-server

4.docker run -d --name proxy-client -p 8080:8080 --link proxy-server:proxy-server proxy-client

To test the setup, use the following curl command:

curl -x http://localhost:8080 http://httpforever.com/
