# Build stage
FROM gradle:8.12.1-jdk21 AS builder

WORKDIR /app
COPY . .

# Build the application
RUN gradle build --no-daemon

# Runtime stage
FROM eclipse-temurin:21-jre-jammy

WORKDIR /app

# Create a non-root user
RUN useradd -m -s /bin/bash javauser

# Copy the built artifact from builder stage
COPY --from=builder /app/build/libs/*.jar app.jar

# Set ownership of the application files
RUN chown -R javauser:javauser /app

# Switch to non-root user
USER javauser

# Health check
HEALTHCHECK --interval=30s --timeout=3s \
  CMD curl -f http://localhost:8080/actuator/health || exit 1

# Expose the application port
EXPOSE 8080

# Set Java options for container environment
ENV JAVA_OPTS="-XX:+UseContainerSupport -XX:MaxRAMPercentage=75.0"

# Run the application
ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar app.jar"]