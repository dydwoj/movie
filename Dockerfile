# jdk 버전 지정
FROM openjdk:17-jdk-slim

# 작업 폴더 지정
WORKDIR /app

# upload 폴더 생성
RUN mkdir -p upload

# 빌드 파일 위치 지정
ARG JAR_FILE=target/*.jar

# 빌드 파일 복사
COPY ${JAR_FILE} app.jar

# 압축해제
# java -jar 파일명
ENTRYPOINT [ "java", "-jar", "app.jar" ]