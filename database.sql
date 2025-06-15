-- phpMyAdmin SQL Dump
-- version 5.2.1
-- https://www.phpmyadmin.net/
--
-- Host: localhost:3306
-- Generation Time: Jun 12, 2025 at 07:08 PM
-- Server version: 5.7.41
-- PHP Version: 8.0.28

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";

CREATE DATABASE IF NOT EXISTS `shelfens`;
USE `shelfens`;


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `h198598_kant-schulleben`
--

-- --------------------------------------------------------

--
-- Table structure for table `articles`
--

CREATE TABLE `articles` (
  `id` int(11) NOT NULL,
  `name` text,
  `content` text,
  `author` int(11) DEFAULT NULL,
  `image` text,
  `title` text,
  `tags` text NOT NULL,
  `date` datetime DEFAULT CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- --------------------------------------------------------

--
-- Table structure for table `benutzer`
--

CREATE TABLE `benutzer` (
  `name` text,
  `id` int(11) NOT NULL,
  `password` text,
  `berechtigung` text,
  `mail` text,
  `benutzername` text,
  `shs` text
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Dumping data for table `benutzer`
--

INSERT INTO `benutzer` (`name`, `id`, `password`, `berechtigung`, `mail`, `benutzername`, `shs`) VALUES
('Kilian Schneider', 3, '$2a$10$1D1gl4FUaorpO9gYRrBSkuJFhLsyK4IKiM3MMp5vBXflRbahSTy4G', '0,', 'monsieurk1209@gmail.com', 'KlnSdr', NULL);

-- --------------------------------------------------------

--
-- Table structure for table `berechtigungen`
--

CREATE TABLE `berechtigungen` (
  `name` varchar(16) NOT NULL,
  `id` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Dumping data for table `berechtigungen`
--

INSERT INTO `berechtigungen` (`name`, `id`) VALUES
('dev', 0),
('shsAuswertung', 3);

-- --------------------------------------------------------

--
-- Table structure for table `handschlag`
--

CREATE TABLE `handschlag` (
  `fach` text,
  `typ` text,
  `klasse` text,
  `terminlink` text,
  `schulden` text,
  `partner` text,
  `id` int(11) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- --------------------------------------------------------

--
-- Table structure for table `shsAnmeldung`
--

CREATE TABLE `shsAnmeldung` (
  `name` text,
  `mail` text,
  `telefon` text,
  `fach` text,
  `zeit` text,
  `einzelnachhilfe` int(11) DEFAULT NULL,
  `nachhilfe` int(11) DEFAULT NULL,
  `klasse` text,
  `zielKlasse` int(11) DEFAULT NULL,
  `accountID` int(11) DEFAULT NULL,
  `id` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Indexes for table `articles`
--
ALTER TABLE `articles`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `benutzer`
--
ALTER TABLE `benutzer`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `shsAnmeldung`
--
ALTER TABLE `shsAnmeldung`
  ADD PRIMARY KEY (`id`);

--
-- AUTO_INCREMENT for dumped tables
--

--
-- AUTO_INCREMENT for table `articles`
--
ALTER TABLE `articles`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=10;

--
-- AUTO_INCREMENT for table `benutzer`
--
ALTER TABLE `benutzer`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=79;

--
-- AUTO_INCREMENT for table `shsAnmeldung`
--
ALTER TABLE `shsAnmeldung`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=25;
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
