@echo off

java -cp %CLASSPATH%;..\build\importscrubber.jar;..\lib\cfparse.jar net.sourceforge.importscrubber.Main c:\importscrubber\etc\FunctionalTest
