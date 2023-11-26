# OpenJDK 17
FROM openjdk:17

# 컨테이너 내부의 /app 디렉토리 생성
WORKDIR /app

# 컨테이너 내부의 /app 디렉토리에 jar 파일 복사
ARG JAR_FILE=build/libs/matrip-server-0.0.1-SNAPSHOT.jar
ARG CONTAINER_JAR_FILE=matrip-server.jar
COPY ${JAR_FILE} /app/${CONTAINER_JAR_FILE}

# Spring Boot를 실행하기 위한 entry point 지정
ENTRYPOINT ["java", "-Dspring.profiles.active=prod", "-jar", "/app/matrip-server.jar"]