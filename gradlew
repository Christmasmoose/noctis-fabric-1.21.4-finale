#!/usr/bin/env sh
##############################################################################
## Gradle startup script for UNIX-like systems
##############################################################################

# Resolve APP_HOME
PRG="$0"
while [ -h "$PRG" ] ; do
  ls=`ls -ld "$PRG"`
  link=`expr "$ls" : '.*-> \(.*\)$'`
  if expr "$link" : '/.*' >/dev/null; then
    PRG="$link"
  else
    PRG=`dirname "$PRG"`"/$link"
  fi
done
SAVED="`pwd`"
cd "`dirname \"$PRG\"`/" >/dev/null
APP_HOME="`pwd -P`"
cd "$SAVED" >/dev/null

APP_NAME="Gradle"
APP_BASE_NAME=`basename "$0"`

# Default JVM options (keep small so CI starts fast)
DEFAULT_JVM_OPTS="-Xms64m -Xmx64m"

# Locate Java
if [ -n "$JAVA_HOME" ] ; then
  if [ -x "$JAVA_HOME/jre/sh/java" ] ; then
    JAVACMD="$JAVA_HOME/jre/sh/java"
  else
    JAVACMD="$JAVA_HOME/bin/java"
  fi
else
  JAVACMD="java"
fi

# Validate Java command
if ! command -v "$JAVACMD" >/dev/null 2>&1 ; then
  echo "ERROR: Java not found. Set JAVA_HOME or ensure 'java' is on PATH." >&2
  exit 1
fi

# Build classpath to the wrapper
CLASSPATH="$APP_HOME/gradle/wrapper/gradle-wrapper.jar"

# Run Gradle Wrapper Main (this is the key fix—use Java, don’t exec the .jar)
exec "$JAVACMD" $DEFAULT_JVM_OPTS \
  -classpath "$CLASSPATH" org.gradle.wrapper.GradleWrapperMain "$@"
