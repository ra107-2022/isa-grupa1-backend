# Stage 1: Build the application
FROM maven:3.9.6-eclipse-temurin-21 AS build
WORKDIR /app
# Copy only the pom first to cache dependencies
COPY pom.xml .
RUN mvn dependency:go-offline
# Copy source and build
COPY src ./src
RUN mvn clean package -DskipTests

# Stage 2: Runtime environment
FROM eclipse-temurin:21-jre-alpine

# Install FFmpeg via apk
RUN apk add --no-cache ffmpeg
RUN apk add --no-cache tzdata
ENV TZ=Europe/Belgrade

WORKDIR /app
# Copy only the built jar from the first stage
COPY --from=build /app/target/*.jar app.jar
# Expose port 8080 for the app and metrics
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]