@echo off

set ROOT=d:\data\importscrubber\importscrubber\

set classpath=%ROOT%lib\bcel.jar
set classpath=%CLASSPATH%;%ROOT%lib\junit.jar
set classpath=%CLASSPATH%;%ROOT%lib\optional.jar
set classpath=%CLASSPATH%;%ROOT%build;
set classpath=%CLASSPATH%;%ROOT%etc;

set path=%path%;d:\jdk1.3.1_03\bin
