# CHAT DOCKERFILE
#
# ------------------------------------------------------------------------------
#               NOTE: THIS DOCKERFILE IS GENERATED VIA "update.sh"
#
#                       PLEASE DO NOT EDIT IT DIRECTLY.
# ------------------------------------------------------------------------------

    FROM openjdk:9
LABEL maintainer = "julien.alaimo@gmail.com"
ENV \
    	INITSYSTEM on \
    	DEBIAN_FRONTEND=noninteractive
# Basic build-time metadata as defined at http://label-schema.org
ARG BUILD_DATE
ARG VCS_REF
LABEL org.label-schema.build-date=$BUILD_DATE \
    	org.label-schema.docker.dockerfile="/Dockerfile" \
    	org.label-schema.name="HAPPY_SERVER"
#--------------Install basepackages--------------# 
RUN apt-get -qq update > /dev/null && \
rm -rf /var/lib/apt/lists/ */tmp/* /var/tmp/*

# Execute command
ADD  CHAT_jar.jar /app/CHAT_jar.jar
ADD  config.json /app/src/App/config.json
ADD  security.policy /app/src/App/security.policy

  EXPOSE 1099:1099

  WORKDIR /app

CMD ["java", "-jar","CHAT_jar.jar","-Dweblogic.oif.serialFilterScope=weblogic", \
"-Dcom.sun.management.jmxremote.local.only=false","-Dcom.sun.management.jmxremote.port=1099","-Dcom.sun.management.jmxremote.rmi.port=1099",\
"-Dcom.sun.management.jmxremote.ssl=false", "-Dcom.sun.management.jmxremote.authenticate=false", "-Djava.rmi.server.hostname=0.0.0.0"]

