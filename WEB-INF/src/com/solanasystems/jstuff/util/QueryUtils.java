package com.solanasystems.jstuff.util;

import static com.solanasystems.jstuff.enums.Roles.ROLE_ADMIN;
import static com.solanasystems.utils.common.Constants.CHAR_SP;

import org.apache.commons.lang3.StringUtils;

import com.solanasystems.utils.util.SqlQueryUtils;

public class QueryUtils {

	private static final String EXCLUDE_ADMIN_QUERY_SUFFIX = "`name` NOT" + SqlQueryUtils.like(ROLE_ADMIN);

	public static String excludeAdminQuery(final boolean isAdmin) {
		return excludeAdminQuery(isAdmin, null);
	}

	public static String excludeAdminQuery(final boolean isAdmin, final String tableName) {
		return isAdmin ? " 1" : CHAR_SP + SqlQueryUtils.tablePrefix(tableName) + EXCLUDE_ADMIN_QUERY_SUFFIX;
	}

	public static String getPopulateFieldQuery(final int size) {
		return StringUtils.repeat(", ?", size);
	}

}
