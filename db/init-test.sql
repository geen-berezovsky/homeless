-- This script will reload initial data to the test schema at localhost
drop database if exists `homeless_test`;
create database `homeless_test` character set utf8 collate utf8_general_ci;
drop user 'homeless_test'@'localhost';
create user 'homeless_test'@'localhost' identified by 'homeless_test';
grant all privileges on `homeless_test`.* to 'homeless_test'@'localhost';
flush privileges;

