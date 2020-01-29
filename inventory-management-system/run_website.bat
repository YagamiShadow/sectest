@echo off
IF "%1"=="" GOTO NO_ARG
docker-compose -f %~dp0docker-compose.yml build %*
docker-compose -f %~dp0docker-compose.yml up %*
GOTO EOF
:NO_ARG
echo Missing service arguments: please use "%0 [service] [service] ..." where service in {web_original, web_patched, db}
:EOF