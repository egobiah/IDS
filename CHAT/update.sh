#!/bin/bash

arches="files"

print_header() {
	cat > $1 <<-EOI
	# CHAT DOCKERFILE
	#
	# ------------------------------------------------------------------------------
	#               NOTE: THIS DOCKERFILE IS GENERATED VIA "update.sh"
	#
	#                       PLEASE DO NOT EDIT IT DIRECTLY.
	# ------------------------------------------------------------------------------
	
	EOI
}

# Print selected image
print_baseimage() {
	cat >> $1 <<-EOI
    FROM openjdk:9
	LABEL maintainer = "julien.alaimo@gmail.com"
	EOI
}

# Print metadata && basepackages
print_basepackages() {
	cat >> $1 <<-'EOI'	
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

EOI
}


# Set working directory and execute command
print_command() {
	cat >> $1 <<-'EOI'
	# Execute command
	ADD  CHAT_jar.jar /app/CHAT_jar.jar
	ADD  config.json /app/src/App/config.json
	ADD  security.policy /app/src/App/security.policy

  EXPOSE 1099:1099

  WORKDIR /app

	CMD ["java", "-jar","CHAT_jar.jar","-Dweblogic.oif.serialFilterScope=weblogic", \
	"-Dcom.sun.management.jmxremote.local.only=false","-Dcom.sun.management.jmxremote.port=1099","-Dcom.sun.management.jmxremote.rmi.port=1099",\
	"-Dcom.sun.management.jmxremote.ssl=false", "-Dcom.sun.management.jmxremote.authenticate=false", "-Djava.rmi.server.hostname=0.0.0.0"]

EOI
} 

# Build the Dockerfiles
mkdir 
for arch in ${arches}
do
	file=gen_dockerfile/${arch}/Dockerfile
		mkdir -p `dirname ${file}` 2>/dev/null
		echo -n "Writing $file..."
		print_header ${file};
		print_baseimage ${file};
		print_basepackages ${file};
		print_command ${file};
    cp out/artifacts/CHAT_jar/CHAT_jar.jar gen_dockerfile/${arch}/CHAT_jar.jar
		echo "done"
done