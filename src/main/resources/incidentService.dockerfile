FROM openjdk:17-jdk-alpine
WORKDIR /app
COPY target/incident-0.0.1-SNAPSHOT.jar /app/incident-service.jar
EXPOSE 8080
# Define environment variables for runtime configurations (optional)
ENV JAVA_OPTS=""

# Run the Spring Boot application
ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar /app/incident-service.jar"]
