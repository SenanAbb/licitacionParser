-- MySQL dump 10.13  Distrib 8.0.22, for Win64 (x86_64)
--
-- Host: 127.0.0.1    Database: licitacion
-- ------------------------------------------------------
-- Server version	5.7.33-log

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!50503 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `tbl_address_format_code`
--

DROP TABLE IF EXISTS `tbl_address_format_code`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `tbl_address_format_code` (
  `address_format_code` int(11) NOT NULL,
  `description` varchar(250) DEFAULT NULL,
  PRIMARY KEY (`address_format_code`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tbl_address_format_code`
--

LOCK TABLES `tbl_address_format_code` WRITE;
/*!40000 ALTER TABLE `tbl_address_format_code` DISABLE KEYS */;
INSERT INTO `tbl_address_format_code` VALUES (0,'NULL'),(1,'prueba');
/*!40000 ALTER TABLE `tbl_address_format_code` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `tbl_address_line`
--

DROP TABLE IF EXISTS `tbl_address_line`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `tbl_address_line` (
  `address_line` int(11) NOT NULL AUTO_INCREMENT,
  `line` varchar(220) NOT NULL,
  `postaladdress` int(11) NOT NULL,
  `fecha_actualizacion` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`address_line`),
  KEY `fk_tbl_address_line_tbl_postal_address1_idx` (`postaladdress`),
  CONSTRAINT `fk_tbl_address_line_tbl_postal_address1` FOREIGN KEY (`postaladdress`) REFERENCES `tbl_postal_address` (`postal_address`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tbl_address_line`
--

LOCK TABLES `tbl_address_line` WRITE;
/*!40000 ALTER TABLE `tbl_address_line` DISABLE KEYS */;
/*!40000 ALTER TABLE `tbl_address_line` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `tbl_contact`
--

DROP TABLE IF EXISTS `tbl_contact`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `tbl_contact` (
  `contact` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(300) DEFAULT NULL,
  `electronicmail` varchar(200) DEFAULT NULL,
  `telephone` varchar(50) DEFAULT NULL,
  `telefax` varchar(50) DEFAULT NULL,
  `fecha_actualizacion` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`contact`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tbl_contact`
--

LOCK TABLES `tbl_contact` WRITE;
/*!40000 ALTER TABLE `tbl_contact` DISABLE KEYS */;
/*!40000 ALTER TABLE `tbl_contact` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `tbl_contract_folder_status`
--

DROP TABLE IF EXISTS `tbl_contract_folder_status`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `tbl_contract_folder_status` (
  `contract_folder_status` int(11) NOT NULL AUTO_INCREMENT,
  `contractfolderid` varchar(50) NOT NULL,
  `contractfolderstatuscode` varchar(50) NOT NULL,
  `fecha_actualizacion` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `entry` int(11) NOT NULL,
  PRIMARY KEY (`contract_folder_status`),
  KEY `fk_tbl_contract_folder_status_tbl_entrys` (`entry`),
  KEY `fk_tbl_contract_folder_status_tbl_contract_folder_status_co_idx` (`contractfolderstatuscode`),
  CONSTRAINT `fk_tbl_contract_folder_status_tbl_contract_folder_status_code1` FOREIGN KEY (`contractfolderstatuscode`) REFERENCES `tbl_contract_folder_status_code` (`contract_folder_status_code`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_tbl_contract_folder_status_tbl_entrys` FOREIGN KEY (`entry`) REFERENCES `tbl_entrys` (`entrys`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tbl_contract_folder_status`
--

LOCK TABLES `tbl_contract_folder_status` WRITE;
/*!40000 ALTER TABLE `tbl_contract_folder_status` DISABLE KEYS */;
/*!40000 ALTER TABLE `tbl_contract_folder_status` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `tbl_contract_folder_status_code`
--

DROP TABLE IF EXISTS `tbl_contract_folder_status_code`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `tbl_contract_folder_status_code` (
  `contract_folder_status_code` varchar(50) NOT NULL,
  `description` varchar(100) NOT NULL,
  PRIMARY KEY (`contract_folder_status_code`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tbl_contract_folder_status_code`
--

LOCK TABLES `tbl_contract_folder_status_code` WRITE;
/*!40000 ALTER TABLE `tbl_contract_folder_status_code` DISABLE KEYS */;
INSERT INTO `tbl_contract_folder_status_code` VALUES ('ADJ','Adjudicada'),('ANUL','Anulada'),('EV','Pendiente de adjudicación'),('PRE','Anuncio Previo'),('PUB','En plazo'),('RES','Resuelta');
/*!40000 ALTER TABLE `tbl_contract_folder_status_code` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `tbl_contracting_party_type_code`
--

DROP TABLE IF EXISTS `tbl_contracting_party_type_code`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `tbl_contracting_party_type_code` (
  `contracting_party_type_code` int(11) NOT NULL,
  `nombre` varchar(100) NOT NULL,
  PRIMARY KEY (`contracting_party_type_code`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tbl_contracting_party_type_code`
--

LOCK TABLES `tbl_contracting_party_type_code` WRITE;
/*!40000 ALTER TABLE `tbl_contracting_party_type_code` DISABLE KEYS */;
INSERT INTO `tbl_contracting_party_type_code` VALUES (1,'Administración General del Estado'),(2,'Comunidad Autónoma'),(3,'Administración Local'),(4,'Entidad de Derecho Público '),(5,'Otras Entidades del Sector Público');
/*!40000 ALTER TABLE `tbl_contracting_party_type_code` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `tbl_country`
--

DROP TABLE IF EXISTS `tbl_country`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `tbl_country` (
  `country` int(11) NOT NULL AUTO_INCREMENT,
  `identificationcode` varchar(3) NOT NULL,
  `name` varchar(150) DEFAULT NULL,
  `fecha_actualizacion` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`country`),
  KEY `fk_tbl_country_tbl_identification_code` (`identificationcode`),
  CONSTRAINT `fk_tbl_country_tbl_identification_code` FOREIGN KEY (`identificationcode`) REFERENCES `tbl_identification_code` (`identification_code`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tbl_country`
--

LOCK TABLES `tbl_country` WRITE;
/*!40000 ALTER TABLE `tbl_country` DISABLE KEYS */;
/*!40000 ALTER TABLE `tbl_country` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `tbl_dgpe`
--

DROP TABLE IF EXISTS `tbl_dgpe`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `tbl_dgpe` (
  `dgpe` int(11) NOT NULL AUTO_INCREMENT,
  `nombre_tabla` varchar(100) NOT NULL,
  PRIMARY KEY (`dgpe`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tbl_dgpe`
--

LOCK TABLES `tbl_dgpe` WRITE;
/*!40000 ALTER TABLE `tbl_dgpe` DISABLE KEYS */;
/*!40000 ALTER TABLE `tbl_dgpe` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `tbl_entrys`
--

DROP TABLE IF EXISTS `tbl_entrys`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `tbl_entrys` (
  `entrys` int(11) NOT NULL AUTO_INCREMENT,
  `id` varchar(2500) NOT NULL,
  `link` varchar(2500) NOT NULL,
  `summary` varchar(400) NOT NULL,
  `title` varchar(2000) NOT NULL,
  `updated` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `fecha_actualizacion` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `ids` int(11) NOT NULL,
  PRIMARY KEY (`entrys`),
  KEY `fk_tbl_entrys_tbl_ids` (`ids`),
  CONSTRAINT `fk_tbl_entrys_tbl_ids` FOREIGN KEY (`ids`) REFERENCES `tbl_ids` (`ids`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tbl_entrys`
--

LOCK TABLES `tbl_entrys` WRITE;
/*!40000 ALTER TABLE `tbl_entrys` DISABLE KEYS */;
/*!40000 ALTER TABLE `tbl_entrys` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `tbl_identification_code`
--

DROP TABLE IF EXISTS `tbl_identification_code`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `tbl_identification_code` (
  `identification_code` varchar(50) NOT NULL,
  `name` varchar(150) NOT NULL,
  `numericcode` int(11) NOT NULL,
  PRIMARY KEY (`identification_code`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tbl_identification_code`
--

LOCK TABLES `tbl_identification_code` WRITE;
/*!40000 ALTER TABLE `tbl_identification_code` DISABLE KEYS */;
INSERT INTO `tbl_identification_code` VALUES ('ES','Spain',724);
/*!40000 ALTER TABLE `tbl_identification_code` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `tbl_ids`
--

DROP TABLE IF EXISTS `tbl_ids`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `tbl_ids` (
  `ids` int(11) NOT NULL AUTO_INCREMENT,
  `fecha_actualizacion` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `modosid` int(11) NOT NULL,
  PRIMARY KEY (`ids`),
  KEY `fk_tbl_ids_tbl_modosid` (`modosid`),
  CONSTRAINT `fk_tbl_ids_tbl_modosid` FOREIGN KEY (`modosid`) REFERENCES `tbl_modosid` (`modosid`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tbl_ids`
--

LOCK TABLES `tbl_ids` WRITE;
/*!40000 ALTER TABLE `tbl_ids` DISABLE KEYS */;
/*!40000 ALTER TABLE `tbl_ids` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `tbl_located_contracting_party`
--

DROP TABLE IF EXISTS `tbl_located_contracting_party`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `tbl_located_contracting_party` (
  `located_contracting_party` int(11) NOT NULL AUTO_INCREMENT,
  `contractingpartytypecode` int(11) NOT NULL,
  `contractfolderstatus` int(11) NOT NULL,
  `fecha_actualizacion` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`located_contracting_party`),
  KEY `fk_tbl_located_contracting_party_tbl_contract_folder_status` (`contractfolderstatus`),
  KEY `fk_tbl_located_contracting_party_tbl_contracting_party_type_code` (`contractingpartytypecode`),
  CONSTRAINT `fk_tbl_located_contracting_party_tbl_contract_folder_status` FOREIGN KEY (`contractfolderstatus`) REFERENCES `tbl_contract_folder_status` (`contract_folder_status`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_tbl_located_contracting_party_tbl_contracting_party_type_code` FOREIGN KEY (`contractingpartytypecode`) REFERENCES `tbl_contracting_party_type_code` (`contracting_party_type_code`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tbl_located_contracting_party`
--

LOCK TABLES `tbl_located_contracting_party` WRITE;
/*!40000 ALTER TABLE `tbl_located_contracting_party` DISABLE KEYS */;
/*!40000 ALTER TABLE `tbl_located_contracting_party` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `tbl_modosid`
--

DROP TABLE IF EXISTS `tbl_modosid`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `tbl_modosid` (
  `modosid` int(11) NOT NULL AUTO_INCREMENT,
  `description` varchar(50) NOT NULL,
  PRIMARY KEY (`modosid`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tbl_modosid`
--

LOCK TABLES `tbl_modosid` WRITE;
/*!40000 ALTER TABLE `tbl_modosid` DISABLE KEYS */;
INSERT INTO `tbl_modosid` VALUES (1,'Automático'),(2,'Manual');
/*!40000 ALTER TABLE `tbl_modosid` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `tbl_parent_located_party`
--

DROP TABLE IF EXISTS `tbl_parent_located_party`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `tbl_parent_located_party` (
  `parent_located_party` int(11) NOT NULL AUTO_INCREMENT,
  `fecha_actualizacion` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `locatedcontractingparty` int(11) NOT NULL,
  PRIMARY KEY (`parent_located_party`),
  KEY `fk_tbl_parent_located_party_tbl_located_contracting_party1_idx` (`locatedcontractingparty`),
  CONSTRAINT `fk_tbl_parent_located_party_tbl_located_contracting_party1` FOREIGN KEY (`locatedcontractingparty`) REFERENCES `tbl_located_contracting_party` (`located_contracting_party`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tbl_parent_located_party`
--

LOCK TABLES `tbl_parent_located_party` WRITE;
/*!40000 ALTER TABLE `tbl_parent_located_party` DISABLE KEYS */;
/*!40000 ALTER TABLE `tbl_parent_located_party` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `tbl_parentlocatedparty_partyname`
--

DROP TABLE IF EXISTS `tbl_parentlocatedparty_partyname`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `tbl_parentlocatedparty_partyname` (
  `partyname` int(11) NOT NULL,
  `parentlocatedparty` int(11) NOT NULL,
  `fecha_actualizacion` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  KEY `fk_tbl_party_name_has_tbl_parent_located_party_tbl_parent_l_idx` (`parentlocatedparty`),
  KEY `fk_tbl_party_name_has_tbl_parent_located_party_tbl_party_na_idx` (`partyname`),
  CONSTRAINT `fk_tbl_party_name_has_tbl_parent_located_party_tbl_parent_loc1` FOREIGN KEY (`parentlocatedparty`) REFERENCES `tbl_parent_located_party` (`parent_located_party`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_tbl_party_name_has_tbl_parent_located_party_tbl_party_name1` FOREIGN KEY (`partyname`) REFERENCES `tbl_party_name` (`party_name`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tbl_parentlocatedparty_partyname`
--

LOCK TABLES `tbl_parentlocatedparty_partyname` WRITE;
/*!40000 ALTER TABLE `tbl_parentlocatedparty_partyname` DISABLE KEYS */;
/*!40000 ALTER TABLE `tbl_parentlocatedparty_partyname` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `tbl_party`
--

DROP TABLE IF EXISTS `tbl_party`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `tbl_party` (
  `party` int(11) NOT NULL AUTO_INCREMENT,
  `websiteuri` varchar(256) DEFAULT NULL,
  `buyerprofileuriid` varchar(500) DEFAULT NULL,
  `locatedcontractingparty` int(11) NOT NULL,
  `fecha_actualizacion` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`party`),
  KEY `fk_tbl_party_tbl_located_contracting_party1_idx` (`locatedcontractingparty`),
  CONSTRAINT `fk_tbl_party_tbl_located_contracting_party1` FOREIGN KEY (`locatedcontractingparty`) REFERENCES `tbl_located_contracting_party` (`located_contracting_party`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tbl_party`
--

LOCK TABLES `tbl_party` WRITE;
/*!40000 ALTER TABLE `tbl_party` DISABLE KEYS */;
/*!40000 ALTER TABLE `tbl_party` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `tbl_party_contact`
--

DROP TABLE IF EXISTS `tbl_party_contact`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `tbl_party_contact` (
  `contact` int(11) NOT NULL,
  `party` int(11) NOT NULL,
  `fecha_actualizacion` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  KEY `fk_tbl_contact_has_tbl_party_tbl_party1_idx` (`party`),
  KEY `fk_tbl_contact_has_tbl_party_tbl_contact1_idx` (`contact`),
  CONSTRAINT `fk_tbl_contact_has_tbl_party_tbl_contact1` FOREIGN KEY (`contact`) REFERENCES `tbl_contact` (`contact`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_tbl_contact_has_tbl_party_tbl_party1` FOREIGN KEY (`party`) REFERENCES `tbl_party` (`party`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tbl_party_contact`
--

LOCK TABLES `tbl_party_contact` WRITE;
/*!40000 ALTER TABLE `tbl_party_contact` DISABLE KEYS */;
/*!40000 ALTER TABLE `tbl_party_contact` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `tbl_party_identification`
--

DROP TABLE IF EXISTS `tbl_party_identification`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `tbl_party_identification` (
  `party_identification` int(11) NOT NULL AUTO_INCREMENT,
  `id` varchar(250) NOT NULL,
  `schemename` varchar(50) NOT NULL,
  `fecha_actualizacion` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`party_identification`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tbl_party_identification`
--

LOCK TABLES `tbl_party_identification` WRITE;
/*!40000 ALTER TABLE `tbl_party_identification` DISABLE KEYS */;
/*!40000 ALTER TABLE `tbl_party_identification` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `tbl_party_name`
--

DROP TABLE IF EXISTS `tbl_party_name`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `tbl_party_name` (
  `party_name` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(300) NOT NULL,
  `fecha_actualizacion` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`party_name`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tbl_party_name`
--

LOCK TABLES `tbl_party_name` WRITE;
/*!40000 ALTER TABLE `tbl_party_name` DISABLE KEYS */;
/*!40000 ALTER TABLE `tbl_party_name` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `tbl_party_partyidentification`
--

DROP TABLE IF EXISTS `tbl_party_partyidentification`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `tbl_party_partyidentification` (
  `party` int(11) NOT NULL,
  `partyidentification` int(11) NOT NULL,
  `fecha_actualizacion` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  KEY `fk_tbl_party_partyidentification_tbl_party` (`party`),
  KEY `fk_tbl_party_partyidentification_tbl_partyidenfication` (`partyidentification`),
  CONSTRAINT `fk_tbl_party_partyidentification_tbl_party` FOREIGN KEY (`party`) REFERENCES `tbl_party` (`party`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_tbl_party_partyidentification_tbl_partyidenfication` FOREIGN KEY (`partyidentification`) REFERENCES `tbl_party_identification` (`party_identification`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tbl_party_partyidentification`
--

LOCK TABLES `tbl_party_partyidentification` WRITE;
/*!40000 ALTER TABLE `tbl_party_partyidentification` DISABLE KEYS */;
/*!40000 ALTER TABLE `tbl_party_partyidentification` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `tbl_party_partyname`
--

DROP TABLE IF EXISTS `tbl_party_partyname`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `tbl_party_partyname` (
  `partyname` int(11) NOT NULL,
  `party` int(11) NOT NULL,
  `fecha_actualizacion` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  KEY `fk_tbl_party_name_has_tbl_party_tbl_party1_idx` (`party`),
  KEY `fk_tbl_party_name_has_tbl_party_tbl_party_name1_idx` (`partyname`),
  CONSTRAINT `fk_tbl_party_name_has_tbl_party_tbl_party1` FOREIGN KEY (`party`) REFERENCES `tbl_party` (`party`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_tbl_party_name_has_tbl_party_tbl_party_name1` FOREIGN KEY (`partyname`) REFERENCES `tbl_party_name` (`party_name`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tbl_party_partyname`
--

LOCK TABLES `tbl_party_partyname` WRITE;
/*!40000 ALTER TABLE `tbl_party_partyname` DISABLE KEYS */;
/*!40000 ALTER TABLE `tbl_party_partyname` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `tbl_party_postaladdress`
--

DROP TABLE IF EXISTS `tbl_party_postaladdress`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `tbl_party_postaladdress` (
  `postaladdress` int(11) NOT NULL,
  `party` int(11) NOT NULL,
  `fecha_actualizacion` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  KEY `fk_tbl_postal_address_has_tbl_party_tbl_party1_idx` (`party`),
  KEY `fk_tbl_postal_address_has_tbl_party_tbl_postal_address1_idx` (`postaladdress`),
  CONSTRAINT `fk_tbl_postal_address_has_tbl_party_tbl_party1` FOREIGN KEY (`party`) REFERENCES `tbl_party` (`party`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_tbl_postal_address_has_tbl_party_tbl_postal_address1` FOREIGN KEY (`postaladdress`) REFERENCES `tbl_postal_address` (`postal_address`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tbl_party_postaladdress`
--

LOCK TABLES `tbl_party_postaladdress` WRITE;
/*!40000 ALTER TABLE `tbl_party_postaladdress` DISABLE KEYS */;
/*!40000 ALTER TABLE `tbl_party_postaladdress` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `tbl_postal_address`
--

DROP TABLE IF EXISTS `tbl_postal_address`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `tbl_postal_address` (
  `postal_address` int(11) NOT NULL AUTO_INCREMENT,
  `addressformatcode` int(11) DEFAULT NULL,
  `cityname` varchar(90) NOT NULL,
  `postalzone` varchar(32) NOT NULL,
  `fecha_actualizacion` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`postal_address`),
  KEY `fk_tbl_postal_address_tbl_address_format_code` (`addressformatcode`),
  CONSTRAINT `fk_tbl_postal_address_tbl_address_format_code` FOREIGN KEY (`addressformatcode`) REFERENCES `tbl_address_format_code` (`address_format_code`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tbl_postal_address`
--

LOCK TABLES `tbl_postal_address` WRITE;
/*!40000 ALTER TABLE `tbl_postal_address` DISABLE KEYS */;
/*!40000 ALTER TABLE `tbl_postal_address` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `tbl_postaladdress_country`
--

DROP TABLE IF EXISTS `tbl_postaladdress_country`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `tbl_postaladdress_country` (
  `country` int(11) NOT NULL,
  `postaladdress` int(11) NOT NULL,
  `fecha_actualizacion` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  KEY `fk_tbl_country_has_tbl_postal_address_tbl_postal_address1_idx` (`postaladdress`),
  KEY `fk_tbl_country_has_tbl_postal_address_tbl_country1_idx` (`country`),
  CONSTRAINT `fk_tbl_country_has_tbl_postal_address_tbl_country1` FOREIGN KEY (`country`) REFERENCES `tbl_country` (`country`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_tbl_country_has_tbl_postal_address_tbl_postal_address1` FOREIGN KEY (`postaladdress`) REFERENCES `tbl_postal_address` (`postal_address`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tbl_postaladdress_country`
--

LOCK TABLES `tbl_postaladdress_country` WRITE;
/*!40000 ALTER TABLE `tbl_postaladdress_country` DISABLE KEYS */;
/*!40000 ALTER TABLE `tbl_postaladdress_country` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping routines for database 'licitacion'
--
/*!50003 DROP PROCEDURE IF EXISTS `newAddressLine` */;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8mb4 */ ;
/*!50003 SET character_set_results = utf8mb4 */ ;
/*!50003 SET collation_connection  = utf8mb4_general_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'STRICT_TRANS_TABLES,NO_AUTO_CREATE_USER,NO_ENGINE_SUBSTITUTION' */ ;
DELIMITER ;;
CREATE DEFINER=`root`@`localhost` PROCEDURE `newAddressLine`(
IN line VARCHAR(220),
IN postal_address INT)
BEGIN
	INSERT INTO tbl_address_line (line, postaladdress)
    VALUES (line, postal_address);
END ;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;
/*!50003 DROP PROCEDURE IF EXISTS `newContact` */;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8mb4 */ ;
/*!50003 SET character_set_results = utf8mb4 */ ;
/*!50003 SET collation_connection  = utf8mb4_general_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'STRICT_TRANS_TABLES,NO_AUTO_CREATE_USER,NO_ENGINE_SUBSTITUTION' */ ;
DELIMITER ;;
CREATE DEFINER=`root`@`localhost` PROCEDURE `newContact`(
IN name VARCHAR(300),
IN electronicmail VARCHAR(250),
IN telephone VARCHAR(50),
IN telefax VARCHAR(50),
OUT contact INT)
BEGIN
	INSERT INTO tbl_contact (name, electronicmail, telephone, telefax)
    VALUES (name, electronicmail, telephone, telefax);
    
    SET contact = last_insert_id();
END ;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;
/*!50003 DROP PROCEDURE IF EXISTS `newContractFolderStatus` */;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8mb4 */ ;
/*!50003 SET character_set_results = utf8mb4 */ ;
/*!50003 SET collation_connection  = utf8mb4_general_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'STRICT_TRANS_TABLES,NO_AUTO_CREATE_USER,NO_ENGINE_SUBSTITUTION' */ ;
DELIMITER ;;
CREATE DEFINER=`root`@`localhost` PROCEDURE `newContractFolderStatus`(
IN contractFolderID VARCHAR(50),
IN contractFolderStatusCode VARCHAR(50),
IN entry INT,
OUT contract_folder_status INT )
BEGIN
	INSERT INTO tbl_contract_folder_status(contractfolderid, contractfolderstatuscode, entry)
    VALUES (contractFolderID, contractFolderStatusCode, entry);

    SET contract_folder_status = LAST_INSERT_ID();
END ;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;
/*!50003 DROP PROCEDURE IF EXISTS `newCountry` */;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8mb4 */ ;
/*!50003 SET character_set_results = utf8mb4 */ ;
/*!50003 SET collation_connection  = utf8mb4_general_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'STRICT_TRANS_TABLES,NO_AUTO_CREATE_USER,NO_ENGINE_SUBSTITUTION' */ ;
DELIMITER ;;
CREATE DEFINER=`root`@`localhost` PROCEDURE `newCountry`(
IN identification_code VARCHAR(3),
IN name VARCHAR(150),
OUT country INT)
BEGIN
	INSERT INTO tbl_country (identificationcode, name) 
	VALUES (identification_code, name);
    
    SET country = last_insert_id();
END ;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;
/*!50003 DROP PROCEDURE IF EXISTS `newEntry` */;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8mb4 */ ;
/*!50003 SET character_set_results = utf8mb4 */ ;
/*!50003 SET collation_connection  = utf8mb4_general_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'STRICT_TRANS_TABLES,NO_AUTO_CREATE_USER,NO_ENGINE_SUBSTITUTION' */ ;
DELIMITER ;;
CREATE DEFINER=`root`@`localhost` PROCEDURE `newEntry`(
	IN id VARCHAR(2500), 
	IN link VARCHAR(2500),
	IN summary VARCHAR(400),
    IN title VARCHAR(2000),
    IN updated TIMESTAMP,
    IN ids INT(11),
    OUT entrys INT(11))
BEGIN 
	INSERT INTO tbl_entrys(id, link, summary, title, updated, ids)
    VALUES (id, link, summary, title, updated, ids);

    SET entrys = LAST_INSERT_ID();
END ;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;
/*!50003 DROP PROCEDURE IF EXISTS `newIds` */;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8mb4 */ ;
/*!50003 SET character_set_results = utf8mb4 */ ;
/*!50003 SET collation_connection  = utf8mb4_general_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'STRICT_TRANS_TABLES,NO_AUTO_CREATE_USER,NO_ENGINE_SUBSTITUTION' */ ;
DELIMITER ;;
CREATE DEFINER=`root`@`localhost` PROCEDURE `newIds`(IN modos_id INT(11), 
	
	OUT ids INT(11))
BEGIN 
	INSERT INTO tbl_ids (modosid)
    VALUES (modos_id);

    SET ids = LAST_INSERT_ID();
END ;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;
/*!50003 DROP PROCEDURE IF EXISTS `newLocatedContractingParty` */;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8mb4 */ ;
/*!50003 SET character_set_results = utf8mb4 */ ;
/*!50003 SET collation_connection  = utf8mb4_general_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'STRICT_TRANS_TABLES,NO_AUTO_CREATE_USER,NO_ENGINE_SUBSTITUTION' */ ;
DELIMITER ;;
CREATE DEFINER=`root`@`localhost` PROCEDURE `newLocatedContractingParty`(
IN contractingPartyTypeCode INT, 
IN contractFolderStatus INT,
OUT located_contracting_party INT)
BEGIN
	INSERT INTO tbl_located_contracting_party(contractingpartytypecode, contractfolderstatus)
    VALUES (contractingPartyTypeCode, contractFolderStatus);

    SET located_contracting_party = LAST_INSERT_ID();
END ;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;
/*!50003 DROP PROCEDURE IF EXISTS `newParentLocatedParty` */;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8mb4 */ ;
/*!50003 SET character_set_results = utf8mb4 */ ;
/*!50003 SET collation_connection  = utf8mb4_general_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'STRICT_TRANS_TABLES,NO_AUTO_CREATE_USER,NO_ENGINE_SUBSTITUTION' */ ;
DELIMITER ;;
CREATE DEFINER=`root`@`localhost` PROCEDURE `newParentLocatedParty`(
IN locatedContractingParty INT,
OUT parent_located_party INT)
BEGIN
	INSERT INTO tbl_parent_located_party(locatedcontractingparty)
	VALUES (locatedContractingParty);
    
    SET parent_located_party = LAST_INSERT_ID();
END ;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;
/*!50003 DROP PROCEDURE IF EXISTS `newParentlocatedpartyPartyname` */;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8mb4 */ ;
/*!50003 SET character_set_results = utf8mb4 */ ;
/*!50003 SET collation_connection  = utf8mb4_general_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'STRICT_TRANS_TABLES,NO_AUTO_CREATE_USER,NO_ENGINE_SUBSTITUTION' */ ;
DELIMITER ;;
CREATE DEFINER=`root`@`localhost` PROCEDURE `newParentlocatedpartyPartyname`(
IN parent_located_party INT,
IN party_name INT)
BEGIN
	INSERT INTO tbl_parentlocatedparty_partyname (parentlocatedparty, partyname)
    VALUES (parent_located_party, party_name);
END ;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;
/*!50003 DROP PROCEDURE IF EXISTS `newParty` */;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8mb4 */ ;
/*!50003 SET character_set_results = utf8mb4 */ ;
/*!50003 SET collation_connection  = utf8mb4_general_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'STRICT_TRANS_TABLES,NO_AUTO_CREATE_USER,NO_ENGINE_SUBSTITUTION' */ ;
DELIMITER ;;
CREATE DEFINER=`root`@`localhost` PROCEDURE `newParty`(
IN websiteuri VARCHAR(256),
IN buyerprofileuriid VARCHAR(500),
IN located_contracting_party INT,
OUT party INT)
BEGIN
	INSERT INTO tbl_party (websiteuri, buyerprofileuriid, locatedcontractingparty)
    VALUES (websiteuri, buyerprofileuriid, located_contracting_party);
    
    SET party = LAST_INSERT_ID();
END ;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;
/*!50003 DROP PROCEDURE IF EXISTS `newPartyContact` */;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8mb4 */ ;
/*!50003 SET character_set_results = utf8mb4 */ ;
/*!50003 SET collation_connection  = utf8mb4_general_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'STRICT_TRANS_TABLES,NO_AUTO_CREATE_USER,NO_ENGINE_SUBSTITUTION' */ ;
DELIMITER ;;
CREATE DEFINER=`root`@`localhost` PROCEDURE `newPartyContact`(
IN party INT, 
IN contact INT)
BEGIN
	INSERT INTO tbl_party_contact (party, contact)
    VALUES (party, contact);
END ;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;
/*!50003 DROP PROCEDURE IF EXISTS `newPartyIdentification` */;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8mb4 */ ;
/*!50003 SET character_set_results = utf8mb4 */ ;
/*!50003 SET collation_connection  = utf8mb4_general_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'STRICT_TRANS_TABLES,NO_AUTO_CREATE_USER,NO_ENGINE_SUBSTITUTION' */ ;
DELIMITER ;;
CREATE DEFINER=`root`@`localhost` PROCEDURE `newPartyIdentification`(
IN id VARCHAR(250),
IN schemeName VARCHAR(50),
OUT party_identification INT)
BEGIN
	INSERT INTO tbl_party_identification (id, schemeName)
    VALUES (id, schemeName);
    
    SET party_identification = last_insert_id();
END ;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;
/*!50003 DROP PROCEDURE IF EXISTS `newPartyName` */;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8mb4 */ ;
/*!50003 SET character_set_results = utf8mb4 */ ;
/*!50003 SET collation_connection  = utf8mb4_general_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'STRICT_TRANS_TABLES,NO_AUTO_CREATE_USER,NO_ENGINE_SUBSTITUTION' */ ;
DELIMITER ;;
CREATE DEFINER=`root`@`localhost` PROCEDURE `newPartyName`(
IN name VARCHAR(300),
OUT party_name INT)
BEGIN
	INSERT INTO tbl_party_name(name)
	VALUES (name);
    
    SET party_name = LAST_INSERT_ID();
END ;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;
/*!50003 DROP PROCEDURE IF EXISTS `newPartyPartyidentification` */;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8mb4 */ ;
/*!50003 SET character_set_results = utf8mb4 */ ;
/*!50003 SET collation_connection  = utf8mb4_general_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'STRICT_TRANS_TABLES,NO_AUTO_CREATE_USER,NO_ENGINE_SUBSTITUTION' */ ;
DELIMITER ;;
CREATE DEFINER=`root`@`localhost` PROCEDURE `newPartyPartyidentification`(
IN party INT,
IN party_identification INT)
BEGIN
	INSERT INTO tbl_party_partyidentification (party, partyidentification)
    VALUES (party, party_identification);
END ;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;
/*!50003 DROP PROCEDURE IF EXISTS `newPartyPartyname` */;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8mb4 */ ;
/*!50003 SET character_set_results = utf8mb4 */ ;
/*!50003 SET collation_connection  = utf8mb4_general_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'STRICT_TRANS_TABLES,NO_AUTO_CREATE_USER,NO_ENGINE_SUBSTITUTION' */ ;
DELIMITER ;;
CREATE DEFINER=`root`@`localhost` PROCEDURE `newPartyPartyname`(
IN party INT,
IN party_name INT)
BEGIN
	INSERT INTO tbl_party_partyname (party, partyname)
    VALUES (party, party_name);
END ;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;
/*!50003 DROP PROCEDURE IF EXISTS `newPartyPostaladdress` */;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8mb4 */ ;
/*!50003 SET character_set_results = utf8mb4 */ ;
/*!50003 SET collation_connection  = utf8mb4_general_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'STRICT_TRANS_TABLES,NO_AUTO_CREATE_USER,NO_ENGINE_SUBSTITUTION' */ ;
DELIMITER ;;
CREATE DEFINER=`root`@`localhost` PROCEDURE `newPartyPostaladdress`(
IN party INT, 
IN postal_address INT)
BEGIN
	INSERT INTO tbl_party_postaladdress (party, postaladdress)
    VALUES (party, postal_address);
END ;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;
/*!50003 DROP PROCEDURE IF EXISTS `newPostaladdress` */;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8mb4 */ ;
/*!50003 SET character_set_results = utf8mb4 */ ;
/*!50003 SET collation_connection  = utf8mb4_general_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'STRICT_TRANS_TABLES,NO_AUTO_CREATE_USER,NO_ENGINE_SUBSTITUTION' */ ;
DELIMITER ;;
CREATE DEFINER=`root`@`localhost` PROCEDURE `newPostaladdress`(
IN addressformatcode INT,
IN cityname VARCHAR(90),
IN postalzone VARCHAR(32),
OUT postal_address INT)
BEGIN
	INSERT INTO tbl_postal_address (addressformatcode, cityname, postalzone)
    VALUES (addressformatcode, cityname, postalzone);
    
    SET postal_address = last_insert_id();
END ;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;
/*!50003 DROP PROCEDURE IF EXISTS `newPostaladdressCountry` */;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8mb4 */ ;
/*!50003 SET character_set_results = utf8mb4 */ ;
/*!50003 SET collation_connection  = utf8mb4_general_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'STRICT_TRANS_TABLES,NO_AUTO_CREATE_USER,NO_ENGINE_SUBSTITUTION' */ ;
DELIMITER ;;
CREATE DEFINER=`root`@`localhost` PROCEDURE `newPostaladdressCountry`(
IN postal_address INT,
IN country INT)
BEGIN
	INSERT INTO tbl_postaladdress_country (postaladdress, country)
    VALUES (postal_address, country);
END ;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2021-05-04 11:23:24
