CREATE DATABASE  IF NOT EXISTS `authandcust` /*!40100 DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci */ /*!80016 DEFAULT ENCRYPTION='N' */;
USE `authandcust`;
-- MySQL dump 10.13  Distrib 8.0.46, for Win64 (x86_64)
--
-- Host: localhost    Database: authandcust
-- ------------------------------------------------------
-- Server version	8.0.46

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
-- Table structure for table `addr_typ_mstr`
--

DROP TABLE IF EXISTS `addr_typ_mstr`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `addr_typ_mstr` (
  `address_type_id` bigint NOT NULL AUTO_INCREMENT,
  `address_type` varchar(50) NOT NULL,
  `description` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`address_type_id`)
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `addr_typ_mstr`
--

LOCK TABLES `addr_typ_mstr` WRITE;
/*!40000 ALTER TABLE `addr_typ_mstr` DISABLE KEYS */;
INSERT INTO `addr_typ_mstr` VALUES (1,'ADDRESS_LINE_1','Primary address line'),(2,'ADDRESS_LINE_2','Secondary address line'),(3,'CITY','City'),(4,'STATE','State or province'),(5,'COUNTRY','Country'),(6,'POSTAL_CODE','Postal or ZIP code');
/*!40000 ALTER TABLE `addr_typ_mstr` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `auth_role`
--

DROP TABLE IF EXISTS `auth_role`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `auth_role` (
  `role_id` bigint NOT NULL AUTO_INCREMENT,
  `role_code` varchar(30) NOT NULL,
  `role_name` varchar(100) NOT NULL,
  `description` varchar(255) DEFAULT NULL,
  `is_active` tinyint(1) DEFAULT '1',
  `AUTHROLE_EFCTV_DT` date DEFAULT NULL,
  `AUTHROLE_CRUD_VALUE` char(1) DEFAULT NULL,
  `AUTHROLE_USER_ID` varchar(100) DEFAULT NULL,
  `AUTHROLE_WS_ID` varchar(100) DEFAULT NULL,
  `AUTHROLE_PRGM_ID` varchar(100) DEFAULT NULL,
  `AUTHROLE_HOST_TS` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `AUTHROLE_LOCAL_TS` timestamp NULL DEFAULT NULL,
  `AUTHROLE_ACPT_TS` timestamp NULL DEFAULT NULL,
  `AUTHROLE_ACPT_TS_UTC_OFS` timestamp NULL DEFAULT NULL,
  `AUTHROLE_UUID` varchar(100) DEFAULT NULL,
  PRIMARY KEY (`role_id`),
  UNIQUE KEY `role_code` (`role_code`),
  CONSTRAINT `chk_auth_role_crud` CHECK ((`AUTHROLE_CRUD_VALUE` in (_utf8mb4'C',_utf8mb4'U',_utf8mb4'D')))
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `auth_role`
--

LOCK TABLES `auth_role` WRITE;
/*!40000 ALTER TABLE `auth_role` DISABLE KEYS */;
INSERT INTO `auth_role` VALUES (1,'CUSTOMER','Customer','Standard banking customer',1,NULL,NULL,NULL,NULL,NULL,'2026-08-27 08:30:02',NULL,NULL,NULL,NULL),(2,'ADMIN','Administrator','System administrator',1,NULL,NULL,NULL,NULL,NULL,'2026-08-27 08:30:02',NULL,NULL,NULL,NULL),(3,'STAFF','Bank Staff','Bank staff user',1,NULL,NULL,NULL,NULL,NULL,'2026-08-27 08:30:02',NULL,NULL,NULL,NULL);
/*!40000 ALTER TABLE `auth_role` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `auth_usr`
--

DROP TABLE IF EXISTS `auth_usr`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `auth_usr` (
  `user_id` bigint NOT NULL AUTO_INCREMENT,
  `CSTID_IDFR` bigint DEFAULT NULL,
  `username` varchar(50) NOT NULL,
  `email` varchar(150) DEFAULT NULL,
  `phone_number` varchar(20) DEFAULT NULL,
  `password_hash` varchar(60) NOT NULL,
  `role_id` bigint NOT NULL,
  `failed_login_attempts` smallint DEFAULT '0',
  `locked_until` timestamp NULL DEFAULT NULL,
  `last_login_at` timestamp NULL DEFAULT NULL,
  `password_changed_at` timestamp NULL DEFAULT NULL,
  `AUTHUSR_EFCTV_DT` date DEFAULT NULL,
  `AUTHUSR_CRUD_VALUE` char(1) DEFAULT NULL,
  `AUTHUSR_USER_ID` varchar(100) DEFAULT NULL,
  `AUTHUSR_WS_ID` varchar(100) DEFAULT NULL,
  `AUTHUSR_PRGM_ID` varchar(100) DEFAULT NULL,
  `AUTHUSR_HOST_TS` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `AUTHUSR_LOCAL_TS` timestamp NULL DEFAULT NULL,
  `AUTHUSR_ACPT_TS` timestamp NULL DEFAULT NULL,
  `AUTHUSR_ACPT_TS_UTC_OFS` timestamp NULL DEFAULT NULL,
  `AUTHUSR_UUID` varchar(100) DEFAULT NULL,
  PRIMARY KEY (`user_id`),
  UNIQUE KEY `username` (`username`),
  UNIQUE KEY `email` (`email`),
  UNIQUE KEY `CSTID_IDFR` (`CSTID_IDFR`),
  UNIQUE KEY `uk_auth_usr_phone` (`phone_number`),
  KEY `fk_auth_user_role` (`role_id`),
  CONSTRAINT `fk_auth_user_customer` FOREIGN KEY (`CSTID_IDFR`) REFERENCES `cust_dtl` (`CSTID_IDFR`),
  CONSTRAINT `fk_auth_user_role` FOREIGN KEY (`role_id`) REFERENCES `auth_role` (`role_id`),
  CONSTRAINT `chk_auth_usr_crud` CHECK ((`AUTHUSR_CRUD_VALUE` in (_utf8mb4'C',_utf8mb4'U',_utf8mb4'D'))),
  CONSTRAINT `chk_auth_usr_email_or_phone` CHECK (((`email` is not null) or (`phone_number` is not null)))
) ENGINE=InnoDB AUTO_INCREMENT=10 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `auth_usr`
--

LOCK TABLES `auth_usr` WRITE;
/*!40000 ALTER TABLE `auth_usr` DISABLE KEYS */;
INSERT INTO `auth_usr` VALUES (2,2,'rahul@gmail.com','rahul@gmail.com','9876543210','$2a$10$evkoSGR6LRAlt09u5Ew/XefWA2PxPL197dHjonzRXw3JfWY1LW2fK',1,0,NULL,NULL,NULL,'2026-08-28','C','SYSTEM','AUTH_SERVICE','REGISTRATION','2026-08-28 06:19:34','2026-08-28 06:19:34','2026-08-28 06:19:34','2026-08-28 06:19:34','cb1ffed2-0d9e-4187-a3dd-0886133936a7'),(3,3,'sundarp@gmail.com','sundarp@gmail.com',NULL,'$2a$10$ygjTCbeU7wIMs4GIaquEy.InbemJlD1ihKT558Es8g0b8e99HOmqq',1,0,NULL,NULL,NULL,'2026-08-28','C','SYSTEM','AUTH_SERVICE','REGISTRATION','2026-08-28 15:00:04','2026-08-28 15:00:04','2026-08-28 15:00:04','2026-08-28 15:00:04','a33ccef7-777d-4aa3-a9dc-803c83c0e9a1'),(4,4,'arjun.test@gmail.com','arjun.test@gmail.com','9123456780','$2a$10$1RyceWNQtgnxjPRMjgqvEOF6MU.403yrgbZR3B/DpwqVVh5SpbMMi',1,0,NULL,NULL,NULL,'2026-08-29','C','SYSTEM','AUTH_SERVICE','REGISTRATION','2026-08-29 14:47:39','2026-08-29 14:47:39','2026-08-29 14:47:39','2026-08-29 14:47:39','abebb732-99f4-4ced-bc6b-3936f6d1fcf0'),(5,5,'testkyc@example.com','testkyc@example.com',NULL,'$2a$10$2YvhyFhDL003hZTO16xI9.z2EartdNRj977JY9thtE4st9gRMHtqm',1,0,NULL,NULL,NULL,'2026-10-06','C','SYSTEM','AUTH_SERVICE','REGISTRATION','2026-10-06 14:46:55','2026-10-06 14:46:55','2026-10-06 14:46:55','2026-10-06 14:46:55','b2488359-8aca-470f-8ead-4070ab23ea74'),(6,6,'rishtest2026@gmail.com','rishtest2026@gmail.com','+919876543219','$2a$10$srRFiFudE4pFTPrTGgqQLucnjXfO86zVSJpze8X6IjLCB/68DDpXC',1,0,NULL,NULL,NULL,'2026-10-09','C','SYSTEM','AUTH_SERVICE','REGISTRATION','2026-10-08 19:08:15','2026-10-08 19:08:15','2026-10-08 19:08:15','2026-10-08 19:08:15','e68fa34d-bd95-4c3e-897b-9cad37aecf2e'),(7,7,'bankadmin2026@gmail.com','bankadmin2026@gmail.com','+919876543220','$2a$10$Z22HfDxfyLpgqWCKG3mt2eNCo217q.BHf/5dKFDgNJwKQ6/HcxfSK',2,0,NULL,NULL,NULL,'2026-10-09','C','SYSTEM','AUTH_SERVICE','REGISTRATION','2026-10-09 09:22:26','2026-10-09 09:22:26','2026-10-09 09:22:26','2026-10-09 09:22:26','8f5caf5a-37c3-4260-887d-32c78ec663f6'),(8,8,'ram.uat.2026@example.com','ram.uat.2026@example.com','+919876543210','$2a$10$geyDv3VW528HGOkLGjoQeOlHxbpmq58U9z2mkv/I4.XWHnquFwQp6',1,0,NULL,NULL,NULL,'2026-10-13','C','SYSTEM','AUTH_SERVICE','REGISTRATION','2026-10-13 17:10:11','2026-10-13 17:10:11','2026-10-13 17:10:11','2026-10-13 17:10:11','153651c4-b61d-4f78-8163-dab0e4deb39f'),(9,NULL,'jack.uat.2026@example.com','jack.uat.2026@example.com','+91987654444','$2a$10$1VybelPTaYegpve1yRq10eV5f6uBDzIFeoWZ2w.j2A1a30ZD7A/HS',3,0,NULL,NULL,NULL,'2026-10-13','C','SYSTEM','AUTH_SERVICE','ADMIN_CREATE_EMPLOYEE','2026-10-13 17:50:55','2026-10-13 17:50:55','2026-10-13 17:50:55','2026-10-13 17:50:55','471f3ba0-bfc5-4c89-a473-83c46baaef13');
/*!40000 ALTER TABLE `auth_usr` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `cntct_typ_mstr`
--

DROP TABLE IF EXISTS `cntct_typ_mstr`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `cntct_typ_mstr` (
  `contact_type_id` bigint NOT NULL AUTO_INCREMENT,
  `contact_type` varchar(50) NOT NULL,
  `description` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`contact_type_id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `cntct_typ_mstr`
--

LOCK TABLES `cntct_typ_mstr` WRITE;
/*!40000 ALTER TABLE `cntct_typ_mstr` DISABLE KEYS */;
INSERT INTO `cntct_typ_mstr` VALUES (1,'MOBILE','Customer mobile number'),(2,'EMAIL','Customer email address');
/*!40000 ALTER TABLE `cntct_typ_mstr` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `cust_acct`
--

DROP TABLE IF EXISTS `cust_acct`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `cust_acct` (
  `account_id` bigint NOT NULL AUTO_INCREMENT,
  `CSTID_IDFR` bigint NOT NULL,
  `product_id` bigint NOT NULL,
  `branch_id` bigint NOT NULL,
  `account_number` varchar(30) NOT NULL,
  `status` varchar(30) NOT NULL,
  `opened_on` date NOT NULL,
  `closed_on` date DEFAULT NULL,
  `CSTACCT_EFCTV_DT` date DEFAULT NULL,
  `CSTACCT_CRUD_VALUE` char(1) DEFAULT NULL,
  `CSTACCT_USER_ID` varchar(100) DEFAULT NULL,
  `CSTACCT_WS_ID` varchar(100) DEFAULT NULL,
  `CSTACCT_PRGM_ID` varchar(100) DEFAULT NULL,
  `CSTACCT_HOST_TS` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `CSTACCT_LOCAL_TS` timestamp NULL DEFAULT NULL,
  `CSTACCT_ACPT_TS` timestamp NULL DEFAULT NULL,
  `CSTACCT_ACPT_TS_UTC_OFS` timestamp NULL DEFAULT NULL,
  `CSTACCT_UUID` varchar(100) DEFAULT NULL,
  PRIMARY KEY (`account_id`),
  UNIQUE KEY `account_number` (`account_number`),
  KEY `fk_cust_acct_customer` (`CSTID_IDFR`),
  CONSTRAINT `fk_cust_acct_customer` FOREIGN KEY (`CSTID_IDFR`) REFERENCES `cust_dtl` (`CSTID_IDFR`),
  CONSTRAINT `chk_cust_acct_crud` CHECK ((`CSTACCT_CRUD_VALUE` in (_utf8mb4'C',_utf8mb4'U',_utf8mb4'D')))
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `cust_acct`
--

LOCK TABLES `cust_acct` WRITE;
/*!40000 ALTER TABLE `cust_acct` DISABLE KEYS */;
INSERT INTO `cust_acct` VALUES (1,6,1,100,'1015690584876728','ACTIVE','2026-10-09',NULL,'2026-10-09','C','SYSTEM','AUTH_SERVICE','ACCOUNT_OPEN','2026-10-09 18:04:18','2026-10-09 18:04:18','2026-10-09 18:04:18','2026-10-09 18:04:18','9ec354f2-d1b9-4976-948c-12afe2bc2664'),(2,8,9,125,'1019119456866145','ACTIVE','2026-10-13',NULL,'2026-10-13','C','SYSTEM','AUTH_SERVICE','ACCOUNT_OPEN','2026-10-13 17:19:06','2026-10-13 17:19:06','2026-10-13 17:19:06','2026-10-13 17:19:06','3fcffb26-ddf8-407f-b8e6-d672c92f0e31');
/*!40000 ALTER TABLE `cust_acct` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `cust_addr_cmpnt`
--

DROP TABLE IF EXISTS `cust_addr_cmpnt`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `cust_addr_cmpnt` (
  `CSTADCMP_ID` bigint NOT NULL AUTO_INCREMENT,
  `CSTID_IDFR` bigint NOT NULL,
  `CSTADCMP_TYPE_ID` bigint NOT NULL,
  `CSTADCMP_TYPE_VALUE` varchar(255) NOT NULL,
  `CSTADCMP_EFFCTV_DT` date DEFAULT NULL,
  `CSTADCMP_CRUD_VAL` char(1) DEFAULT NULL,
  `CSTADCMP_USR_ID` varchar(100) DEFAULT NULL,
  `CSTADCMP_WS_ID` varchar(100) DEFAULT NULL,
  `CSTADCMP_PRGM_ID` varchar(100) DEFAULT NULL,
  `CSTADCMP_HOST_TS` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `CSTADCMP_LOCAL_TS` timestamp NULL DEFAULT NULL,
  `CSTADCMP_ACPT_TS` timestamp NULL DEFAULT NULL,
  `CSTADCMP_ACPT_TS_UTC_OFS` timestamp NULL DEFAULT NULL,
  `CSTADCMP_UUID` varchar(100) DEFAULT NULL,
  PRIMARY KEY (`CSTADCMP_ID`),
  KEY `fk_cust_addr_customer` (`CSTID_IDFR`),
  KEY `fk_cust_addr_type` (`CSTADCMP_TYPE_ID`),
  CONSTRAINT `fk_cust_addr_customer` FOREIGN KEY (`CSTID_IDFR`) REFERENCES `cust_dtl` (`CSTID_IDFR`),
  CONSTRAINT `fk_cust_addr_type` FOREIGN KEY (`CSTADCMP_TYPE_ID`) REFERENCES `addr_typ_mstr` (`address_type_id`),
  CONSTRAINT `chk_cust_addr_cmpnt_crud` CHECK ((`CSTADCMP_CRUD_VAL` in (_utf8mb4'C',_utf8mb4'U',_utf8mb4'D')))
) ENGINE=InnoDB AUTO_INCREMENT=19 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `cust_addr_cmpnt`
--

LOCK TABLES `cust_addr_cmpnt` WRITE;
/*!40000 ALTER TABLE `cust_addr_cmpnt` DISABLE KEYS */;
INSERT INTO `cust_addr_cmpnt` VALUES (1,5,1,'123 MG Road','2026-10-06','C','SYSTEM','AUTH_SERVICE','CUSTOMER_KYC','2026-10-06 14:50:27','2026-10-06 14:50:27','2026-10-06 14:50:27','2026-10-06 14:50:27','35483d6d-cbaa-40bf-b36b-8c4a44e96191'),(2,5,2,'Near Main Circle','2026-10-06','C','SYSTEM','AUTH_SERVICE','CUSTOMER_KYC','2026-10-06 14:50:27','2026-10-06 14:50:27','2026-10-06 14:50:27','2026-10-06 14:50:27','35483d6d-cbaa-40bf-b36b-8c4a44e96191'),(3,5,3,'Bengaluru','2026-10-06','C','SYSTEM','AUTH_SERVICE','CUSTOMER_KYC','2026-10-06 14:50:27','2026-10-06 14:50:27','2026-10-06 14:50:27','2026-10-06 14:50:27','35483d6d-cbaa-40bf-b36b-8c4a44e96191'),(4,5,4,'Karnataka','2026-10-06','C','SYSTEM','AUTH_SERVICE','CUSTOMER_KYC','2026-10-06 14:50:27','2026-10-06 14:50:27','2026-10-06 14:50:27','2026-10-06 14:50:27','35483d6d-cbaa-40bf-b36b-8c4a44e96191'),(5,5,5,'India','2026-10-06','C','SYSTEM','AUTH_SERVICE','CUSTOMER_KYC','2026-10-06 14:50:27','2026-10-06 14:50:27','2026-10-06 14:50:27','2026-10-06 14:50:27','35483d6d-cbaa-40bf-b36b-8c4a44e96191'),(6,5,6,'560001','2026-10-06','C','SYSTEM','AUTH_SERVICE','CUSTOMER_KYC','2026-10-06 14:50:27','2026-10-06 14:50:27','2026-10-06 14:50:27','2026-10-06 14:50:27','35483d6d-cbaa-40bf-b36b-8c4a44e96191'),(7,6,1,'45 Residency Road','2026-10-09','C','SYSTEM','AUTH_SERVICE','CUSTOMER_KYC','2026-10-08 19:13:03','2026-10-08 19:13:03','2026-10-08 19:13:03','2026-10-08 19:13:03','e8670be7-9948-499f-9a03-2f8603a1f9da'),(8,6,2,'Near Richmond Circle','2026-10-09','C','SYSTEM','AUTH_SERVICE','CUSTOMER_KYC','2026-10-08 19:13:03','2026-10-08 19:13:03','2026-10-08 19:13:03','2026-10-08 19:13:03','e8670be7-9948-499f-9a03-2f8603a1f9da'),(9,6,3,'Bengaluru','2026-10-09','C','SYSTEM','AUTH_SERVICE','CUSTOMER_KYC','2026-10-08 19:13:03','2026-10-08 19:13:03','2026-10-08 19:13:03','2026-10-08 19:13:03','e8670be7-9948-499f-9a03-2f8603a1f9da'),(10,6,4,'Karnataka','2026-10-09','C','SYSTEM','AUTH_SERVICE','CUSTOMER_KYC','2026-10-08 19:13:03','2026-10-08 19:13:03','2026-10-08 19:13:03','2026-10-08 19:13:03','e8670be7-9948-499f-9a03-2f8603a1f9da'),(11,6,5,'India','2026-10-09','C','SYSTEM','AUTH_SERVICE','CUSTOMER_KYC','2026-10-08 19:13:03','2026-10-08 19:13:03','2026-10-08 19:13:03','2026-10-08 19:13:03','e8670be7-9948-499f-9a03-2f8603a1f9da'),(12,6,6,'560025','2026-10-09','C','SYSTEM','AUTH_SERVICE','CUSTOMER_KYC','2026-10-08 19:13:03','2026-10-08 19:13:03','2026-10-08 19:13:03','2026-10-08 19:13:03','e8670be7-9948-499f-9a03-2f8603a1f9da'),(13,8,1,'22 MG Road','2026-10-13','C','SYSTEM','AUTH_SERVICE','CUSTOMER_KYC','2026-10-13 17:16:26','2026-10-13 17:16:26','2026-10-13 17:16:26','2026-10-13 17:16:26','999fd6b9-c54c-49d4-bf3c-b0b937aec964'),(14,8,2,'Near Trinity Metro Station','2026-10-13','C','SYSTEM','AUTH_SERVICE','CUSTOMER_KYC','2026-10-13 17:16:26','2026-10-13 17:16:26','2026-10-13 17:16:26','2026-10-13 17:16:26','999fd6b9-c54c-49d4-bf3c-b0b937aec964'),(15,8,3,'Bengaluru','2026-10-13','C','SYSTEM','AUTH_SERVICE','CUSTOMER_KYC','2026-10-13 17:16:26','2026-10-13 17:16:26','2026-10-13 17:16:26','2026-10-13 17:16:26','999fd6b9-c54c-49d4-bf3c-b0b937aec964'),(16,8,4,'Karnataka','2026-10-13','C','SYSTEM','AUTH_SERVICE','CUSTOMER_KYC','2026-10-13 17:16:26','2026-10-13 17:16:26','2026-10-13 17:16:26','2026-10-13 17:16:26','999fd6b9-c54c-49d4-bf3c-b0b937aec964'),(17,8,5,'India','2026-10-13','C','SYSTEM','AUTH_SERVICE','CUSTOMER_KYC','2026-10-13 17:16:26','2026-10-13 17:16:26','2026-10-13 17:16:26','2026-10-13 17:16:26','999fd6b9-c54c-49d4-bf3c-b0b937aec964'),(18,8,6,'560001','2026-10-13','C','SYSTEM','AUTH_SERVICE','CUSTOMER_KYC','2026-10-13 17:16:26','2026-10-13 17:16:26','2026-10-13 17:16:26','2026-10-13 17:16:26','999fd6b9-c54c-49d4-bf3c-b0b937aec964');
/*!40000 ALTER TABLE `cust_addr_cmpnt` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `cust_cl`
--

DROP TABLE IF EXISTS `cust_cl`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `cust_cl` (
  `CSTCL_ID` bigint NOT NULL AUTO_INCREMENT,
  `CSTCL_TYP` varchar(100) NOT NULL,
  `CSTCL_TYP_VALUE` varchar(100) NOT NULL,
  `CSTCL_CRUD_VALUE` char(1) DEFAULT NULL,
  `CSTCL_USER_ID` varchar(100) DEFAULT NULL,
  `CSTCL_WS_ID` varchar(100) DEFAULT NULL,
  `CSTCL_PRGM_ID` varchar(100) DEFAULT NULL,
  `CSTCL_HOST_TS` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `CSTCL_LOCAL_TS` timestamp NULL DEFAULT NULL,
  `CSTCL_ACPT_TS` timestamp NULL DEFAULT NULL,
  `CSTCL_ACPT_TS_UTC_OFST` timestamp NULL DEFAULT NULL,
  `CSTCL_UUID` varchar(100) DEFAULT NULL,
  PRIMARY KEY (`CSTCL_ID`),
  UNIQUE KEY `uq_cust_cl_type_value` (`CSTCL_TYP`,`CSTCL_TYP_VALUE`),
  CONSTRAINT `chk_cust_cl_crud` CHECK ((`CSTCL_CRUD_VALUE` in (_cp850'C',_cp850'U',_cp850'D')))
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `cust_cl`
--

LOCK TABLES `cust_cl` WRITE;
/*!40000 ALTER TABLE `cust_cl` DISABLE KEYS */;
INSERT INTO `cust_cl` VALUES (1,'AGE','MINOR','C','SYSTEM','AUTH_SERVICE','CUSTOMER_CLASSIFICATION','2026-10-08 18:23:15',NULL,NULL,NULL,NULL),(2,'AGE','ADULT','C','SYSTEM','AUTH_SERVICE','CUSTOMER_CLASSIFICATION','2026-10-08 18:23:15',NULL,NULL,NULL,NULL),(3,'AGE','SENIOR_CITIZEN','C','SYSTEM','AUTH_SERVICE','CUSTOMER_CLASSIFICATION','2026-10-08 18:23:15',NULL,NULL,NULL,NULL);
/*!40000 ALTER TABLE `cust_cl` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `cust_cntct_dtls`
--

DROP TABLE IF EXISTS `cust_cntct_dtls`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `cust_cntct_dtls` (
  `CSTCNDT_REC_ID` bigint NOT NULL AUTO_INCREMENT,
  `CSTID_IDFR` bigint NOT NULL,
  `CSTCNDT_ID` bigint NOT NULL,
  `CSTCNDT_VALUE` varchar(255) NOT NULL,
  `CSTCNDT_STRT_DT` date DEFAULT NULL,
  `CSTCNDT_END_DT` date DEFAULT NULL,
  `CSTPRID_EFFCTV_DT` date DEFAULT NULL,
  `CSTPRID_CRUD_VAL` char(1) DEFAULT NULL,
  `CSTPRID_USR_ID` varchar(100) DEFAULT NULL,
  `CSTPRID_WS_ID` varchar(100) DEFAULT NULL,
  `CSTPRID_PRGM_ID` varchar(100) DEFAULT NULL,
  `CSTPRID_HOST_TS` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `CSTPRID_LOCAL_TS` timestamp NULL DEFAULT NULL,
  `CSTPRID_ACPT_TS` timestamp NULL DEFAULT NULL,
  `CSTPRID_ACPT_TS_UTC_OFST` timestamp NULL DEFAULT NULL,
  `CSTPRID_UUID` varchar(100) DEFAULT NULL,
  PRIMARY KEY (`CSTCNDT_REC_ID`),
  KEY `fk_cust_cntct_customer` (`CSTID_IDFR`),
  KEY `fk_cust_cntct_type` (`CSTCNDT_ID`),
  CONSTRAINT `fk_cust_cntct_customer` FOREIGN KEY (`CSTID_IDFR`) REFERENCES `cust_dtl` (`CSTID_IDFR`),
  CONSTRAINT `fk_cust_cntct_type` FOREIGN KEY (`CSTCNDT_ID`) REFERENCES `cntct_typ_mstr` (`contact_type_id`),
  CONSTRAINT `chk_cust_cntct_dtls_crud` CHECK ((`CSTPRID_CRUD_VAL` in (_utf8mb4'C',_utf8mb4'U',_utf8mb4'D')))
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `cust_cntct_dtls`
--

LOCK TABLES `cust_cntct_dtls` WRITE;
/*!40000 ALTER TABLE `cust_cntct_dtls` DISABLE KEYS */;
INSERT INTO `cust_cntct_dtls` VALUES (1,5,1,'+919999999999',NULL,NULL,'2026-10-06','C','SYSTEM','AUTH_SERVICE','CUSTOMER_KYC','2026-10-06 14:50:27','2026-10-06 14:50:27','2026-10-06 14:50:27','2026-10-06 14:50:27','35483d6d-cbaa-40bf-b36b-8c4a44e96191'),(2,5,2,'testkyc@example.com',NULL,NULL,'2026-10-06','C','SYSTEM','AUTH_SERVICE','CUSTOMER_KYC','2026-10-06 14:50:27','2026-10-06 14:50:27','2026-10-06 14:50:27','2026-10-06 14:50:27','35483d6d-cbaa-40bf-b36b-8c4a44e96191'),(3,6,1,'+919876543211',NULL,NULL,'2026-10-09','C','SYSTEM','AUTH_SERVICE','CUSTOMER_KYC','2026-10-08 19:13:03','2026-10-08 19:13:03','2026-10-08 19:13:03','2026-10-08 19:13:03','e8670be7-9948-499f-9a03-2f8603a1f9da'),(4,6,2,'rishclassification2026@example.com',NULL,NULL,'2026-10-09','C','SYSTEM','AUTH_SERVICE','CUSTOMER_KYC','2026-10-08 19:13:03','2026-10-08 19:13:03','2026-10-08 19:13:03','2026-10-08 19:13:03','e8670be7-9948-499f-9a03-2f8603a1f9da'),(5,8,1,'+919876543210',NULL,NULL,'2026-10-13','C','SYSTEM','AUTH_SERVICE','CUSTOMER_KYC','2026-10-13 17:16:26','2026-10-13 17:16:26','2026-10-13 17:16:26','2026-10-13 17:16:26','999fd6b9-c54c-49d4-bf3c-b0b937aec964'),(6,8,2,'ram.uat.2026@example.com',NULL,NULL,'2026-10-13','C','SYSTEM','AUTH_SERVICE','CUSTOMER_KYC','2026-10-13 17:16:26','2026-10-13 17:16:26','2026-10-13 17:16:26','2026-10-13 17:16:26','999fd6b9-c54c-49d4-bf3c-b0b937aec964');
/*!40000 ALTER TABLE `cust_cntct_dtls` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `cust_dtl`
--

DROP TABLE IF EXISTS `cust_dtl`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `cust_dtl` (
  `CSTID_IDFR` bigint NOT NULL AUTO_INCREMENT,
  `CUST_TYPE` bigint NOT NULL,
  `CUST_FULL_NAME` varchar(255) NOT NULL,
  `CUST_DOB` date DEFAULT NULL,
  `CUST_STATUS` bigint NOT NULL,
  `CUST_GENDR` varchar(20) DEFAULT NULL,
  `CUST_PRFRD_LNG` varchar(50) DEFAULT NULL,
  `CUST_CNTRY_ORGTN` varchar(100) DEFAULT NULL,
  `CUST_EFCTV_DT` date DEFAULT NULL,
  `CUST_CRUD_VALUE` char(1) DEFAULT NULL,
  `CUST_USR_ID` varchar(100) DEFAULT NULL,
  `CUST_WS_ID` varchar(100) DEFAULT NULL,
  `CUST_PRGM_ID` varchar(100) DEFAULT NULL,
  `CUST_HOST_TS` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `CUST_LOCAL_TS` timestamp NULL DEFAULT NULL,
  `CUST_ACPT_TS` timestamp NULL DEFAULT NULL,
  `CUST_ACPT_TS_UTC_OFST` timestamp NULL DEFAULT NULL,
  `CUST_UUID` varchar(100) DEFAULT NULL,
  `CSTCL_ID` bigint DEFAULT NULL,
  PRIMARY KEY (`CSTID_IDFR`),
  KEY `fk_cust_type` (`CUST_TYPE`),
  KEY `fk_cust_status` (`CUST_STATUS`),
  KEY `idx_cust_dtl_cl` (`CSTCL_ID`),
  CONSTRAINT `fk_cust_dtl_cl` FOREIGN KEY (`CSTCL_ID`) REFERENCES `cust_cl` (`CSTCL_ID`),
  CONSTRAINT `fk_cust_status` FOREIGN KEY (`CUST_STATUS`) REFERENCES `cust_sts_mstr` (`status_id`),
  CONSTRAINT `fk_cust_type` FOREIGN KEY (`CUST_TYPE`) REFERENCES `cust_typ_mstr` (`type_id`),
  CONSTRAINT `chk_cust_dtl_crud` CHECK ((`CUST_CRUD_VALUE` in (_utf8mb4'C',_utf8mb4'U',_utf8mb4'D')))
) ENGINE=InnoDB AUTO_INCREMENT=9 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `cust_dtl`
--

LOCK TABLES `cust_dtl` WRITE;
/*!40000 ALTER TABLE `cust_dtl` DISABLE KEYS */;
INSERT INTO `cust_dtl` VALUES (2,1,'Rahul Sharma',NULL,2,NULL,NULL,NULL,'2026-08-28','C','SYSTEM','AUTH_SERVICE','REGISTRATION','2026-08-28 06:19:34','2026-08-28 06:19:34','2026-08-28 06:19:34','2026-08-28 06:19:34','cb1ffed2-0d9e-4187-a3dd-0886133936a7',NULL),(3,1,'Sundar Pichai',NULL,2,NULL,NULL,NULL,'2026-08-28','C','SYSTEM','AUTH_SERVICE','REGISTRATION','2026-08-28 15:00:04','2026-08-28 15:00:04','2026-08-28 15:00:04','2026-08-28 15:00:04','a33ccef7-777d-4aa3-a9dc-803c83c0e9a1',NULL),(4,1,'Arjun Mehta',NULL,2,NULL,NULL,NULL,'2026-08-29','C','SYSTEM','AUTH_SERVICE','REGISTRATION','2026-08-29 14:47:39','2026-08-29 14:47:39','2026-08-29 14:47:39','2026-08-29 14:47:39','abebb732-99f4-4ced-bc6b-3936f6d1fcf0',NULL),(5,1,'Test KYC Customer','2004-05-15',2,'MALE','ENGLISH','INDIA','2026-10-06','U','SYSTEM','AUTH_SERVICE','CUSTOMER_KYC','2026-10-06 14:50:27','2026-10-06 14:50:27','2026-10-06 14:50:27','2026-10-06 14:50:27','35483d6d-cbaa-40bf-b36b-8c4a44e96191',NULL),(6,1,'Rish Classification Test','2004-05-15',2,'MALE','ENGLISH','INDIA','2026-10-09','U','SYSTEM','AUTH_SERVICE','CUSTOMER_KYC','2026-10-08 19:13:03','2026-10-08 19:13:03','2026-10-08 19:13:03','2026-10-08 19:13:03','e8670be7-9948-499f-9a03-2f8603a1f9da',2),(7,1,'Bank Admin',NULL,4,NULL,NULL,NULL,'2026-10-09','C','SYSTEM','AUTH_SERVICE','REGISTRATION','2026-10-09 09:22:26','2026-10-09 09:22:26','2026-10-09 09:22:26','2026-10-09 09:22:26','8f5caf5a-37c3-4260-887d-32c78ec663f6',NULL),(8,1,'Ram Kumar Sharma','2004-05-15',2,'MALE','ENGLISH','INDIA','2026-10-13','U','SYSTEM','AUTH_SERVICE','CUSTOMER_KYC','2026-10-13 17:16:26','2026-10-13 17:16:26','2026-10-13 17:16:26','2026-10-13 17:16:26','999fd6b9-c54c-49d4-bf3c-b0b937aec964',2);
/*!40000 ALTER TABLE `cust_dtl` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `cust_idfn`
--

DROP TABLE IF EXISTS `cust_idfn`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `cust_idfn` (
  `CSTID_REC_ID` bigint NOT NULL AUTO_INCREMENT,
  `CSTID_IDFR` bigint NOT NULL,
  `CSTCL_ID_TYPE` bigint NOT NULL,
  `CSTID_ID_ITEM` varchar(100) NOT NULL,
  `CSTID_EFCTV_DT` date DEFAULT NULL,
  `CSTID_CRUD_VALUE` char(1) DEFAULT NULL,
  `CSTID_USER_ID` varchar(100) DEFAULT NULL,
  `CSTID_WS_ID` varchar(100) DEFAULT NULL,
  `CSTID_PRGM_ID` varchar(100) DEFAULT NULL,
  `CSTID_HOST_TS` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `CSTID_LOCAL_TS` timestamp NULL DEFAULT NULL,
  `CSTID_ACPT_TS` timestamp NULL DEFAULT NULL,
  `CSTID_ACPT_TS_UTC_OFST` timestamp NULL DEFAULT NULL,
  `CSTID_UUID` varchar(100) DEFAULT NULL,
  PRIMARY KEY (`CSTID_REC_ID`),
  KEY `fk_cust_idfn_customer` (`CSTID_IDFR`),
  KEY `fk_cust_idfn_type` (`CSTCL_ID_TYPE`),
  CONSTRAINT `fk_cust_idfn_customer` FOREIGN KEY (`CSTID_IDFR`) REFERENCES `cust_dtl` (`CSTID_IDFR`),
  CONSTRAINT `fk_cust_idfn_type` FOREIGN KEY (`CSTCL_ID_TYPE`) REFERENCES `idfn_typ_mstr` (`identification_type_id`),
  CONSTRAINT `chk_cust_idfn_crud` CHECK ((`CSTID_CRUD_VALUE` in (_utf8mb4'C',_utf8mb4'U',_utf8mb4'D')))
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `cust_idfn`
--

LOCK TABLES `cust_idfn` WRITE;
/*!40000 ALTER TABLE `cust_idfn` DISABLE KEYS */;
INSERT INTO `cust_idfn` VALUES (1,5,1,'ABCDE1234F','2026-10-06','C','SYSTEM','AUTH_SERVICE','CUSTOMER_KYC','2026-10-06 14:50:27','2026-10-06 14:50:27','2026-10-06 14:50:27','2026-10-06 14:50:27','35483d6d-cbaa-40bf-b36b-8c4a44e96191'),(2,6,1,'PQRSX5678K','2026-10-09','C','SYSTEM','AUTH_SERVICE','CUSTOMER_KYC','2026-10-08 19:13:03','2026-10-08 19:13:03','2026-10-08 19:13:03','2026-10-08 19:13:03','e8670be7-9948-499f-9a03-2f8603a1f9da'),(3,8,1,'ABCPR1234F','2026-10-13','C','SYSTEM','AUTH_SERVICE','CUSTOMER_KYC','2026-10-13 17:16:26','2026-10-13 17:16:26','2026-10-13 17:16:26','2026-10-13 17:16:26','999fd6b9-c54c-49d4-bf3c-b0b937aec964');
/*!40000 ALTER TABLE `cust_idfn` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `cust_nme_cmpnt`
--

DROP TABLE IF EXISTS `cust_nme_cmpnt`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `cust_nme_cmpnt` (
  `CSTNMCMP_ID` bigint NOT NULL AUTO_INCREMENT,
  `CSTID_IDFR` bigint NOT NULL,
  `CSTNMCMP_TYPE_ID` bigint NOT NULL,
  `CSTNMCMP_TYPE_VALUE` varchar(255) NOT NULL,
  `CSTNMCMP_EFFCTV_DT` date DEFAULT NULL,
  `CSTNMCMP_CRUD_VAL` char(1) DEFAULT NULL,
  `CSTNMCMP_USR_ID` varchar(100) DEFAULT NULL,
  `CSTNMCMP_WS_ID` varchar(100) DEFAULT NULL,
  `CSTNMCMP_PRGM_ID` varchar(100) DEFAULT NULL,
  `CSTNMCMP_HOST_TS` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `CSTNMCMP_LOCAL_TS` timestamp NULL DEFAULT NULL,
  `CSTNMCMP_ACPT_TS` timestamp NULL DEFAULT NULL,
  `CSTNMCMP_ACPT_TS_UTC_OFS` timestamp NULL DEFAULT NULL,
  `CSTNMCMP_UUID` varchar(100) DEFAULT NULL,
  PRIMARY KEY (`CSTNMCMP_ID`),
  KEY `fk_cust_name_customer` (`CSTID_IDFR`),
  KEY `fk_cust_name_type` (`CSTNMCMP_TYPE_ID`),
  CONSTRAINT `fk_cust_name_customer` FOREIGN KEY (`CSTID_IDFR`) REFERENCES `cust_dtl` (`CSTID_IDFR`),
  CONSTRAINT `fk_cust_name_type` FOREIGN KEY (`CSTNMCMP_TYPE_ID`) REFERENCES `nme_typ_mstr` (`name_type_id`),
  CONSTRAINT `chk_cust_nme_cmpnt_crud` CHECK ((`CSTNMCMP_CRUD_VAL` in (_utf8mb4'C',_utf8mb4'U',_utf8mb4'D')))
) ENGINE=InnoDB AUTO_INCREMENT=10 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `cust_nme_cmpnt`
--

LOCK TABLES `cust_nme_cmpnt` WRITE;
/*!40000 ALTER TABLE `cust_nme_cmpnt` DISABLE KEYS */;
INSERT INTO `cust_nme_cmpnt` VALUES (1,5,1,'Test','2026-10-06','C','SYSTEM','AUTH_SERVICE','CUSTOMER_KYC','2026-10-06 14:50:27','2026-10-06 14:50:27','2026-10-06 14:50:27','2026-10-06 14:50:27','35483d6d-cbaa-40bf-b36b-8c4a44e96191'),(2,5,2,'KYC','2026-10-06','C','SYSTEM','AUTH_SERVICE','CUSTOMER_KYC','2026-10-06 14:50:27','2026-10-06 14:50:27','2026-10-06 14:50:27','2026-10-06 14:50:27','35483d6d-cbaa-40bf-b36b-8c4a44e96191'),(3,5,3,'Customer','2026-10-06','C','SYSTEM','AUTH_SERVICE','CUSTOMER_KYC','2026-10-06 14:50:27','2026-10-06 14:50:27','2026-10-06 14:50:27','2026-10-06 14:50:27','35483d6d-cbaa-40bf-b36b-8c4a44e96191'),(4,6,1,'Rish','2026-10-09','C','SYSTEM','AUTH_SERVICE','CUSTOMER_KYC','2026-10-08 19:13:03','2026-10-08 19:13:03','2026-10-08 19:13:03','2026-10-08 19:13:03','e8670be7-9948-499f-9a03-2f8603a1f9da'),(5,6,2,'Classification','2026-10-09','C','SYSTEM','AUTH_SERVICE','CUSTOMER_KYC','2026-10-08 19:13:03','2026-10-08 19:13:03','2026-10-08 19:13:03','2026-10-08 19:13:03','e8670be7-9948-499f-9a03-2f8603a1f9da'),(6,6,3,'Test','2026-10-09','C','SYSTEM','AUTH_SERVICE','CUSTOMER_KYC','2026-10-08 19:13:03','2026-10-08 19:13:03','2026-10-08 19:13:03','2026-10-08 19:13:03','e8670be7-9948-499f-9a03-2f8603a1f9da'),(7,8,1,'Ram','2026-10-13','C','SYSTEM','AUTH_SERVICE','CUSTOMER_KYC','2026-10-13 17:16:26','2026-10-13 17:16:26','2026-10-13 17:16:26','2026-10-13 17:16:26','999fd6b9-c54c-49d4-bf3c-b0b937aec964'),(8,8,2,'Kumar','2026-10-13','C','SYSTEM','AUTH_SERVICE','CUSTOMER_KYC','2026-10-13 17:16:26','2026-10-13 17:16:26','2026-10-13 17:16:26','2026-10-13 17:16:26','999fd6b9-c54c-49d4-bf3c-b0b937aec964'),(9,8,3,'Sharma','2026-10-13','C','SYSTEM','AUTH_SERVICE','CUSTOMER_KYC','2026-10-13 17:16:26','2026-10-13 17:16:26','2026-10-13 17:16:26','2026-10-13 17:16:26','999fd6b9-c54c-49d4-bf3c-b0b937aec964');
/*!40000 ALTER TABLE `cust_nme_cmpnt` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `cust_sts_mstr`
--

DROP TABLE IF EXISTS `cust_sts_mstr`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `cust_sts_mstr` (
  `status_id` bigint NOT NULL AUTO_INCREMENT,
  `status_code` varchar(30) NOT NULL,
  `status_name` varchar(100) NOT NULL,
  `description` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`status_id`),
  UNIQUE KEY `status_code` (`status_code`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `cust_sts_mstr`
--

LOCK TABLES `cust_sts_mstr` WRITE;
/*!40000 ALTER TABLE `cust_sts_mstr` DISABLE KEYS */;
INSERT INTO `cust_sts_mstr` VALUES (1,'CLOSED','Closed','Customer relationship terminated'),(2,'ACTIVE','Active','Normal active customer'),(3,'LOCKED','Locked','Customer temporarily locked for security reasons'),(4,'KYC_PENDING','KYC Pending','Customer registration completed but KYC information is pending');
/*!40000 ALTER TABLE `cust_sts_mstr` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `cust_typ_mstr`
--

DROP TABLE IF EXISTS `cust_typ_mstr`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `cust_typ_mstr` (
  `type_id` bigint NOT NULL AUTO_INCREMENT,
  `type_code` varchar(30) NOT NULL,
  `type_name` varchar(100) NOT NULL,
  `description` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`type_id`),
  UNIQUE KEY `type_code` (`type_code`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `cust_typ_mstr`
--

LOCK TABLES `cust_typ_mstr` WRITE;
/*!40000 ALTER TABLE `cust_typ_mstr` DISABLE KEYS */;
INSERT INTO `cust_typ_mstr` VALUES (1,'INDIVIDUAL','Individual','Individual customer'),(2,'ORGANIZATION','Organization','Organization customer');
/*!40000 ALTER TABLE `cust_typ_mstr` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `idfn_typ_mstr`
--

DROP TABLE IF EXISTS `idfn_typ_mstr`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `idfn_typ_mstr` (
  `identification_type_id` bigint NOT NULL AUTO_INCREMENT,
  `identification_type` varchar(50) NOT NULL,
  `description` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`identification_type_id`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `idfn_typ_mstr`
--

LOCK TABLES `idfn_typ_mstr` WRITE;
/*!40000 ALTER TABLE `idfn_typ_mstr` DISABLE KEYS */;
INSERT INTO `idfn_typ_mstr` VALUES (1,'PAN','Permanent Account Number'),(2,'AADHAAR','Aadhaar identification number'),(3,'PASSPORT','Passport identification'),(4,'DRIVING_LICENSE','Driving licence identification'),(5,'VOTER_ID','Voter identification');
/*!40000 ALTER TABLE `idfn_typ_mstr` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `login_audit`
--

DROP TABLE IF EXISTS `login_audit`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `login_audit` (
  `login_id` bigint NOT NULL AUTO_INCREMENT,
  `user_id` bigint DEFAULT NULL,
  `login_timestamp` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `logout_timestamp` timestamp NULL DEFAULT NULL,
  `login_status` varchar(30) NOT NULL,
  `failure_reason` varchar(255) DEFAULT NULL,
  `ip_address` varchar(45) DEFAULT NULL,
  `user_agent` text,
  `created_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`login_id`),
  KEY `fk_audit_user` (`user_id`)
) ENGINE=InnoDB AUTO_INCREMENT=21 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `login_audit`
--

LOCK TABLES `login_audit` WRITE;
/*!40000 ALTER TABLE `login_audit` DISABLE KEYS */;
INSERT INTO `login_audit` VALUES (1,6,'2026-10-09 18:32:49','2026-10-09 19:23:36','SUCCESS',NULL,'0:0:0:0:0:0:0:1','PostmanRuntime/2.10.1','2026-10-09 18:32:49'),(2,6,'2026-10-09 18:34:05',NULL,'FAILED','INCORRECT_PASSWORD','0:0:0:0:0:0:0:1','PostmanRuntime/2.10.1','2026-10-09 18:34:05'),(3,NULL,'2026-10-09 19:16:36',NULL,'FAILED','UNKNOWN_USERNAME','0:0:0:0:0:0:0:1','PostmanRuntime/2.10.1','2026-10-09 19:16:36'),(4,NULL,'2026-10-13 13:26:16',NULL,'FAILED','UNKNOWN_USERNAME','0:0:0:0:0:0:0:1','PostmanRuntime/2.11.0','2026-10-13 13:26:16'),(5,NULL,'2026-10-13 13:26:27',NULL,'FAILED','UNKNOWN_USERNAME','0:0:0:0:0:0:0:1','PostmanRuntime/2.11.0','2026-10-13 13:26:27'),(6,6,'2026-10-13 13:27:05',NULL,'SUCCESS',NULL,'0:0:0:0:0:0:0:1','PostmanRuntime/2.11.0','2026-10-13 13:27:05'),(7,NULL,'2026-10-13 17:13:57',NULL,'FAILED','UNKNOWN_USERNAME','0:0:0:0:0:0:0:1','Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/155.0.0.0 Safari/537.36 Edg/155.0.0.0','2026-10-13 17:13:57'),(8,8,'2026-10-13 17:14:52',NULL,'SUCCESS',NULL,'0:0:0:0:0:0:0:1','Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/155.0.0.0 Safari/537.36 Edg/155.0.0.0','2026-10-13 17:14:52'),(9,7,'2026-10-13 17:33:06',NULL,'SUCCESS',NULL,'0:0:0:0:0:0:0:1','PostmanRuntime/2.11.0','2026-10-13 17:33:06'),(10,7,'2026-10-13 17:46:30',NULL,'SUCCESS',NULL,'0:0:0:0:0:0:0:1','PostmanRuntime/2.11.0','2026-10-13 17:46:30'),(11,9,'2026-10-13 17:57:27',NULL,'SUCCESS',NULL,'0:0:0:0:0:0:0:1','Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/155.0.0.0 Safari/537.36 Edg/155.0.0.0','2026-10-13 17:57:27'),(12,9,'2026-10-13 17:58:58',NULL,'SUCCESS',NULL,'0:0:0:0:0:0:0:1','Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/155.0.0.0 Safari/537.36 Edg/155.0.0.0','2026-10-13 17:58:58'),(13,9,'2026-10-13 18:01:32','2026-10-13 18:02:20','SUCCESS',NULL,'0:0:0:0:0:0:0:1','Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/155.0.0.0 Safari/537.36 Edg/155.0.0.0','2026-10-13 18:01:32'),(14,6,'2026-10-13 18:02:37','2026-10-13 18:03:12','SUCCESS',NULL,'0:0:0:0:0:0:0:1','Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/155.0.0.0 Safari/537.36 Edg/155.0.0.0','2026-10-13 18:02:37'),(15,9,'2026-10-13 18:03:35','2026-10-13 18:12:57','SUCCESS',NULL,'0:0:0:0:0:0:0:1','Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/155.0.0.0 Safari/537.36 Edg/155.0.0.0','2026-10-13 18:03:35'),(16,9,'2026-10-13 18:13:23','2026-10-13 18:13:45','SUCCESS',NULL,'0:0:0:0:0:0:0:1','Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/155.0.0.0 Safari/537.36 Edg/155.0.0.0','2026-10-13 18:13:23'),(17,6,'2026-10-13 18:14:57',NULL,'SUCCESS',NULL,'0:0:0:0:0:0:0:1','Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/155.0.0.0 Safari/537.36 Edg/155.0.0.0','2026-10-13 18:14:57'),(18,6,'2026-10-13 18:19:06','2026-10-13 18:20:28','SUCCESS',NULL,'0:0:0:0:0:0:0:1','Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/155.0.0.0 Safari/537.36 Edg/155.0.0.0','2026-10-13 18:19:06'),(19,6,'2026-10-13 18:20:41',NULL,'SUCCESS',NULL,'0:0:0:0:0:0:0:1','Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/155.0.0.0 Safari/537.36 Edg/155.0.0.0','2026-10-13 18:20:41'),(20,6,'2026-10-13 18:20:57',NULL,'SUCCESS',NULL,'0:0:0:0:0:0:0:1','Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/155.0.0.0 Safari/537.36 Edg/155.0.0.0','2026-10-13 18:20:57');
/*!40000 ALTER TABLE `login_audit` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `nme_typ_mstr`
--

DROP TABLE IF EXISTS `nme_typ_mstr`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `nme_typ_mstr` (
  `name_type_id` bigint NOT NULL AUTO_INCREMENT,
  `name_type` varchar(50) NOT NULL,
  `description` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`name_type_id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `nme_typ_mstr`
--

LOCK TABLES `nme_typ_mstr` WRITE;
/*!40000 ALTER TABLE `nme_typ_mstr` DISABLE KEYS */;
INSERT INTO `nme_typ_mstr` VALUES (1,'FIRST_NAME','Customer first name'),(2,'MIDDLE_NAME','Customer middle name'),(3,'LAST_NAME','Customer last name');
/*!40000 ALTER TABLE `nme_typ_mstr` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2026-10-14  0:40:51
