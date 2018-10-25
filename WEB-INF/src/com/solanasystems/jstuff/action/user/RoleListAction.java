package com.solanasystems.jstuff.action.user;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.solanasystems.jstuff.action.CommonAction;
import com.solanasystems.jstuff.bo.UserBO;
import com.solanasystems.jstuff.om.Role;

public class RoleListAction extends CommonAction {

	public ActionForward execute(final ActionMapping mapping, final ActionForm form, final HttpServletRequest request, final HttpServletResponse response)
			throws Exception {

		if (isNotAdmin(request))
			return roleErrorForward(mapping, request);

		final UserBO bo = UserBO.getInstance();
		final List<Role> list = bo.getRoleList(true);
		request.setAttribute(DATA_ROLE_LIST, list);
		return mapping.findForward(FORWARD_SUCCESS);
	}

}
