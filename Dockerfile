FROM openjdk:8
LABEL maintainer="Kasra Madadipouya <kasra@madadipouya.com>"
LABEL version="0.1"
RUN mkdir /tmp/workforce
RUN mkdir /tmp/workforce/logs
VOLUME /tmp/workforce
WORKDIR /tmp/workforce
COPY target/workforce-0.0.1-SNAPSHOT.jar workforce.jar
#COPY src/main/resources/test.properties application.properties
RUN apt-get update && apt-get upgrade -y && apt-get install vim -y
ENTRYPOINT java -jar workforce.jar --logging.file=logs/logs.txt
EXPOSE 8080