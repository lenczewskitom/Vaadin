# https://spring.io/guides/gs/spring-boot-docker/
FROM openjdk:17-jdk-alpine
COPY vaadin-0.0.1-SNAPSHOT.jar vaadin-0.0.1-SNAPSHOT.jar
CMD ["java","-jar","vaadin-0.0.1-SNAPSHOT.jar"]