# Chat-Room Demo Project

## Overview

The purpose of this project is to practice TDD and make a simple example of a chat room using Websocket and RabbiMQ.

### How run?

To run the project it will be necessary that the docker-compose is installed on your machine. If you do not have the docker-compose, you must follow the step-by-step instructions by the [Docker](https://docs.docker.com/engine/) team to perform the installation.

First, you must up the database container. 

```sh
sudo docker-compose up -d
```

then run the command

```sh
./mvnw spring-boot:run
```

For the tests run the command:

```sh
./mvnw test
```

 ## Documentation
 
 I used the swagger to document the api's routes, http methods, parameters and responses.

 <strong>URL: </strong> <i>http://localhost:8080/swagger-ui.html</i>
