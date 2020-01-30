@echo off
netstat -an | findstr /i /r ":80[^\n]*listening" > NUL 2>&1
IF %ERRORLEVEL% NEQ 0 GOTO NO_WEB_SERVER
netstat -an | findstr /i /r ":3307[^\n]*listening" > NUL 2>&1
IF %ERRORLEVEL% NEQ 0 GOTO NO_SQL_SERVER
GOTO POSTCHECK

:NO_WEB_SERVER
echo [WARNING] It seems that the web server is not running on port 80
echo [WARNING] All the tests will fail
echo [WARNING] Start the web server by running the startup_website script
GOTO POSTCHECK

:NO_SQL_SERVER
echo [WARNING] It seems that the SQL server is not running on port 3307
echo [WARNING] All the tests will fail
echo [WARNING] Start the sql server by running the startup_website script
GOTO POSTCHECK

:POSTCHECK
.\test_cases\gradlew.bat -p .\test_cases\ --rerun-tasks check

