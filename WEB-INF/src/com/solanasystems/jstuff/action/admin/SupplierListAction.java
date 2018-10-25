package com.solanasystems.jstuff.action.admin;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;

import com.solanasystems.jstuff.om.Supplier;

public class SupplierListAction extends AdminCommonAction {

	protected String getForwardName(final ActionMapping mapping, final ActionForm form, final HttpServletRequest request, final HttpServletResponse response,
			final int companyId) throws Exception {
		final List<Supplier> list = adminBO.getSupplierList(companyId);
		request.setAttribute(DATA_SUPPLIER_LIST, list);
		return FORWARD_SUCCESS;
	}

}
