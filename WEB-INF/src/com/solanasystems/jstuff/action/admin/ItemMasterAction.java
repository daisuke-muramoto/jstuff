package com.solanasystems.jstuff.action.admin;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;

import com.solanasystems.jstuff.om.ItemMaster;

public class ItemMasterAction extends AdminCommonAction {

	protected String getForwardName(final ActionMapping mapping, final ActionForm form, final HttpServletRequest request, final HttpServletResponse response,
			final int companyId) throws Exception {
		request.getSession().removeAttribute(DATA_CATEGORY_LIST);
		final List<ItemMaster> list = adminBO.getItemMaster(companyId);
		request.setAttribute(DATA_ITEM_MASTER_LIST, list);
		return FORWARD_SUCCESS;
	}

}
