#!/usr/bin/env bash
set -euxo pipefail

JAVA_17_HOME=$(/usr/libexec/java_home -v17)
export JAVA_HOME=${JAVA_17_HOME}

./gradlew -Dorg.gradle.java.home="${JAVA_17_HOME}" clean bootJar

mkdir -p build/docker
java -Djarmode=layertools -jar build/libs/*.jar extract --destination build/docker
docker build -t venator85/openai-server .

# run locale:
# docker run -p 8080:8080 -v $(pwd)/db:/db venator85/openai-server

# run su server:
# docker login
# docker run -p 8080:8080 -v $(pwd)/db:/db venator85/openai-server:latest
