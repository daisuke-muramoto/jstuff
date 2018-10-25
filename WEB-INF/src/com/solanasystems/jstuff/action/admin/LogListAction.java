package com.solanasystems.jstuff.action.admin;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;

import com.solanasystems.jstuff.om.Log;

public class LogListAction extends AdminCommonAction {

	protected String getForwardName(final ActionMapping mapping, final ActionForm form, final HttpServletRequest request, final HttpServletResponse response,
			final int companyId) throws Exception {
		request.getSession().removeAttribute(DATA_COMPANY_LIST);
		final List<Log> list = adminBO.getLogList();
		request.setAttribute(DATA_LOG_LIST, list);
		return FORWARD_SUCCESS;
	}

}
