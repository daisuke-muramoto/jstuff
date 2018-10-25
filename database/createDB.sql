
CREATE DATABASE `jstuffDB` ;
USE `jstuffDB` ;

CREATE TABLE `Role` (
 `id` INT( 10 ) UNSIGNED NOT NULL AUTO_INCREMENT ,
 `name` VARCHAR( 32 ) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL ,
 PRIMARY KEY ( `id` ) ,
 UNIQUE KEY ( `name` )
) ENGINE = MyISAM ;

INSERT INTO `Role` (`id`, `name`) VALUES 
( 1, 'admin' ), ( 2, 'manager' ), ( 3, 'user' ) ;


CREATE TABLE `User` (
 `id` INT( 10 ) UNSIGNED NOT NULL AUTO_INCREMENT ,
 `name` VARCHAR( 128 ) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL ,
 `pass` VARCHAR( 64 ) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL ,
 `lastUsedCompanyId` INT( 10 ) UNSIGNED NOT NULL ,
 PRIMARY KEY ( `id` ) ,
 UNIQUE KEY ( `name` )
) ENGINE = MyISAM ;

INSERT INTO `User` (`id`, `name`, `pass`, `lastUsedCompanyId`) VALUES
( 1, 'david', CONCAT( 'MD5:', MD5('david_pass') ), 1 ), ( 2, 'taiyoshi', CONCAT( 'MD5:', MD5('taiyoshi_pass') ), 1 ) ;


CREATE TABLE `UserRole` (
 `userId` INT( 10 ) UNSIGNED NOT NULL ,
 `roleId` INT( 10 ) UNSIGNED NOT NULL ,
 PRIMARY KEY ( `userId` , `roleId` )
) ENGINE = MyISAM ;

INSERT INTO `UserRole` (`userId`, `roleId`) VALUES
( 1, 1 ), ( 2, 1 ) ;


CREATE TABLE `Company` (
 `id` INT( 10 ) UNSIGNED NOT NULL AUTO_INCREMENT ,
 `name` VARCHAR( 128 ) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL ,
 `address` VARCHAR( 255 ) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL ,
 PRIMARY KEY ( `id` ) ,
 UNIQUE KEY ( `name` )
) ENGINE = MyISAM ;

INSERT INTO `Company` (`id`, `name`, `address`) VALUES ( 1, 'Solana Systems', '' ) ;


CREATE TABLE `UserCompanyIds` (
 `userId` INT( 10 ) UNSIGNED NOT NULL ,
 `companyId` INT( 10 ) UNSIGNED NOT NULL ,
 PRIMARY KEY ( `userId` , `companyId` )
) ENGINE = MyISAM ;


CREATE TABLE `Location` (
 `id` INT( 10 ) UNSIGNED NOT NULL AUTO_INCREMENT ,
 `companyId` INT( 10 ) UNSIGNED NOT NULL ,
 `name` VARCHAR( 128 ) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL ,
 `address` VARCHAR( 255 ) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL ,
 PRIMARY KEY ( `id` ) ,
 UNIQUE KEY `name` ( `companyId`, `name` ) ,
 KEY ( `companyId` )
) ENGINE = MyISAM ;

CREATE TABLE `Category` (
 `id` INT( 10 ) UNSIGNED NOT NULL AUTO_INCREMENT ,
 `companyId` INT( 10 ) UNSIGNED NOT NULL ,
 `name` VARCHAR( 128 ) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL ,
 `description` VARCHAR( 255 ) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL ,
 PRIMARY KEY ( `id` ) ,
 UNIQUE KEY `name` ( `companyId`, `name` ) ,
 KEY ( `companyId` )
) ENGINE = MyISAM ;

CREATE TABLE `Stuff` (
 `id` INT( 10 ) UNSIGNED NOT NULL AUTO_INCREMENT ,
 `companyId` INT( 10 ) UNSIGNED NOT NULL ,
 `categoryId` INT( 10 ) UNSIGNED NOT NULL ,
 `name` VARCHAR( 128 ) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL ,
 `description` VARCHAR( 255 ) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL ,
 PRIMARY KEY ( `id` ) ,
 UNIQUE KEY `name` ( `companyId`, `name` ) ,
 KEY ( `companyId` ) ,
 KEY ( `categoryId` )
) ENGINE = MyISAM ;

CREATE TABLE `Vendor` (
 `id` INT( 10 ) UNSIGNED NOT NULL AUTO_INCREMENT ,
 `companyId` INT( 10 ) UNSIGNED NOT NULL ,
 `name` VARCHAR( 128 ) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL ,
 `description` VARCHAR( 255 ) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL ,
 PRIMARY KEY ( `id` ) ,
 UNIQUE KEY `name` ( `companyId`, `name` ) ,
 KEY ( `companyId` )
) ENGINE = MyISAM ;
