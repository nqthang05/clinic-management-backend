# Bước 1: Sử dụng JDK 25 chính thức từ Eclipse Temurin
FROM eclipse-temurin:25-jdk AS build
WORKDIR /app

# Copy Maven Wrapper (Có sẵn trong thư mục dự án của bạn)
COPY mvnw .
COPY .mvn .mvn
COPY pom.xml .

# Cấp quyền thực thi cho file mvnw trên môi trường Linux của Docker
RUN chmod +x mvnw

# Copy toàn bộ mã nguồn
COPY src ./src

# Build ứng dụng bằng chính Maven Wrapper thay vì Docker Maven
RUN ./mvnw clean package -DskipTests

# Bước 2: Môi trường chạy siêu nhẹ với JRE 25
FROM eclipse-temurin:25-jre
WORKDIR /app
COPY --from=build /app/target/*.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]