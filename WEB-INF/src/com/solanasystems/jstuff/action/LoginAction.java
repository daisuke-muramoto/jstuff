package com.solanasystems.jstuff.action;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

public class LoginAction extends CommonAction {

	public ActionForward execute(final ActionMapping mapping, final ActionForm form, final HttpServletRequest request, final HttpServletResponse response)
			throws Exception {
		getUser(request);
		setUserCompanyList(request);
		return mapping.findForward(getPath(isLoggedIn(request)));
	}

	protected boolean isLoggedIn(final HttpServletRequest req) {
		return req.getUserPrincipal() != null && StringUtils.isNotBlank(req.getUserPrincipal().getName());
	}

	protected String getPath(final boolean isLoggedIn) {
		return isLoggedIn ? FORWARD_LIST : FORWARD_SUCCESS;
	}

}
