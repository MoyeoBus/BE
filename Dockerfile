FROM openjdk:21-jdk-slim
COPY modules/bootstrap/api-gateway/build/libs/*.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "/app.jar"]