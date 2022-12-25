#!/bin/sh

# JAVA_OPTS
# ENABLE_JMX

## if JAVA_OPTS has been set already, we assume that it contains the memory configuration
[ -z "${JAVA_OPTS}" ] && JAVA_OPTS="-Xmx512m"

## enable JMX port if requested
if [ "${ENABLE_JMX}" = "true" ] ; then
    JAVA_OPTS="${JAVA_OPTS} -Dcom.sun.management.jmxremote"
    JAVA_OPTS="${JAVA_OPTS} -Dcom.sun.management.jmxremote.authenticate=false"
    JAVA_OPTS="${JAVA_OPTS} -Dcom.sun.management.jmxremote.ssl=false"
    JAVA_OPTS="${JAVA_OPTS} -Dcom.sun.management.jmxremote.local.only=false"
    JAVA_OPTS="${JAVA_OPTS} -Dcom.sun.management.jmxremote.port=1099"
    JAVA_OPTS="${JAVA_OPTS} -Dcom.sun.management.jmxremote.rmi.port=1099"
    JAVA_OPTS="${JAVA_OPTS} -Djava.rmi.server.hostname=127.0.0.1"
fi

## enable debugs if requested
if [ "${ENABLE_DEBUG}" = "true" ] ; then
    JAVA_OPTS="${JAVA_OPTS} -Xdebug -agentlib:jdwp=transport=dt_socket,server=y,suspend=n,address=5005"
fi

## Configure Java extra options
if [ -n "${JAVA_OPTS_EXTRA}" ] ; then
    JAVA_OPTS="${JAVA_OPTS} ${JAVA_OPTS_EXTRA}"
fi

## Configure Java
if [ -n "${JAVA_HOME}" ] ; then
	JAVA="${JAVA_HOME}/bin/java"
else
	JAVA="$(which java)"
fi

if [ -z "${JAVA}" -o ! -x "${JAVA}" ] ; then
	echo
	echo "ERROR: Java could not be found!"
	echo
	echo "You have to set the JAVA_HOME environment variable or add the java executable to your system's PATH environment."
	echo
	exit 1
fi

## print environment and execute
printenv
exec "${JAVA}" -jar  ${JAVA_OPTS} /app/application.jar "$@"
