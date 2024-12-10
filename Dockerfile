# Стадия сборки
FROM maven:3.8.5-openjdk-17 AS builder
WORKDIR /app
COPY . .
RUN mvn clean package

# Стадия выполнения
FROM openjdk:17-jdk-slim
WORKDIR /app
COPY --from=builder /app/application/target/application-1.0.0-SNAPSHOT.jar app.jar
CMD ["java", "-jar", "app.jar"]