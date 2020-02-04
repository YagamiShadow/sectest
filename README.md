# Inventory Management System
# XSS vulnerabilities analysis
by Francesco Tescari

### Locations:
##### Report: /report/report.pdf
##### Vulnerabilities table: /report/vulnerabilities.pdf
##### Original website: /inventory-management-system/original/ims
##### Fixed website:    /inventory-management-system/patched/ims
##### Test files: /test_cases/src/test/java/it/unitn/sectest/xss_suite
##### Pixy analysis original website: /pixy_analysis/original
##### Pixy analysis fixed website: /pixy_analysis/patched

###### Usage instructions:
To start up either the original web application or the patched one, make sure that
   - Docker and docker-compose are installed (if not, install them or run ```./setup_docker.sh```)
   - The port 80 is not being used
   - The port 3307 is not being used

Then startup the docker services for the desired web application (original or patched) with:
```sh
./startup_website_original.sh
```
or
```sh
./startup_website_patched.sh
```
Then wait for docker to download and build the necessary files and wait the full startup process of the PHP and MySQL services.
Make sure that you can reach http://localhost/inventory-management-system/ and it works correctly
To run the test cases, make sure that you have installed the Mozilla Firefox browser and Java (at least 1.8), then run
```sh
./run_all_tests.sh
```
The tests are designed to work on an initially clean database, if you use the startup_website, docker based scripts then it is automatically clean.
If you want to run the tests on a different instance of the database, make sure it is clean, with the database store populated by the store.sql tables. 

###### Additional information:
If the scripts don't work, 
```docker-compose up mysql_db web_original|web_patched ``` in the /inventory-management-system/ directory to startup the original or patched web app.
```./gradlew --rerun-tasks check ``` in the /test_cases/ directory to run the test cases.

The tests and the scripts have been tested on Ubuntu 18 and Windows 10 machines

A brief textual description of the proof of concept exploits is added as a comment on top of the related test case, in the java source code.



