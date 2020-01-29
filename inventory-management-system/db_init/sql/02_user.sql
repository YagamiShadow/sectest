CREATE USER 'test_ims'@'%' IDENTIFIED BY 'test_ims';
GRANT ALL ON store.* TO 'test_ims'@'%';
FLUSH PRIVILEGES;
