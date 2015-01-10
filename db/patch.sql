-- This patch should be applied to production after integration

ALTER TABLE `homeless`.`RecievedService` CHANGE COLUMN `id` `id` INT(11) NOT NULL AUTO_INCREMENT  ;
update Client set nightStay=1 where nightStay is null;
ALTER TABLE ServContract add documentId int(11);

CREATE TABLE `homeless`.`Room` (  `id` INT NOT NULL AUTO_INCREMENT,  `roomnumber` VARCHAR(20) NULL,  `roommaxlivers` INT(5) NULL,  `roomnotes` TEXT NULL,  `currentnumoflivers` INT(5) NULL,  PRIMARY KEY (`id`));

INSERT INTO `homeless`.`Room` (`id`, `roomnumber`, `roommaxlivers`) VALUES ('1', '1.1', '8');
INSERT INTO `homeless`.`Room` (`id`, `roomnumber`, `roommaxlivers`) VALUES ('2', '1.2', '10');
INSERT INTO `homeless`.`Room` (`id`, `roomnumber`, `roommaxlivers`) VALUES ('3', '1.3', '11');
INSERT INTO `homeless`.`Room` (`id`, `roomnumber`, `roommaxlivers`) VALUES ('4', '2.3', '12');
INSERT INTO `homeless`.`Room` (`id`, `roomnumber`, `roommaxlivers`) VALUES ('5', '2.2', '10');

CREATE TABLE `homeless`.`CustomDocumentRegistry` (
  `id` INT(11) NOT NULL AUTO_INCREMENT,
  `client` INT(11) NULL,
  `docNum` VARCHAR(255) NULL,
  `type` TEXT NULL,
  `preamble` TEXT NULL,
  `mainPart` TEXT NULL,
  `finalPart` TEXT NULL,
  `forWhom` TEXT NULL,
  `signature` TEXT NULL,
  `performerText` TEXT NULL,
  `performerId` INT(11) NULL,
  `date` DATETIME NULL,
  PRIMARY KEY (`id`))
  ENGINE=InnoDB
DEFAULT CHARACTER SET = utf8
COLLATE = utf8_general_ci;

CREATE TABLE `homeless`.`ZAGSRequestDocumentRegistry` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `client` INT(11) NULL,
  `forWhom` TEXT NULL,
  `name` TEXT NULL,
  `whereWasBorn` TEXT NULL,
  `address` TEXT NULL,
  `mother` TEXT NULL,
  `father` TEXT NULL,
  `performerId` INT(11) NULL,
  `date` DATETIME NULL,
  PRIMARY KEY (`id`))
  ENGINE=InnoDB
DEFAULT CHARACTER SET = utf8
COLLATE = utf8_general_ci;

CREATE TABLE `homeless`.`RegistrationDocumentRegistry` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `client` INT(11) NULL,
  `documentId` INT(11) NULL,
  `dateFrom` DATETIME NULL,
  `dateTill` DATETIME NULL,
  `performerId` INT(11) NULL,
  `date` DATETIME NULL,
  PRIMARY KEY (`id`))
  ENGINE=InnoDB
  DEFAULT CHARACTER SET = utf8
  COLLATE = utf8_general_ci;

