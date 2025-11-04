FROM eclipse-temurin:21-jdk

# 1. 작업 디렉토리 설정
WORKDIR /app

# 2. 애플리케이션 JAR 복사
COPY modules/bootstrap/api-gateway/build/libs/*.jar app.jar

# 3. 필요한 패키지 설치 + OSM 파일 다운로드
RUN mkdir -p /app/osm && \
    wget -O /app/osm/south-korea.osm.pbf \
    https://download.geofabrik.de/asia/south-korea-latest.osm.pbf

# 4. 포트 명시
EXPOSE 8080

# 5. 실행
ENTRYPOINT ["java", "-jar", "app.jar"]