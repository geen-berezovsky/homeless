-- MySQL dump 10.13  Distrib 5.6.17, for Win64 (x86_64)
--
-- Host: localhost    Database: homeless
-- ------------------------------------------------------
-- Server version	5.6.23-log

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `BasicDocumentRegistry`
--

DROP TABLE IF EXISTS `BasicDocumentRegistry`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `BasicDocumentRegistry` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `type` int(11) DEFAULT NULL,
  `docNum` varchar(45) DEFAULT NULL,
  `client` int(11) DEFAULT NULL,
  `documentId` int(11) DEFAULT NULL,
  `dateFrom` datetime DEFAULT NULL,
  `dateTill` datetime DEFAULT NULL,
  `performerId` int(11) DEFAULT NULL,
  `date` datetime DEFAULT NULL,
  `travelCity` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK_BasicDocumentRegistry_type` (`type`),
  CONSTRAINT `FK_BasicDocumentRegistry_type` FOREIGN KEY (`type`) REFERENCES `BasicDocumentRegistryType` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=14587 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `BasicDocumentRegistry`
--

LOCK TABLES `BasicDocumentRegistry` WRITE;
/*!40000 ALTER TABLE `BasicDocumentRegistry` DISABLE KEYS */;
/*!40000 ALTER TABLE `BasicDocumentRegistry` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `BasicDocumentRegistryType`
--

DROP TABLE IF EXISTS `BasicDocumentRegistryType`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `BasicDocumentRegistryType` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `caption` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=21 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `BasicDocumentRegistryType`
--

LOCK TABLES `BasicDocumentRegistryType` WRITE;
/*!40000 ALTER TABLE `BasicDocumentRegistryType` DISABLE KEYS */;
INSERT INTO `BasicDocumentRegistryType` VALUES (11,'Справка о регистрации'),(12,'Направление на санобработку'),(13,'Справка для проезда'),(14,'Направление в диспансер'),(15,'Справка о социальной помощи'),(16,'Транзит'),(20,'Неизвестно');
/*!40000 ALTER TABLE `BasicDocumentRegistryType` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `Breadwinner`
--

DROP TABLE IF EXISTS `Breadwinner`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `Breadwinner` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `audience` int(11) NOT NULL,
  `caption` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=13 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `Breadwinner`
--

LOCK TABLES `Breadwinner` WRITE;
/*!40000 ALTER TABLE `Breadwinner` DISABLE KEYS */;
INSERT INTO `Breadwinner` VALUES (1,1,'Постоянная работа'),(2,2,'Временная работа'),(3,2,'Пенсия'),(4,2,'Попрошайничество'),(5,0,'Помощь близких'),(6,0,'Другие'),(7,0,'Собирательство'),(9,0,'Социальные пособия'),(10,0,'Помощь благ-х организаций'),(11,0,'Ребцентры'),(12,0,'Помощь церкви');
/*!40000 ALTER TABLE `Breadwinner` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `ChronicDisease`
--

DROP TABLE IF EXISTS `ChronicDisease`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `ChronicDisease` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `caption` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `ChronicDisease`
--

LOCK TABLES `ChronicDisease` WRITE;
/*!40000 ALTER TABLE `ChronicDisease` DISABLE KEYS */;
INSERT INTO `ChronicDisease` VALUES (1,'Гепатиты В, С'),(2,'ВИЧ'),(3,'Психиатрия'),(4,'Туберкулез'),(5,'Другие');
/*!40000 ALTER TABLE `ChronicDisease` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `Client`
--

DROP TABLE IF EXISTS `Client`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `Client` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `avatar` longblob,
  `contacts` longtext,
  `date` datetime DEFAULT NULL,
  `dependents` int(11) NOT NULL,
  `firstname` varchar(255) DEFAULT NULL,
  `gender` tinyint(1) NOT NULL,
  `homelessdate` datetime DEFAULT NULL,
  `isStudent` int(11) NOT NULL,
  `liveInFlat` int(11) NOT NULL,
  `martialStatus` int(11) NOT NULL,
  `memo` longtext,
  `middlename` varchar(255) DEFAULT NULL,
  `photoCheckSum` varchar(255) DEFAULT NULL,
  `photoName` varchar(255) DEFAULT NULL,
  `profession` varchar(255) DEFAULT NULL,
  `regDate` datetime DEFAULT NULL,
  `surname` varchar(255) DEFAULT NULL,
  `uniqBreadwinner` varchar(255) DEFAULT NULL,
  `uniqDisease` varchar(255) DEFAULT NULL,
  `uniqReason` varchar(255) DEFAULT NULL,
  `whereWasBorn` varchar(255) DEFAULT NULL,
  `education` int(11) DEFAULT NULL,
  `familycommunication` int(11) DEFAULT NULL,
  `nightStay` int(11) DEFAULT NULL,
  `deathDate` datetime DEFAULT NULL,
  `deathReason` varchar(255) DEFAULT NULL,
  `deathCity` varchar(255) DEFAULT NULL,
  `lastLiving` int(11) DEFAULT '1',
  `lastRegistration` int(11) DEFAULT '1',
  `hasNotice` tinyint(1) DEFAULT '0',
  PRIMARY KEY (`id`),
  KEY `FK7877DFEB4D3B23C6` (`education`),
  KEY `FK7877DFEB12C10578` (`nightStay`),
  KEY `FK7877DFEB2C9716BA` (`familycommunication`),
  KEY `lastLiving_SubRegion_idx` (`lastLiving`),
  KEY `lastRegistration_SubRegion_idx` (`lastRegistration`),
  CONSTRAINT `FK7877DFEB12C10578` FOREIGN KEY (`nightStay`) REFERENCES `NightStay` (`id`),
  CONSTRAINT `FK7877DFEB2C9716BA` FOREIGN KEY (`familycommunication`) REFERENCES `FamilyCommunication` (`id`),
  CONSTRAINT `FK7877DFEB4D3B23C6` FOREIGN KEY (`education`) REFERENCES `Education` (`id`),
  CONSTRAINT `lastLiving_SubRegion` FOREIGN KEY (`lastLiving`) REFERENCES `SubRegion` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `lastRegistration_SubRegion` FOREIGN KEY (`lastRegistration`) REFERENCES `SubRegion` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=16594 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `Client`
--

LOCK TABLES `Client` WRITE;
/*!40000 ALTER TABLE `Client` DISABLE KEYS */;
/*!40000 ALTER TABLE `Client` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `ContractControl`
--

DROP TABLE IF EXISTS `ContractControl`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `ContractControl` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `comments` longtext,
  `endDate` datetime DEFAULT NULL,
  `servcontract` int(11) DEFAULT NULL,
  `contractpoints` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK4E7DABCB9C72A254` (`contractpoints`),
  KEY `FK4E7DABCB62D40A3A` (`servcontract`),
  CONSTRAINT `FK4E7DABCB62D40A3A` FOREIGN KEY (`servcontract`) REFERENCES `ServContract` (`id`),
  CONSTRAINT `FK4E7DABCB9C72A254` FOREIGN KEY (`contractpoints`) REFERENCES `ContractPoints` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=33027 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `ContractControl`
--

LOCK TABLES `ContractControl` WRITE;
/*!40000 ALTER TABLE `ContractControl` DISABLE KEYS */;
/*!40000 ALTER TABLE `ContractControl` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `ContractPoints`
--

DROP TABLE IF EXISTS `ContractPoints`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `ContractPoints` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `audience` int(11) NOT NULL,
  `caption` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=29 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `ContractPoints`
--

LOCK TABLES `ContractPoints` WRITE;
/*!40000 ALTER TABLE `ContractPoints` DISABLE KEYS */;
INSERT INTO `ContractPoints` VALUES (1,1,'Восстановление, получение  паспорта'),(2,0,'Оформление пенсии'),(3,0,'Получение полиса ОМС'),(4,1,'Восстановление, получение ИНН'),(5,1,'Получение документов, подтверждающих трудовой стаж'),(6,0,'Оформление инвалидности'),(7,0,'Устройство в интернат'),(8,0,'Оформление временной регистрации по адресу Ночлежки'),(9,0,'Помощь в оформлении регистрации по другому адресу'),(10,1,'Помощь в трудоустройстве'),(11,1,'Помощь в трудоустройстве с проживанием'),(12,1,'Устройство в городской дом ночного пребывания'),(13,0,'Устройство в реабилитационные центры для алко- и наркозависимых'),(14,0,'Получение лечения, устройство в больницу'),(15,0,'Покупка лекарств (перенесено в разовые услуги)'),(16,0,'Поиск родственников'),(17,0,'Возвращение домой в СПб или другой город'),(18,0,'Восстановление или установление гражданства'),(19,0,'Юридическое сопровождение в суде'),(20,0,'Получение жилья'),(21,0,'Отмена мошеннической сделки и возврат жилья'),(22,0,'Подготовка запросов и(или) заявлений'),(23,1,'Реабилитационная программа Дом на пол дороги'),(24,0,'Восстановление документов об образовании'),(25,0,'Получение повторного свидетельства о рождении (или получение документов ЗАГС)'),(26,0,'Получение/восстановление военного билета'),(27,0,'Получение загранпаспорта'),(28,0,'Получение технических средств реабилитации (протезно-ортопедических изделий)');
/*!40000 ALTER TABLE `ContractPoints` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `ContractResult`
--

DROP TABLE IF EXISTS `ContractResult`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `ContractResult` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `caption` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=9 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `ContractResult`
--

LOCK TABLES `ContractResult` WRITE;
/*!40000 ALTER TABLE `ContractResult` DISABLE KEYS */;
INSERT INTO `ContractResult` VALUES (1,'В процессе выполнения'),(2,'Выполнен полностью'),(3,'Выполнен частично по причине отказа клиента'),(4,'Выполнен частично по другим причинам'),(5,'Не выполнен по причине отказа клиента'),(6,'Не выполнен по другим причинам'),(7,'Выполнен частично по причине неявки клиента'),(8,'Не выполнен по причине неявки клиента');
/*!40000 ALTER TABLE `ContractResult` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `CustomDocumentRegistry`
--

DROP TABLE IF EXISTS `CustomDocumentRegistry`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `CustomDocumentRegistry` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `client` int(11) DEFAULT NULL,
  `docNum` varchar(255) DEFAULT NULL,
  `type` text,
  `preamble` text,
  `mainPart` text,
  `finalPart` text,
  `forWhom` text,
  `signature` text,
  `performerText` text,
  `performerId` int(11) DEFAULT NULL,
  `date` datetime DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `CustomDocumentRegistry`
--

LOCK TABLES `CustomDocumentRegistry` WRITE;
/*!40000 ALTER TABLE `CustomDocumentRegistry` DISABLE KEYS */;
/*!40000 ALTER TABLE `CustomDocumentRegistry` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `DocCliche`
--

DROP TABLE IF EXISTS `DocCliche`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `DocCliche` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `audience` int(11) NOT NULL,
  `caption` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=11 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `DocCliche`
--

LOCK TABLES `DocCliche` WRITE;
/*!40000 ALTER TABLE `DocCliche` DISABLE KEYS */;
INSERT INTO `DocCliche` VALUES (1,0,'СПб БОО «Ночлежка» занимается оказанием социально-юридической помощи лицам БОМЖ.'),(2,0,'СПб БОО «Ночлежка» занимается оказанием медико-социальной и юридической помощи лицам без определенного места жительства, а также гражданам, оказавшимся в сложной жизненной ситуации.'),(3,1,'Убедительно просим Вас не остаться безучастными к человеку, оказавшемуся в сложной жизненной ситуации. '),(4,1,'В случае невозможности предоставить данную информацию просим сообщить о порядке ее получения. '),(5,1,'Ответ просим дополнительно направить по факсу. '),(6,1,'Благодарим за сотрудничество и понимание. '),(7,2,'Заявление'),(8,2,'Жалоба'),(9,2,'Запрос'),(10,2,'Благодарность');
/*!40000 ALTER TABLE `DocCliche` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `DocType`
--

DROP TABLE IF EXISTS `DocType`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `DocType` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `addressProof` tinyint(1) NOT NULL,
  `audience` int(11) NOT NULL,
  `birthProof` tinyint(1) NOT NULL,
  `caption` varchar(255) DEFAULT NULL,
  `photoProof` tinyint(1) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=34 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `DocType`
--

LOCK TABLES `DocType` WRITE;
/*!40000 ALTER TABLE `DocType` DISABLE KEYS */;
INSERT INTO `DocType` VALUES (1,1,1,1,'Паспорт',1),(2,0,1,1,'Военный билет',1),(3,0,0,1,'Свидетельство о рождении',0),(4,1,0,1,'Свид. о рег. по Ф8',0),(5,0,1,1,'Временное удостоверение личности',1),(6,0,1,1,'Паспорт иностранного гражданина',1),(7,0,1,1,'Водительские права',1),(8,0,1,1,'Справка об освобождении из МЛС',1),(9,0,1,1,'Разрешение на временное проживание',1),(10,0,1,1,'Вид на жительство',1),(11,1,1,1,'Свид. о рег. по Ф9',0),(12,0,0,0,'???',10),(13,0,10,0,'Ходатайство',0),(14,1,10,0,'Ходатайство Центра по профилактике и борьбе со СПИД и инфекционными заболеваниями',0),(20,0,10,0,'Пропуск',1),(21,1,10,0,'Паспорт родителя',0),(23,1,10,0,'СО СЛОВ',0),(24,0,10,1,'Свидетельство на возвращение',1),(27,1,10,1,'Ходатайство от приюта \"Транзит\" ',0),(28,1,10,1,'Справка МВД Республики Беларусь',1),(29,1,10,1,'Загранпаспорт РФ',1),(30,1,10,1,'Справка УФМС об установлении личности',1),(31,1,10,0,'паспорт СССР',0),(32,1,10,1,'справка',0),(33,1,10,1,'Свидетельство о смерти',0);
/*!40000 ALTER TABLE `DocType` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `Document`
--

DROP TABLE IF EXISTS `Document`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `Document` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `address` varchar(255) DEFAULT NULL,
  `city` varchar(255) DEFAULT NULL,
  `date` datetime DEFAULT NULL,
  `docNum` varchar(255) DEFAULT NULL,
  `docPrefix` varchar(255) DEFAULT NULL,
  `registration` int(11) NOT NULL,
  `whereAndWhom` varchar(255) DEFAULT NULL,
  `client` int(11) DEFAULT NULL,
  `doctype` int(11) DEFAULT NULL,
  `worker` int(11) DEFAULT NULL,
  `tempRegDateFrom` datetime DEFAULT NULL,
  `tempRegDateTo` datetime DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK3737353BCB1DEFA` (`doctype`),
  KEY `FK3737353BA4679646` (`worker`),
  KEY `FK3737353B5FC8D6E0` (`client`),
  CONSTRAINT `FK3737353B5FC8D6E0` FOREIGN KEY (`client`) REFERENCES `Client` (`id`),
  CONSTRAINT `FK3737353BA4679646` FOREIGN KEY (`worker`) REFERENCES `Worker` (`id`),
  CONSTRAINT `FK3737353BCB1DEFA` FOREIGN KEY (`doctype`) REFERENCES `DocType` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=8228 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `Document`
--

LOCK TABLES `Document` WRITE;
/*!40000 ALTER TABLE `Document` DISABLE KEYS */;
/*!40000 ALTER TABLE `Document` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `DocumentScan`
--

DROP TABLE IF EXISTS `DocumentScan`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `DocumentScan` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `doctype` int(11) DEFAULT NULL,
  `path` text,
  `uploadingDate` datetime DEFAULT NULL,
  `comments` text,
  `client` int(11) DEFAULT NULL,
  `worker` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `SCAN_CLIENT_idx` (`client`),
  KEY `SCAN_DOCTYPE_idx` (`doctype`),
  KEY `SCAN_WORKER_idx` (`worker`),
  CONSTRAINT `SCAN_CLIENT` FOREIGN KEY (`client`) REFERENCES `Client` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `SCAN_DOCTYPE` FOREIGN KEY (`doctype`) REFERENCES `DocType` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `SCAN_WORKER` FOREIGN KEY (`worker`) REFERENCES `Worker` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `DocumentScan`
--

LOCK TABLES `DocumentScan` WRITE;
/*!40000 ALTER TABLE `DocumentScan` DISABLE KEYS */;
/*!40000 ALTER TABLE `DocumentScan` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `Education`
--

DROP TABLE IF EXISTS `Education`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `Education` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `audience` int(11) NOT NULL,
  `caption` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=8 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `Education`
--

LOCK TABLES `Education` WRITE;
/*!40000 ALTER TABLE `Education` DISABLE KEYS */;
INSERT INTO `Education` VALUES (1,0,'Нет ответа'),(2,0,'Нет образования'),(3,0,'Начальное'),(4,0,'Незаконченное среднее'),(5,1,'Среднее'),(6,1,'Среднее специальное'),(7,1,'Высшее, н/высшее');
/*!40000 ALTER TABLE `Education` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `FamilyCommunication`
--

DROP TABLE IF EXISTS `FamilyCommunication`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `FamilyCommunication` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `caption` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `FamilyCommunication`
--

LOCK TABLES `FamilyCommunication` WRITE;
/*!40000 ALTER TABLE `FamilyCommunication` DISABLE KEYS */;
INSERT INTO `FamilyCommunication` VALUES (1,'Нет ответа'),(2,'Видится'),(3,'Не видится'),(4,'Нет родственников');
/*!40000 ALTER TABLE `FamilyCommunication` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `NightStay`
--

DROP TABLE IF EXISTS `NightStay`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `NightStay` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `caption` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `NightStay`
--

LOCK TABLES `NightStay` WRITE;
/*!40000 ALTER TABLE `NightStay` DISABLE KEYS */;
INSERT INTO `NightStay` VALUES (1,'Нет ответа'),(2,'Улица'),(3,'Гос. ДНП'),(4,'Др. гос. учреждение'),(5,'Другое');
/*!40000 ALTER TABLE `NightStay` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `ReasonOfHomeless`
--

DROP TABLE IF EXISTS `ReasonOfHomeless`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `ReasonOfHomeless` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `caption` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=14 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `ReasonOfHomeless`
--

LOCK TABLES `ReasonOfHomeless` WRITE;
/*!40000 ALTER TABLE `ReasonOfHomeless` DISABLE KEYS */;
INSERT INTO `ReasonOfHomeless` VALUES (1,'Мошенничество/Вымогательство'),(2,'Осуждение к лишению свободы'),(3,'Семейные проблемы'),(4,'Вынужденный переселенец'),(5,'Выселение из служебного жилья'),(6,'Трудовая миграция'),(7,'Беспричинно потянуло странствовать'),(8,'Сгорела квартира (дом)'),(9,'Выпускник интерната'),(10,'Другие'),(11,'Взыскание жилья за долги'),(12,'Конфликт с соседями'),(13,'Продал и пропил');
/*!40000 ALTER TABLE `ReasonOfHomeless` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `RecievedService`
--

DROP TABLE IF EXISTS `RecievedService`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `RecievedService` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `date` datetime DEFAULT NULL,
  `client` int(11) DEFAULT NULL,
  `servicesType` int(11) DEFAULT NULL,
  `worker` int(11) DEFAULT NULL,
  `cash` int(11) DEFAULT NULL,
  `comment` text,
  PRIMARY KEY (`id`),
  KEY `FK8DD747ECA4679646` (`worker`),
  KEY `FK8DD747EC5FC8D6E0` (`client`),
  KEY `FK8DD747EC2912C51A` (`servicesType`),
  CONSTRAINT `FK8DD747EC2912C51A` FOREIGN KEY (`servicesType`) REFERENCES `ServicesType` (`id`),
  CONSTRAINT `FK8DD747EC5FC8D6E0` FOREIGN KEY (`client`) REFERENCES `Client` (`id`),
  CONSTRAINT `FK8DD747ECA4679646` FOREIGN KEY (`worker`) REFERENCES `Worker` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=33209 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `RecievedService`
--

LOCK TABLES `RecievedService` WRITE;
/*!40000 ALTER TABLE `RecievedService` DISABLE KEYS */;
/*!40000 ALTER TABLE `RecievedService` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `Region`
--

DROP TABLE IF EXISTS `Region`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `Region` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `caption` varchar(255) DEFAULT NULL,
  `abbreviation` varchar(10) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `Region`
--

LOCK TABLES `Region` WRITE;
/*!40000 ALTER TABLE `Region` DISABLE KEYS */;
INSERT INTO `Region` VALUES (1,'Неизвестно','?'),(2,'Санкт-Петербург','СПб'),(3,'Ленинградская область','ЛО');
/*!40000 ALTER TABLE `Region` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `Room`
--

DROP TABLE IF EXISTS `Room`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `Room` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `roomnumber` varchar(20) DEFAULT NULL,
  `roommaxlivers` int(5) DEFAULT NULL,
  `roomnotes` text,
  `currentnumoflivers` int(5) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `Room`
--

LOCK TABLES `Room` WRITE;
/*!40000 ALTER TABLE `Room` DISABLE KEYS */;
INSERT INTO `Room` VALUES (1,'1.1',8,NULL,0),(2,'1.2',10,NULL,0),(3,'1.3',11,NULL,0),(4,'2.3',12,NULL,0),(5,'2.2',10,NULL,0);
/*!40000 ALTER TABLE `Room` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `Rules`
--

DROP TABLE IF EXISTS `Rules`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `Rules` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `caption` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=8 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `Rules`
--

LOCK TABLES `Rules` WRITE;
/*!40000 ALTER TABLE `Rules` DISABLE KEYS */;
INSERT INTO `Rules` VALUES (1,'Председатель'),(2,'Социальный работник'),(3,'Юрист'),(4,'Помощник юриста'),(5,'Правовой консультант'),(6,'Менеджер'),(7,'Руководитель консультационной службы');
/*!40000 ALTER TABLE `Rules` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `ServContract`
--

DROP TABLE IF EXISTS `ServContract`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `ServContract` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `commentResult` varchar(255) DEFAULT NULL,
  `docNum` varchar(255) DEFAULT NULL,
  `startDate` datetime DEFAULT NULL,
  `stopDate` datetime DEFAULT NULL,
  `client` int(11) DEFAULT NULL,
  `contractresult` int(11) DEFAULT NULL,
  `worker` int(11) DEFAULT NULL,
  `documentId` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK3F222368A23578C8` (`contractresult`),
  KEY `FK3F222368A4679646` (`worker`),
  KEY `FK3F2223685FC8D6E0` (`client`),
  CONSTRAINT `FK3F2223685FC8D6E0` FOREIGN KEY (`client`) REFERENCES `Client` (`id`),
  CONSTRAINT `FK3F222368A23578C8` FOREIGN KEY (`contractresult`) REFERENCES `ContractResult` (`id`),
  CONSTRAINT `FK3F222368A4679646` FOREIGN KEY (`worker`) REFERENCES `Worker` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1687 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `ServContract`
--

LOCK TABLES `ServContract` WRITE;
/*!40000 ALTER TABLE `ServContract` DISABLE KEYS */;
/*!40000 ALTER TABLE `ServContract` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `ServicesType`
--

DROP TABLE IF EXISTS `ServicesType`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `ServicesType` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `caption` varchar(255) DEFAULT NULL,
  `money` tinyint(1) DEFAULT '0',
  `document` tinyint(1) DEFAULT '0',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=101 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `ServicesType`
--

LOCK TABLES `ServicesType` WRITE;
/*!40000 ALTER TABLE `ServicesType` DISABLE KEYS */;
INSERT INTO `ServicesType` VALUES (1,'Консультация',0,0),(2,'Продукты/Средства гигиены',0,0),(3,'Комплект одежды',0,0),(4,'Получена корреспонденция',0,0),(5,'Передана корреспонденция',0,0),(6,'Покупка лекарств',1,0),(11,'Справка о регистрации',0,1),(12,'Направление на санобработку',0,1),(13,'Справка для проезда',0,1),(14,'Направление в диспансер',0,1),(15,'Справка социальная помощь',0,1),(16,'Изготовление фотографий',0,0),(17,'Написание заявлений/запросов',0,0),(18,'Оплата проезда',1,0),(19,'Консультация психолога',0,0),(20,'Оплата пошлины',1,0),(100,'Справка Транзит',0,1);
/*!40000 ALTER TABLE `ServicesType` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `ShelterHistory`
--

DROP TABLE IF EXISTS `ShelterHistory`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `ShelterHistory` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `dipthVac` datetime DEFAULT NULL,
  `fluorogr` datetime DEFAULT NULL,
  `hepotitsVac` datetime DEFAULT NULL,
  `inShelter` datetime DEFAULT NULL,
  `outShelter` datetime DEFAULT NULL,
  `roomId` int(11) NOT NULL,
  `typhVac` datetime DEFAULT NULL,
  `client` int(11) DEFAULT NULL,
  `shelterresult` int(11) DEFAULT NULL,
  `servContract` int(11) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `FK6BA300F5FC8D6E0` (`client`),
  KEY `FK6BA300F7553201A` (`shelterresult`),
  KEY `ShelterHistory_ServContract_idx` (`servContract`),
  CONSTRAINT `FK6BA300F5FC8D6E0` FOREIGN KEY (`client`) REFERENCES `Client` (`id`),
  CONSTRAINT `FK6BA300F7553201A` FOREIGN KEY (`shelterresult`) REFERENCES `ShelterResult` (`id`),
  CONSTRAINT `Sh_SC_FK001` FOREIGN KEY (`servContract`) REFERENCES `ServContract` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=337 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `ShelterHistory`
--

LOCK TABLES `ShelterHistory` WRITE;
/*!40000 ALTER TABLE `ShelterHistory` DISABLE KEYS */;
/*!40000 ALTER TABLE `ShelterHistory` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `ShelterResult`
--

DROP TABLE IF EXISTS `ShelterResult`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `ShelterResult` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `caption` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `ShelterResult`
--

LOCK TABLES `ShelterResult` WRITE;
/*!40000 ALTER TABLE `ShelterResult` DISABLE KEYS */;
INSERT INTO `ShelterResult` VALUES (1,'Проживает'),(2,'Выбыл в общем порядке'),(3,'Выбыл по причине нарушения условий');
/*!40000 ALTER TABLE `ShelterResult` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `SubRegion`
--

DROP TABLE IF EXISTS `SubRegion`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `SubRegion` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `region` int(11) DEFAULT NULL,
  `caption` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `region_idx` (`region`),
  CONSTRAINT `region_subregion` FOREIGN KEY (`region`) REFERENCES `Region` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=37 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `SubRegion`
--

LOCK TABLES `SubRegion` WRITE;
/*!40000 ALTER TABLE `SubRegion` DISABLE KEYS */;
INSERT INTO `SubRegion` VALUES (1,1,'Неизвестно'),(2,2,'Адмиралтейский'),(3,2,'Василеостровский'),(4,2,'Выборгский'),(5,2,'Калининский'),(6,2,'Кировский'),(7,2,'Колпинский'),(8,2,'Красногвардейский'),(9,2,'Красносельский'),(10,2,'Кронштадтский'),(11,2,'Курортный'),(12,2,'Московский'),(13,2,'Невский'),(14,2,'Петроградский'),(15,2,'Петродворцовый'),(16,2,'Приморский'),(17,2,'Пушкинский'),(18,2,'Фрунзенский'),(19,2,'Центральный'),(20,3,'Бокситогорский'),(21,3,'Волосовский'),(22,3,'Волховский'),(23,3,'Всеволожский'),(24,3,'Выборгский'),(25,3,'Гатчинский'),(26,3,'Кингисеппский'),(27,3,'Киришский'),(28,3,'Кировский'),(29,3,'Лодейнопольский'),(30,3,'Ломоносовский'),(31,3,'Лужский'),(32,3,'Подпорожский'),(33,3,'Приозерский'),(34,3,'Сланцевский'),(35,3,'Тихвинский'),(36,3,'Тосненский');
/*!40000 ALTER TABLE `SubRegion` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `Worker`
--

DROP TABLE IF EXISTS `Worker`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `Worker` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `firstname` varchar(255) DEFAULT NULL,
  `middlename` varchar(255) DEFAULT NULL,
  `password` varchar(255) DEFAULT NULL,
  `surname` varchar(255) DEFAULT NULL,
  `warrantDate` datetime DEFAULT NULL,
  `warrantNum` varchar(255) DEFAULT NULL,
  `rules` int(11) DEFAULT NULL,
  `primefacesskin` varchar(60) DEFAULT NULL,
  `fired` tinyint(1) DEFAULT '0',
  PRIMARY KEY (`id`),
  KEY `FK9AC73F9ECAF79164` (`rules`),
  CONSTRAINT `FK9AC73F9ECAF79164` FOREIGN KEY (`rules`) REFERENCES `Rules` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=15 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `Worker`
--

LOCK TABLES `Worker` WRITE;
/*!40000 ALTER TABLE `Worker` DISABLE KEYS */;
INSERT INTO `Worker` VALUES (1,'Григорий','Сергеевич','6216f8a75fd5bb3d5f22b6f9958cdede3fc086c2','Свердлин','2011-06-06 00:00:00','78АА0561688',1,'glass-x',0),(2,'Валентина','Марьяновна','6216f8a75fd5bb3d5f22b6f9958cdede3fc086c2','Борейко','2013-02-07 00:00:00','17',2,'glass-x',0),(3,'Ольга','Игоревна','6216f8a75fd5bb3d5f22b6f9958cdede3fc086c2','Алферова','2013-02-07 00:00:00','16',2,NULL,1),(4,'Антонина','Александровна','6216f8a75fd5bb3d5f22b6f9958cdede3fc086c2','Невская','2013-08-08 00:00:00','18',2,NULL,1),(5,'Светлана','Владимировна','6216f8a75fd5bb3d5f22b6f9958cdede3fc086c2','Быстрова','2013-02-07 00:00:00','3',2,NULL,1),(6,'Екатерина','Александровна','6216f8a75fd5bb3d5f22b6f9958cdede3fc086c2','Диковская','2013-02-12 00:00:00','19',7,NULL,0),(7,'Игорь','Залманович','6216f8a75fd5bb3d5f22b6f9958cdede3fc086c2','Карлинский','2013-02-07 00:00:00','ПРОШУ МЕНЯ ЗАПОЛНИТЬ',5,NULL,0),(8,'Елена','Игоревна','6216f8a75fd5bb3d5f22b6f9958cdede3fc086c2','Кондрахина','2013-02-07 00:00:00','06112014',4,NULL,0),(9,'Елизавета','Дмитриевна','6216f8a75fd5bb3d5f22b6f9958cdede3fc086c2','Лаврентьева','2013-02-07 00:00:00','ПРОШУ МЕНЯ ЗАПОЛНИТЬ',6,NULL,0),(10,'Вячеслав','Анатольевич','6216f8a75fd5bb3d5f22b6f9958cdede3fc086c2','Самонов','2014-10-27 00:00:00','б/н',3,NULL,0),(11,'Кира','Сергеевна','6216f8a75fd5bb3d5f22b6f9958cdede3fc086c2','Подлипаева','2014-12-29 00:00:00','21',2,NULL,0),(12,'Наталья','Руслановна','6216f8a75fd5bb3d5f22b6f9958cdede3fc086c2','Шавлохова','2015-02-06 00:00:00','20',2,'start',0),(13,'Роман','Валерьевич','6216f8a75fd5bb3d5f22b6f9958cdede3fc086c2','Ширшов','2015-06-22 00:00:00','12',2,NULL,0),(14,'Волонтеры','','6216f8a75fd5bb3d5f22b6f9958cdede3fc086c2','','1970-01-01 00:00:00','10000000',2,'glass-x',0);
/*!40000 ALTER TABLE `Worker` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `ZAGSRequestDocumentRegistry`
--

DROP TABLE IF EXISTS `ZAGSRequestDocumentRegistry`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `ZAGSRequestDocumentRegistry` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `client` int(11) DEFAULT NULL,
  `forWhom` text,
  `name` text,
  `whereWasBorn` text,
  `address` text,
  `mother` text,
  `father` text,
  `performerId` int(11) DEFAULT NULL,
  `date` datetime DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `ZAGSRequestDocumentRegistry`
--

LOCK TABLES `ZAGSRequestDocumentRegistry` WRITE;
/*!40000 ALTER TABLE `ZAGSRequestDocumentRegistry` DISABLE KEYS */;
/*!40000 ALTER TABLE `ZAGSRequestDocumentRegistry` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `link_breadwinner_client`
--

DROP TABLE IF EXISTS `link_breadwinner_client`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `link_breadwinner_client` (
  `breadwinners_id` int(11) NOT NULL,
  `clients_id` int(11) NOT NULL,
  PRIMARY KEY (`breadwinners_id`,`clients_id`),
  KEY `FK818F8638F279B1C7` (`clients_id`),
  KEY `FK818F8638BD81D24B` (`breadwinners_id`),
  CONSTRAINT `FK818F8638BD81D24B` FOREIGN KEY (`breadwinners_id`) REFERENCES `Breadwinner` (`id`),
  CONSTRAINT `FK818F8638F279B1C7` FOREIGN KEY (`clients_id`) REFERENCES `Client` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `link_breadwinner_client`
--

LOCK TABLES `link_breadwinner_client` WRITE;
/*!40000 ALTER TABLE `link_breadwinner_client` DISABLE KEYS */;
/*!40000 ALTER TABLE `link_breadwinner_client` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `link_chronicdisease_client`
--

DROP TABLE IF EXISTS `link_chronicdisease_client`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `link_chronicdisease_client` (
  `diseases_id` int(11) NOT NULL,
  `clients_id` int(11) NOT NULL,
  PRIMARY KEY (`diseases_id`,`clients_id`),
  KEY `FKA68B0C4FF279B1C7` (`clients_id`),
  KEY `FKA68B0C4FBB265A63` (`diseases_id`),
  CONSTRAINT `FKA68B0C4FBB265A63` FOREIGN KEY (`diseases_id`) REFERENCES `ChronicDisease` (`id`),
  CONSTRAINT `FKA68B0C4FF279B1C7` FOREIGN KEY (`clients_id`) REFERENCES `Client` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `link_chronicdisease_client`
--

LOCK TABLES `link_chronicdisease_client` WRITE;
/*!40000 ALTER TABLE `link_chronicdisease_client` DISABLE KEYS */;
/*!40000 ALTER TABLE `link_chronicdisease_client` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `link_reasonofhomeless_client`
--

DROP TABLE IF EXISTS `link_reasonofhomeless_client`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `link_reasonofhomeless_client` (
  `reasonofhomeless_id` int(11) NOT NULL,
  `clients_id` int(11) NOT NULL,
  PRIMARY KEY (`reasonofhomeless_id`,`clients_id`),
  KEY `FKE675E932F279B1C7` (`clients_id`),
  KEY `FKE675E932501B0FA4` (`reasonofhomeless_id`),
  CONSTRAINT `FKE675E932501B0FA4` FOREIGN KEY (`reasonofhomeless_id`) REFERENCES `ReasonOfHomeless` (`id`),
  CONSTRAINT `FKE675E932F279B1C7` FOREIGN KEY (`clients_id`) REFERENCES `Client` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `link_reasonofhomeless_client`
--

LOCK TABLES `link_reasonofhomeless_client` WRITE;
/*!40000 ALTER TABLE `link_reasonofhomeless_client` DISABLE KEYS */;
/*!40000 ALTER TABLE `link_reasonofhomeless_client` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2016-01-24 21:35:30
