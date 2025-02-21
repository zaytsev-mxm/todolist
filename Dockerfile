FROM amazoncorretto:21

# Mmetadata
LABEL version="1.0.2"
LABEL description="ToDo list backend"
LABEL maintainer="zaytsev.mxm@gmail.com"

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