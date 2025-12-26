# Build stage
FROM eclipse-temurin:17-jdk-alpine AS build
WORKDIR /app

# Copy gradle files
COPY build.gradle settings.gradle ./
COPY gradle ./gradle
COPY gradlew gradlew.bat ./

# Download dependencies
RUN chmod +x ./gradlew && ./gradlew dependencies --no-daemon || true

# Copy source code
COPY src ./src

# Build the application
RUN ./gradlew bootJar --no-daemon

# Runtime stage
FROM eclipse-temurin:17-jre-alpine
WORKDIR /app

# Copy the built jar
COPY --from=build /app/build/libs/*.jar app.jar

# Expose port
EXPOSE 8080

# Set memory limits for free tier
ENV JAVA_OPTS="-Xmx256m -Xms128m"

# Run the application
ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar app.jar"]
