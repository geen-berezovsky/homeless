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


CREATE TABLE `homeless`.`BasicDocumentRegistryType` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `caption` VARCHAR(255) NULL,
  PRIMARY KEY (`id`))
  ENGINE=InnoDB
  DEFAULT CHARACTER SET = utf8
  COLLATE = utf8_general_ci;



CREATE TABLE `homeless`.`BasicDocumentRegistry` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `type` INT(11) NULL,
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

alter table BasicDocumentRegistry add
constraint FK_BasicDocumentRegistry_type
foreign key (`type`)
references `BasicDocumentRegistryType`(`id`);

ALTER TABLE BasicDocumentRegistryType AUTO_INCREMENT = 1;

insert into BasicDocumentRegistryType (`caption`) values ('Справка о регистрации');
insert into BasicDocumentRegistryType (`caption`) values ('Направление в диспансер');
insert into BasicDocumentRegistryType (`caption`) values ('Справка о социальной помощи');
insert into BasicDocumentRegistryType (`caption`) values ('Направление на санобработку');
insert into BasicDocumentRegistryType (`caption`) values ('Справка для проезда');
insert into BasicDocumentRegistryType (`caption`) values ('Транзит');

-- remove паспорт1, паспорт2 etc
update Document set doctype = 1 where ((doctype = 15) OR (doctype = 16) OR (doctype = 17) OR (doctype = 18) OR (doctype = 19));
delete from DocType where ((id = 15) or (id = 16) or (id = 17) or (id = 18) or (id = 19));

-- remove 'Неизвестно' from ServicesType
set foreign_key_checks = 0;
delete from ServicesType where id = 20;
set foreign_key_checks = 1;
