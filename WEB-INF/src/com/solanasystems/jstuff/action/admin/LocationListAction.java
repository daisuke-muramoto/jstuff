package com.solanasystems.jstuff.action.admin;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;

import com.solanasystems.jstuff.om.Location;

public class LocationListAction extends AdminCommonAction {

	protected String getForwardName(final ActionMapping mapping, final ActionForm form, final HttpServletRequest request, final HttpServletResponse response,
			final int companyId) throws Exception {
		if (isUser(request))
			return FORWARD_ROLE_ERROR;
		final List<Location> list = adminBO.getLocationList(companyId);
		request.setAttribute(DATA_LOCATION_LIST, list);
		return FORWARD_SUCCESS;
	}

}
