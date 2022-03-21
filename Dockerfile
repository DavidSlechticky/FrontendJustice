FROM openjdk:8-jdk-alpine
VOLUME /tmp
ADD /target/frontend-0.0.1-SNAPSHOT.jar app.jar
ENTRYPOINT ["java","-Duser.timezone=Europe/Prague","-Djava.security.egd=file:/dev/./urandom","-jar","/app.jar"]
