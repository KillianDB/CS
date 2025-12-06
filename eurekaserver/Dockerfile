FROM docker.io/eclipse-temurin:21-jdk-alpine as builder
WORKDIR /app
COPY . .
RUN ./mvnw clean package -DskipTests

FROM docker.io/eclipse-temurin:21-jdk-alpine
WORKDIR /app
COPY --from=builder /app/target/eurekaserver-0.0.1-SNAPSHOT.jar eurekaserver.jar
EXPOSE 8761
ENTRYPOINT ["java", "-jar", "eurekaserver.jar"]