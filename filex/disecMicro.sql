-- MySQL dump 10.13  Distrib 5.7.17, for Linux (x86_64)
--
-- Host: 172.29.14.103    Database: disecMicro
-- ------------------------------------------------------
-- Server version	5.5.29-MariaDB-log

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
-- Table structure for table `download`
--

DROP TABLE IF EXISTS `download`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `download` (
  `group` varchar(60) NOT NULL COMMENT 'Grupo do arquivo',
  `name` varchar(60) NOT NULL COMMENT 'Nome visível (apelido) do arquivo',
  `file` varchar(160) NOT NULL COMMENT 'Caminho e nome do Arquivo para download',
  `enabled` tinyint(1) NOT NULL DEFAULT '1' COMMENT 'habilitado - 1 / desabilitado - 0',
  `dt_reg` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT 'Data do registro na tabela',
  PRIMARY KEY (`group`,`name`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `hits_uri`
--

DROP TABLE IF EXISTS `hits_uri`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `hits_uri` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `total_hits` int(11) NOT NULL,
  `top_uri` varchar(80) NOT NULL,
  `hits_uri` int(11) NOT NULL,
  `seconds` bigint(20) NOT NULL,
  `time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=MyISAM AUTO_INCREMENT=6 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `log`
--

DROP TABLE IF EXISTS `log`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `log` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT 'Código automático Identificador do log',
  `ip` varchar(45) NOT NULL COMMENT 'Endereço IP do cliente',
  `url` varchar(80) NOT NULL COMMENT 'URL acessada',
  `context` varchar(45) NOT NULL COMMENT 'Contexto da URI',
  `uri` varchar(60) NOT NULL COMMENT 'URI acessada',
  `cd_usu` char(8) NOT NULL COMMENT 'Matrícula do usuário',
  `uor_depe` int(8) NOT NULL COMMENT 'UOR da dependência',
  `uor_eqp` int(8) NOT NULL COMMENT 'UOR da Equipe/Divisão',
  `autd` tinyint(1) NOT NULL DEFAULT '0' COMMENT 'Autorizado - 1 / Não Autorizado - 0',
  `dt_log` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT 'Data do log',
  PRIMARY KEY (`id`)
) ENGINE=MyISAM AUTO_INCREMENT=1203 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `proc_exec`
--

DROP TABLE IF EXISTS `proc_exec`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `proc_exec` (
  `group` varchar(60) NOT NULL COMMENT 'Grupo de comandos',
  `name` varchar(60) NOT NULL COMMENT 'Nome do comando',
  `command` varchar(255) NOT NULL COMMENT 'Comando a ser executado no servidor',
  `wait_output` tinyint(1) NOT NULL COMMENT 'habilitado = 1 / desabilitado = 0\nEste campo indica se o microserviço irá esperar a execução do comando e retornar seu conteúdo de saída.',
  `dt_reg` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT 'Data de registro do comando',
  `enabled` tinyint(1) NOT NULL DEFAULT '1' COMMENT 'habilitado = 1 / desabilitado = 0',
  `desc` varchar(255) DEFAULT NULL COMMENT 'habilitado = 1 / desabilitado = 2\nEste campo indica se o microserviço irá esperar pela execução do comando ',
  PRIMARY KEY (`group`,`name`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `sqls`
--

DROP TABLE IF EXISTS `sqls`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `sqls` (
  `group` varchar(60) NOT NULL COMMENT 'Nome do Grupo SQL',
  `name` varchar(60) NOT NULL COMMENT 'Nome da consulta SQL',
  `sql` text NOT NULL COMMENT 'Consulta SQL',
  `enabled` tinyint(1) NOT NULL DEFAULT '1' COMMENT 'habilitado - 1 / desabilitado - 0',
  `dt_sql` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT 'Data do registro da consulta na tabela',
  PRIMARY KEY (`name`,`group`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `upload`
--

DROP TABLE IF EXISTS `upload`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `upload` (
  `group` varchar(60) NOT NULL COMMENT 'Grupo do arquivo',
  `name` varchar(60) NOT NULL COMMENT 'Nome do recurso de upload para controle de acesso e classificação de diferentes uploads dentro do mesmo grupo.',
  `path` varchar(128) NOT NULL COMMENT 'Caminho da pasta onde será salvo o arquivo carregado',
  `max_size` bigint(20) NOT NULL DEFAULT '52428800' COMMENT 'Tamanho máximo do arquivo',
  `allowed_ext` varchar(60) DEFAULT NULL COMMENT 'Extensões de arquivo permitidas',
  `enabled` tinyint(1) NOT NULL DEFAULT '1' COMMENT 'habilitado - 1 / desabilitado - 0',
  PRIMARY KEY (`group`,`name`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping events for database 'disecMicro'
--

--
-- Dumping routines for database 'disecMicro'
--
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2017-05-15 18:36:55
