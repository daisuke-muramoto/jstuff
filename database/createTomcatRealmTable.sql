
	DROP TABLE IF EXISTS `UserRoleTomcat` ;
	CREATE TABLE `UserRoleTomcat` (
	 `userName` VARCHAR( 128 ) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL ,
	 `password` VARCHAR( 64 ) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL ,
	 `roleName` VARCHAR( 32 ) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL ,
	 PRIMARY KEY ( `userName` , `roleName` )
	) ENGINE = MYISAM ;
	INSERT INTO `UserRoleTomcat` ( `userName` , `password` , `roleName` ) SELECT u.`name`, SUBSTR(u.`pass`, 5), r.`name` 
	FROM `User` u LEFT JOIN `UserRole` ur ON u.`id` = ur.`userId` LEFT JOIN `Role` r ON ur.`roleId` = r.`id` ;
	