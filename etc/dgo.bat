@echo off

SET MAIN=net.sourceforge.importscrubber.ImportScrubberGUI

java -cp %CLASSPATH%;..\build;..\lib\bcel.jar %MAIN%
