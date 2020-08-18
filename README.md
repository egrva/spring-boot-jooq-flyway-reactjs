# Spring Boot REST-API with React.js UI

This example app shows how to create a [Kotlin](https://kotlinlang.org/) [Spring Boot](https://spring.io/projects/spring-boot) REST-API, send data to the server and display it with a [React](https://reactjs.org/) UI.
Also, it contains integration tests based on [Testcontainers](https://www.testcontainers.org/).

* [Getting Started](#getting-started)
* [Development environment](#development-environment)
* [Running the tests](#running-the-tests)
* [Used technologies](#used-technologies)

## Getting Started

Node-gradle plugin is used to build a React UI:

```groovy
plugins {
    id 'com.github.node-gradle.node' version "2.2.4"
}
```

So, if you add new dependency to `package.json`, run next command after this

```bash
./gradlew npm_run_build
```

To rebuild React UI, use the following commands:

```bash
./gradlew buildReactApp
```

Also, this application uses jOOQ to generate safety sql queries.
If you want to rebuild jOOQ tables, run next command:

```bash
./gradlew generateCsmartJooqSchemaSource
```

## Development environment

To launch development environment for this application you should install [Docker](https://docs.docker.com/get-docker/) and [Docker Compose](https://docs.docker.com/compose/install/).


To build docker image of this example application, use the following command:

```bash
./gradlew clean dockerBuildImage
```
To run the docker containers, change directory to the `dev-env` and run:

```bash
docker-compose up
```

## Running the tests

For integration testing are used next dependencies:

* [spring-boot-starter-test](https://docs.spring.io/spring/docs/4.3.7.RELEASE/spring-framework-reference/htmlsingle/#testing)
* [JUnit](https://junit.org/junit4/)
* [PostgreSQL](https://www.postgresql.org/) and [Selenium](https://www.selenium.dev/) starting with [Testcontainers](https://www.testcontainers.org/)

To run tests, use the following commands:

```bash
./gradlew integrationTest
```

## Used technologies

##### For back-end:
* [Gradle](https://gradle.org/)
* [Kotlin](https://kotlinlang.org/)
* [Spring Boot](https://spring.io/projects/spring-boot) 

##### For front-end:
* [React.js](https://reactjs.org/)
* [npm](https://www.npmjs.com/)

##### For database connectivity:
* [PostgreSQL JDBC](https://github.com/pgjdbc/pgjdbc)
* [Flyway](https://flywaydb.org/) 
* [jOOQ](https://www.jooq.org/)
