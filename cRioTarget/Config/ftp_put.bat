@echo off
Ping 10.1.11.2 -n 1 -w 1000 > NUL

if errorlevel 1 (
echo You are not connected to the robot
goto end
)

ftp -s:ftp_command.txt
:end