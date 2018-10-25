package com.solanasystems.jstuff.action.admin;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.solanasystems.jstuff.action.CommonAction;
import com.solanasystems.jstuff.bo.AdminBO;

public abstract class AdminCommonAction extends CommonAction {

	protected static final AdminBO adminBO = AdminBO.getInstance();

	protected abstract String getForwardName(final ActionMapping mapping, final ActionForm form, final HttpServletRequest request,
			final HttpServletResponse response, final int companyId) throws Exception;

	public ActionForward execute(final ActionMapping mapping, final ActionForm form, final HttpServletRequest request, final HttpServletResponse response)
			throws Exception {
		if (isCancelled(request))
			return mapping.findForward(FORWARD_LIST);
		return mapping.findForward(this.getForwardName(mapping, form, request, response, getUserCompanyId(request)));
	}

}
