# Smart devices microservice project

The project consists of four microservices: home-service, user-service, device-service and api-gateway. 
There are houses and rooms which contain smart devices linked to the certain user.

Stack: Kotlin, Spring Boot, postgresql, jwt auth & scheduling tasks, Kafka message broker, mockito for unit-tests.

# User Service

User service is used for managing clients accounts: registration, authentication, sign out, etc.

# Home Service

Home service is used for managing houses and rooms they contain. Creation, modification and deletion are available.

# Device Service

Device service is using Tuya API for fetching devices data. One can modify the state of created device: for example: turn on/turn off the power, change the temperature.

# API Gateway Service

API Gateway service is using API gateway pattern for routing requests. So the working schema is as following:

<p align="center"><img src="https://github.com/mBayzigitov/smart-devices-microservice/assets/91501162/1e8b5d2f-bfd6-4f78-ba2c-e4218df2a35f" width="760"></p>

# Available requests

<p float="left" align="middle">
  <img src="https://github.com/mBayzigitov/smart-devices-microservice/assets/91501162/00c32aa1-99d2-49bf-b036-8fb0cc20ccbd" width="400">
  <img src="https://github.com/mBayzigitov/smart-devices-microservice/assets/91501162/a86f39db-7c09-4c92-926d-fadc68d2e001" width="385">
</p>

<p align="center"><img src="https://github.com/mBayzigitov/smart-devices-microservice/assets/91501162/6fe87a67-ace3-4d0e-9117-ff87fd9f2c40" width="400"></p>

## While working on this project I practiced:

- Kotlin w/ Spring Boot
- JWT
- API Gateway, Database per Service patterns
- JPA, JDBC
- Docker, Kafka
- Logback logging
- Unit-tests using springmockk
- Liquibase
