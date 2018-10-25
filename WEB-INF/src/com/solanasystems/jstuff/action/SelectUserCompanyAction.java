package com.solanasystems.jstuff.action;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.solanasystems.jstuff.bo.UserBO;
import com.solanasystems.jstuff.om.User;
import com.solanasystems.jstuff.util.UserUtils;

public class SelectUserCompanyAction extends CommonAction {

	public ActionForward execute(final ActionMapping mapping, final ActionForm form, final HttpServletRequest request, final HttpServletResponse response)
			throws Exception {
		final int companyId = getInt(request, REQ_COMPANY_ID);
		final User user = getUser(request);
		final UserBO bo = UserBO.getInstance();
		bo.updateLastUsedCompanyId(user.getId(), companyId);
		user.setCompanyId(companyId);
		UserUtils.setUser(request, user);
		return mapping.findForward(FORWARD_SUCCESS);
	}

}
