#!/bin/sh

PRG="$0"
# Lifted from mvn
while [ -h "$PRG" ] ; do
    ls=`ls -ld "$PRG"`
    link=`expr "$ls" : '.*-> \(.*\)$'`
    if expr "$link" : '/.*' > /dev/null; then
        PRG="$link"
    else
        PRG="`dirname "$PRG"`/$link"
    fi
done

DIR=`dirname $PRG`
IS_HOME=${DIR}/../../lib/rt

echo "java -cp $IS_HOME/bcel-5.2.jar:${DIR}/../../build/classes net.sourceforge.importscrubber.CLI"
java -cp $IS_HOME/bcel-5.2.jar:${DIR}/../../build/classes net.sourceforge.importscrubber.CLI $*
