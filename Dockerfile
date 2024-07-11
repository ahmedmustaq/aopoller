FROM openjdk:8-jdk-alpine
RUN apk --no-cache add curl
MAINTAINER raziqtechnologies.com
COPY target/availoffers-poller.jar availoffers-poller.jar
COPY custom-extractors.xml custom-extractors.xml
COPY extractors.xsd extractors.xsd
COPY functions.xsd functions.xsd
ENTRYPOINT ["java","-jar","/availoffers-poller.jar"]