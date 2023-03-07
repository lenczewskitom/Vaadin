FROM openjdk:17-jdk-alpine
COPY /usr/src/app/build/libs/Vaadin-0.0.1-SNAPSHOT-plain.jar /Vaadin-0.0.1-SNAPSHOT-plain.jar
ENTRYPOINT ["java","-jar","/Vaadin-0.0.1-SNAPSHOT-plain.jar"]