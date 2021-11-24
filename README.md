# users-control-system

Project development using Java | Spring Boot | Spring Cloud Config Server | Angular 13 | RabbitMQ | Kafka | PostgreSQL | Redis

### Prerequisites

The local ambient have must installed:
 - Java 11 or more version.
 - Maven 3.8 or more version.
 - Node 15 or more version.
 - Angular CLI 13.

The applications will persist data in DB's PostgreSQL and Redis. You can utility the docker container to facilitate. The same is valid for the Kafka and RabbitMQ.
Below is the docker images list utilitized.
 - postgres
 - redis
 - rabbitmq:3.8.3-management
 - confluentinc/cp-kafka:latest
 - confluentinc/cp-zookeeper:latest

The applications load the information of configuration from a Configuration Server, you can modify to the server of your preferences or utilit the application users-control-config-server as your Configuration Server ([more details](/users-control-config-server)). Case wish use a server of your preferences, modify application.properties file exists in users-control-api and users-control-message with correct informations. This step must is the first.


## users-control-api

After clone the projeto, inside diretory users-control-api execute the comands:

`$ mvn clean package `

Next

`$ java -jar target/users-control-api-0.0.1-SNAPSHOT.jar `


## users-control-message

After clone the projeto, inside diretory users-control-message execute the comands:

`$ mvn clean package`

Next

`$ java -jar target/users-control-message-0.0.1-SNAPSHOT.jar `


## users-control-web

After clone the projeto, inside diretory users-control-web execute the comands:

`$ ng server `


## users-control-config-server

After clone the projeto, inside diretory users-control-config-server execute the comands:

`$ mvn clean package `

Next

`$ java -jar target/users-control-config-server-0.0.1-SNAPSHOT.jar `


