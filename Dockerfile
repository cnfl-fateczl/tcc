# Etapa 1: build
FROM maven:3.9.9-eclipse-temurin-21 AS builder
WORKDIR /app

# Copia o arquivo pom.xml e baixa dependências antes (para cache mais eficiente)
COPY pom.xml .
RUN mvn dependency:go-offline -B

# Copia o restante do código e faz o build
COPY src ./src
RUN mvn clean package -DskipTests

# Etapa 2: runtime
FROM eclipse-temurin:21-jdk
WORKDIR /app

# Copia o JAR gerado
COPY --from=builder /app/target/gerencia-restaurante-0.0.1-SNAPSHOT.jar app.jar

# Define o comando padrão para executar o app
ENTRYPOINT ["java", "-jar", "app.jar"]
