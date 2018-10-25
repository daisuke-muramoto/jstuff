package com.solanasystems.jstuff.action;

import static com.solanasystems.jstuff.enums.Roles.ROLE_ADMIN;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;

import com.solanasystems.jstuff.bo.UserBO;
import com.solanasystems.jstuff.om.User;
import com.solanasystems.utils.action.VersionAction;
import com.solanasystems.utils.exception.ApplicationException;

public class DisplayVersionAction extends VersionAction {

	private static final String USER_NAME = "david", OK = "dbok", NG = "dbng", DB_STATUS = "dbStatus";

	private static final UserBO bo = UserBO.getInstance();

	public void exec(final ActionMapping mapping, final ActionForm form, final HttpServletRequest request, final HttpServletResponse response) throws Exception {
		try {
			final User user = bo.readUser(USER_NAME);
			final String dbStatus = user != null && USER_NAME.equals(user.getName()) ? OK : NG;
			request.setAttribute(DB_STATUS, dbStatus);
		} catch (ApplicationException e) {
		}
		if (request.getUserPrincipal() != null && StringUtils.isNotBlank(request.getUserPrincipal().getName()) && request.isUserInRole(ROLE_ADMIN)) {
			set(request, true);
			request.setAttribute("isAuth", true);
		}
	}

}
