#!/bin/bash
command -v wget >/dev/null 2>&1
if [ $? -eq 0 ] ; then
	wget -qO- http://localhost/inventory-management-system &> /dev/null || { echo >&2 "[WARNING] It seems that the web server is not running on port 80
[WARNING] All the tests will fail
[WARNING] Start the web server by running the startup_website script"; }
else 
	echo "[WARNING] Cannot use wget to verify that the website is up and running"
fi
./test_cases/gradlew -p ./test_cases/ --rerun-tasks check
