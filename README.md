# Quarkus Social ⭐

Social network application with Quarkus

## Environment Variables

DATABASE_URL=jdbc:postgresql://localhost:5432/quarkus-social
DATABASE_USER=postgres
DATABASE_PASS=postgres
DATABASE_GEN=update

## Dev Mode

.../quarkus-social> `./mvnw quarkus:dev`

## Test Mode

.../quarkus-social> `./mvnw quarkus:test`

## How to build

> You need to replace the environment variables with their values in `application.properties` file

.../quarkus-social> `./mvnw clean package -DskipTests`

## How to execute the builded project

.../quarkus-social/target/quarkus-app> `java -jar quarkus-run.jar`

## How to use with Docker

> You need to configure your environment to use PostgreSQL. Or just for testing, use H2 in-memory database in test and production mode

1. Build the application;
2. .../quarkus-social> `docker build -f src/main/docker/Dockerfile.jvm -t quarkus-social:1.0 .` (don`t forget the dot);
3. .../> `docker run -i --rm -p 8080:8080 quarkus-social:1.0`
