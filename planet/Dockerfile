FROM maven:3.6.0-jdk-11-slim AS planet-builder
COPY src /app/src
COPY pom.xml /app
RUN mvn -f /app/pom.xml clean install

FROM openjdk:11-jre-slim
COPY --from=planet-builder /app/target/*.jar /app/planet.jar
COPY --from=planet-builder /app/target/docs /app
WORKDIR /app
EXPOSE 8080
CMD ["java", "-jar", "planet.jar"]
