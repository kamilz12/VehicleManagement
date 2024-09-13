SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0;
SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0;
SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION';

-- -----------------------------------------------------
-- Schema vehicle
-- -----------------------------------------------------
CREATE SCHEMA IF NOT EXISTS `vehicle` DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci ;
USE `vehicle` ;

-- -----------------------------------------------------
-- Table `vehicle`.`authorities`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `vehicle`.`authorities` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `authority` VARCHAR(50) NOT NULL,
  PRIMARY KEY (`id`))
ENGINE = InnoDB
DEFAULT CHARACTER SET = latin1;


-- -----------------------------------------------------
-- Table `vehicle`.`maintenance_status`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `vehicle`.`maintenance_status` (
  `id` INT NOT NULL,
  `status` VARCHAR(30) NOT NULL,
  PRIMARY KEY (`id`))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8mb4
COLLATE = utf8mb4_0900_ai_ci;


-- -----------------------------------------------------
-- Table `vehicle`.`vehicle`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `vehicle`.`vehicle` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `make` VARCHAR(255) NOT NULL,
  `model` VARCHAR(255) NOT NULL,
  `year` INT NOT NULL,
  `intern_rest_id` INT NOT NULL,
  `enginename` VARCHAR(255) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE INDEX `intern_rest_id_UNIQUE` (`intern_rest_id` ASC) VISIBLE,
  INDEX `make_index` (`make` ASC) INVISIBLE,
  INDEX `model_index` (`model` ASC) VISIBLE)
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8mb4
COLLATE = utf8mb4_0900_ai_ci;


-- -----------------------------------------------------
-- Table `vehicle`.`maintenance`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `vehicle`.`maintenance` (
  `id` INT NOT NULL,
  `mileage` INT NOT NULL,
  `dateIn` DATE NOT NULL,
  `dateOut` DATE NOT NULL,
  `timeIn` TIME NOT NULL,
  `timeOut` TIME NOT NULL,
  `cost` FLOAT(7,2) NOT NULL,
  `vehicle_id` INT NOT NULL,
  `maintenance_status_id` INT NOT NULL,
  PRIMARY KEY (`id`),
  INDEX `fk_maintenance_vehicle1_idx` (`vehicle_id` ASC) VISIBLE,
  INDEX `fk_maintenance_maintenance_status1_idx` (`maintenance_status_id` ASC) VISIBLE,
  CONSTRAINT `fk_maintenance_maintenance_status1`
    FOREIGN KEY (`maintenance_status_id`)
    REFERENCES `vehicle`.`maintenance_status` (`id`),
  CONSTRAINT `fk_maintenance_vehicle1`
    FOREIGN KEY (`vehicle_id`)
    REFERENCES `vehicle`.`vehicle` (`id`))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8mb4
COLLATE = utf8mb4_0900_ai_ci;


-- -----------------------------------------------------
-- Table `vehicle`.`maintenance_rec`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `vehicle`.`maintenance_rec` (
  `id` INT NOT NULL,
  `name` VARCHAR(45) NOT NULL,
  `description` TEXT NOT NULL,
  `solved` TINYINT NOT NULL,
  `maintenance_id` INT NOT NULL,
  PRIMARY KEY (`id`),
  INDEX `fk_maintenance_rek_maintenance1_idx` (`maintenance_id` ASC) VISIBLE,
  CONSTRAINT `fk_maintenance_rek_maintenance1`
    FOREIGN KEY (`maintenance_id`)
    REFERENCES `vehicle`.`maintenance` (`id`))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8mb4
COLLATE = utf8mb4_0900_ai_ci;


-- -----------------------------------------------------
-- Table `vehicle`.`users`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `vehicle`.`users` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `password` CHAR(68) NOT NULL,
  `enabled` TINYINT NOT NULL,
  `username` VARCHAR(50) NULL DEFAULT NULL,
  PRIMARY KEY (`id`))
ENGINE = InnoDB
DEFAULT CHARACTER SET = latin1;


-- -----------------------------------------------------
-- Table `vehicle`.`users_has_authorities`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `vehicle`.`users_has_authorities` (
  `users_id` INT NOT NULL,
  `authorities_id` INT NOT NULL,
  PRIMARY KEY (`users_id`, `authorities_id`),
  INDEX `fk_users_has_authorities_authorities1_idx` (`authorities_id` ASC) VISIBLE,
  INDEX `fk_users_has_authorities_users1_idx` (`users_id` ASC) VISIBLE,
  CONSTRAINT `fk_users_has_authorities_authorities1`
    FOREIGN KEY (`authorities_id`)
    REFERENCES `vehicle`.`authorities` (`id`),
  CONSTRAINT `fk_users_has_authorities_users1`
    FOREIGN KEY (`users_id`)
    REFERENCES `vehicle`.`users` (`id`))
ENGINE = InnoDB
DEFAULT CHARACTER SET = latin1;


-- -----------------------------------------------------
-- Table `vehicle`.`users_has_vehicle`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `vehicle`.`users_has_vehicle` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `vehicle_id` INT NOT NULL,
  `users_id` INT NOT NULL,
  `mileage` VARCHAR(45) NOT NULL,
  `vin` VARCHAR(17) NOT NULL,
  `owned` TINYINT NOT NULL,
  PRIMARY KEY (`id`, `vehicle_id`, `users_id`),
  INDEX `fk_users_has_vehicle_vehicle2_idx` (`vehicle_id` ASC) VISIBLE,
  INDEX `fk_users_has_vehicle_users2_idx` (`users_id` ASC) VISIBLE,
  CONSTRAINT `fk_users_has_vehicle_users2`
    FOREIGN KEY (`users_id`)
    REFERENCES `vehicle`.`users` (`id`),
  CONSTRAINT `fk_users_has_vehicle_vehicle2`
    FOREIGN KEY (`vehicle_id`)
    REFERENCES `vehicle`.`vehicle` (`id`))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8mb4
COLLATE = utf8mb4_0900_ai_ci;

SET SQL_MODE=@OLD_SQL_MODE;
SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS;
SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS;
