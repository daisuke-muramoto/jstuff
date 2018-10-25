package com.solanasystems.jstuff.action.admin;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;

import com.solanasystems.jstuff.form.LocationForm;
import com.solanasystems.jstuff.om.Location;
import com.solanasystems.utils.exception.ApplicationException;

public class LocationCRUDAction extends AdminCommonAction {

	protected String getForwardName(final ActionMapping mapping, final ActionForm form, final HttpServletRequest request, final HttpServletResponse response,
			final int companyId) throws Exception {

		if (isUser(request))
			return FORWARD_ROLE_ERROR;
		
		String path = FORWARD_LIST;

		switch (getAction(request)) {

			case SAVE: {
				final LocationForm locationForm = (LocationForm) form;
				final Location location = new Location();
				BeanUtils.copyProperties(location, locationForm);
				location.setCompanyId(companyId);
				try {
					adminBO.createUpdateLocation(location);
					addSavedSesMessage(request, SAVE_MES_LOCATION);
				} catch (ApplicationException e) {
					addError(request, e);
					path = FORWARD_EDIT;
				}
				break;
			}

			case READ: {
				final LocationForm locationForm = new LocationForm();
				final int id = getInt(request, REQ_ID);
				if (id > 0) {
					final Location location = adminBO.readLocation(id, companyId);
					if (location.getId() > 0 && id == location.getId())
						BeanUtils.copyProperties(locationForm, location);
				}
				request.setAttribute(DATA_LOCATION_FORM, locationForm);
				path = FORWARD_EDIT;
				break;
			}

			case DELETE: {
				final int id = getInt(request, REQ_ID);
				try {
					if (adminBO.deleteLocation(id, companyId))
						addDeletedSesMessage(request, DEL_MES_LOCATION);
					else
						addDeletedSesError(request, DEL_MES_LOCATION);
				} catch (ApplicationException e) {
					addSesError(request, e);
				}
				break;
			}

		}

		return path;
	}

}
