package com.solanasystems.jstuff.action.admin;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;

import com.solanasystems.jstuff.form.SupplierForm;
import com.solanasystems.jstuff.om.Supplier;
import com.solanasystems.utils.exception.ApplicationException;

public class SupplierCRUDAction extends AdminCommonAction {

	protected String getForwardName(final ActionMapping mapping, final ActionForm form, final HttpServletRequest request, final HttpServletResponse response,
			final int companyId) throws Exception {

		String path = FORWARD_LIST;

		switch (getAction(request)) {

			case SAVE: {
				final SupplierForm supplierForm = (SupplierForm) form;
				final Supplier supplier = new Supplier();
				BeanUtils.copyProperties(supplier, supplierForm);
				supplier.setCompanyId(companyId);
				try {
					adminBO.createUpdateSupplier(supplier);
					addSavedSesMessage(request, SAVE_MES_SUPPLIER);
				} catch (ApplicationException e) {
					addError(request, e);
					path = FORWARD_EDIT;
				}
				break;
			}

			case READ: {
				final SupplierForm supplierForm = new SupplierForm();
				final int id = getInt(request, REQ_ID);
				if (id > 0) {
					final Supplier supplier = adminBO.readSupplier(id, companyId);
					if (supplier.getId() > 0 && id == supplier.getId())
						BeanUtils.copyProperties(supplierForm, supplier);
				}
				request.setAttribute(DATA_SUPPLIER_FORM, supplierForm);
				path = FORWARD_EDIT;
				break;
			}

			case DELETE: {
				final int id = getInt(request, REQ_ID);
				try {
					if (adminBO.deleteSupplier(id, companyId))
						addDeletedSesMessage(request, DEL_MES_SUPPLIER);
					else
						addDeletedSesError(request, DEL_MES_SUPPLIER);
				} catch (ApplicationException e) {
					addSesError(request, e);
				}
				break;
			}

		}

		return path;
	}

}
