@echo off

SET MAIN=net.sourceforge.importscrubber.ImportScrubberGUI

java -cp %CLASSPATH%;..\src;..\lib\bcel.jar %MAIN%
