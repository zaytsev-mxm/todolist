FROM openjdk:20-jdk-slim

# Mmetadata
LABEL version="1.0.0"
LABEL description="ToDo list backend"
LABEL maintainer="zaytsev.mxm@gmail.com"

# inotify-tools: Allows Gradle to watch for file changes in the container.
RUN apt-get update && \
    apt-get install -y --no-install-recommends \
    findutils \
    inotify-tools \
    && rm -rf /var/lib/apt/lists/*

# Set the working directory inside the container
WORKDIR /app

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

# Copy tools needed for running the app
COPY tools .