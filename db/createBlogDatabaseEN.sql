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
-- Table `Blog`.`User`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `Blog`.`User` ;

CREATE TABLE IF NOT EXISTS `Blog`.`User` (
  `UID` INT NOT NULL AUTO_INCREMENT,
  `Username` VARCHAR(16) NOT NULL,
  `Password` VARCHAR(32) NOT NULL,
  `isBlogger` TINYINT NOT NULL,
  PRIMARY KEY (`UID`),
  UNIQUE INDEX `Username_UNIQUE` (`Username` ASC))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `Blog`.`Post`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `Blog`.`Post` ;

CREATE TABLE IF NOT EXISTS `Blog`.`Post` (
  `PID` INT NOT NULL AUTO_INCREMENT,
  `Date` DATETIME NOT NULL,
  `Author` INT NOT NULL,
  `Parent` INT NULL,
  PRIMARY KEY (`PID`),
  INDEX `ParentFK_idx` (`Parent` ASC),
  INDEX `AuthorFK_idx` (`Author` ASC),
  CONSTRAINT `ParentFK`
    FOREIGN KEY (`Parent`)
    REFERENCES `Blog`.`Post` (`PID`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `AuthorFK`
    FOREIGN KEY (`Author`)
    REFERENCES `Blog`.`User` (`UID`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `Blog`.`Article`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `Blog`.`Article` ;

CREATE TABLE IF NOT EXISTS `Blog`.`Article` (
  `AID` INT NOT NULL,
  `Title` VARCHAR(45) NOT NULL,
  `Text` TEXT NOT NULL,
  PRIMARY KEY (`AID`),
  CONSTRAINT `aPostFK`
    FOREIGN KEY (`AID`)
    REFERENCES `Blog`.`Post` (`PID`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `Blog`.`Comment`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `Blog`.`Comment` ;

CREATE TABLE IF NOT EXISTS `Blog`.`Comment` (
  `CID` INT NOT NULL,
  `Text` TINYTEXT NOT NULL,
  PRIMARY KEY (`CID`),
  CONSTRAINT `kPostFK`
    FOREIGN KEY (`CID`)
    REFERENCES `Blog`.`Post` (`PID`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


SET SQL_MODE=@OLD_SQL_MODE;
SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS;
SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS;
