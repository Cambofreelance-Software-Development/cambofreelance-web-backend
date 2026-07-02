# Stage 1: Build the application
FROM gradle:8.5-jdk21 AS builder

# Set the working directory
WORKDIR /app

# Copy Gradle wrapper and build files
COPY gradle gradle
COPY gradlew .
COPY build.gradle .
COPY settings.gradle .

# Copy source code
COPY src ./src

# Set timezone explicitly
ENV TZ=Asia/Phnom_Penh

# Configure the timezone in the container
RUN ln -snf /usr/share/zoneinfo/$TZ /etc/localtime && \
    echo $TZ > /etc/timezone

# Make the wrapper executable and build the application
RUN sed -i 's/\r$//' ./gradlew && chmod +x ./gradlew && ./gradlew bootJar --no-daemon


# Stage 2: Run the application
FROM eclipse-temurin:21-jdk

# Set timezone in the runtime container too
ENV TZ=Asia/Phnom_Penh

# Set the working directory
WORKDIR /app

# Copy the packaged JAR file from the build stage
COPY --from=builder /app/build/libs/*.jar app.jar

# Expose application port (adjust if necessary)
EXPOSE 26010

# Print timezone information before starting the app
RUN echo "Container timezone set to: $(date)"

# Run the Spring Boot application
ENTRYPOINT ["java","-Duser.timezone=Asia/Phnom_Penh","-jar", "/app/app.jar"]