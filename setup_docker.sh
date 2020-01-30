#!/bin/bash
if [ "$EUID" -eq 0 ]; then
	echo >&2 "[WARNING] You should not run this script as root if you want to setup a sudoless docker installation"
fi
echo "[INFO ] Installing docker..." 
sudo apt install docker-compose -y || { echo >&2 "[ERROR] Failed to install docker-compose"; exit 1; }
echo "[INFO ] Creating docker group" 
sudo groupadd docker
echo "[INFO ] Addiung user ${USER} to docker group" 
sudo usermod -aG docker $USER || { echo >&2 "[ERROR] Failed to add user ${USER} to docker group"; exit 1; }
echo "[INFO ] Restarting docker service" || { echo >&2 "[ERROR] Failed to restart docker service"; exit 1; }
sudo service docker restart
if [ "$EUID" -ne 0 ]; then
echo "[INFO ] Logging into docker group" 
newgrp docker
fi
