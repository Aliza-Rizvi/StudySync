-- MySQL dump 10.13  Distrib 8.4.13, for Win64 (x86_64)
--
-- Host: localhost    Database: studysync_db
-- ------------------------------------------------------
-- Server version	8.4.13

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!50503 SET NAMES utf8mb4 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Current Database: `studysync_db`
--

CREATE DATABASE /*!32312 IF NOT EXISTS*/ `studysync_db` /*!40100 DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci */ /*!80016 DEFAULT ENCRYPTION='N' */;

USE `studysync_db`;

--
-- Table structure for table `attendance`
--

DROP TABLE IF EXISTS `attendance`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `attendance` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `present` bit(1) NOT NULL,
  `session_id` bigint NOT NULL,
  `student_id` bigint NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UKnbi0gmpy43dhov2lmuulybn8f` (`session_id`,`student_id`),
  KEY `FK7121lveuhtmu9wa6m90ayd5yg` (`student_id`),
  CONSTRAINT `FK7121lveuhtmu9wa6m90ayd5yg` FOREIGN KEY (`student_id`) REFERENCES `students` (`id`),
  CONSTRAINT `FKqwjlf8u6yr3c973279swcy9by` FOREIGN KEY (`session_id`) REFERENCES `study_sessions` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `attendance`
--

LOCK TABLES `attendance` WRITE;
/*!40000 ALTER TABLE `attendance` DISABLE KEYS */;
/*!40000 ALTER TABLE `attendance` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `availability_slots`
--

DROP TABLE IF EXISTS `availability_slots`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `availability_slots` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `day_of_week` enum('FRIDAY','MONDAY','SATURDAY','SUNDAY','THURSDAY','TUESDAY','WEDNESDAY') NOT NULL,
  `end_time` time(6) NOT NULL,
  `start_time` time(6) NOT NULL,
  `student_id` bigint NOT NULL,
  PRIMARY KEY (`id`),
  KEY `FKot1bynbvuijwbpsogo4932g0k` (`student_id`),
  CONSTRAINT `FKot1bynbvuijwbpsogo4932g0k` FOREIGN KEY (`student_id`) REFERENCES `students` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=9 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `availability_slots`
--

LOCK TABLES `availability_slots` WRITE;
/*!40000 ALTER TABLE `availability_slots` DISABLE KEYS */;
INSERT INTO `availability_slots` VALUES (1,'MONDAY','17:00:00.000000','14:00:00.000000',1),(2,'WEDNESDAY','12:00:00.000000','10:00:00.000000',1),(3,'MONDAY','18:00:00.000000','15:00:00.000000',2),(4,'THURSDAY','11:00:00.000000','09:00:00.000000',2),(5,'WEDNESDAY','13:00:00.000000','11:00:00.000000',3),(6,'FRIDAY','18:00:00.000000','16:00:00.000000',3),(7,'MONDAY','16:00:00.000000','14:00:00.000000',4),(8,'FRIDAY','19:00:00.000000','17:00:00.000000',4);
/*!40000 ALTER TABLE `availability_slots` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `courses`
--

DROP TABLE IF EXISTS `courses`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `courses` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `code` varchar(255) NOT NULL,
  `department` varchar(255) DEFAULT NULL,
  `name` varchar(255) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UK61og8rbqdd2y28rx2et5fdnxd` (`code`)
) ENGINE=InnoDB AUTO_INCREMENT=9 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `courses`
--

LOCK TABLES `courses` WRITE;
/*!40000 ALTER TABLE `courses` DISABLE KEYS */;
INSERT INTO `courses` VALUES (1,'SE-301','Software Engineering','Software Construction and Development'),(2,'CS-302','Computer Science','Computer Networks'),(3,'CS-303','Computer Science','Database Systems'),(4,'CS-304','Computer Science','Operating Systems'),(5,'SE-305','Software Engineering','Software Requirements Engineering'),(6,'SE-306','Software Engineering','Software Quality Engineering'),(7,'CS-307','Computer Science','Artificial Intelligence'),(8,'CS-308','Computer Science','Web Engineering');
/*!40000 ALTER TABLE `courses` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `group_members`
--

DROP TABLE IF EXISTS `group_members`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `group_members` (
  `group_id` bigint NOT NULL,
  `student_id` bigint NOT NULL,
  PRIMARY KEY (`group_id`,`student_id`),
  KEY `FKriqbhx06deaevh9o051g4fo95` (`student_id`),
  CONSTRAINT `FK2hpxidj62j5p535elxscas5ig` FOREIGN KEY (`group_id`) REFERENCES `study_groups` (`id`),
  CONSTRAINT `FKriqbhx06deaevh9o051g4fo95` FOREIGN KEY (`student_id`) REFERENCES `students` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `group_members`
--

LOCK TABLES `group_members` WRITE;
/*!40000 ALTER TABLE `group_members` DISABLE KEYS */;
INSERT INTO `group_members` VALUES (1,1),(1,2);
/*!40000 ALTER TABLE `group_members` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `join_requests`
--

DROP TABLE IF EXISTS `join_requests`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `join_requests` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `created_at` datetime(6) DEFAULT NULL,
  `request_type` enum('INVITE','REQUEST') NOT NULL,
  `status` enum('ACCEPTED','PENDING','REJECTED') NOT NULL,
  `group_id` bigint NOT NULL,
  `student_id` bigint NOT NULL,
  PRIMARY KEY (`id`),
  KEY `FKe54cpon7sot1cdt4b3ilxhvoq` (`group_id`),
  KEY `FK4pqvjscyift1pdd7voofy62i1` (`student_id`),
  CONSTRAINT `FK4pqvjscyift1pdd7voofy62i1` FOREIGN KEY (`student_id`) REFERENCES `students` (`id`),
  CONSTRAINT `FKe54cpon7sot1cdt4b3ilxhvoq` FOREIGN KEY (`group_id`) REFERENCES `study_groups` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `join_requests`
--

LOCK TABLES `join_requests` WRITE;
/*!40000 ALTER TABLE `join_requests` DISABLE KEYS */;
/*!40000 ALTER TABLE `join_requests` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `note_links`
--

DROP TABLE IF EXISTS `note_links`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `note_links` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `added_at` datetime(6) DEFAULT NULL,
  `title` varchar(255) NOT NULL,
  `url` varchar(1000) NOT NULL,
  `added_by` bigint NOT NULL,
  `session_id` bigint NOT NULL,
  PRIMARY KEY (`id`),
  KEY `FKdvous078ytmg16fw2o5ggkcee` (`added_by`),
  KEY `FKwhuqi1a0oamktbga7iqdbb1i` (`session_id`),
  CONSTRAINT `FKdvous078ytmg16fw2o5ggkcee` FOREIGN KEY (`added_by`) REFERENCES `students` (`id`),
  CONSTRAINT `FKwhuqi1a0oamktbga7iqdbb1i` FOREIGN KEY (`session_id`) REFERENCES `study_sessions` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `note_links`
--

LOCK TABLES `note_links` WRITE;
/*!40000 ALTER TABLE `note_links` DISABLE KEYS */;
/*!40000 ALTER TABLE `note_links` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `notifications`
--

DROP TABLE IF EXISTS `notifications`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `notifications` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `created_at` datetime(6) DEFAULT NULL,
  `link` varchar(255) DEFAULT NULL,
  `message` varchar(500) NOT NULL,
  `seen` bit(1) NOT NULL,
  `student_id` bigint NOT NULL,
  PRIMARY KEY (`id`),
  KEY `FKlchct0805t9w7mapbehsi6dcw` (`student_id`),
  CONSTRAINT `FKlchct0805t9w7mapbehsi6dcw` FOREIGN KEY (`student_id`) REFERENCES `students` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `notifications`
--

LOCK TABLES `notifications` WRITE;
/*!40000 ALTER TABLE `notifications` DISABLE KEYS */;
/*!40000 ALTER TABLE `notifications` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `ratings`
--

DROP TABLE IF EXISTS `ratings`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `ratings` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `comment` varchar(500) DEFAULT NULL,
  `created_at` datetime(6) DEFAULT NULL,
  `stars` int NOT NULL,
  `rated_student_id` bigint DEFAULT NULL,
  `rater_id` bigint NOT NULL,
  `session_id` bigint NOT NULL,
  PRIMARY KEY (`id`),
  KEY `FKr4sjgiu0tkedsissv00cxafkw` (`rated_student_id`),
  KEY `FKhfr0wxn302d6xwe94qvd2u9ax` (`rater_id`),
  KEY `FKacwwbug05efb5l9yolncyqpj9` (`session_id`),
  CONSTRAINT `FKacwwbug05efb5l9yolncyqpj9` FOREIGN KEY (`session_id`) REFERENCES `study_sessions` (`id`),
  CONSTRAINT `FKhfr0wxn302d6xwe94qvd2u9ax` FOREIGN KEY (`rater_id`) REFERENCES `students` (`id`),
  CONSTRAINT `FKr4sjgiu0tkedsissv00cxafkw` FOREIGN KEY (`rated_student_id`) REFERENCES `students` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `ratings`
--

LOCK TABLES `ratings` WRITE;
/*!40000 ALTER TABLE `ratings` DISABLE KEYS */;
/*!40000 ALTER TABLE `ratings` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `student_courses`
--

DROP TABLE IF EXISTS `student_courses`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `student_courses` (
  `student_id` bigint NOT NULL,
  `course_id` bigint NOT NULL,
  PRIMARY KEY (`student_id`,`course_id`),
  KEY `FKsfpq78oyrqua1h0obpl7ulc18` (`course_id`),
  CONSTRAINT `FKsfpq78oyrqua1h0obpl7ulc18` FOREIGN KEY (`course_id`) REFERENCES `courses` (`id`),
  CONSTRAINT `FKwj1l0mta35u161acdl2tupoo` FOREIGN KEY (`student_id`) REFERENCES `students` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `student_courses`
--

LOCK TABLES `student_courses` WRITE;
/*!40000 ALTER TABLE `student_courses` DISABLE KEYS */;
INSERT INTO `student_courses` VALUES (1,1),(2,1),(3,1),(4,1),(1,2),(3,2),(4,2),(2,3);
/*!40000 ALTER TABLE `student_courses` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `students`
--

DROP TABLE IF EXISTS `students`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `students` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `cgpa` double NOT NULL,
  `department` varchar(255) DEFAULT NULL,
  `email` varchar(255) NOT NULL,
  `full_name` varchar(255) NOT NULL,
  `password_hash` varchar(255) NOT NULL,
  `sap_id` varchar(255) DEFAULT NULL,
  `semester` int NOT NULL,
  `study_style` enum('DISCUSSION_ONLINE','DISCUSSION_PHYSICAL','QUIET_ONLINE','QUIET_PHYSICAL') DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UKe2rndfrsx22acpq2ty1caeuyw` (`email`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `students`
--

LOCK TABLES `students` WRITE;
/*!40000 ALTER TABLE `students` DISABLE KEYS */;
INSERT INTO `students` VALUES (1,3.4,'Software Engineering','ali@demo.com','Ali Khan','d3ad9315b7be5dd53b31a273b3b3aba5defe700808305aa16a3062b76658a791',NULL,6,'DISCUSSION_PHYSICAL'),(2,3.6,'Software Engineering','sara@demo.com','Sara Ahmed','d3ad9315b7be5dd53b31a273b3b3aba5defe700808305aa16a3062b76658a791',NULL,6,'DISCUSSION_PHYSICAL'),(3,2.9,'Software Engineering','hamza@demo.com','Hamza Raza','d3ad9315b7be5dd53b31a273b3b3aba5defe700808305aa16a3062b76658a791',NULL,6,'QUIET_ONLINE'),(4,3.5,'Software Engineering','fatima@demo.com','Fatima Noor','d3ad9315b7be5dd53b31a273b3b3aba5defe700808305aa16a3062b76658a791',NULL,6,'DISCUSSION_ONLINE');
/*!40000 ALTER TABLE `students` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `study_groups`
--

DROP TABLE IF EXISTS `study_groups`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `study_groups` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `created_at` datetime(6) DEFAULT NULL,
  `description` varchar(500) DEFAULT NULL,
  `max_members` int NOT NULL,
  `name` varchar(255) NOT NULL,
  `course_id` bigint NOT NULL,
  `created_by` bigint NOT NULL,
  PRIMARY KEY (`id`),
  KEY `FKllnygr7cgvo5lmjpfx0mmt3mo` (`course_id`),
  KEY `FKec6qu06w81j3aapoxsuempq73` (`created_by`),
  CONSTRAINT `FKec6qu06w81j3aapoxsuempq73` FOREIGN KEY (`created_by`) REFERENCES `students` (`id`),
  CONSTRAINT `FKllnygr7cgvo5lmjpfx0mmt3mo` FOREIGN KEY (`course_id`) REFERENCES `courses` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `study_groups`
--

LOCK TABLES `study_groups` WRITE;
/*!40000 ALTER TABLE `study_groups` DISABLE KEYS */;
INSERT INTO `study_groups` VALUES (1,'2026-11-20 16:32:54.431026','Revising design patterns and UML before the final.',6,'SCD Final Prep',1,1);
/*!40000 ALTER TABLE `study_groups` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `study_sessions`
--

DROP TABLE IF EXISTS `study_sessions`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `study_sessions` (
  `session_type` varchar(31) NOT NULL,
  `id` bigint NOT NULL AUTO_INCREMENT,
  `created_at` datetime(6) DEFAULT NULL,
  `duration_minutes` int NOT NULL,
  `reminder_sent` bit(1) NOT NULL,
  `start_time` datetime(6) NOT NULL,
  `title` varchar(255) NOT NULL,
  `exam_topic` varchar(255) DEFAULT NULL,
  `location` varchar(255) DEFAULT NULL,
  `meeting_link` varchar(255) DEFAULT NULL,
  `venue` varchar(255) DEFAULT NULL,
  `created_by` bigint NOT NULL,
  `group_id` bigint NOT NULL,
  PRIMARY KEY (`id`),
  KEY `FK5561aotpynkjn101jp5qm70yl` (`created_by`),
  KEY `FK4abwpvuum3o657pqdq99rk4gw` (`group_id`),
  CONSTRAINT `FK4abwpvuum3o657pqdq99rk4gw` FOREIGN KEY (`group_id`) REFERENCES `study_groups` (`id`),
  CONSTRAINT `FK5561aotpynkjn101jp5qm70yl` FOREIGN KEY (`created_by`) REFERENCES `students` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `study_sessions`
--

LOCK TABLES `study_sessions` WRITE;
/*!40000 ALTER TABLE `study_sessions` DISABLE KEYS */;
INSERT INTO `study_sessions` VALUES ('PHYSICAL',1,'2026-11-20 16:32:54.459699',90,_binary '','2026-11-18 14:00:00.000000','Design Patterns Revision',NULL,NULL,NULL,'Library Room 2',1,1),('ONLINE',2,'2026-11-20 16:32:54.473373',60,_binary '\0','2026-11-23 15:00:00.000000','UML Diagrams Practice',NULL,NULL,'https://meet.google.com/abc-defg-hij',NULL,2,1);
/*!40000 ALTER TABLE `study_sessions` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2026-11-21 23:20:30
