-- MySQL Script generated by MySQL Workbench
-- Wed Oct 28 14:27:25 2020
-- model: New model    Version: 1.0
-- MySQL Workbench Forward Engineering

SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0;
SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0;
SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION';

-- -----------------------------------------------------
-- Schema Blog
-- -----------------------------------------------------
DROP SCHEMA IF EXISTS `Blog` ;

-- -----------------------------------------------------
-- Schema Blog
-- -----------------------------------------------------
CREATE SCHEMA IF NOT EXISTS `Blog` DEFAULT CHARACTER SET utf8 ;
USE `Blog` ;

-- -----------------------------------------------------
-- Table `Blog`.`Nutzer`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `Blog`.`Nutzer` ;

CREATE TABLE IF NOT EXISTS `Blog`.`Nutzer` (
  `NID` INT NOT NULL AUTO_INCREMENT,
  `Nutzername` VARCHAR(16) NOT NULL,
  `Passwort` VARCHAR(32) NOT NULL,
  `istBlogger` TINYINT NOT NULL,
  PRIMARY KEY (`NID`),
  UNIQUE INDEX `Nutzername_UNIQUE` (`Nutzername` ASC))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `Blog`.`Beitrag`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `Blog`.`Beitrag` ;

CREATE TABLE IF NOT EXISTS `Blog`.`Beitrag` (
  `BID` INT NOT NULL AUTO_INCREMENT,
  `Datum` DATETIME NOT NULL,
  `Verfasser` INT NOT NULL,
  `Oberbeitrag` INT NULL,
  PRIMARY KEY (`BID`),
  INDEX `OberbeitragFK_idx` (`Oberbeitrag` ASC),
  INDEX `VerfasserFK_idx` (`Verfasser` ASC),
  CONSTRAINT `OberbeitragFK`
    FOREIGN KEY (`Oberbeitrag`)
    REFERENCES `Blog`.`Beitrag` (`BID`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `VerfasserFK`
    FOREIGN KEY (`Verfasser`)
    REFERENCES `Blog`.`Nutzer` (`NID`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `Blog`.`Artikel`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `Blog`.`Artikel` ;

CREATE TABLE IF NOT EXISTS `Blog`.`Artikel` (
  `AID` INT NOT NULL,
  `Titel` VARCHAR(45) NOT NULL,
  `Text` TEXT NOT NULL,
  PRIMARY KEY (`AID`),
  CONSTRAINT `aBeitragFK`
    FOREIGN KEY (`AID`)
    REFERENCES `Blog`.`Beitrag` (`BID`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `Blog`.`Kommentar`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `Blog`.`Kommentar` ;

CREATE TABLE IF NOT EXISTS `Blog`.`Kommentar` (
  `KID` INT NOT NULL,
  `Text` TINYTEXT NOT NULL,
  PRIMARY KEY (`KID`),
  CONSTRAINT `kBeitragFK`
    FOREIGN KEY (`KID`)
    REFERENCES `Blog`.`Beitrag` (`BID`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


SET SQL_MODE=@OLD_SQL_MODE;
SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS;
SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS;
