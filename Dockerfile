FROM maven:4.0.0-openjdk-17-slim AS builder
COPY pom.xml /app/
COPY src /app/src
RUN --mount=type=cache,target=/root/.m2 mvn -f /app/pom.xml clean package -DskipTests

FROM openjdk:17-jdk-alpine
COPY target/VehicleManagement.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]