# Build 
FROM eclipse-temurin:17-jdk-alpine AS builder

WORKDIR /app

# Instalar Maven
RUN apk add --no-cache maven

# Copiar pom.xml e baixar dependências
COPY pom.xml ./
RUN mvn dependency:go-offline -B

# Copiar código-fonte e buildar
COPY src ./src
RUN mvn clean package -DskipTests

FROM eclipse-temurin:17-jre-alpine

WORKDIR /app

# Instalar wget para checar a saúde do serviço
RUN apk add --no-cache wget

# Criar usuário não-root
RUN addgroup -g 1001 -S appgroup && \
    adduser -S appuser -u 1001 -G appgroup

# Copiar jar construído da fase de build
COPY --from=builder /app/target/*.jar app.jar

# Mudar propriedade para appuser (um usuário com privilégio limitados, mantendo o container seguro)
RUN chown appuser:appgroup app.jar

# Muda para usuário não-root
USER appuser

# Expõe a porta
EXPOSE 8082

# Checa a saúde do serviço
HEALTHCHECK --interval=30s --timeout=3s --start-period=30s --retries=3 \
    CMD wget --no-verbose --tries=1 --spider http://localhost:8082/api/estudantes/health || exit 1

# Roda a aplicação
ENTRYPOINT ["java", "-jar", "app.jar"]