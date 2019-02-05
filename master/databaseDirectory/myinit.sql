CREATE DATABASE CloudProjectMaster ;
CREATE USER 'cloudProjectMasterUser'@'%' IDENTIFIED BY '12345';
GRANT ALL PRIVILEGES ON CloudProjectMaster.* TO 'cloudProjectMasterUser'@'%';
