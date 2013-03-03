#!/bin/sh

logs="WEB-INF/logs"
if [ ! -d "$logs" ]; then
  mkdir -p "$logs"
  echo "$logs created ..."
fi

libs=$( echo ${MICRO_HOME}/lib/*.jar ${MICRO_HOME}/dist/*.jar WEB-INF/lib/*.jar . | sed 's/ /:/g')
appdir=`pwd .`

# Start Micro with the embedded Jetty server.
DEBUG="-agentlib:jdwp=transport=dt_socket,server=y,suspend=n,address=5005"
OPT="${DEBUG} -Dnet.sf.ehcache.skipUpdateCheck=true -Xmx128m -Xss512k -XX:+UseCompressedOops -Dfile.encoding=UTF-8"
SMITH="-javaagent:WEB-INF/smith-1.0.jar=period=1,classes=${appdir}/WEB-INF/classes,loglevel=INFO"
java $OPT -cp "$libs:WEB-INF/classes" $SMITH ca.simplegames.micro.WebServer . 8080 $1 $2 $3 $4 $5 $6
