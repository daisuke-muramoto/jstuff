package com.solanasystems.jstuff.action.admin;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;

import com.solanasystems.jstuff.form.CategoryForm;
import com.solanasystems.jstuff.om.Category;
import com.solanasystems.utils.exception.ApplicationException;

public class CategoryCRUDAction extends AdminCommonAction {

	protected String getForwardName(final ActionMapping mapping, final ActionForm form, final HttpServletRequest request, final HttpServletResponse response,
			final int companyId) throws Exception {

		String path = FORWARD_LIST;

		switch (getAction(request)) {

			case SAVE: {
				final CategoryForm categoryForm = (CategoryForm) form;
				final Category category = new Category();
				BeanUtils.copyProperties(category, categoryForm);
				category.setCompanyId(companyId);
				try {
					adminBO.createUpdateCategory(category);
					addSavedSesMessage(request, MES_CATEGORY);
				} catch (ApplicationException e) {
					addError(request, e);
					path = FORWARD_EDIT;
				}
				break;
			}

			case READ: {
				final CategoryForm categoryForm = new CategoryForm();
				final int id = getInt(request, REQ_ID);
				if (id > 0) {
					final Category category = adminBO.readCategory(id, companyId);
					if (category.getId() > 0 && id == category.getId())
						BeanUtils.copyProperties(categoryForm, category);
				}
				request.setAttribute(DATA_CATEGORY_FORM, categoryForm);
				path = FORWARD_EDIT;
				break;
			}

			case DELETE: {
				final int id = getInt(request, REQ_ID);
				try {
					if (adminBO.deleteCategory(id, companyId))
						addDeletedSesMessage(request, MES_CATEGORY);
					else
						addDeletedSesError(request, MES_CATEGORY);
				} catch (ApplicationException e) {
					addSesError(request, e);
				}
				break;
			}

		}

		return path;
	}

}
