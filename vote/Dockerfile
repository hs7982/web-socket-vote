FROM openjdk:21
EXPOSE 8080
COPY /build/libs/vote-0.0.1-SNAPSHOT.jar app.jar
ENV TZ="Asia/Seoul"

ENTRYPOINT ["java","-jar","app.jar"]
