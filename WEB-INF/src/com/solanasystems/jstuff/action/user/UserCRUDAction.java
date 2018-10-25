package com.solanasystems.jstuff.action.user;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.solanasystems.jstuff.action.CommonAction;
import com.solanasystems.jstuff.bo.UserBO;
import com.solanasystems.jstuff.form.UserForm;
import com.solanasystems.jstuff.om.Company;
import com.solanasystems.jstuff.om.Role;
import com.solanasystems.jstuff.om.User;
import com.solanasystems.jstuff.util.UserUtils;
import com.solanasystems.utils.exception.ApplicationException;

public class UserCRUDAction extends CommonAction {

	private static final UserBO bo = UserBO.getInstance();

	public ActionForward execute(final ActionMapping mapping, final ActionForm form, final HttpServletRequest request, final HttpServletResponse response)
			throws Exception {

		if (isCancelled(request))
			return mapping.findForward(FORWARD_LIST);
		else if (isUser(request))
			return roleErrorForward(mapping, request);

		final User loginUser = UserUtils.getUser(request, bo);
		final int loginUserId = loginUser.getId(), companyId = getUserCompanyId(request);
		final boolean isAdmin = isAdmin(request);
		String path = FORWARD_LIST;
		UserForm userForm = null;
		User user = null;

		switch (getAction(request)) {

			case SAVE: {
				userForm = (UserForm) form;
				user = new User();
				BeanUtils.copyProperties(user, userForm);
				try {
					bo.createUpdateUser(user);
					addSavedSesMessage(request, MES_USER);
					if (loginUserId == userForm.getId()) {
						user.setPass(null);
						user.setCompanyId(companyId);
						UserUtils.setUser(request, user);
					}
					removeList(request);
				} catch (ApplicationException e) {
					addError(request, e);
					path = FORWARD_EDIT;
				}
				break;
			}

			case READ: {
				final int userId = getInt(request, REQ_USER_ID);
				userForm = new UserForm();
				if (userId > 0 && canAccessUser(isAdmin, userId, companyId)) {
					user = bo.readUser(userId);
					BeanUtils.copyProperties(userForm, user);
				}
				request.setAttribute(DATA_USER_FORM, userForm);

				final List<Role> rl = bo.getRoleList(isAdmin);
				request.getSession().setAttribute(DATA_ROLE_LIST, rl);
				final List<Company> cl = getUserCompanyList(request);
				request.getSession().setAttribute(DATA_COMPANY_LIST, cl);
				path = FORWARD_EDIT;
				break;
			}

			case DELETE: {
				final int userId = getInt(request, REQ_USER_ID);
				if (userId > 0) {
					if (loginUserId == userId) {
						addSesError(request, "errors.user.loginUser");
					} else {
						try {
							if (canAccessUser(isAdmin, userId, companyId))
								bo.deleteUser(userId);
							addDeletedSesMessage(request, MES_USER);
						} catch (ApplicationException e) {
							addSesError(request, e);
						}
					}
				}
				break;
			}

			default: {
				removeList(request);
				break;
			}

		}

		return mapping.findForward(path);
	}

	private boolean canAccessUser(final boolean isAdmin, final int userId, final int companyId) throws ApplicationException {
		return isAdmin || bo.canAccessUser(userId, companyId);
	}

	protected static void removeList(final HttpServletRequest request) {
		request.getSession().removeAttribute(DATA_ROLE_LIST);
		request.getSession().removeAttribute(DATA_COMPANY_LIST);
	}

}
