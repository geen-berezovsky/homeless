select 1
ALTER TABLE `homeless`.`ServContract` CHANGE COLUMN `commentResult` `commentResult` TEXT NULL DEFAULT NULL ;
ALTER TABLE `homeless`.`ShelterHistory` ADD COLUMN `comments` MEDIUMTEXT NULL AFTER `servContract`;
ALTER TABLE `homeless`.`ZAGSRequestDocumentRegistry` CHANGE COLUMN `name` `respAddress` TEXT NULL DEFAULT NULL ;


