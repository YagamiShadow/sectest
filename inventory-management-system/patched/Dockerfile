FROM php:7

# install needed packages to run the application
RUN docker-php-ext-install mysqli
RUN docker-php-ext-enable mysqli

# add target application
ADD ./ims /ims

# target php application
EXPOSE 80
ENTRYPOINT ["php", "-S", "0.0.0.0:80", "-t", "/ims"]