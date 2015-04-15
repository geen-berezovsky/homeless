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
  `docNum` VARCHAR(45) NULL,
  `client` INT(11) NULL,
  `documentId` INT(11) NULL,
  `dateFrom` DATETIME NULL,
  `dateTill` DATETIME NULL,
  `performerId` INT(11) NULL,
  `date` DATETIME NULL,
  `travelCity` VARCHAR(255) NULL,
  PRIMARY KEY (`id`))
  ENGINE=InnoDB
  DEFAULT CHARACTER SET = utf8
  COLLATE = utf8_general_ci;

alter table BasicDocumentRegistry add
constraint FK_BasicDocumentRegistry_type
foreign key (`type`)
references `BasicDocumentRegistryType`(`id`);

ALTER TABLE BasicDocumentRegistryType AUTO_INCREMENT = 1;

insert into BasicDocumentRegistryType (`id`, `caption`) values (11, 'Справка о регистрации');
insert into BasicDocumentRegistryType (`id`, `caption`) values (14,'Направление в диспансер');
insert into BasicDocumentRegistryType (`id`, `caption`) values (15, 'Справка о социальной помощи');
insert into BasicDocumentRegistryType (`id`, `caption`) values (12, 'Направление на санобработку');
insert into BasicDocumentRegistryType (`id`, `caption`) values (13, 'Справка для проезда');
insert into BasicDocumentRegistryType (`id`, `caption`) values (16, 'Транзит');
insert into BasicDocumentRegistryType (`id`, `caption`) values (20, 'Неизвестно');

-- remove паспорт1, паспорт2 etc
update Document set doctype = 1 where ((doctype = 15) OR (doctype = 16) OR (doctype = 17) OR (doctype = 18) OR (doctype = 19));
delete from DocType where ((id = 15) or (id = 16) or (id = 17) or (id = 18) or (id = 19));

-- remove 'Неизвестно' from ServicesType
set foreign_key_checks = 0;
delete from ServicesType where id = 20;
set foreign_key_checks = 1;

ALTER TABLE `homeless`.`RecievedService`
ADD COLUMN `cash` INT(11) NULL AFTER `worker`,
ADD COLUMN `comment` TEXT NULL AFTER `cash`;

ALTER TABLE `homeless`.`ServicesType`
ADD COLUMN `money` TINYINT(1) NULL DEFAULT 0 AFTER `caption`,
ADD COLUMN `document` TINYINT(1) NULL DEFAULT 0 AFTER `money`;

update ServicesType set document = 1 where id = 11 or id =12 or id =13 or id = 14 or id =15 or id=100;

update ServicesType set money = 1 where id = 6;
insert into ServicesType (id, caption, money) values (16, 'Изготовление фотографий', 0);
insert into ServicesType (id, caption, money) values (17, 'Написание заявлений/запросов', 0);
insert into ServicesType (id, caption, money) values (18, 'Оплата проезда', 1);
insert into ServicesType (id, caption, money) values (19, 'Консультация психолога', 0);
insert into ServicesType (id, caption, money) values (20, 'Оплата пошлины', 1);

-- NOTE!!! DELETE THE TABLE GivenCertificate after its migration

-- HS-4
update document set doctype = 13 where doctype=25;
delete from DocType where id=25;

update document set doctype = 13 where doctype=26;
delete from DocType where id=26;

update document set doctype = 8 where doctype=22;
delete from DocType where id=22;

-- HS-10
ALTER TABLE `homeless`.`Document`
ADD COLUMN `tempRegDateFrom` DATETIME NULL DEFAULT NULL AFTER `worker`,
ADD COLUMN `tempRegDateTo` DATETIME NULL DEFAULT NULL AFTER `tempRegDateFrom`;


