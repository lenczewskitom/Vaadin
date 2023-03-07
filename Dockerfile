FROM openjdk:17-jdk-alpine

WORKDIR /usr/src/app

COPY . .

ENTRYPOINT ["java","-jar","/Vaadin/build/libs/savings-0.0.1-SNAPSHOT.jar"]
