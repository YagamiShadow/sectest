FROM mysql:5.7

# prepare the database
COPY sql/01_store.sql /docker-entrypoint-initdb.d/01_store.sql
COPY sql/02_user.sql /docker-entrypoint-initdb.d/02_user.sql

