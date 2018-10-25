package com.solanasystems.jstuff.filter;

import static com.solanasystems.jstuff.common.Constants.ACTION_DELETE;
import static com.solanasystems.jstuff.common.Constants.ACTION_SAVE;
import static com.solanasystems.jstuff.common.Constants.SES_IS_SET_USER_COMPANY_LIST;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.solanasystems.jstuff.action.CommonAction;
import com.solanasystems.jstuff.util.UserUtils;
import com.solanasystems.utils.filter.EncodingFilter;
import com.solanasystems.utils.util.Utils;

public class SetCharacterEncodingFilter extends EncodingFilter {

	public void setFilterData(final HttpServletRequest req, final HttpServletResponse res) throws IOException {
	}

	public void setDoFilterData(final HttpServletRequest req, final HttpServletResponse res) throws IOException {
		UserUtils.setRoleFlag(req);
		if (req.getUserPrincipal() != null && !Utils.toBool(req.getSession().getAttribute(SES_IS_SET_USER_COMPANY_LIST))) {
			CommonAction.setUserCompanyList(req);
			req.getSession().setAttribute(SES_IS_SET_USER_COMPANY_LIST, true);
		}
	}

	public boolean isChangeDataAction(final HttpServletRequest req) {
		return req.getParameter(ACTION_SAVE) != null || req.getParameter(ACTION_DELETE) != null;
	}

}
