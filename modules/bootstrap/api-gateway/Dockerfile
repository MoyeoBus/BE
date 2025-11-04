FROM eclipse-temurin:21-jdk

WORKDIR /app

COPY modules/bootstrap/api-gateway/build/libs/*.jar app.jar

RUN mkdir -p /app/osm && \
    wget -O /app/osm/south-korea.osm.pbf \
    https://download.geofabrik.de/asia/south-korea-latest.osm.pbf

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]