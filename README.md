# Quarkus Social ⭐
 Social network application with Quarkus

## Environment Variables
DATABASE_URL=jdbc:postgresql://localhost:5432/quarkus-social
DATABASE_USER=postgres
DATABASE_PASS=postgres
DATABASE_GEN=update

## Dev Mode
 .../quarkus-social> ```./mvnw quarkus:dev```

## Test Mode
 .../quarkus-social> ```./mvnw quarkus:test```

## How to build
 .../quarkus-social> ```./mvnw clean package -DskipTests```

 ## How to execute the builded project
 .../quarkus-social/target/quarkus-app> ```java -jar quarkus-run.jar```
