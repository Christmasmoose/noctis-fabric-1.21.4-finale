#!/bin/sh
# Gradle start up script for UN*X

DIR="$( cd "$( dirname "$0" )" && pwd )"

# Add default JVM options here. You can also use JAVA_OPTS and GRADLE_OPTS to pass JVM options to this script.
DEFAULT_JVM_OPTS=""

exec "$DIR/gradle/wrapper/gradle-wrapper.jar" "$@"
