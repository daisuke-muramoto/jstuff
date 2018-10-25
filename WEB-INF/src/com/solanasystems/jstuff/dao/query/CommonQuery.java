package com.solanasystems.jstuff.dao.query;

import com.solanasystems.jstuff.util.QueryUtils;

public interface CommonQuery {

	public static final String TABLE_JOIN_USER_ROLE = " LEFT JOIN `UserRole` ur ON u.`id` = ur.`userId`";
	public static final String TABLE_JOIN_ROLE = " LEFT JOIN `Role` r ON ur.`roleId` = r.`id`";
	public static final String TABLES_USER_ROLE = " FROM `User` u" + TABLE_JOIN_USER_ROLE;
	public static final String TABLES_ALL_USER_ROLE = TABLES_USER_ROLE + TABLE_JOIN_ROLE;
	public static final String TABLE_JOIN_USER_COMPANY_IDS = " LEFT JOIN `UserCompanyIds` uc ON u.`id` = uc.`userId`";

	public static final String EXCLUDE_ADMIN_USER_QUERY = QueryUtils.excludeAdminQuery(false, "r");

}
