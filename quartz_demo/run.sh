#!/bin/sh

libs=$( echo ${MICRO_HOME}/lib/*.jar ${MICRO_HOME}/dist/*.jar WEB-INF/lib/*.jar . | sed 's/ /:/g')

# Start Micro with the embedded Jetty server.
DEBUG="-agentlib:jdwp=transport=dt_socket,server=y,suspend=n,address=6006"

NO_QUARTZ_UPDATE_CHECK="-Dorg.quartz.scheduler.skipUpdateCheck=true"
NO_EHCACHE_UPDATE_CHECK="-Dnet.sf.ehcache.skipUpdateCheck=true"
OPT="$NO_EHCACHE_UPDATE_CHECK $NO_QUARTZ_UPDATE_CHECK -Xmx128m -Xss512k -XX:+UseCompressedOops"
java $OPT $DEBUG -cp "$libs:WEB-INF/classes" ca.simplegames.micro.WebServer . 8080 $1 $2 $3 $4 $5 $6
