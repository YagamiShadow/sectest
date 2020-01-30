@echo off
WHERE docker-compose > NUL 2>&1
IF %ERRORLEVEL% NEQ 0 GOTO NO_DOCKER
IF "%1"=="" GOTO NO_ARG
echo [INFO ] Starting services %*...
docker-compose -f %~dp0docker-compose.yml build %*
docker-compose -f %~dp0docker-compose.yml up %*
GOTO EOF
:NO_ARG
echo [ERROR] Missing service arguments: please use "%0 [service] [service] ..." where service in {web_original, web_patched, mysql_db}
GOTO EOF
:NO_DOCKER
echo [ERROR] It seems docker is not installed on your system: if you want to use this script then go to www.docker.com and install it
:EOF
