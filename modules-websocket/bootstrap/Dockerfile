FROM eclipse-temurin:21-jdk

WORKDIR /app

# JAR 파일 복사
COPY modules-websocket/bootstrap/build/libs/*.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]