FROM amazoncorretto:21-alpine3.19

RUN mkdir /opt/atc
COPY target/atc-1.0-SNAPSHOT.jar /opt/atc/atc.jar

WORKDIR /opt/atc
ENTRYPOINT [ "java", "-jar", "atc.jar" ]