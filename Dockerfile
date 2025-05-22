FROM maven:4.0.0-openjdk-17-slim AS builder
WORKDIR /app
COPY pom.xml .
COPY src ./src
RUN --mount=type=cache,target=/root/.m2 mvn clean package -DskipTests

FROM openjdk:17-jdk-alpine
COPY --from=builder /app/target/VehicleManagement.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]