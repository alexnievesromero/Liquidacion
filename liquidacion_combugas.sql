-- MySQL dump 10.13  Distrib 8.0.42, for Win64 (x86_64)
--
-- Host: localhost    Database: liquidacion_combugas
-- ------------------------------------------------------
-- Server version	8.0.42

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
-- Table structure for table `descargue`
--

DROP TABLE IF EXISTS `descargue`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `descargue` (
  `id_descargue` int NOT NULL AUTO_INCREMENT,
  PRIMARY KEY (`id_descargue`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `descargue`
--

LOCK TABLES `descargue` WRITE;
/*!40000 ALTER TABLE `descargue` DISABLE KEYS */;
/*!40000 ALTER TABLE `descargue` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `empleado`
--

DROP TABLE IF EXISTS `empleado`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `empleado` (
  `id_empleado` bigint NOT NULL AUTO_INCREMENT,
  `nombre` varchar(255) DEFAULT NULL,
  `usuario` varchar(255) DEFAULT NULL,
  `contrasena` varchar(255) NOT NULL,
  `rol` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id_empleado`)
) ENGINE=InnoDB AUTO_INCREMENT=19 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `empleado`
--

LOCK TABLES `empleado` WRITE;
/*!40000 ALTER TABLE `empleado` DISABLE KEYS */;
INSERT INTO `empleado` VALUES (4,'ALEXANDER NIEVES ROMERO','alex','$2a$10$nduoJnj5y3zq/0NhUykXEO62VfJlAFAF2CXb15qT7k4xqA/FEnCdW','ADMIN'),(6,'JOSE LOPES PATERNINA','lopes','$2a$10$lKSRZ31lSITSPs7AFq4WOO2xDRrXGgMZLwL7Uj1oRDOwHokz2ROTi','EMPLEADO'),(16,'JESUS PERES PERES','jesu','123456','EMPLEADO'),(17,'EDITH ROMERO REYES','EDITH','6691','EMPLEADO'),(18,'ANDRES GALVIS','GALVIS','$2a$10$IqG60UeIRzHMknUoI83HTukum0RjIxTTzHQXIe183kp8ljtANdIRa','ADMIN');
/*!40000 ALTER TABLE `empleado` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `isla`
--

DROP TABLE IF EXISTS `isla`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `isla` (
  `id_isla` bigint NOT NULL AUTO_INCREMENT,
  `isla` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id_isla`),
  UNIQUE KEY `numero` (`isla`)
) ENGINE=InnoDB AUTO_INCREMENT=8 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `isla`
--

LOCK TABLES `isla` WRITE;
/*!40000 ALTER TABLE `isla` DISABLE KEYS */;
INSERT INTO `isla` VALUES (1,'Isla 1'),(2,'Isla 2'),(3,'Isla 3'),(4,'Isla 4'),(5,'Isla 5'),(6,'Isla GNV 1'),(7,'Isla GNV 2');
/*!40000 ALTER TABLE `isla` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `lectura_surtidor`
--

DROP TABLE IF EXISTS `lectura_surtidor`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `lectura_surtidor` (
  `id_lectura` bigint NOT NULL AUTO_INCREMENT,
  `id_turno` bigint DEFAULT NULL,
  `id_surtidor` bigint DEFAULT NULL,
  `lectura_final` bigint DEFAULT NULL,
  `lectura_inicial` bigint DEFAULT NULL,
  `tipo_combustible` varchar(255) DEFAULT NULL,
  `gls` decimal(38,2) DEFAULT NULL,
  `pesos` decimal(38,2) DEFAULT NULL,
  PRIMARY KEY (`id_lectura`),
  KEY `FK1gar56ucccyuhjrq0q0pngqln` (`id_turno`),
  KEY `FK8v3l9ykoyprt9fonv6w7hfl2` (`id_surtidor`),
  CONSTRAINT `FK1gar56ucccyuhjrq0q0pngqln` FOREIGN KEY (`id_turno`) REFERENCES `turno` (`id_turno`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `FK8v3l9ykoyprt9fonv6w7hfl2` FOREIGN KEY (`id_surtidor`) REFERENCES `surtidor` (`id_surtidor`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=24 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `lectura_surtidor`
--

LOCK TABLES `lectura_surtidor` WRITE;
/*!40000 ALTER TABLE `lectura_surtidor` DISABLE KEYS */;
INSERT INTO `lectura_surtidor` VALUES (22,48,1,17,12,'CORRIENTE',5.00,78000.00),(23,48,1,1222,1111,'CORRIENTE',111.00,1842600.00);
/*!40000 ALTER TABLE `lectura_surtidor` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `preciocombustibles`
--

DROP TABLE IF EXISTS `preciocombustibles`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `preciocombustibles` (
  `id_precio` bigint NOT NULL AUTO_INCREMENT,
  `tipocombustible` varchar(255) NOT NULL,
  `precio` decimal(38,2) NOT NULL DEFAULT '0.00',
  PRIMARY KEY (`id_precio`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `preciocombustibles`
--

LOCK TABLES `preciocombustibles` WRITE;
/*!40000 ALTER TABLE `preciocombustibles` DISABLE KEYS */;
INSERT INTO `preciocombustibles` VALUES (1,'CORRIENTE',16600.00),(2,'ACPM',14000.00),(3,'GNV',2500.00);
/*!40000 ALTER TABLE `preciocombustibles` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `sesion`
--

DROP TABLE IF EXISTS `sesion`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `sesion` (
  `id_sesion` bigint NOT NULL AUTO_INCREMENT,
  `id_empleado` bigint DEFAULT NULL,
  `hora_login` datetime NOT NULL,
  `hora_logout` datetime DEFAULT NULL,
  `estado` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id_sesion`),
  KEY `FKi8xq943tgttw968yyx76p371r` (`id_empleado`),
  CONSTRAINT `FKi8xq943tgttw968yyx76p371r` FOREIGN KEY (`id_empleado`) REFERENCES `empleado` (`id_empleado`)
) ENGINE=InnoDB AUTO_INCREMENT=17 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `sesion`
--

LOCK TABLES `sesion` WRITE;
/*!40000 ALTER TABLE `sesion` DISABLE KEYS */;
INSERT INTO `sesion` VALUES (1,6,'2025-07-09 10:26:01','2025-07-09 10:30:45','FINALIZADA'),(6,6,'2025-07-09 13:48:53',NULL,'ACTIVA'),(9,4,'2025-07-09 17:25:44','2025-07-09 17:41:16','INACTIVO'),(10,6,'2025-07-09 17:47:18','2025-07-09 17:52:46','INACTIVO'),(11,18,'2025-07-09 18:03:20','2025-07-09 18:07:23','INACTIVO'),(12,4,'2025-07-09 18:04:42','2025-07-09 18:08:15','INACTIVO'),(13,6,'2025-07-09 18:12:23','2025-07-10 15:50:53','INACTIVO'),(14,18,'2025-07-09 18:15:01',NULL,'ACTIVO'),(15,4,'2025-07-10 12:50:05',NULL,'ACTIVO'),(16,6,'2025-07-10 15:51:27',NULL,'ACTIVO');
/*!40000 ALTER TABLE `sesion` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `surtidor`
--

DROP TABLE IF EXISTS `surtidor`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `surtidor` (
  `id_surtidor` bigint NOT NULL AUTO_INCREMENT,
  `nombre` varchar(255) DEFAULT NULL,
  `id_isla` bigint DEFAULT NULL,
  PRIMARY KEY (`id_surtidor`),
  KEY `FKr320f9l1ilj3txs5i7bq64r1l` (`id_isla`),
  CONSTRAINT `FKr320f9l1ilj3txs5i7bq64r1l` FOREIGN KEY (`id_isla`) REFERENCES `isla` (`id_isla`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=10 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `surtidor`
--

LOCK TABLES `surtidor` WRITE;
/*!40000 ALTER TABLE `surtidor` DISABLE KEYS */;
INSERT INTO `surtidor` VALUES (1,'1 - 2',1),(2,'3 - 4',1),(3,'5 - 6',2),(4,'7 - 8 ',2),(5,'9 - 10',3),(6,'11 - 12',3),(7,'13 - 14',4),(8,'15 - 16',4),(9,'17 - 18',6);
/*!40000 ALTER TABLE `surtidor` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `turno`
--

DROP TABLE IF EXISTS `turno`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `turno` (
  `id_turno` bigint NOT NULL AUTO_INCREMENT,
  `id_empleado` bigint NOT NULL,
  `id_isla` bigint NOT NULL,
  `fecha` date NOT NULL,
  `turno` varchar(255) DEFAULT NULL,
  `hora_inicio` datetime NOT NULL,
  `hora_fin` datetime DEFAULT NULL,
  `numero_turno` bigint NOT NULL,
  `estado` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id_turno`),
  UNIQUE KEY `numero_turno_UNIQUE` (`numero_turno`),
  KEY `FK39q0dwa7syxmgh1m3pxj67pd` (`id_isla`),
  KEY `FKhyyo0e4asmovio3x8jmma8vbi` (`id_empleado`),
  CONSTRAINT `FK39q0dwa7syxmgh1m3pxj67pd` FOREIGN KEY (`id_isla`) REFERENCES `isla` (`id_isla`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `FKhyyo0e4asmovio3x8jmma8vbi` FOREIGN KEY (`id_empleado`) REFERENCES `empleado` (`id_empleado`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=50 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `turno`
--

LOCK TABLES `turno` WRITE;
/*!40000 ALTER TABLE `turno` DISABLE KEYS */;
INSERT INTO `turno` VALUES (36,6,1,'2025-07-09','Turno1 06 AM - 02 PM','2025-07-09 10:25:00','2025-07-09 10:30:45',1,'CERRADO'),(45,6,1,'2025-07-09','Turno2 02 PM - 10 PM','2025-07-09 13:48:00','2025-07-09 17:52:46',2,'INACTIVO'),(48,6,2,'2025-07-09','Turno2 02 PM - 10 PM','2025-07-09 18:32:00','2025-07-10 15:50:53',3,'INACTIVO'),(49,6,1,'2025-07-10','','2025-07-10 15:51:00',NULL,4,'ACTIVO');
/*!40000 ALTER TABLE `turno` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `ventas`
--

DROP TABLE IF EXISTS `ventas`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `ventas` (
  `id_ventas` bigint NOT NULL AUTO_INCREMENT,
  `id_turno` bigint NOT NULL,
  `venta_total` decimal(38,2) DEFAULT '0.00',
  `entregas` decimal(38,2) DEFAULT '0.00',
  `credito_satelite` decimal(38,2) DEFAULT '0.00',
  `credito_local` decimal(38,2) DEFAULT NULL,
  `prepago_local` decimal(38,2) DEFAULT '0.00',
  `pagos_electronicos` decimal(38,2) DEFAULT '0.00',
  `pagos_Qr` decimal(38,2) DEFAULT '0.00',
  `monedas` decimal(38,2) DEFAULT '0.00',
  `gastos` decimal(38,2) DEFAULT '0.00',
  `puntos_terpel` decimal(38,2) DEFAULT '0.00',
  `devoluciones` decimal(38,2) DEFAULT '0.00',
  `otros` decimal(38,2) DEFAULT NULL,
  `cheques` decimal(38,2) DEFAULT '0.00',
  `regalos` decimal(38,2) DEFAULT NULL,
  `prestamos` decimal(38,2) DEFAULT NULL,
  `descuadres` decimal(38,2) DEFAULT NULL,
  `efectivo_final` decimal(38,2) DEFAULT '0.00',
  `total` decimal(38,2) DEFAULT NULL,
  `diferencia` decimal(38,2) DEFAULT '0.00',
  `observaciones` varchar(255) DEFAULT 'NO HAY OBSERVACIONES',
  PRIMARY KEY (`id_ventas`),
  KEY `FKnjowislo7cbog1rqtqqbjnlji` (`id_turno`),
  CONSTRAINT `FKnjowislo7cbog1rqtqqbjnlji` FOREIGN KEY (`id_turno`) REFERENCES `turno` (`id_turno`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `ventas`
--

LOCK TABLES `ventas` WRITE;
/*!40000 ALTER TABLE `ventas` DISABLE KEYS */;
INSERT INTO `ventas` VALUES (1,48,78000.00,0.00,0.00,0.00,0.00,0.00,0.00,0.00,0.00,0.00,0.00,0.00,0.00,0.00,0.00,0.00,78000.00,78000.00,0.00,''),(2,48,1842600.00,0.00,0.00,0.00,0.00,0.00,0.00,0.00,0.00,0.00,0.00,0.00,0.00,0.00,0.00,0.00,1842600.00,1842600.00,0.00,'');
/*!40000 ALTER TABLE `ventas` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2025-07-10 19:05:38
