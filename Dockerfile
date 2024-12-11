FROM openjdk:20-jdk-slim

# inotify-tools: Allows Gradle to watch for file changes in the container.
RUN apt-get update && \
    apt-get install -y --no-install-recommends \
    findutils \
    inotify-tools \
    && rm -rf /var/lib/apt/lists/*

# Set the working directory inside the container
WORKDIR /app

# Set environment variables for the database connection
ENV DB_URL=${DB_URL}
ENV DB_USER=${DB_USER}
ENV DB_PASSWORD=${DB_PASSWORD}
ENV PORT_APP=${PORT_APP}

# Expose port 8080
EXPOSE ${PORT_APP}

# Copy the Gradle wrapper and related files
COPY gradlew .
COPY gradle gradle
RUN chmod +x gradlew

# Copy the build configuration files
COPY build.gradle.kts .
COPY settings.gradle.kts .
COPY gradle.properties .

# Copy tools
COPY .env .
COPY tools .

# Command to run the application in continuous mode
CMD ["sh", "./tools/start.sh"]