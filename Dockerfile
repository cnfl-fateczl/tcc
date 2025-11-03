# Etapa 1: build
FROM maven:3.9.9-eclipse-temurin-21 AS builder
WORKDIR /app
COPY pom.xml .
COPY src ./src
RUN mvn clean package -DskipTests

# Etapa 2: runtime
FROM eclipse-temurin:21-jdk
WORKDIR /app
COPY --from=builder /app/target/gerencia-restaurante-0.0.1-SNAPSHOT.jar app.jar

# Define o comando de execução
ENTRYPOINT ["java", "-jar", "app.jar"]
