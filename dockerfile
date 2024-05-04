FROM amazoncorretto:21-alpine3.19

RUN mkdir /opt/atc
COPY build/libs/atc-all.jar /opt/atc/atc.jar

WORKDIR /opt/atc
ENTRYPOINT [ "java", "-jar", "atc.jar" ]