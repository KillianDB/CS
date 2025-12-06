FROM docker.io/eclipse-temurin:21-jdk-alpine as builder
WORKDIR /app
COPY . .
RUN chmod +x ./mvnw
RUN ./mvnw clean package -DskipTests

FROM docker.io/eclipse-temurin:21-jdk-alpine
WORKDIR /app
COPY --from=builder /app/target/gateway-0.0.1-SNAPSHOT.jar gateway.jar
EXPOSE 9000
ENTRYPOINT ["java", "-jar", "gateway.jar"]