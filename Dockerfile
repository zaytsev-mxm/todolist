# Use a Debian-based OpenJDK image as the base
FROM openjdk:20-jdk-slim

# Install xargs (findutils)
RUN apt-get update && \
    apt-get install -y --no-install-recommends findutils && \
    rm -rf /var/lib/apt/lists/*

# Set the working directory inside the container
WORKDIR /app

# Copy the Gradle wrapper and project files
COPY gradlew .
COPY gradle gradle
COPY build.gradle.kts .
COPY gradle.properties .
COPY settings.gradle.kts .
COPY src src

# Grant execute permission to the Gradle wrapper
RUN chmod +x gradlew

# Build the application
RUN ./gradlew installDist

# Expose port 8080
EXPOSE 8080

# Set environment variables for the database connection
ENV DB_URL=${DB_URL}
ENV DB_USER=${DB_USER}
ENV DB_PASSWORD=${DB_PASSWORD}

# Set the entry point to run the application
CMD ["./build/install/maxzaytsev-todolist/bin/maxzaytsev-todolist"]