FROM openjdk:21-jdk-slim

WORKDIR /app

COPY modules/bootstrap/api-gateway/build/libs/*.jar app.jar

RUN mkdir -p /app/osm

COPY modules/infrastructure/persistence/src/main/resources/osm/south-korea-251017.osm.pbf \
     /app/osm/south-korea-251017.osm.pbf

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]