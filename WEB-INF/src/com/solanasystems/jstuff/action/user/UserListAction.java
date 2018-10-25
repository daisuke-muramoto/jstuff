package com.solanasystems.jstuff.action.user;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.solanasystems.jstuff.action.CommonAction;
import com.solanasystems.jstuff.bo.UserBO;
import com.solanasystems.jstuff.om.User;
import com.solanasystems.jstuff.util.UserUtils;

public class UserListAction extends CommonAction {

	public ActionForward execute(final ActionMapping mapping, final ActionForm form, final HttpServletRequest request, final HttpServletResponse response)
			throws Exception {
		UserCRUDAction.removeList(request);
		if (isUser(request))
			return roleErrorForward(mapping, request);

		final UserBO bo = UserBO.getInstance();
		final int userId = UserUtils.getUserId(request, bo);
		final List<User> list = bo.getUserList(isAdmin(request) ? 0 : getUserCompanyId(request));
		request.setAttribute(DATA_USER_ID, userId);
		request.setAttribute(DATA_USER_LIST, list);
		return mapping.findForward(FORWARD_SUCCESS);
	}

}
