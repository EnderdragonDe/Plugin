SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0;
SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0;
SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='TRADITIONAL,ALLOW_INVALID_DATES';

CREATE SCHEMA IF NOT EXISTS `$dbname` DEFAULT CHARACTER SET utf8 COLLATE utf8_general_ci ;
USE `$dbname` ;

-- -----------------------------------------------------
-- Table `$dbname`.`$prefix_players`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `$dbname`.`$prefix_players` ;

CREATE  TABLE IF NOT EXISTS `$dbname`.`$prefix_players` (
  `player_id` INT NOT NULL AUTO_INCREMENT ,
  `name` VARCHAR(16) NOT NULL ,
  `online` INT(11) NOT NULL DEFAULT 0 ,
  `exp_perc` INT(3) UNSIGNED NOT NULL DEFAULT 0 ,
  `exp_total` SMALLINT(5) UNSIGNED NULL DEFAULT 0 ,
  `level` SMALLINT(5) UNSIGNED NULL DEFAULT 1 ,
  `food_level` TINYINT(2) UNSIGNED NULL ,
  `health` TINYINT(2) UNSIGNED NULL DEFAULT NULL ,
  `first_login` INT(11) NULL ,
  `logins` INT(11) NULL ,
  PRIMARY KEY (`player_id`) );


-- -----------------------------------------------------
-- Table `$dbname`.`$prefix_detailed_log_players`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `$dbname`.`$prefix_detailed_log_players` ;

CREATE  TABLE IF NOT EXISTS `$dbname`.`$prefix_detailed_log_players` (
  `detailed_log_players_id` INT NOT NULL AUTO_INCREMENT ,
  `player_id` INT NOT NULL ,
  `time` INT(11) NOT NULL ,
  `world` VARCHAR(255) NULL ,
  `x` INT NULL ,
  `y` INT NULL ,
  `z` INT NULL ,
  `is_login` TINYINT(1) NULL DEFAULT 1 ,
  PRIMARY KEY (`detailed_log_players_id`, `player_id`) ,
  INDEX `fk_player_id4_idx` (`player_id` ASC) ,
  CONSTRAINT `fk_player_id4`
    FOREIGN KEY (`player_id` )
    REFERENCES `$dbname`.`$prefix_players` (`player_id` )
    ON DELETE CASCADE
    ON UPDATE NO ACTION);


-- -----------------------------------------------------
-- Table `$dbname`.`$prefix_distance_players`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `$dbname`.`$prefix_distance_players` ;

CREATE  TABLE IF NOT EXISTS `$dbname`.`$prefix_distance_players` (
  `distance_player_id` INT NOT NULL AUTO_INCREMENT ,
  `player_id` INT NOT NULL ,
  `foot` BIGINT(20) UNSIGNED NOT NULL DEFAULT 0 ,
  `boat` BIGINT(20) UNSIGNED NOT NULL DEFAULT 0 ,
  `minecart` BIGINT(20) UNSIGNED NOT NULL DEFAULT 0 ,
  `pig` BIGINT(20) UNSIGNED NOT NULL DEFAULT 0 ,
  PRIMARY KEY (`distance_player_id`, `player_id`) ,
  INDEX `fk_player_id1_idx` (`player_id` ASC) ,
  CONSTRAINT `fk_player_id1`
    FOREIGN KEY (`player_id` )
    REFERENCES `$dbname`.`$prefix_players` (`player_id` )
    ON DELETE CASCADE
    ON UPDATE NO ACTION);


-- -----------------------------------------------------
-- Table `$dbname`.`$prefix_creatures`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `$dbname`.`$prefix_creatures` ;

CREATE  TABLE IF NOT EXISTS `$dbname`.`$prefix_creatures` (
  `creature_id` INT NOT NULL AUTO_INCREMENT ,
  `tp_name` VARCHAR(100) NULL ,
  PRIMARY KEY (`creature_id`) );


-- -----------------------------------------------------
-- Table `$dbname`.`$prefix_materials`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `$dbname`.`$prefix_materials` ;

CREATE  TABLE IF NOT EXISTS `$dbname`.`$prefix_materials` (
  `material_id` INT NOT NULL ,
  `data` SMALLINT(3) UNSIGNED NULL DEFAULT 0 ,
  `tp_name` VARCHAR(45) NULL ,
  PRIMARY KEY (`material_id`) ,
  UNIQUE INDEX `tp_name_UNIQUE` (`tp_name` ASC) );


-- -----------------------------------------------------
-- Table `$dbname`.`$prefix_detailed_pvp_kills`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `$dbname`.`$prefix_detailed_pvp_kills` ;

CREATE  TABLE IF NOT EXISTS `$dbname`.`$prefix_detailed_pvp_kills` (
  `detailed_pvp_id` INT NOT NULL AUTO_INCREMENT ,
  `material_id` INT NOT NULL ,
  `player_id` INT NOT NULL ,
  `victim_id` INT NOT NULL ,
  `cause` VARCHAR(45) NULL ,
  `world` VARCHAR(255) NULL ,
  `x` INT NULL ,
  `y` INT NULL ,
  `z` INT NULL ,
  `time` INT(11) NULL ,
  PRIMARY KEY (`detailed_pvp_id`, `material_id`, `player_id`, `victim_id`) ,
  INDEX `fk_player_id17_idx` (`player_id` ASC) ,
  INDEX `fk_player_id18_idx` (`victim_id` ASC) ,
  INDEX `fk_material_id11_idx` (`material_id` ASC) ,
  CONSTRAINT `fk_player_id17`
    FOREIGN KEY (`player_id` )
    REFERENCES `$dbname`.`$prefix_players` (`player_id` )
    ON DELETE CASCADE
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_player_id18`
    FOREIGN KEY (`victim_id` )
    REFERENCES `$dbname`.`$prefix_players` (`player_id` )
    ON DELETE CASCADE
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_material_id11`
    FOREIGN KEY (`material_id` )
    REFERENCES `$dbname`.`$prefix_materials` (`material_id` )
    ON DELETE CASCADE
    ON UPDATE NO ACTION);


-- -----------------------------------------------------
-- Table `$dbname`.`$prefix_detailed_death_players`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `$dbname`.`$prefix_detailed_death_players` ;

CREATE  TABLE IF NOT EXISTS `$dbname`.`$prefix_detailed_death_players` (
  `detailed_death_players_id` INT NOT NULL AUTO_INCREMENT ,
  `player_id` INT NOT NULL ,
  `cause` VARCHAR(45) NOT NULL ,
  `world` VARCHAR(255) NULL ,
  `x` INT NULL ,
  `y` INT NULL ,
  `z` INT NULL ,
  `time` INT NULL ,
  PRIMARY KEY (`detailed_death_players_id`, `player_id`) ,
  INDEX `fk_player_id2_idx` (`player_id` ASC) ,
  CONSTRAINT `fk_player_id2`
    FOREIGN KEY (`player_id` )
    REFERENCES `$dbname`.`$prefix_players` (`player_id` )
    ON DELETE CASCADE
    ON UPDATE NO ACTION);


-- -----------------------------------------------------
-- Table `$dbname`.`$prefix_detailed_placed_blocks`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `$dbname`.`$prefix_detailed_placed_blocks` ;

CREATE  TABLE IF NOT EXISTS `$dbname`.`$prefix_detailed_placed_blocks` (
  `detailed_placed_blocks_id` INT NOT NULL AUTO_INCREMENT ,
  `material_id` INT NOT NULL ,
  `player_id` INT NOT NULL ,
  `world` VARCHAR(255) NULL ,
  `x` INT NULL ,
  `y` INT NULL ,
  `z` INT NULL ,
  `time` INT(11) NULL ,
  PRIMARY KEY (`detailed_placed_blocks_id`, `material_id`, `player_id`) ,
  INDEX `fk_player_id7_idx` (`player_id` ASC) ,
  INDEX `fk_material_id3_idx` (`material_id` ASC) ,
  CONSTRAINT `fk_player_id7`
    FOREIGN KEY (`player_id` )
    REFERENCES `$dbname`.`$prefix_players` (`player_id` )
    ON DELETE CASCADE
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_material_id3`
    FOREIGN KEY (`material_id` )
    REFERENCES `$dbname`.`$prefix_materials` (`material_id` )
    ON DELETE CASCADE
    ON UPDATE NO ACTION);


-- -----------------------------------------------------
-- Table `$dbname`.`$prefix_detailed_destroyed_blocks`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `$dbname`.`$prefix_detailed_destroyed_blocks` ;

CREATE  TABLE IF NOT EXISTS `$dbname`.`$prefix_detailed_destroyed_blocks` (
  `detailed_destroyed_blocks_id` INT NOT NULL AUTO_INCREMENT ,
  `material_id` INT NOT NULL ,
  `player_id` INT NOT NULL ,
  `world` VARCHAR(255) NULL ,
  `x` INT NULL ,
  `y` INT NULL ,
  `z` INT NULL ,
  `time` INT(11) NULL ,
  PRIMARY KEY (`detailed_destroyed_blocks_id`, `material_id`, `player_id`) ,
  INDEX `fk_player_id6_idx` (`player_id` ASC) ,
  INDEX `fk_material_id2_idx` (`material_id` ASC) ,
  CONSTRAINT `fk_player_id6`
    FOREIGN KEY (`player_id` )
    REFERENCES `$dbname`.`$prefix_players` (`player_id` )
    ON DELETE CASCADE
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_material_id2`
    FOREIGN KEY (`material_id` )
    REFERENCES `$dbname`.`$prefix_materials` (`material_id` )
    ON DELETE CASCADE
    ON UPDATE NO ACTION);


-- -----------------------------------------------------
-- Table `$dbname`.`$prefix_detailed_dropped_items`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `$dbname`.`$prefix_detailed_dropped_items` ;

CREATE  TABLE IF NOT EXISTS `$dbname`.`$prefix_detailed_dropped_items` (
  `detailed_dropped_items_id` INT NOT NULL AUTO_INCREMENT ,
  `material_id` INT NOT NULL ,
  `player_id` INT NOT NULL ,
  `world` VARCHAR(255) NULL ,
  `x` INT NULL ,
  `y` INT NULL ,
  `z` INT NULL ,
  `time` INT(11) NULL ,
  PRIMARY KEY (`detailed_dropped_items_id`, `material_id`, `player_id`) ,
  INDEX `fk_player_id11_idx` (`player_id` ASC) ,
  INDEX `fk_material_id7_idx` (`material_id` ASC) ,
  CONSTRAINT `fk_player_id11`
    FOREIGN KEY (`player_id` )
    REFERENCES `$dbname`.`$prefix_players` (`player_id` )
    ON DELETE CASCADE
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_material_id7`
    FOREIGN KEY (`material_id` )
    REFERENCES `$dbname`.`$prefix_materials` (`material_id` )
    ON DELETE CASCADE
    ON UPDATE NO ACTION);


-- -----------------------------------------------------
-- Table `$dbname`.`$prefix_detailed_pickedup_items`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `$dbname`.`$prefix_detailed_pickedup_items` ;

CREATE  TABLE IF NOT EXISTS `$dbname`.`$prefix_detailed_pickedup_items` (
  `detailed_pickedup_items_id` INT NOT NULL AUTO_INCREMENT ,
  `material_id` INT NOT NULL ,
  `player_id` INT NOT NULL ,
  `world` VARCHAR(255) NULL ,
  `x` INT NULL ,
  `y` INT NULL ,
  `z` INT NULL ,
  `time` INT(11) NULL ,
  PRIMARY KEY (`detailed_pickedup_items_id`, `material_id`, `player_id`) ,
  INDEX `fk_player_id12_idx` (`player_id` ASC) ,
  INDEX `fk_material_id8_idx` (`material_id` ASC) ,
  CONSTRAINT `fk_player_id12`
    FOREIGN KEY (`player_id` )
    REFERENCES `$dbname`.`$prefix_players` (`player_id` )
    ON DELETE CASCADE
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_material_id8`
    FOREIGN KEY (`material_id` )
    REFERENCES `$dbname`.`$prefix_materials` (`material_id` )
    ON DELETE CASCADE
    ON UPDATE NO ACTION);


-- -----------------------------------------------------
-- Table `$dbname`.`$prefix_settings`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `$dbname`.`$prefix_settings` ;

CREATE  TABLE IF NOT EXISTS `$dbname`.`$prefix_settings` (
  `key` VARCHAR(64) NOT NULL ,
  `value` TEXT NOT NULL ,
  PRIMARY KEY (`key`) );


-- -----------------------------------------------------
-- Table `$dbname`.`$prefix_detailed_pve_kills`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `$dbname`.`$prefix_detailed_pve_kills` ;

CREATE  TABLE IF NOT EXISTS `$dbname`.`$prefix_detailed_pve_kills` (
  `detailed_pve_id` INT NOT NULL AUTO_INCREMENT ,
  `material_id` INT NOT NULL ,
  `creature_id` INT NOT NULL ,
  `player_id` INT NOT NULL ,
  `cause` VARCHAR(45) NULL ,
  `world` VARCHAR(255) NULL ,
  `x` INT NULL ,
  `y` INT NULL ,
  `z` INT NULL ,
  `time` INT(11) NULL ,
  `player_killed` TINYINT(1) NULL DEFAULT false ,
  PRIMARY KEY (`detailed_pve_id`, `material_id`, `creature_id`, `player_id`) ,
  INDEX `fk_player_id16_idx` (`player_id` ASC) ,
  INDEX `fk_material_id10_idx` (`material_id` ASC) ,
  INDEX `fk_creature_id2_idx` (`creature_id` ASC) ,
  CONSTRAINT `fk_player_id16`
    FOREIGN KEY (`player_id` )
    REFERENCES `$dbname`.`$prefix_players` (`player_id` )
    ON DELETE CASCADE
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_material_id10`
    FOREIGN KEY (`material_id` )
    REFERENCES `$dbname`.`$prefix_materials` (`material_id` )
    ON DELETE CASCADE
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_creature_id2`
    FOREIGN KEY (`creature_id` )
    REFERENCES `$dbname`.`$prefix_creatures` (`creature_id` )
    ON DELETE CASCADE
    ON UPDATE NO ACTION);


-- -----------------------------------------------------
-- Table `$dbname`.`$prefix_total_blocks`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `$dbname`.`$prefix_total_blocks` ;

CREATE  TABLE IF NOT EXISTS `$dbname`.`$prefix_total_blocks` (
  `total_blocks_id` INT NOT NULL AUTO_INCREMENT ,
  `material_id` INT NOT NULL ,
  `player_id` INT NOT NULL ,
  `destroyed` INT UNSIGNED NULL DEFAULT 0 ,
  `placed` INT UNSIGNED NULL DEFAULT 0 ,
  PRIMARY KEY (`total_blocks_id`, `material_id`, `player_id`) ,
  INDEX `fk_player_id5_idx` (`player_id` ASC) ,
  INDEX `fk_material_id1_idx` (`material_id` ASC) ,
  CONSTRAINT `fk_player_id5`
    FOREIGN KEY (`player_id` )
    REFERENCES `$dbname`.`$prefix_players` (`player_id` )
    ON DELETE CASCADE
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_material_id1`
    FOREIGN KEY (`material_id` )
    REFERENCES `$dbname`.`$prefix_materials` (`material_id` )
    ON DELETE CASCADE
    ON UPDATE NO ACTION);


-- -----------------------------------------------------
-- Table `$dbname`.`$prefix_total_items`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `$dbname`.`$prefix_total_items` ;

CREATE  TABLE IF NOT EXISTS `$dbname`.`$prefix_total_items` (
  `total_items_id` INT NOT NULL AUTO_INCREMENT ,
  `material_id` INT NOT NULL ,
  `player_id` INT NOT NULL ,
  `dropped` INT UNSIGNED NULL DEFAULT 0 ,
  `picked_up` INT UNSIGNED NULL DEFAULT 0 ,
  `used` INT UNSIGNED NULL DEFAULT 0 ,
  PRIMARY KEY (`total_items_id`, `material_id`, `player_id`) ,
  INDEX `fk_player_id10_idx` (`player_id` ASC) ,
  INDEX `fk_material_id6_idx` (`material_id` ASC) ,
  CONSTRAINT `fk_player_id10`
    FOREIGN KEY (`player_id` )
    REFERENCES `$dbname`.`$prefix_players` (`player_id` )
    ON DELETE CASCADE
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_material_id6`
    FOREIGN KEY (`material_id` )
    REFERENCES `$dbname`.`$prefix_materials` (`material_id` )
    ON DELETE CASCADE
    ON UPDATE NO ACTION);


-- -----------------------------------------------------
-- Table `$dbname`.`$prefix_total_death_players`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `$dbname`.`$prefix_total_death_players` ;

CREATE  TABLE IF NOT EXISTS `$dbname`.`$prefix_total_death_players` (
  `total_death_players_id` INT NOT NULL AUTO_INCREMENT ,
  `player_id` INT NOT NULL ,
  `cause` VARCHAR(45) NOT NULL ,
  `times` VARCHAR(45) NULL ,
  PRIMARY KEY (`total_death_players_id`, `player_id`) ,
  INDEX `fk_player_id3_idx` (`player_id` ASC) ,
  CONSTRAINT `fk_player_id3`
    FOREIGN KEY (`player_id` )
    REFERENCES `$dbname`.`$prefix_players` (`player_id` )
    ON DELETE CASCADE
    ON UPDATE NO ACTION);


-- -----------------------------------------------------
-- Table `$dbname`.`$prefix_total_pvp_kills`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `$dbname`.`$prefix_total_pvp_kills` ;

CREATE  TABLE IF NOT EXISTS `$dbname`.`$prefix_total_pvp_kills` (
  `total_pvp_id` INT NOT NULL AUTO_INCREMENT ,
  `material_id` INT NOT NULL ,
  `player_id` INT NOT NULL ,
  `victim_id` INT NOT NULL ,
  `times` INT(10) NULL ,
  PRIMARY KEY (`total_pvp_id`, `material_id`, `player_id`, `victim_id`) ,
  INDEX `fk_player_id14_idx` (`player_id` ASC) ,
  INDEX `fk_player_id15_idx` (`victim_id` ASC) ,
  INDEX `fk_material_id9_idx` (`material_id` ASC) ,
  CONSTRAINT `fk_player_id14`
    FOREIGN KEY (`player_id` )
    REFERENCES `$dbname`.`$prefix_players` (`player_id` )
    ON DELETE CASCADE
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_player_id15`
    FOREIGN KEY (`victim_id` )
    REFERENCES `$dbname`.`$prefix_players` (`player_id` )
    ON DELETE CASCADE
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_material_id9`
    FOREIGN KEY (`material_id` )
    REFERENCES `$dbname`.`$prefix_materials` (`material_id` )
    ON DELETE CASCADE
    ON UPDATE NO ACTION);


-- -----------------------------------------------------
-- Table `$dbname`.`$prefix_total_pve_kills`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `$dbname`.`$prefix_total_pve_kills` ;

CREATE  TABLE IF NOT EXISTS `$dbname`.`$prefix_total_pve_kills` (
  `total_pve_id` INT NOT NULL AUTO_INCREMENT ,
  `material_id` INT NOT NULL ,
  `creature_id` INT NOT NULL ,
  `player_id` INT NOT NULL ,
  `player_killed` INT(10) NULL DEFAULT 0 ,
  `creature_killd` INT(10) NULL DEFAULT 0 ,
  PRIMARY KEY (`total_pve_id`, `material_id`, `creature_id`, `player_id`) ,
  INDEX `fk_player_id13_idx` (`player_id` ASC) ,
  INDEX `fk_material_id12_idx` (`material_id` ASC) ,
  INDEX `fk_creature_id1_idx` (`creature_id` ASC) ,
  CONSTRAINT `fk_player_id13`
    FOREIGN KEY (`player_id` )
    REFERENCES `$dbname`.`$prefix_players` (`player_id` )
    ON DELETE CASCADE
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_material_id12`
    FOREIGN KEY (`material_id` )
    REFERENCES `$dbname`.`$prefix_materials` (`material_id` )
    ON DELETE CASCADE
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_creature_id1`
    FOREIGN KEY (`creature_id` )
    REFERENCES `$dbname`.`$prefix_creatures` (`creature_id` )
    ON DELETE CASCADE
    ON UPDATE NO ACTION);


-- -----------------------------------------------------
-- Table `$dbname`.`$prefix_detailed_used_items`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `$dbname`.`$prefix_detailed_used_items` ;

CREATE  TABLE IF NOT EXISTS `$dbname`.`$prefix_detailed_used_items` (
  `detailed_used_items_id` INT NOT NULL ,
  `material_id` INT NOT NULL ,
  `player_id` INT NOT NULL ,
  `world` VARCHAR(255) NULL ,
  `x` VARCHAR(45) NULL ,
  `y` VARCHAR(45) NULL ,
  `z` VARCHAR(45) NULL ,
  `time` INT NULL ,
  PRIMARY KEY (`detailed_used_items_id`, `material_id`, `player_id`) ,
  INDEX `fk_player_id8_idx` (`player_id` ASC) ,
  INDEX `fk_material_id4_idx` (`material_id` ASC) ,
  CONSTRAINT `fk_player_id8`
    FOREIGN KEY (`player_id` )
    REFERENCES `$dbname`.`$prefix_players` (`player_id` )
    ON DELETE CASCADE
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_material_id4`
    FOREIGN KEY (`material_id` )
    REFERENCES `$dbname`.`$prefix_materials` (`material_id` )
    ON DELETE CASCADE
    ON UPDATE NO ACTION);

USE `$dbname` ;


SET SQL_MODE=@OLD_SQL_MODE;
SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS;
SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS;