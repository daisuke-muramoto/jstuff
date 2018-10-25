package com.solanasystems.jstuff.action.admin;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;

import com.solanasystems.jstuff.form.CompanyForm;
import com.solanasystems.jstuff.om.Company;
import com.solanasystems.utils.exception.ApplicationException;

public class CompanyCRUDAction extends AdminCommonAction {

	protected String getForwardName(final ActionMapping mapping, final ActionForm form, final HttpServletRequest request, final HttpServletResponse response,
			final int companyId) throws Exception {

		if (isNotAdmin(request))
			return FORWARD_ROLE_ERROR;

		String path = FORWARD_LIST;

		switch (getAction(request)) {

			case SAVE: {
				final CompanyForm companyForm = (CompanyForm) form;
				final Company company = new Company();
				BeanUtils.copyProperties(company, companyForm);
				try {
					adminBO.createUpdateCompany(company);
					addSavedSesMessage(request, SAVE_MES_COMPANY);
					setUserCompanyList(request);
				} catch (ApplicationException e) {
					addError(request, e);
					path = FORWARD_EDIT;
				}
				break;
			}

			case READ: {
				final CompanyForm companyForm = new CompanyForm();
				final int id = getInt(request, REQ_ID);
				if (id > 0) {
					final Company company = adminBO.readCompany(id);
					if (company.getId() > 0 && id == company.getId())
						BeanUtils.copyProperties(companyForm, company);
				}
				request.setAttribute(DATA_COMPANY_FORM, companyForm);
				path = FORWARD_EDIT;
				break;
			}

			case DELETE: {
				final int id = getInt(request, REQ_ID);
				try {
					if (adminBO.deleteCompany(id)) {
						addDeletedSesMessage(request, DEL_MES_COMPANY);
						setUserCompanyList(request);
					} else {
						addDeletedSesError(request, DEL_MES_COMPANY);
					}
				} catch (ApplicationException e) {
					addSesError(request, e);
				}
				break;
			}

		}

		return path;
	}

}
