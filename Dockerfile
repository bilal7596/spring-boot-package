# Use an official OpenJDK runtime as a parent image
FROM openjdk:17-jdk-slim AS build

# Set the working directory
WORKDIR /app

COPY gradlew ./
COPY gradle gradle

# Copy the rest of the project files
COPY . .

# Run the Gradle build to create the JAR file
RUN ./gradlew build --no-daemon

# Use a JDK image to run the application
FROM openjdk:17-jdk-slim

# Set the working directory
WORKDIR /app

# Copy the JAR file from the build stage
COPY --from=build /app/build/libs/*.jar app.jar

# Expose the port on which the app will run
#EXPOSE 8090

# Run the application
ENTRYPOINT ["java", "-jar", "app.jar"]