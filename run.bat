@echo off
REM Start the Java server with the included batch file:
REM   run.bat
REM
REM Or use this exact command from the project root:
REM   powershell.exe -NoProfile -Command "Set-Location 'C:\Users\dell\OneDrive\Desktop\college event management system'; java -cp 'out;lib\mysql-connector-j-9.2.0.jar' com.college.Main"
setlocal
set PROJECT_ROOT=%~dp0
set CLASSPATH=%PROJECT_ROOT%out;%PROJECT_ROOT%lib\mysql-connector-j-9.2.0.jar
java -cp "%CLASSPATH%" com.college.Main
pause
