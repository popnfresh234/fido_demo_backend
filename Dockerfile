# Fetching latest version of Java
FROM openjdk:latest

# Setting up work directory
WORKDIR /app

# Copy the jar file into our app
COPY ./build/libs/api-login-0.0.1-SNAPSHOT.jar /app

# Exposing port 8080
EXPOSE 8080

# Starting the application
CMD ["java", "-jar", "api-login-0.0.1-SNAPSHOT.jar"]