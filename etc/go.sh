#!/bin/sh

MAIN=net.sourceforge.importscrubber.ImportScrubberGUI
CP=${CLASSPATH}:../lib/importscrubber.jar:../lib/bcel.jar

java -cp $CP $MAIN
