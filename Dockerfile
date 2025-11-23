# Etapa 1: Build da aplicação
FROM maven:3.9.9-eclipse-temurin-21 AS builder

WORKDIR /app

# Copia o pom.xml e baixa dependências (melhora cache)
COPY pom.xml .
RUN mvn dependency:go-offline -B

# Copia todo o restante e compila
COPY src ./src
RUN mvn clean package -DskipTests

# Etapa 2: Runtime
FROM eclipse-temurin:21-jre

WORKDIR /app

# Copia o JAR gerado
COPY --from=builder /app/target/gerencia-restaurante-0.0.1-SNAPSHOT.jar app.jar

# Expõe a porta
EXPOSE 8080

# Comando para rodar a aplicação
ENTRYPOINT ["java", "-jar", "app.jar"]
