#!/bin/sh

logs="WEB-INF/logs"
if [ ! -d "$logs" ]; then
  mkdir -p "$logs"
  echo "$logs created ..."
fi

libs=$( echo ${MICRO_HOME}/lib/*.jar ${MICRO_HOME}/dist/*.jar WEB-INF/lib/*.jar . | sed 's/ /:/g')

# Start Micro with the embedded Jetty server.
DEBUG="-agentlib:jdwp=transport=dt_socket,server=y,suspend=n,address=5005"
OPT="${DEBUG} -Dnet.sf.ehcache.skipUpdateCheck=true -Xmx128m -Xss512k -XX:+UseCompressedOops -Dfile.encoding=UTF-8"
DUMP="-XX:+HeapDumpOnOutOfMemoryError"
java $OPT $DUMP -cp "$libs:WEB-INF/classes" ca.simplegames.micro.WebServer . 8080 $1 $2 $3 $4 $5 $6

# use jmap to get a heap dump of any running java application:
#  jmap -dump:format=b pid
# Example: 
#  $ jmap -dump:format=b,file=test.hprof 3422
#  Dumping heap to /Users/florin/test.hprof ...