FROM openjdk:21-jdk-slim

# 1. 작업 디렉토리 설정
WORKDIR /app

# 2. 애플리케이션 JAR 복사
COPY modules/bootstrap/api-gateway/build/libs/*.jar app.jar

# 3. GraphHopper 경로용 디렉토리 생성
RUN mkdir -p /app/osm \
    && mkdir -p /app/graph-cache

# 4. OSM 파일 복사 (Git LFS 사용 시 .gitattributes 포함 필요)
COPY modules/infrastructure/persistence/src/main/resources/osm/south-korea-251017.osm.pbf \
     /app/osm/south-korea-251017.osm.pbf

# 5. 포트 명시
EXPOSE 8080

# 6. 애플리케이션 실행
ENTRYPOINT ["java", "-jar", "app.jar"]