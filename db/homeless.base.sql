-- MySQL dump 10.13  Distrib 5.5.35, for debian-linux-gnu (x86_64)
--
-- Host: localhost    Database: homeless-cut
-- ------------------------------------------------------
-- Server version	5.5.35-0ubuntu0.13.10.2

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
) ENGINE=InnoDB AUTO_INCREMENT=9 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `Breadwinner`
--

LOCK TABLES `Breadwinner` WRITE;
/*!40000 ALTER TABLE `Breadwinner` DISABLE KEYS */;
INSERT INTO `Breadwinner` VALUES (1,1,'Постоянная работа'),(2,2,'Временная работа'),(3,2,'Пенсия'),(4,2,'Попрошайничество'),(5,0,'Помощь близких'),(6,0,'Другие'),(7,0,'Сбор вторсырья'),(8,0,'Сбор продуктов (помойка)');
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
  PRIMARY KEY (`id`),
  KEY `FK7877DFEB4D3B23C6` (`education`),
  KEY `FK7877DFEB12C10578` (`nightStay`),
  KEY `FK7877DFEB2C9716BA` (`familycommunication`),
  CONSTRAINT `FK7877DFEB12C10578` FOREIGN KEY (`nightStay`) REFERENCES `NightStay` (`id`),
  CONSTRAINT `FK7877DFEB2C9716BA` FOREIGN KEY (`familycommunication`) REFERENCES `FamilyCommunication` (`id`),
  CONSTRAINT `FK7877DFEB4D3B23C6` FOREIGN KEY (`education`) REFERENCES `Education` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=13216 DEFAULT CHARSET=utf8;
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
) ENGINE=InnoDB AUTO_INCREMENT=6544 DEFAULT CHARSET=utf8;
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
) ENGINE=InnoDB AUTO_INCREMENT=23 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `ContractPoints`
--

LOCK TABLES `ContractPoints` WRITE;
/*!40000 ALTER TABLE `ContractPoints` DISABLE KEYS */;
INSERT INTO `ContractPoints` VALUES (1,1,'Восстановление, получение  паспорта'),(2,0,'Оформление пенсии'),(3,0,'Получение полиса ОМС'),(4,1,'Восстановление, получение ИНН'),(5,1,'Получение документов, подтверждающих трудовой стаж'),(6,0,'Оформление инвалидности'),(7,0,'Устройство в интернат'),(8,0,'Оформление временной регистрации по адресу Ночлежки'),(9,0,'Помощь в оформлении регистрации по другому адресу'),(10,1,'Помощь в трудоустройстве'),(11,1,'Помощь в трудоустройстве с проживанием'),(12,1,'Устройство в городской дом ночного пребывания'),(13,0,'Устройство в реабилитационные центры для алко- и наркозависимых'),(14,0,'Получение лечения, устройство в больницу'),(15,0,'Покупка лекарств (перенесено в разовые услуги)'),(16,0,'Поиск родственников'),(17,0,'Возвращение домой в СПб или другой город'),(18,0,'Восстановление или установление гражданства'),(19,0,'Юридическое сопровождение в суде'),(20,0,'Получение жилья'),(21,0,'Отмена мошеннической сделки и возврат жилья'),(22,0,'Подготовка запросов и(или) заявлений');
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
) ENGINE=InnoDB AUTO_INCREMENT=28 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `DocType`
--

LOCK TABLES `DocType` WRITE;
/*!40000 ALTER TABLE `DocType` DISABLE KEYS */;
INSERT INTO `DocType` VALUES (1,1,1,1,'Паспорт',1),(2,0,1,1,'Военный билет',1),(3,0,0,1,'Свидетельство о рождении',0),(4,1,0,1,'Свид. о рег. по Ф8',0),(5,0,1,1,'Временное удостоверение личности',1),(6,0,1,1,'Паспорт иностранного гражданина',1),(7,0,1,1,'Водительские права',1),(8,0,1,1,'Справка об освобождении из МЛС',1),(9,0,1,1,'Разрешение на временное проживание',1),(10,0,1,1,'Вид на жительство',1),(11,1,1,1,'Свид. о рег. по Ф9',0),(12,0,0,0,'???',10),(13,0,10,0,'Ходатайство',0),(14,1,10,0,'Ходатайство Центра по профилактике и борьбе со СПИД и инфекционными заболеваниями',0),(15,1,10,0,'паспорт1',0),(16,1,10,0,'паспорт2',0),(17,1,10,0,'паспорт3',0),(18,1,10,0,'паспорт4',0),(19,0,10,0,'папорт1',0),(20,0,10,0,'Пропуск',1),(21,1,10,0,'Паспорт родителя',0),(22,1,10,0,'справка об освобождении',0),(23,1,10,0,'СО СЛОВ',0),(24,0,10,1,'Свидетельство на возвращение',1),(25,1,10,0,'ходатайство о регистрации ',0),(26,1,10,0,'Ходатайство 1',0),(27,1,10,1,'Ходатайство от приюта \"Транзит\" ',0);
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
  PRIMARY KEY (`id`),
  KEY `FK3737353BCB1DEFA` (`doctype`),
  KEY `FK3737353BA4679646` (`worker`),
  KEY `FK3737353B5FC8D6E0` (`client`),
  CONSTRAINT `FK3737353B5FC8D6E0` FOREIGN KEY (`client`) REFERENCES `Client` (`id`),
  CONSTRAINT `FK3737353BA4679646` FOREIGN KEY (`worker`) REFERENCES `Worker` (`id`),
  CONSTRAINT `FK3737353BCB1DEFA` FOREIGN KEY (`doctype`) REFERENCES `DocType` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=5502 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `Document`
--

LOCK TABLES `Document` WRITE;
/*!40000 ALTER TABLE `Document` DISABLE KEYS */;
/*!40000 ALTER TABLE `Document` ENABLE KEYS */;
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
INSERT INTO `Education` VALUES (1,0,'Нет ответа'),(2,0,'Нет образования'),(3,0,'Нет образования'),(4,0,'Незаконченное среднее'),(5,1,'Среднее'),(6,1,'Среднее специальное'),(7,1,'Высшее, н/высшее');
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
INSERT INTO `FamilyCommunication` VALUES (1,'Нет ответа'),(2,'Видится'),(3,'Нет ответа'),(4,'Нет родственников');
/*!40000 ALTER TABLE `FamilyCommunication` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `GivenCertificate`
--

DROP TABLE IF EXISTS `GivenCertificate`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `GivenCertificate` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `date` datetime DEFAULT NULL,
  `num` varchar(255) DEFAULT NULL,
  `type` int(11) NOT NULL,
  `client` int(11) DEFAULT NULL,
  `worker` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FKC67D95FAA4679646` (`worker`),
  KEY `FKC67D95FA5FC8D6E0` (`client`),
  CONSTRAINT `FKC67D95FA5FC8D6E0` FOREIGN KEY (`client`) REFERENCES `Client` (`id`),
  CONSTRAINT `FKC67D95FAA4679646` FOREIGN KEY (`worker`) REFERENCES `Worker` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=4348 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `GivenCertificate`
--

LOCK TABLES `GivenCertificate` WRITE;
/*!40000 ALTER TABLE `GivenCertificate` DISABLE KEYS */;
/*!40000 ALTER TABLE `GivenCertificate` ENABLE KEYS */;
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
INSERT INTO `NightStay` VALUES (1,'Нет ответа'),(2,'Нет ответа'),(3,'Гос. ДНП'),(4,'Др. гос. учреждение'),(5,'Другое');
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
) ENGINE=InnoDB AUTO_INCREMENT=11 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `ReasonOfHomeless`
--

LOCK TABLES `ReasonOfHomeless` WRITE;
/*!40000 ALTER TABLE `ReasonOfHomeless` DISABLE KEYS */;
INSERT INTO `ReasonOfHomeless` VALUES (1,'Махинации с недвиж.'),(2,'Бывший заключенный'),(3,'Семейные проблемы'),(4,'Беженец'),(5,'Служебное жилье'),(6,'Трудовая миграция'),(7,'Личный выбор'),(8,'Сгорела квартира (дом)'),(9,'Выпускник интерната'),(10,'Другие');
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
  PRIMARY KEY (`id`),
  KEY `FK8DD747ECA4679646` (`worker`),
  KEY `FK8DD747EC5FC8D6E0` (`client`),
  KEY `FK8DD747EC2912C51A` (`servicesType`),
  CONSTRAINT `FK8DD747EC2912C51A` FOREIGN KEY (`servicesType`) REFERENCES `ServicesType` (`id`),
  CONSTRAINT `FK8DD747EC5FC8D6E0` FOREIGN KEY (`client`) REFERENCES `Client` (`id`),
  CONSTRAINT `FK8DD747ECA4679646` FOREIGN KEY (`worker`) REFERENCES `Worker` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=7618 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `RecievedService`
--

LOCK TABLES `RecievedService` WRITE;
/*!40000 ALTER TABLE `RecievedService` DISABLE KEYS */;
/*!40000 ALTER TABLE `RecievedService` ENABLE KEYS */;
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
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `Rules`
--

LOCK TABLES `Rules` WRITE;
/*!40000 ALTER TABLE `Rules` DISABLE KEYS */;
INSERT INTO `Rules` VALUES (1,'Директор'),(2,'Социальный работник'),(3,'Юрист'),(4,'Помощник юриста'),(5,'Правовой консультант'),(6,'Менеджер');
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
  PRIMARY KEY (`id`),
  KEY `FK3F222368A23578C8` (`contractresult`),
  KEY `FK3F222368A4679646` (`worker`),
  KEY `FK3F2223685FC8D6E0` (`client`),
  CONSTRAINT `FK3F2223685FC8D6E0` FOREIGN KEY (`client`) REFERENCES `Client` (`id`),
  CONSTRAINT `FK3F222368A23578C8` FOREIGN KEY (`contractresult`) REFERENCES `ContractResult` (`id`),
  CONSTRAINT `FK3F222368A4679646` FOREIGN KEY (`worker`) REFERENCES `Worker` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=536 DEFAULT CHARSET=utf8;
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
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=101 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `ServicesType`
--

LOCK TABLES `ServicesType` WRITE;
/*!40000 ALTER TABLE `ServicesType` DISABLE KEYS */;
INSERT INTO `ServicesType` VALUES (1,'Консультация'),(2,'Продукты/Средства гигиены'),(3,'Комплект одежды'),(4,'Получена корреспонденция'),(5,'Передана корреспонденция'),(6,'Покупка лекарств'),(11,'Справка о регистрации'),(12,'Направление на санобработку'),(13,'Справка для проезда'),(14,'Направление в диспансер'),(15,'Справка социальная помощь'),(20,'Неизвестно'),(100,'Справка Транзит');
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
  PRIMARY KEY (`id`),
  KEY `FK6BA300F5FC8D6E0` (`client`),
  KEY `FK6BA300F7553201A` (`shelterresult`),
  CONSTRAINT `FK6BA300F5FC8D6E0` FOREIGN KEY (`client`) REFERENCES `Client` (`id`),
  CONSTRAINT `FK6BA300F7553201A` FOREIGN KEY (`shelterresult`) REFERENCES `ShelterResult` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=117 DEFAULT CHARSET=utf8;
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
-- Table structure for table `Tranzit`
--

DROP TABLE IF EXISTS `Tranzit`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `Tranzit` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `n_worker` int(11) DEFAULT NULL,
  `n_client` int(11) DEFAULT NULL,
  `servdate` datetime DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=209 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `Tranzit`
--

LOCK TABLES `Tranzit` WRITE;
/*!40000 ALTER TABLE `Tranzit` DISABLE KEYS */;
/*!40000 ALTER TABLE `Tranzit` ENABLE KEYS */;
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
  PRIMARY KEY (`id`),
  KEY `FK9AC73F9ECAF79164` (`rules`),
  CONSTRAINT `FK9AC73F9ECAF79164` FOREIGN KEY (`rules`) REFERENCES `Rules` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=10 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `Worker`
--

LOCK TABLES `Worker` WRITE;
/*!40000 ALTER TABLE `Worker` DISABLE KEYS */;
INSERT INTO `Worker` VALUES (1,'Григорий','Сергеевич','111','Свердлин','2011-06-06 00:00:00','78АА0561688',1),(2,'Валентина','Марьяновна','111','Борейко','2013-02-07 00:00:00','17',2),(3,'Ольга','Игоревна','111','Алферова','2013-02-07 00:00:00','16',2),(4,'Антонина','Александровна','111','Невская','2013-08-08 00:00:00','18',2),(5,'Светлана','Владимировна','111','Быстрова','2013-02-07 00:00:00','3',2),(6,'Екатерина','Александровна','111','Диковская','2013-02-12 00:00:00','19',3),(7,'Игорь','Залманович','111','Карлинский','2013-02-07 00:00:00','ПРОШУ МЕНЯ ЗАПОЛНИТЬ',5),(8,'Елена','Игоревна','111','Кондрахина','2013-02-07 00:00:00','ПРОШУ МЕНЯ ЗАПОЛНИТЬ',4),(9,'Елизавета','?','111','Лаврентьева','2013-02-07 00:00:00','ПРОШУ МЕНЯ ЗАПОЛНИТЬ',6);
/*!40000 ALTER TABLE `Worker` ENABLE KEYS */;
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

-- Dump completed on 2014-03-23 21:36:55
