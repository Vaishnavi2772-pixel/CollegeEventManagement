@echo off
setlocal
set PROJECT_ROOT=%~dp0
set CLASSPATH=%PROJECT_ROOT%out;%PROJECT_ROOT%lib\mysql-connector-j-9.2.0.jar
java -cp "%CLASSPATH%" com.college.Main
pause
