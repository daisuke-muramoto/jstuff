package com.solanasystems.jstuff.action.admin;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;

import com.solanasystems.jstuff.om.Company;

public class CompanyListAction extends AdminCommonAction {

	protected String getForwardName(final ActionMapping mapping, final ActionForm form, final HttpServletRequest request, final HttpServletResponse response,
			final int companyId) throws Exception {
		if (isNotAdmin(request))
			return FORWARD_ROLE_ERROR;
		final List<Company> list = adminBO.getCompanyList();
		request.setAttribute(DATA_COMPANY_LIST, list);
		return FORWARD_SUCCESS;
	}

}
