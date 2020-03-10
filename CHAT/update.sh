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
	ADD  CHAT.jar /app/CHAT.jar

	CMD ["java", "-jar","/app/CHAT.jar"]

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
    cp out/artifacts/CHAT_jar/CHAT.jar gen_dockerfile/${arch}/CHAT.jar
		echo "done"
done