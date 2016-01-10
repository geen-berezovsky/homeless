-- This patch should be applied to production after integration

ALTER TABLE `RecievedService` CHANGE COLUMN `id` `id` INT(11) NOT NULL AUTO_INCREMENT  ;
update Client set nightStay=1 where nightStay is null;
ALTER TABLE ServContract add documentId int(11);

CREATE TABLE `Room` (  `id` INT NOT NULL AUTO_INCREMENT,  `roomnumber` VARCHAR(20) NULL,  `roommaxlivers` INT(5) NULL,  `roomnotes` TEXT NULL,  `currentnumoflivers` INT(5) NULL,  PRIMARY KEY (`id`));

INSERT INTO `Room` (`id`, `roomnumber`, `roommaxlivers`) VALUES ('1', '1.1', '8');
INSERT INTO `Room` (`id`, `roomnumber`, `roommaxlivers`) VALUES ('2', '1.2', '10');
INSERT INTO `Room` (`id`, `roomnumber`, `roommaxlivers`) VALUES ('3', '1.3', '11');
INSERT INTO `Room` (`id`, `roomnumber`, `roommaxlivers`) VALUES ('4', '2.3', '12');
INSERT INTO `Room` (`id`, `roomnumber`, `roommaxlivers`) VALUES ('5', '2.2', '10');

CREATE TABLE `CustomDocumentRegistry` (
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

CREATE TABLE `ZAGSRequestDocumentRegistry` (
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


CREATE TABLE `BasicDocumentRegistryType` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `caption` VARCHAR(255) NULL,
  PRIMARY KEY (`id`))
  ENGINE=InnoDB
  DEFAULT CHARACTER SET = utf8
  COLLATE = utf8_general_ci;



CREATE TABLE `BasicDocumentRegistry` (
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

ALTER TABLE `RecievedService`
ADD COLUMN `cash` INT(11) NULL AFTER `worker`,
ADD COLUMN `comment` TEXT NULL AFTER `cash`;

ALTER TABLE `ServicesType`
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
update `Document` set doctype = 13 where doctype=25;
delete from DocType where id=25;

update `Document` set doctype = 13 where doctype=26;
delete from DocType where id=26;

update `Document` set doctype = 8 where doctype=22;
delete from DocType where id=22;

-- HS-10
ALTER TABLE `Document`
ADD COLUMN `tempRegDateFrom` DATETIME NULL DEFAULT NULL AFTER `worker`,
ADD COLUMN `tempRegDateTo` DATETIME NULL DEFAULT NULL AFTER `tempRegDateFrom`;

ALTER TABLE `Worker`
ADD COLUMN `primefacesskin` VARCHAR(60) NULL DEFAULT NULL AFTER `rules`;

INSERT INTO `ContractPoints` (`id`, `audience`, `caption`) VALUES ('24', '0', 'Восстановление документов об образовании');
INSERT INTO `ContractPoints` (`id`, `audience`, `caption`) VALUES ('25', '0', 'Получение повторного свидетельства о рождении (или получение документов ЗАГС)');
INSERT INTO `ContractPoints` (`id`, `audience`, `caption`) VALUES ('26', '0', 'Получение/восстановление военного билета');
INSERT INTO `ContractPoints` (`id`, `audience`, `caption`) VALUES ('27', '0', 'Получение загранпаспорта');
INSERT INTO `ContractPoints` (`id`, `audience`, `caption`) VALUES ('28', '0', 'Получение технических средств реабилитации (протезно-ортопедических изделий)');

-- Fix doctype for all Workers
create temporary table UPD_PASS (select doc.id from Document doc join Worker w on doc.worker = w.id);
update Document set doctype = 1 where id in (select id from UPD_PASS);
drop table UPD_PASS;

update `Worker` set password = sha1(password);

-- Implementing client's death registration
ALTER TABLE `homeless`.`Client`
ADD COLUMN `deathDate` DATETIME NULL DEFAULT NULL AFTER `nightStay`,
ADD COLUMN `deathReason` VARCHAR(255) NULL DEFAULT NULL AFTER `deathDate`,
ADD COLUMN `deathCity` VARCHAR(255) NULL DEFAULT NULL AFTER `deathReason`,
ADD COLUMN `deathDocPath` TINYTEXT NULL DEFAULT NULL AFTER `deathCity`;

update Rules set caption = 'Председатель' where id=1;

CREATE TABLE `homeless`.`DocumentScan` (
  `id` INT(11) NOT NULL AUTO_INCREMENT,
  `doctype` INT(11) NULL,
  `path` TEXT NULL,
  `uploadingDate` DATETIME NULL,
  `comments` TEXT NULL,
  `client` INT(11) NULL,
  `worker` INT(11) NULL,
  PRIMARY KEY (`id`),
  INDEX `SCAN_CLIENT_idx` (`client` ASC),
  INDEX `SCAN_DOCTYPE_idx` (`doctype` ASC),
  INDEX `SCAN_WORKER_idx` (`worker` ASC),
  CONSTRAINT `SCAN_CLIENT`
  FOREIGN KEY (`client`)
  REFERENCES `homeless`.`Client` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `SCAN_DOCTYPE`
  FOREIGN KEY (`doctype`)
  REFERENCES `homeless`.`DocType` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `SCAN_WORKER`
  FOREIGN KEY (`worker`)
  REFERENCES `homeless`.`Worker` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION);

ALTER TABLE `homeless`.`Client` DROP COLUMN `deathDocPath`; -- REMOVING UNNECESSARY COLUMN, THAT ADDED BEFORE
insert into DocType(addressProof, audience, birthProof, caption, photoProof) values (1,10,1,'Свидетельство о смерти',0);

-- Changes in the base form
update Breadwinner set caption='Собирательство' where id=7;
insert into Breadwinner (audience,caption) values (0,'Социальные пособия');
insert into Breadwinner (audience,caption) values (0,'Помощь благ-х организаций');
insert into Breadwinner (audience,caption) values (0,'Ребцентры');
insert into Breadwinner (audience,caption) values (0,'Помощь церкви');

update ReasonOfHomeless set caption='Мошенничество/Вымогательство' where id=1;
update ReasonOfHomeless set caption='Осуждение к лишению свободы' where id=2;
update ReasonOfHomeless set caption='Вынужденный переселенец' where id=4;
update ReasonOfHomeless set caption='Выселение из служебного жилья' where id=5;
update ReasonOfHomeless set caption='Беспричинно потянуло странствовать' where id=7;
insert into ReasonOfHomeless (caption) values ('Взыскание жилья за долги');
insert into ReasonOfHomeless (caption) values ('Конфликт с соседями');
insert into ReasonOfHomeless (caption) values ('Продал и пропил');

CREATE TABLE `homeless`.`Region` (
  `id` INT(11) NOT NULL AUTO_INCREMENT,
  `caption` VARCHAR(255) NULL,
  PRIMARY KEY (`id`));

CREATE TABLE `homeless`.`SubRegion` (
  `id` INT(11) NOT NULL AUTO_INCREMENT,
  `region` INT(11) NULL,
  `caption` VARCHAR(255) NULL,
  PRIMARY KEY (`id`),
  INDEX `region_idx` (`region` ASC),
  CONSTRAINT `region_subregion`
  FOREIGN KEY (`region`)
  REFERENCES `homeless`.`Region` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION);



insert into Region (caption) values ('Неизвестно');
insert into Region (caption) values ('Санкт-Петербург');
insert into Region (caption) values ('Ленинградская область');

insert into SubRegion(region,caption) values (1,'Неизвестно');
insert into SubRegion(region,caption) values (2,'Адмиралтейский');
insert into SubRegion(region,caption) values (2,'Василеостровский');
insert into SubRegion(region,caption) values (2,'Выборгский');
insert into SubRegion(region,caption) values (2,'Калининский');
insert into SubRegion(region,caption) values (2,'Кировский');
insert into SubRegion(region,caption) values (2,'Колпинский');
insert into SubRegion(region,caption) values (2,'Красногвардейский');
insert into SubRegion(region,caption) values (2,'Красносельский');
insert into SubRegion(region,caption) values (2,'Кронштадтский');
insert into SubRegion(region,caption) values (2,'Курортный');
insert into SubRegion(region,caption) values (2,'Московский');
insert into SubRegion(region,caption) values (2,'Невский');
insert into SubRegion(region,caption) values (2,'Петроградский');
insert into SubRegion(region,caption) values (2,'Петродворцовый');
insert into SubRegion(region,caption) values (2,'Приморский');
insert into SubRegion(region,caption) values (2,'Пушкинский');
insert into SubRegion(region,caption) values (2,'Фрунзенский');
insert into SubRegion(region,caption) values (2,'Центральный');
insert into SubRegion(region,caption) values (3,'Бокситогорский');
insert into SubRegion(region,caption) values (3,'Волосовский');
insert into SubRegion(region,caption) values (3,'Волховский');
insert into SubRegion(region,caption) values (3,'Всеволожский');
insert into SubRegion(region,caption) values (3,'Выборгский');
insert into SubRegion(region,caption) values (3,'Гатчинский');
insert into SubRegion(region,caption) values (3,'Кингисеппский');
insert into SubRegion(region,caption) values (3,'Киришский');
insert into SubRegion(region,caption) values (3,'Кировский');
insert into SubRegion(region,caption) values (3,'Лодейнопольский');
insert into SubRegion(region,caption) values (3,'Ломоносовский');
insert into SubRegion(region,caption) values (3,'Лужский');
insert into SubRegion(region,caption) values (3,'Подпорожский');
insert into SubRegion(region,caption) values (3,'Приозерский');
insert into SubRegion(region,caption) values (3,'Сланцевский');
insert into SubRegion(region,caption) values (3,'Тихвинский');
insert into SubRegion(region,caption) values (3,'Тосненский');

ALTER TABLE `homeless`.`Client`
ADD COLUMN `lastLiving` INT(11) NULL DEFAULT 1 AFTER `deathCity`,
ADD COLUMN `lastRegistration` INT(11) NULL DEFAULT 1 AFTER `lastLiving`,
ADD INDEX `lastLiving_SubRegion_idx` (`lastLiving` ASC),
ADD INDEX `lastRegistration_SubRegion_idx` (`lastRegistration` ASC);
ALTER TABLE `homeless`.`Client`
ADD CONSTRAINT `lastLiving_SubRegion`
FOREIGN KEY (`lastLiving`)
REFERENCES `homeless`.`SubRegion` (`id`)
  ON DELETE NO ACTION
  ON UPDATE NO ACTION,
ADD CONSTRAINT `lastRegistration_SubRegion`
FOREIGN KEY (`lastRegistration`)
REFERENCES `homeless`.`SubRegion` (`id`)
  ON DELETE NO ACTION
  ON UPDATE NO ACTION;


-- ************************

ALTER TABLE `homeless`.`Region`
ADD COLUMN `abbreviation` VARCHAR(10) NULL DEFAULT NULL AFTER `caption`;

update Region set abbreviation = '?' where id=1;
update Region set abbreviation = 'СПб' where id=2;
update Region set abbreviation = 'ЛО' where id=3;

ALTER TABLE `homeless`.`Client`
ADD COLUMN `hasNotice` TINYINT(1) NULL DEFAULT '0' AFTER `lastRegistration`;

-- careful deleting breadwinner with = 8
CREATE TEMPORARY TABLE new_tbl AS select c.clients_id clients_id from link_breadwinner_client c
where c.breadwinners_id = 8 and NOT exists (select clients_id d from link_breadwinner_client d
where d.breadwinners_id = 7
      and d.clients_id = c.clients_id);

update link_breadwinner_client set breadwinners_id = 7 where breadwinners_id = 8 and clients_id in (select clients_id from new_tbl);

delete from link_breadwinner_client where breadwinners_id = 8;

delete from Breadwinner where id=8;

drop temporary table new_tbl;
-- ***

-- marking fired workers for removing them from login data and keeping their changes in database
ALTER TABLE `homeless`.`Worker`
ADD COLUMN `fired` TINYINT(1) NULL DEFAULT '0' AFTER `primefacesskin`;

update Worker set fired=1 where (id=4 or id=5 or id=3);
-- ***

insert into Rules (id, caption) values (7,'Руководитель консультационной службы');
update Worker set rules = 7 where id=6;
update Worker set middlename='Руслановна' where id=12;
update Worker set middlename='Дмитриевна' where id=9;
