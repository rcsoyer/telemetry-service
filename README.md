# telemetry-service
Manages telemetry data from multiple entry points

### Process IoT devices data
Consume Kafka topics to process data in scalable way.
The message events in the topics are telemetry data published by IoT devices.

### The application provides REST APIs for users to: 
- register accounts;
- login and obtain a JWT to access other resources; 
- register IoT Devices;
- view summary data about the registered IoT devices;

The resources are secured with JWT, Bearer Token, authentication.

## Setup local development environment

The minimum requirements to run the application are:

- ``Java 21``
    - any ``JDK 21`` distro should work: Eclipse Temurin, OpenJDK, Oracle JDK...etc
- ``Docker engine``
    - the integration tests need a Docker engine to run `Postgres` and `Kafka` on containers;
    - `Docker Desktop` is an easy way to get Docker on your OS;
        - https://docs.docker.com/desktop/
- If you run the application through an `IDE`, such as `IntelliJ IDEA`, you may need to enable
  annotation processing to support `Lombok` and `MapStruct`.


## Building the application

The application can be built via gradle on your IDE.

Or you can build the application via the command line in terminal:

````./gradlew clean build````

### CI/CD pipeline
This project uses GitHub Actions for CI/CD pipeline: [gradle build](.github/workflows/gradle.yml).

The pipeline is triggered on every push to the `master` branch.

The pipeline runs the following steps:
- Git checkout the code
- Setup Java 21
- Build the application
    - Run the unit tests
    - Run the integration tests

## Running the application

The project provides an easy to use ``dev`` Spring profile:
[application-dev.yml](src/main/resources/application-dev.yml).

From that file, you can adjust the connection to local Postgres and Kafka instances.

Or you may simply run a Postgres and Kafka on a Docker container with the expected url configuration
and credentials described in the file.

### Docker compose
- Run the docker compose file, in the /dev folder of this project,
  to create the `Postgres` and `Kafka`  containers needed to run the application
  on Spring's dev profile.

The application then can be run in two ways:

- through the IDE
    - IDEs via auto-detection of the main class with the Spring profile: dev
- through the command line
    - ```./gradlew bootRun --args='--spring.profiles.active=dev'```

### Step by step usage
1. Import the postman collection located in the /dev folder of the project;
2. Create an Account with: http://localhost:8080/swagger-ui/index.html#/account-controller/register;
2. Login with that Account via browser or via REST API (check the options under the "Featues");
3. With a JWT at hand, now IoT Devices can be registered: http://localhost:8080/swagger-ui/index.html#/io-t-device-controller/register;
4. After registering a device Kafka events can be published with a registered deviceId(the app generated UUID);
5. Use the APIs of different IoT Devices to view the summary data using the source deviceId

#### Publish to Kafka topics
The Kafka Topics are created automatically when the application runs.
In order to publish events to the topics it's needed a Kafka client.

On IntelliJ IDEA, it's possible to use the Big Data Tools to connect to a Message broker.
In this case the local Kafka Broker from which get a Producer and publish events to the topics.
To configure the Kafka Broker set the following in the Big Data Tools:
- Enable connection: check the box
- Bootstrap servers: 127.0.0.1:9092
- Authentication: none


There are 3 topics, one for each supported IoT Device type:
1. fridgeEvents
In the Kafka client producer specify for this topic:
- key - type string: fridgeTelemetryEvent
- value - type json:
```json
{
  "deviceId" : "UUID" //the UUID of a registered IoTDevice
  "temperature" : double //a numeric double value with the temperature
}
```
- header: 
  - `__TypeId__` : fridgeTelemetryEvent

2. coffeeMachineEvents
   In the Kafka client producer specify for this topic:
- key - type string: coffeeMachineTelemetryEvent
- value - type json:
```json
{
  "deviceId" : "UUID" //the UUID of a registered IoTDevice
  "status" : "READY | ERROR | IN_PROGRESS | IDLE" //an enum definition with allowed event values
}
```
- header:
  - `__TypeId__` : coffeeMachineTelemetryEvent


3. thermostatEvents
      In the Kafka client producer specify for this topic:
- key - type string: thermostatTelemetryEvent
- value - type json:
```json
{
  "deviceId" : "UUID" //the UUID of a registered IoTDevice
  "temperature" : double //a numeric double value with the temperature
  "humidity" : double //a numeric double value with the humidity
}
```
- header:
  - `__TypeId__` : thermostatTelemetryEvent


## API Testing
A postman collection is available in the project dev directory:
[telemetry-service postman collection](dev/Telemetry-Service-API.postman_collection.json).

This collection can simply be imported into Postman and used to test the API features exposed by
the application.

## Features

### Security
Unless specified, all the endpoints require authentication to access.

If the client is not authenticated the application may redirect the client to the `login` page.

### OpenAPI documentation

The application provides an OpenAPI documentation for better integration with other services.

- The `Swagger UI` can be accessed via:
    - http://localhost:8080/swagger-ui.html
- The `OpenAPI` `yaml` can be downloaded via:
    - http://localhost:8080/v3/api-docs.yaml

### Manage Accounts

- **Register** - ````POST /accounts````
    - http://localhost:8080/swagger-ui/index.html#/account-controller/register
    - Create a new client's Account in the platform
    - Only legal age clients are allowed to register;
    - The username must be unique;
    - Upon successful registration, a default random secure password is generated and returned to
      the user;
    - No authentication is required to register;
- Login
    - ```GET /login```
        - via browser its possible to access the login UI page: http://localhost:8080/login
        - this is the ``formLogin`` feature automatically provided by Spring Security;
        - upon successful login, the user receives their `JWT` that allows them to access protected
          resources that they own;
        - *Reference*: https://docs.spring.io/spring-security/reference/servlet/authentication/passwords/form.html
    - ````POST /login````
        - http://localhost:8080/swagger-ui/index.html#/login-endpoint/post_login
        - Existing clients can login with their username and password;
        - A `JWT` is generated upon successfully authenticated client;
        - This endpoint is generated automatically by Spring Security and connects to a custom
          success handler that generates the `JWT`;
        - This is the same endpoint used by the `formLogin` feature;
- Account Overview listing - ````GET /accounts````
    - http://localhost:8080/swagger-ui/index.html#/account-controller/getAccountOverview
    - List all the client's Bank Accounts;
    - This is a secured endpoint that requires a valid `JWT` for access;
    - If a valid `JWT` is provided, the client's banking details associated with the token are
      fetched and returned;

### Actuator

The application provides the Spring Boot Actuator endpoints for monitoring and management.

- info ```GET /info```
    - http://localhost:8080/swagger-ui/index.html#/Actuator/info
    - Provides general information about the running application;
    - http://localhost:8080/info
    - Provides general information about the application;
    - No authentication is required;
- health ```GET /health```
    - http://localhost:8080/swagger-ui/index.html#/Actuator/health
    - Provides metrics about the health of the application;
    - No authentication is required;
- liveness ```GET /health/liveness```
    - http://localhost:8080/health/liveness
    - No authentication is required;
- readiness ```GET /health/readiness```
    - http://localhost:8080/health/readiness
    - Provides metrics about the readiness of the application;
    - No authentication is required;