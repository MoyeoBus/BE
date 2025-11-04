FROM eclipse-temurin:21-jdk

WORKDIR /app

# JAR 파일 복사
COPY modules/bootstrap/api-gateway/build/libs/*.jar app.jar

# Git LFS로 관리되는 OSM 파일 복사
COPY modules/infrastructure/persistence/src/main/resources/osm/south-korea-251017.osm.pbf /app/osm/south-korea.osm.pbf

# graph-cache 디렉토리 생성
RUN mkdir -p /app/graph-cache

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]