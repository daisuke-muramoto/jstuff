package com.solanasystems.jstuff.action.admin;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;

import com.solanasystems.jstuff.form.ItemMasterForm;
import com.solanasystems.jstuff.om.Category;
import com.solanasystems.jstuff.om.ItemMaster;
import com.solanasystems.utils.exception.ApplicationException;

public class ItemMasterCRUDAction extends AdminCommonAction {

	protected String getForwardName(final ActionMapping mapping, final ActionForm form, final HttpServletRequest request, final HttpServletResponse response,
			final int companyId) throws Exception {

		String path = FORWARD_LIST;

		switch (getAction(request)) {

			case SAVE: {
				final ItemMasterForm itemMasterForm = (ItemMasterForm) form;
				final ItemMaster itemMaster = new ItemMaster();
				BeanUtils.copyProperties(itemMaster, itemMasterForm);
				itemMaster.setCompanyId(companyId);
				try {
					adminBO.createUpdateItemMaster(itemMaster);
					addSavedSesMessage(request, SAVE_MES_ITEM_MASTER);
					request.getSession().removeAttribute(DATA_CATEGORY_LIST);
				} catch (ApplicationException e) {
					addError(request, e);
					path = FORWARD_EDIT;
				}
				break;
			}

			case READ: {
				final ItemMasterForm itemMasterForm = new ItemMasterForm();
				final int id = getInt(request, REQ_ID);
				if (id > 0) {
					final ItemMaster itemMaster = adminBO.readItemMaster(id, companyId);
					if (itemMaster.getId() > 0 && id == itemMaster.getId())
						BeanUtils.copyProperties(itemMasterForm, itemMaster);
				}
				request.setAttribute(DATA_ITEM_MASTER_FORM, itemMasterForm);
				setCategoryList(request, companyId);
				path = FORWARD_EDIT;
				break;
			}

			case DELETE: {
				final int id = getInt(request, REQ_ID);
				try {
					if (adminBO.deleteItemMaster(id, companyId))
						addDeletedSesMessage(request, DEL_MES_ITEM_MASTER);
					else
						addDeletedSesError(request, DEL_MES_ITEM_MASTER);
				} catch (ApplicationException e) {
					addSesError(request, e);
				}
				break;
			}

		}

		return path;
	}

	private void setCategoryList(final HttpServletRequest req, final int companyId) throws ApplicationException {
		final List<Category> list = adminBO.getCategoryList(companyId);
		setListData(req.getSession(), list, DATA_CATEGORY_LIST);
	}

}
