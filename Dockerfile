# Bước 1: Build với image Maven chạy trên nền Eclipse Temurin (Java 25)
FROM maven:3-eclipse-temurin-25 AS build
WORKDIR /app
COPY pom.xml .
COPY src ./src
RUN mvn clean package -DskipTests
# Bước 2: Chạy ứng dụng với JRE của Eclipse Temurin (nhẹ và bảo mật)
FROM eclipse-temurin:25-jre-alpine
WORKDIR /app
COPY --from=build /app/target/*.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]