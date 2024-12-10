# Используем базовый образ с поддержкой Java 21 (или версии вашей JVM)
FROM openjdk:21-jdk-slim

# Указываем рабочую директорию внутри контейнера
WORKDIR /app

# Копируем файл сборки JAR в контейнер
COPY application/target/application-1.0.0-SNAPSHOT.jar app.jar

# Указываем команду запуска приложения
ENTRYPOINT ["java", "-jar", "app.jar"]