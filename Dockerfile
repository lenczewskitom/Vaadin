FROM tomcat:8.0-alpine

LABEL maintainer="lenczewski.tom@gmail.com"

COPY build/libs/Vaadin-0.0.1-SNAPSHOT.war /usr/local/tomcat/webapps/vaadin.war

CMD ["catalina.sh", "run"]
