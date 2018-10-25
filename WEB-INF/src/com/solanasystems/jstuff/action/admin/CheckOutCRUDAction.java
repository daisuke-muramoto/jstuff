package com.solanasystems.jstuff.action.admin;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.beanutils.ConvertUtils;
import org.apache.commons.beanutils.converters.DateConverter;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;

import com.solanasystems.jstuff.form.CheckOutForm;
import com.solanasystems.jstuff.om.CheckOut;
import com.solanasystems.jstuff.om.Location;
import com.solanasystems.jstuff.om.ItemMaster;
import com.solanasystems.jstuff.om.Supplier;
import com.solanasystems.jstuff.om.Client;
import com.solanasystems.utils.exception.ApplicationException;

public class CheckOutCRUDAction extends AdminCommonAction {

	protected String getForwardName(final ActionMapping mapping, final ActionForm form, final HttpServletRequest request, final HttpServletResponse response,
			final int companyId) throws Exception {

		String path = FORWARD_LIST;

		switch (getAction(request)) {

			case SAVE: {
				final CheckOutForm checkOutForm = (CheckOutForm) form;
				final CheckOut checkout = new CheckOut();
				ConvertUtils.register(new DateConverter(null), java.util.Date.class);
				BeanUtils.copyProperties(checkout, checkOutForm);
				SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
				Date date = format.parse(checkOutForm.getCheckOutDate());
				checkout.setDate(date);
				checkout.setCompanyId(companyId);
				try {
					adminBO.createUpdateCheckOut(checkout);
					addSavedSesMessage(request, SAVE_MES_CHECKOUT);
					request.getSession().removeAttribute(DATA_ITEM_MASTER_LIST);
					request.getSession().removeAttribute(DATA_SUPPLIER_LIST);
					request.getSession().removeAttribute(DATA_LOCATION_LIST);
					request.getSession().removeAttribute(DATA_CLIENT_LIST);
				} catch (ApplicationException e) {
					addError(request, e);
					path = FORWARD_EDIT;
				}
				break;
			}

			case READ: {
				final CheckOutForm checkOutForm = new CheckOutForm();
				final int id = getInt(request, REQ_ID);
				if (id > 0) {
					final CheckOut checkout = adminBO.readCheckOut(id, companyId);
					if (checkout.getId() > 0 && id == checkout.getId())
						ConvertUtils.register(new DateConverter(null), java.util.Date.class);
						BeanUtils.copyProperties(checkOutForm, checkout);
						if (checkout.getDate() != null) {
							String datestr;
							datestr = new SimpleDateFormat("yyyy-MM-dd").format(checkout.getDate());
							checkOutForm.setCheckOutDate(datestr);
					}
				}
				request.setAttribute(DATA_CHECKOUT_FORM, checkOutForm);
				setItemMaster(request, companyId);
				setSupplierList(request, companyId);
				setLocationList(request, companyId);
				setClientList(request, companyId);
				path = FORWARD_EDIT;
				break;
			}

			case DELETE: {
				final int id = getInt(request, REQ_ID);
				try {
					if (adminBO.deleteCheckOut(id, companyId))
						addDeletedSesMessage(request, DEL_MES_CHECKOUT);
					else
						addDeletedSesError(request, DEL_MES_CHECKOUT);
				} catch (ApplicationException e) {
					addSesError(request, e);
				}
				break;
			}

		}

		return path;
	}

	private void setItemMaster(final HttpServletRequest req, final int companyId) throws ApplicationException {
		final List<ItemMaster> list = adminBO.getItemMaster(companyId);
		setListData(req.getSession(), list, DATA_ITEM_MASTER_LIST);
	}

	private void setSupplierList(final HttpServletRequest req, final int companyId) throws ApplicationException {
		final List<Supplier> list = adminBO.getSupplierList(companyId);
		setListData(req.getSession(), list, DATA_SUPPLIER_LIST);
	}

	private void setLocationList(final HttpServletRequest req, final int companyId) throws ApplicationException {
		final List<Location> list = adminBO.getLocationList(companyId);
		setListData(req.getSession(), list, DATA_LOCATION_LIST);
	}

	private void setClientList(final HttpServletRequest req, final int companyId) throws ApplicationException {
		final List<Client> list = adminBO.getClientList(companyId);
		setListData(req.getSession(), list, DATA_CLIENT_LIST);
	}

}
