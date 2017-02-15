#!/bin/sh

#-agentlib:jdwp=transport=dt_socket,suspend=y,address=localhost:40709 

$JAVA_HOME/bin/java -Dlog4j.sym.home=/tmp/ \
-Dfile.encoding=UTF-8 \
-jar ~/.m2/repository/com/mcreations/renderer/velocity-renderer/1.0-SNAPSHOT/velocity-renderer-1.0-SNAPSHOT-jar-with-dependencies.jar \
-s ./src -f .*.vm -d /tmp/
