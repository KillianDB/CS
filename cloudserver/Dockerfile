FROM docker.io/eclipse-temurin:21-jdk-alpine as builder
WORKDIR /app
COPY . .
RUN chmod +x ./mvnw
RUN ./mvnw clean package -DskipTests

FROM docker.io/eclipse-temurin:21-jdk-alpine
WORKDIR /app

# Install wget for healthcheck
RUN apk add --no-cache wget

COPY --from=builder /app/target/cloudserver-0.0.1-SNAPSHOT.jar cloudserver.jar
EXPOSE 8888

# HEALTHCHECK for configserver availability
HEALTHCHECK --interval=5s --timeout=3s --start-period=10s --retries=10 \
  CMD wget --no-verbose --tries=1 --spider http://localhost:8888/actuator/health || exit 1

ENTRYPOINT ["java", "-jar", "cloudserver.jar"]
