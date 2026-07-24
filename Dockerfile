# ─── Stage 1: Build ───
FROM gradle:8.5-jdk21 AS builder

WORKDIR /app

# 1. Copy build config + wrapper first (layer caching)
COPY gradle gradle
COPY gradlew build.gradle settings.gradle ./

# 2. Fix line endings & pre-download dependencies (cached unless build files change)
RUN sed -i 's/\r$//' ./gradlew && \
    chmod +x ./gradlew && \
    ./gradlew dependencies --no-daemon || true

# 3. Copy source and build
COPY src ./src
RUN ./gradlew bootJar --no-daemon

# ─── Stage 2: Runtime ───
FROM eclipse-temurin:21-jre-alpine

ENV TZ=Asia/Phnom_Penh

# Alpine needs tzdata installed; set timezone
RUN apk add --no-cache tzdata && \
    ln -snf /usr/share/zoneinfo/$TZ /etc/localtime && \
    echo $TZ > /etc/timezone

# Create a non-root user and group
RUN addgroup -S spring && adduser -S spring -G spring

WORKDIR /app

# Copy jar and give ownership to the non-root user
COPY --from=builder --chown=spring:spring /app/build/libs/*.jar app.jar

USER spring

# Port Configuration Default is: 8080
ARG APP_PORT=26022
ENV SERVER_PORT=${APP_PORT}
EXPOSE ${APP_PORT}

# Optional: lets Docker/K8s know if the app is alive (needs spring-boot-actuator)
#HEALTHCHECK --interval=30s --timeout=5s --start-period=40s --retries=3 \
#    CMD wget -qO- http://localhost:26010/actuator/health | grep -q '"UP"' || exit 1

ENTRYPOINT ["java", \
  "-XX:MaxRAMPercentage=75.0", \
  "-Duser.timezone=Asia/Phnom_Penh", \
  "-jar", "/app/app.jar"]