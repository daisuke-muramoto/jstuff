--
-- - Added on 13th Jul 2018 for project process sheet
--

ALTER TABLE `Stuff` ADD `Piece` INT( 10 ) UNSIGNED NOT NULL AFTER `name` ;

--
-- - Added on 17th Jul 2018 for project process sheet
--

ALTER TABLE `Stuff` CHANGE `Piece` `quantity` INT( 10 ) ;
ALTER TABLE `Stuff` ADD `vendorId` INT( 10 ) UNSIGNED NOT NULL AFTER `categoryId` ;
ALTER TABLE `Stuff` ADD `locationId` INT( 10 ) UNSIGNED NOT NULL AFTER `description` ;

--
-- - Added on 23th Jul 2018 for project process sheet
--

CREATE TABLE `Stock` (
 `id` INT( 10 ) UNSIGNED NOT NULL AUTO_INCREMENT ,
 `companyId` INT( 10 ) UNSIGNED NOT NULL ,
 `itemId` INT( 10 ) UNSIGNED NOT NULL ,
 `locationId` INT( 10 ) UNSIGNED NOT NULL ,
 `quantity` INT( 10 ) UNSIGNED NOT NULL ,
 `comment` VARCHAR( 255 ) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL ,
 `date` DATETIME NULL DEFAULT NULL ,
 PRIMARY KEY ( `id` ) ,
 KEY ( `companyId` ) ,
 KEY ( `itemId` )
) ENGINE = MyISAM ;

CREATE TABLE `Delivery` (
 `id` INT( 10 ) UNSIGNED NOT NULL AUTO_INCREMENT ,
 `companyId` INT( 10 ) UNSIGNED NOT NULL ,
 `itemId` INT( 10 ) UNSIGNED NOT NULL ,
 `locationId` INT( 10 ) UNSIGNED NOT NULL ,
 `quantity` INT( 10 ) UNSIGNED NOT NULL ,
 `comment` VARCHAR( 255 ) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL ,
 `date` DATETIME NULL DEFAULT NULL ,
 PRIMARY KEY ( `id` ) ,
 KEY ( `companyId` ) ,
 KEY ( `itemId` )
) ENGINE = MyISAM ;

ALTER TABLE `Stuff` DROP COLUMN `quantity` , DROP COLUMN `locationId` ;

--
-- - Added on 24th Jul 2018 for project process sheet
--

ALTER TABLE `Stock` CHANGE `date` `stockedDate` date ;
ALTER TABLE `Delivery` CHANGE `date` `deliveredDate` date ;

--
-- - Added on 30th Jul 2018 for project process sheet
--

ALTER TABLE `Stock` ADD `categoryId` INT( 10 ) UNSIGNED NOT NULL AFTER `itemId` ;
ALTER TABLE `Delivery` ADD `categoryId` INT( 10 ) UNSIGNED NOT NULL AFTER `itemId` ;

--
-- - Added on 6th Aug 2018 for project process sheet
--

ALTER TABLE `Stock` CHANGE `stockedDate` `date` date ;
ALTER TABLE `Delivery` CHANGE `deliveredDate` `date` date ;

ALTER TABLE `Stock` ADD `roleId` INT( 10 ) UNSIGNED NOT NULL AFTER `id` ;
ALTER TABLE `Delivery` ADD `roleId` INT( 10 ) UNSIGNED NOT NULL AFTER `id` ;

DROP TABLE `Delivery` ;

--
-- - Added on 8th Aug 2018 for project process sheet
--

ALTER TABLE `Stock` DROP COLUMN `categoryId` ;

--
-- - Added on 27th Aug 2018 for project process sheet
--

ALTER TABLE `Stuff` CHANGE `vendorId` `supplierId` INT( 10 ) UNSIGNED NOT NULL ;

ALTER TABLE `Vendor` RENAME TO `Supplier` ;
ALTER TABLE `Stock` RENAME TO `Inventory` ;
ALTER TABLE `Stuff` RENAME TO `ItemMaster` ;

--
-- - Added on 28th Aug 2018 for project process sheet
--

ALTER TABLE `Inventory` ADD `supplierId` INT( 10 ) UNSIGNED NOT NULL AFTER `itemId` ;

UPDATE `Inventory`, `ItemMaster` SET `Inventory`.`supplierId` = `ItemMaster`.`supplierId` WHERE `Inventory`.`itemId` = `ItemMaster`.`id` ;

ALTER TABLE `ItemMaster` DROP COLUMN `supplierId` ;

--
-- - Added on 11th Sep 2018 for project process sheet
--

ALTER TABLE `ItemMaster` ADD `UPC` VARCHAR( 13 ) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL AFTER `name` ;

--
-- - Added on 14th Sep 2018 for project process sheet
--

ALTER TABLE `ItemMaster` CHANGE COLUMN `UPC` `CODE` VARCHAR( 13 ) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL ;

ALTER TABLE `Location` ADD `name_ja` VARCHAR( 128 ) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL AFTER `address` ;
ALTER TABLE `Location` ADD `address_ja` VARCHAR( 255 ) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL AFTER `name_ja` ;
ALTER TABLE `Company` ADD `name_ja` VARCHAR( 128 ) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL AFTER `address` ;
ALTER TABLE `Company` ADD `address_ja` VARCHAR( 255 ) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL AFTER `name_ja` ;
ALTER TABLE `Supplier` ADD `name_ja` VARCHAR( 128 ) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL AFTER `description` ;
ALTER TABLE `Supplier` ADD `description_ja` VARCHAR( 255 ) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL AFTER `name_ja` ;
ALTER TABLE `Category` ADD `name_ja` VARCHAR( 128 ) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL AFTER `description` ;
ALTER TABLE `Category` ADD `description_ja` VARCHAR( 255 ) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL AFTER `name_ja` ;
ALTER TABLE `ItemMaster` ADD `name_ja` VARCHAR( 128 ) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL AFTER `description` ;
ALTER TABLE `ItemMaster` ADD `CODE_ja` VARCHAR( 13 ) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL AFTER `name_ja` ;
ALTER TABLE `ItemMaster` ADD `description_ja` VARCHAR( 255 ) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL AFTER `CODE_ja` ;
ALTER TABLE `Inventory` ADD `comment_ja` VARCHAR( 255 ) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL AFTER `comment` ;

ALTER TABLE `Category` CHANGE COLUMN `name_ja` `nameJA` VARCHAR( 128 ) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL ;
ALTER TABLE `Category` CHANGE COLUMN `description_ja` `descriptionJA` VARCHAR( 255 ) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL ;

ALTER TABLE `Category` ADD UNIQUE KEY `nameJA` ( `companyId`, `nameJA` ) ;

--
-- - Added on 27th Sep 2018 for project process sheet
--

ALTER TABLE `Category` DROP KEY `nameJA` ;

ALTER TABLE `Location` CHANGE COLUMN `name_ja` `nameJA` VARCHAR( 128 ) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL ;
ALTER TABLE `Location` CHANGE COLUMN `address_ja` `addressJA` VARCHAR( 255 ) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL ;
ALTER TABLE `Company` CHANGE COLUMN `name_ja` `nameJA` VARCHAR( 128 ) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL ;
ALTER TABLE `Company` CHANGE COLUMN `address_ja` `addressJA` VARCHAR( 255 ) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL ;
ALTER TABLE `Supplier` CHANGE COLUMN `name_ja` `nameJA` VARCHAR( 128 ) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL ;
ALTER TABLE `Supplier` CHANGE COLUMN `description_ja` `descriptionJA` VARCHAR( 255 ) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL ;
ALTER TABLE `ItemMaster` CHANGE COLUMN `CODE` `code` VARCHAR( 13 ) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL ;
ALTER TABLE `ItemMaster` CHANGE COLUMN `name_ja` `nameJA` VARCHAR( 128 ) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL ;
ALTER TABLE `ItemMaster` CHANGE COLUMN `CODE_ja` `codeJA` VARCHAR( 13 ) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL ;
ALTER TABLE `ItemMaster` CHANGE COLUMN `description_ja` `descriptionJA` VARCHAR( 255 ) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL ;
ALTER TABLE `Inventory` CHANGE COLUMN `comment_ja` `commentJA` VARCHAR( 255 ) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL ;

--
-- - Added on 28th Sep 2018 for project process sheet
--

ALTER TABLE `Category` ADD UNIQUE KEY `nameJA` ( `companyId`, `nameJA` ) ;
ALTER TABLE `Company` ADD UNIQUE KEY `nameJA` ( `nameJA` ) ;
ALTER TABLE `ItemMaster` ADD UNIQUE KEY `nameJA` ( `companyId`, `nameJA` ) ;
ALTER TABLE `Location` ADD UNIQUE KEY `nameJA` ( `companyId`, `nameJA` ) ;
ALTER TABLE `Supplier` ADD UNIQUE KEY `nameJA` ( `companyId`, `nameJA` ) ;

--
-- - Added on 1st Oct 2018 for project process sheet
--

DROP TABLE `Category` ;
DROP TABLE `Company` ;
DROP TABLE `Inventory` ;
DROP TABLE `ItemMaster` ;
DROP TABLE `Location` ;
DROP TABLE `Supplier` ;

CREATE TABLE `Company` (
 `id` INT( 10 ) UNSIGNED NOT NULL AUTO_INCREMENT ,
 `name` VARCHAR( 128 ) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL ,
 `address` VARCHAR( 255 ) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL ,
 `nameJA` VARCHAR( 128 ) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL ,
 `addressJA` VARCHAR( 255 ) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL ,
 PRIMARY KEY ( `id` ) ,
 UNIQUE KEY ( `name` ) ,
 UNIQUE KEY ( `nameJA` )
) ENGINE = MyISAM ;

INSERT INTO `Company` (`id`, `name`, `address`, `nameJA`, `addressJA`) VALUES ( 1, 'Solana Systems', '', '', '' ) ;

CREATE TABLE `Location` (
 `id` INT( 10 ) UNSIGNED NOT NULL AUTO_INCREMENT ,
 `companyId` INT( 10 ) UNSIGNED NOT NULL ,
 `name` VARCHAR( 128 ) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL ,
 `address` VARCHAR( 255 ) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL ,
 `nameJA` VARCHAR( 128 ) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL ,
 `addressJA` VARCHAR( 255 ) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL ,
 PRIMARY KEY ( `id` ) ,
 UNIQUE KEY `name` ( `companyId`, `name` ) ,
 UNIQUE KEY `nameJA` ( `companyId`, `nameJA` ) ,
 KEY ( `companyId` )
) ENGINE = MyISAM ;

CREATE TABLE `Category` (
 `id` INT( 10 ) UNSIGNED NOT NULL AUTO_INCREMENT ,
 `companyId` INT( 10 ) UNSIGNED NOT NULL ,
 `name` VARCHAR( 128 ) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL ,
 `description` VARCHAR( 255 ) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL ,
 `nameJA` VARCHAR( 128 ) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL ,
 `descriptionJA` VARCHAR( 255 ) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL ,
 PRIMARY KEY ( `id` ) ,
 UNIQUE KEY `name` ( `companyId`, `name` ) ,
 UNIQUE KEY `nameJA` ( `companyId`, `nameJA` ) ,
 KEY ( `companyId` )
) ENGINE = MyISAM ;

CREATE TABLE `ItemMaster` (
 `id` INT( 10 ) UNSIGNED NOT NULL AUTO_INCREMENT ,
 `companyId` INT( 10 ) UNSIGNED NOT NULL ,
 `categoryId` INT( 10 ) UNSIGNED NOT NULL ,
 `name` VARCHAR( 128 ) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL ,
 `code` VARCHAR(13) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL,
 `description` VARCHAR( 255 ) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL ,
 `nameJA` VARCHAR( 128 ) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL ,
 `descriptionJA` VARCHAR( 255 ) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL ,
 PRIMARY KEY ( `id` ) ,
 UNIQUE KEY `name` ( `companyId`, `name` ) ,
 UNIQUE KEY `nameJA` ( `companyId`, `nameJA` ) ,
 KEY ( `companyId` ) ,
 KEY ( `categoryId` )
) ENGINE = MyISAM ;

CREATE TABLE `Supplier` (
 `id` INT( 10 ) UNSIGNED NOT NULL AUTO_INCREMENT ,
 `companyId` INT( 10 ) UNSIGNED NOT NULL ,
 `name` VARCHAR( 128 ) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL ,
 `description` VARCHAR( 255 ) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL ,
 `nameJA` VARCHAR( 128 ) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL ,
 `descriptionJA` VARCHAR( 255 ) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL ,
 PRIMARY KEY ( `id` ) ,
 UNIQUE KEY `name` ( `companyId`, `name` ) ,
 UNIQUE KEY `nameJA` ( `companyId`, `nameJA` ) ,
 KEY ( `companyId` )
) ENGINE = MyISAM ;

CREATE TABLE `Inventory` (
 `id` INT(10) UNSIGNED NOT NULL AUTO_INCREMENT ,
 `roleId` INT(10) UNSIGNED NOT NULL ,
 `companyId` INT(10) UNSIGNED NOT NULL ,
 `itemId` INT(10) UNSIGNED NOT NULL ,
 `supplierId` INT(10) UNSIGNED NOT NULL ,
 `locationId` INT(10) UNSIGNED NOT NULL ,
 `quantity` INT(10) UNSIGNED NOT NULL ,
 `comment` VARCHAR(255) DEFAULT NULL ,
 `date` DATE DEFAULT NULL ,
 PRIMARY KEY (`id`) ,
 KEY `roleId` (`roleId`) ,
 KEY `companyId` (`companyId`) ,
 KEY `itemId` (`itemId`) ,
 KEY `supplierId` (`supplierId`) ,
 KEY `locationId` (`locationId`)
) ENGINE=MyISAM  ;

--
-- - Added on 3rd Oct 2018 for project process sheet
--

CREATE TABLE `Log` (
 `id` INT(10) UNSIGNED NOT NULL AUTO_INCREMENT ,
 `companyId` INT(10) UNSIGNED NOT NULL ,
 `userName` VARCHAR( 32 ) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL ,
 `dateTime` VARCHAR( 128 ) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL ,
 `ipAddress` VARCHAR( 128 ) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL ,
 `tableName` VARCHAR( 128 ) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL ,
 `details` VARCHAR( 128 ) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL ,
 PRIMARY KEY (`id`) ,
 KEY `companyId` (`companyId`) 
) ENGINE=MyISAM  ;

--
-- - Added on 12th Oct 2018 for project process sheet
--

CREATE TABLE `Client` (
 `id` INT( 10 ) UNSIGNED NOT NULL AUTO_INCREMENT ,
 `companyId` INT( 10 ) UNSIGNED NOT NULL ,
 `name` VARCHAR( 128 ) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL ,
 `description` VARCHAR( 255 ) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL ,
 `nameJA` VARCHAR( 128 ) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL ,
 `descriptionJA` VARCHAR( 255 ) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL ,
 PRIMARY KEY ( `id` ) ,
 UNIQUE KEY `name` ( `companyId`, `name` ) ,
 UNIQUE KEY `nameJA` ( `companyId`, `nameJA` ) ,
 KEY ( `companyId` )
) ENGINE = MyISAM ;

ALTER TABLE `Inventory` ADD `clientId` INT( 10 ) UNSIGNED NOT NULL AFTER `itemId` ;
