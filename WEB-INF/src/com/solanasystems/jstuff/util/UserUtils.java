package com.solanasystems.jstuff.util;

import static com.solanasystems.jstuff.common.Constants.DATA_IS_LOGGED_IN;
import static com.solanasystems.jstuff.common.Constants.SES_LOGIN_USER;
import static com.solanasystems.jstuff.enums.Roles.ROLES_ALL;
import static com.solanasystems.jstuff.enums.Roles.ROLE_ADMIN;
import static com.solanasystems.jstuff.enums.Roles.ROLE_MANAGER;
import static com.solanasystems.jstuff.enums.Roles.ROLE_USER;

import javax.servlet.http.HttpServletRequest;

import com.solanasystems.jstuff.bo.UserBO;
import com.solanasystems.jstuff.om.User;
import com.solanasystems.utils.common.Log4J;
import com.solanasystems.utils.exception.ApplicationException;
import com.solanasystems.utils.util.Utils;

public class UserUtils {

	public static void setRoleFlag(final HttpServletRequest req) {
		for (String role : ROLES_ALL) {
			if (isUserInRole(req, role)) {
				req.setAttribute(Utils.attrName("is", role), true);
				req.setAttribute(DATA_IS_LOGGED_IN, true);
				break;
			}
		}
	}

	public static boolean isAdmin(final HttpServletRequest req) {
		return isUserInRole(req, ROLE_ADMIN);
	}

	public static boolean isManager(final HttpServletRequest req) {
		return isUserInRole(req, ROLE_MANAGER);
	}

	public static boolean isUser(final HttpServletRequest req) {
		return isUserInRole(req, ROLE_USER);
	}

	private static boolean isUserInRole(final HttpServletRequest req, final String role) {
		return req.isUserInRole(role);
	}

	public static void setUser(final HttpServletRequest req, final User user) {
		req.getSession().setAttribute(SES_LOGIN_USER, user);
	}

	public static User getUser(final HttpServletRequest req) {
		return getUser(req, null);
	}

	public static User getUser(final HttpServletRequest req, UserBO bo) {
		User user = null;
		final Object o = req.getSession().getAttribute(SES_LOGIN_USER);
		if (o instanceof User) {
			user = (User) o;
		} else if (req.getUserPrincipal() != null) {
			try {
				if (bo == null)
					bo = UserBO.getInstance();
				user = bo.readUser(req.getUserPrincipal().getName());
				setUser(req, user);
			} catch (ApplicationException e) {
				Log4J.error(UserUtils.class, "error in readUser() " + e.getMessage());
			}
		}
		return user;
	}

	public static int getUserId(final HttpServletRequest req) {
		return getUserId(req, null);
	}

	public static int getUserId(final HttpServletRequest req, final UserBO bo) {
		final User u = getUser(req, bo);
		return u != null ? u.getId() : 0;
	}

	public static boolean isNotAdminUser(final int roleId) {
		return roleId != 1;
	}

}
