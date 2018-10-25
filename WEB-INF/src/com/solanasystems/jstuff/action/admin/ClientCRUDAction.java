package com.solanasystems.jstuff.action.admin;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;

import com.solanasystems.jstuff.form.ClientForm;
import com.solanasystems.jstuff.om.Client;
import com.solanasystems.utils.exception.ApplicationException;

public class ClientCRUDAction extends AdminCommonAction {

	protected String getForwardName(final ActionMapping mapping, final ActionForm form, final HttpServletRequest request, final HttpServletResponse response,
			final int companyId) throws Exception {

		String path = FORWARD_LIST;

		switch (getAction(request)) {

			case SAVE: {
				final ClientForm clientForm = (ClientForm) form;
				final Client client = new Client();
				BeanUtils.copyProperties(client, clientForm);
				client.setCompanyId(companyId);
				try {
					adminBO.createUpdateClient(client);
					addSavedSesMessage(request, MES_CLIENT);
				} catch (ApplicationException e) {
					addError(request, e);
					path = FORWARD_EDIT;
				}
				break;
			}

			case READ: {
				final ClientForm clientForm = new ClientForm();
				final int id = getInt(request, REQ_ID);
				if (id > 0) {
					final Client client = adminBO.readClient(id, companyId);
					if (client.getId() > 0 && id == client.getId())
						BeanUtils.copyProperties(clientForm, client);
				}
				request.setAttribute(DATA_CLIENT_FORM, clientForm);
				path = FORWARD_EDIT;
				break;
			}

			case DELETE: {
				final int id = getInt(request, REQ_ID);
				try {
					if (adminBO.deleteClient(id, companyId))
						addDeletedSesMessage(request, MES_CLIENT);
					else
						addDeletedSesError(request, MES_CLIENT);
				} catch (ApplicationException e) {
					addSesError(request, e);
				}
				break;
			}

		}

		return path;
	}

}
