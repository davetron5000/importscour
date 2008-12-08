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

IS_HOME=`dirname $PRG`

java -cp $IS_HOME/bcel-5.2.jar:$IS_HOME/importscour.jar net.sourceforge.importscrubber.CLI $*
