#!/bin/bash

java -cp lib/rt/bcel.jar:build/classes net.sourceforge.importscrubber.ImportScrubber $*
