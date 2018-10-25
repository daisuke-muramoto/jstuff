package com.solanasystems.jstuff.action;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.solanasystems.jstuff.bo.AdminBO;
import com.solanasystems.jstuff.om.Company;
import com.solanasystems.jstuff.om.User;
import com.solanasystems.jstuff.util.UserUtils;
import com.solanasystems.utils.action.ActionUtils;
import com.solanasystems.utils.common.Constants;
import com.solanasystems.utils.exception.ApplicationException;
import com.solanasystems.utils.util.Utils;

public class CommonAction extends ActionUtils implements com.solanasystems.jstuff.common.Constants {

	public static final String FORWARD_CANCEL = Constants.FORWARD_CANCEL, FORWARD_ERROR = Constants.FORWARD_ERROR, FORWARD_LIST = Constants.FORWARD_LIST,
			FORWARD_SUCCESS = Constants.FORWARD_SUCCESS;

	private static final String ERRORS_USER_ROLE = "errors.user.role";

	public static void setUserCompanyList(final HttpServletRequest req) {
		setListData(req.getSession(), getUserCompanyList(req), SES_USER_COMPANY_LIST);
	}

	public static List<Company> getUserCompanyList(final HttpServletRequest req) {
		List<Company> list = null;
		final boolean isAdmin = UserUtils.isAdmin(req);
		if (isAdmin || UserUtils.isManager(req) || UserUtils.isUser(req)) {
			try {
				final AdminBO bo = AdminBO.getInstance();
				list = bo.getCompanyList(isAdmin ? 0 : UserUtils.getUserId(req));
				if (Utils.hasNotListData(list) || list.size() == 1)
					list = null;
			} catch (ApplicationException e) {
				logger.error("error in getUserCompanyList() " + e.getMessage());
			}
		}
		return list;
	}

	protected ActionForward roleErrorForward(final ActionMapping mapping, final HttpServletRequest req) {
		return mapping.findForward(roleErrorForward(req));
	}

	protected String roleErrorForward(final HttpServletRequest req) {
		addRoleError(req);
		return FORWARD_ROLE_ERROR;
	}

	protected void addRoleError(final HttpServletRequest req) {
		addSesError(req, ERRORS_USER_ROLE);
	}

	protected int getAction(final HttpServletRequest req) {
		if (req.getParameter(ACTION_READ) != null)
			return READ;
		else if (req.getParameter(ACTION_SAVE) != null)
			return SAVE;
		else if (req.getParameter(ACTION_DELETE) != null)
			return DELETE;
		return CANCEL;
	}

	protected boolean isAdmin(final HttpServletRequest req) {
		return UserUtils.isAdmin(req);
	}

	protected boolean isNotAdmin(final HttpServletRequest req) {
		return !isAdmin(req);
	}

	protected boolean isManager(final HttpServletRequest req) {
		return UserUtils.isManager(req);
	}

	protected boolean isUser(final HttpServletRequest req) {
		return UserUtils.isUser(req);
	}

	protected int getUserId(final HttpServletRequest req) {
		return UserUtils.getUserId(req);
	}

	protected int getUserIdForRole(final HttpServletRequest req) {
		return isUser(req) ? getUserId(req) : 0;
	}

	protected User getUser(final HttpServletRequest req) {
		return UserUtils.getUser(req);
	}

	protected String getUserName(final HttpServletRequest req) {
		return getUser(req).getName();
	}

	protected int getUserCompanyId(final HttpServletRequest req) {
		return getUser(req).getCompanyId();
	}

}
