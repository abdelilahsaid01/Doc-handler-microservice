FROM openjdk:11

# Set the working directory inside the container
WORKDIR /app

# Copy the packaged JAR file into the container at the defined working directory
COPY target/*.jar /app/config-service.jar

# Specify the command to run your application
CMD ["java", "-jar", "config-service.jar"]