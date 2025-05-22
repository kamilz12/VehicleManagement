FROM maven:3.9.9-eclipse-temurin-17-alpine AS builder
WORKDIR /app
COPY pom.xml .
COPY src ./src
RUN --mount=type=cache,target=/root/.m2 mvn clean package -DskipTests

FROM openjdk:17-jdk-alpine
COPY --from=builder /app/target/VehicleManagement.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]