#!/bin/bash
command -v docker-compose >/dev/null 2>&1 || { echo >&2 "[ERROR  ] It seems docker is not installed on your system: if you want to use this script install it first (sudo apt install docker-compose)"; exit 1; }
if [ $# -eq 0 ] ; then
    echo '[ERROR  ] Missing service arguments: please use "${0} [service] [service] ..." where service in {web_original, web_patched, mysql_db}'
    exit 1
fi
DIR="$( cd "$( dirname "${BASH_SOURCE[0]}" )" >/dev/null 2>&1 && pwd )"
docker-compose -f $DIR/docker-compose.yml build $@
if [ $? -ne 0 ] ; then
	echo '[ERROR  ] Failed to run docker-compose build'
	if [ "$EUID" -ne 0 ] ; then
	    echo "[WARNING] If you are not able to connect to docker daemon then:"
	    echo "[WARNING] - Make sure docker service is running: sudo service docker restart"
	    echo "[WARNING] - You are not part of the docker group: add the current user to the \"docker\" group (https://docs.docker.com/install/linux/linux-postinstall/) or run this script as root"
	    exit 1
	fi
	echo '[INFO   ] Restarting docker service'
	service docker restart || { echo >&2 "[ERROR  ] Failed to restart docker service"; exit 1; }
	docker-compose -f $DIR/docker-compose.yml build $@ || { echo >&2 "[ERROR  ] Failed to build docker services"; exit 1; } 
fi
docker-compose -f $DIR/docker-compose.yml up $@
