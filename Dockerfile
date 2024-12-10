# Используем базовый образ с поддержкой Java 21 (или версии вашей JVM)
FROM openjdk:21-jdk-slim

# Указываем рабочую директорию внутри контейнера
WORKDIR /app

COPY . .
RUN mvn clean package

FROM openjdk:17-jdk-slim
WORKDIR /app
COPY --from=builder /app/application/target/application-1.0.0-SNAPSHOT.jar app.jar
CMD ["java", "-jar", "app.jar"]