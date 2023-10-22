# Используем официальный образ Java
FROM eclipse-temurin:17.0.8.1_1-jre-ubi9-minimal

# Создаем директорию внутри контейнера
WORKDIR /app

# Копируем JAR файл с зависимостями в контейнер
COPY target/walletService-1.0-SNAPSHOT-jar-with-dependencies.jar /app/walletService.jar

# Устанавливаем команду, которая будет выполняться при запуске контейнера
CMD ["java", "-jar", "/app/walletService.jar"]