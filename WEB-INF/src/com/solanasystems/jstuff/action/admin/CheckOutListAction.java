package com.solanasystems.jstuff.action.admin;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;

import com.solanasystems.jstuff.om.CheckOut;

public class CheckOutListAction extends AdminCommonAction {

	protected String getForwardName(final ActionMapping mapping, final ActionForm form, final HttpServletRequest request, final HttpServletResponse response,
			final int companyId) throws Exception {
		request.getSession().removeAttribute(DATA_ITEM_MASTER_LIST);
		request.getSession().removeAttribute(DATA_SUPPLIER_LIST);
		request.getSession().removeAttribute(DATA_LOCATION_LIST);
		request.getSession().removeAttribute(DATA_CLIENT_LIST);
		final List<CheckOut> list = adminBO.getCheckOutList(companyId);
		request.setAttribute(DATA_CHECKOUT_LIST, list);
		return FORWARD_SUCCESS;
	}

}
