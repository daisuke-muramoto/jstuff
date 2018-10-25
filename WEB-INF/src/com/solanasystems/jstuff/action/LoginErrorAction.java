package com.solanasystems.jstuff.action;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

public class LoginErrorAction extends LoginAction {

	public ActionForward execute(final ActionMapping mapping, final ActionForm form, final HttpServletRequest request, final HttpServletResponse response)
			throws Exception {
		final boolean isLoggedIn = isLoggedIn(request);
		if (!isLoggedIn)
			addError(request, "errors.login");
		return mapping.findForward(getPath(isLoggedIn));
	}

}
