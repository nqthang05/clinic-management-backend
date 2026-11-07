# Bước 1: Môi trường Build sử dụng Maven và Java 25
FROM maven:3.8.5-openjdk-25 AS build
WORKDIR /app
COPY pom.xml .
COPY src ./src
RUN mvn clean package -DskipTests
# Bước 2: Môi trường chạy (Chỉ lấy file .jar để cho nhẹ server)
FROM openjdk:25-jdk-slim
WORKDIR /app
COPY --from=build /app/target/*.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]