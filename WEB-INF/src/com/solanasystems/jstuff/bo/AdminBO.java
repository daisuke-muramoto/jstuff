package com.solanasystems.jstuff.bo;

import static com.solanasystems.jstuff.common.ErrorConstants.ERRORS_UNIQUE_NAME;
import static com.solanasystems.jstuff.common.ErrorConstants.ERRORS_REQUIRED_QUANTITY;

import java.util.List;

import com.solanasystems.jstuff.dao.AdminDAO;
import com.solanasystems.jstuff.dao.DAOFactory;
import com.solanasystems.jstuff.om.Category;
import com.solanasystems.jstuff.om.Client;
import com.solanasystems.jstuff.om.Company;
import com.solanasystems.jstuff.om.CheckOut;
import com.solanasystems.jstuff.om.Location;
import com.solanasystems.jstuff.om.CheckIn;
import com.solanasystems.jstuff.om.ItemMaster;
import com.solanasystems.jstuff.om.Log;
import com.solanasystems.jstuff.om.Supplier;
import com.solanasystems.utils.bo.BOUtils;
import com.solanasystems.utils.exception.ApplicationException;
import com.solanasystems.utils.exception.DAOException;

public class AdminBO extends BOUtils {

	private final AdminDAO dao;

	private AdminBO(final AdminDAO dao) {
		this.dao = dao;
	}

	public static AdminBO getInstance() {
		return new AdminBO(DAOFactory.getAdminDAO());
	}

	//Company
	public void createUpdateCompany(final Company company) throws ApplicationException {
		try {
			if (company.getId() > 0)
				dao.updateCompany(company);
			else
				dao.createCompany(company);
		} catch (DAOException e) {
			throw exception(this, "createUpdateCompany()", ERRORS_UNIQUE_NAME, e);
		}
	}

	public Company readCompany(final int id) throws ApplicationException {
		try {
			return dao.readCompany(id);
		} catch (DAOException e) {
			throw exception(this, "readCompany()", e);
		}
	}

	public boolean deleteCompany(final int id) throws ApplicationException {
		try {
			return dao.deleteCompany(id);
		} catch (DAOException e) {
			throw exception(this, "deleteCompany()", e);
		}
	}

	public List<Company> getCompanyList() throws ApplicationException {
		try {
			return dao.getCompanyList("");
		} catch (DAOException e) {
			throw exception(this, "getCompanyList()", e);
		}
	}

	public List<Company> getCompanyList(final int userId) throws ApplicationException {
		try {
			final String query = userId > 0 ? " WHERE `id` IN (SELECT `companyId` FROM `UserCompanyIds` WHERE `userId` = " + userId + ")" : "";
			return dao.getCompanyList(query);
		} catch (DAOException e) {
			throw exception(this, "getCompanyList()", e);
		}
	}

	//Location
	public void createUpdateLocation(final Location location) throws ApplicationException {
		try {
			if (location.getId() > 0)
				dao.updateLocation(location);
			else
				dao.createLocation(location);
		} catch (DAOException e) {
			throw exception(this, "createUpdateLocation()", ERRORS_UNIQUE_NAME, e);
		}
	}

	public Location readLocation(final int id, final int companyId) throws ApplicationException {
		try {
			return dao.readLocation(id, companyId);
		} catch (DAOException e) {
			throw exception(this, "readLocation()", e);
		}
	}

	public boolean deleteLocation(final int id, final int companyId) throws ApplicationException {
		try {
			return dao.deleteLocation(id, companyId);
		} catch (DAOException e) {
			throw exception(this, "deleteLocation()", e);
		}
	}

	public List<Location> getLocationList(final int companyId) throws ApplicationException {
		try {
			return dao.getLocationList(companyId);
		} catch (DAOException e) {
			throw exception(this, "getLocationList()", e);
		}
	}

	//Category
	public void createUpdateCategory(final Category category) throws ApplicationException {
		try {
			if (category.getId() > 0)
				dao.updateCategory(category);
			else
				dao.createCategory(category);
		} catch (DAOException e) {
			throw exception(this, "createUpdateCategory()", ERRORS_UNIQUE_NAME, e);
		}
	}

	public Category readCategory(final int id, final int companyId) throws ApplicationException {
		try {
			return dao.readCategory(id, companyId);
		} catch (DAOException e) {
			throw exception(this, "readCategory()", e);
		}
	}

	public boolean deleteCategory(final int id, final int companyId) throws ApplicationException {
		try {
			return dao.deleteCategory(id, companyId);
		} catch (DAOException e) {
			throw exception(this, "deleteCategory()", e);
		}
	}

	public List<Category> getCategoryList(final int companyId) throws ApplicationException {
		try {
			return dao.getCategoryList(companyId);
		} catch (DAOException e) {
			throw exception(this, "getCategoryList()", e);
		}
	}

	//ItemMaster
	public void createUpdateItemMaster(final ItemMaster itemMaster) throws ApplicationException {
		try {
			if (itemMaster.getId() > 0)
				dao.updateItemMaster(itemMaster);
			else
				dao.createItemMaster(itemMaster);
		} catch (DAOException e) {
			throw exception(this, "createUpdateItemMaster()", ERRORS_UNIQUE_NAME, e);
		}
	}

	public ItemMaster readItemMaster(final int id, final int companyId) throws ApplicationException {
		try {
			return dao.readItemMaster(id, companyId);
		} catch (DAOException e) {
			throw exception(this, "readItemMaster()", e);
		}
	}

	public boolean deleteItemMaster(final int id, final int companyId) throws ApplicationException {
		try {
			return dao.deleteItemMaster(id, companyId);
		} catch (DAOException e) {
			throw exception(this, "deleteItemMaster()", e);
		}
	}

	public List<ItemMaster> getItemMaster(final int companyId) throws ApplicationException {
		try {
			return dao.getItemMaster(companyId);
		} catch (DAOException e) {
			throw exception(this, "getItemMaster()", e);
		}
	}

	//Supplier
	public void createUpdateSupplier(final Supplier supplier) throws ApplicationException {
		try {
			if (supplier.getId() > 0)
				dao.updateSupplier(supplier);
			else
				dao.createSupplier(supplier);
		} catch (DAOException e) {
			throw exception(this, "createUpdateSupplier()", ERRORS_UNIQUE_NAME, e);
		}
	}

	public Supplier readSupplier(final int id, final int companyId) throws ApplicationException {
		try {
			return dao.readSupplier(id, companyId);
		} catch (DAOException e) {
			throw exception(this, "readSupplier()", e);
		}
	}

	public boolean deleteSupplier(final int id, final int companyId) throws ApplicationException {
		try {
			return dao.deleteSupplier(id, companyId);
		} catch (DAOException e) {
			throw exception(this, "deleteSupplier()", e);
		}
	}

	public List<Supplier> getSupplierList(final int companyId) throws ApplicationException {
		try {
			return dao.getSupplierList(companyId);
		} catch (DAOException e) {
			throw exception(this, "getSupplierList()", e);
		}
	}

	//Client
	public void createUpdateClient(final Client client) throws ApplicationException {
		try {
			if (client.getId() > 0)
				dao.updateClient(client);
			else
				dao.createClient(client);
		} catch (DAOException e) {
			throw exception(this, "createUpdateClient()", ERRORS_UNIQUE_NAME, e);
		}
	}

	public Client readClient(final int id, final int companyId) throws ApplicationException {
		try {
			return dao.readClient(id, companyId);
		} catch (DAOException e) {
			throw exception(this, "readClient()", e);
		}
	}

	public boolean deleteClient(final int id, final int companyId) throws ApplicationException {
		try {
			return dao.deleteClient(id, companyId);
		} catch (DAOException e) {
			throw exception(this, "deleteClient()", e);
		}
	}

	public List<Client> getClientList(final int companyId) throws ApplicationException {
		try {
			return dao.getClientList(companyId);
		} catch (DAOException e) {
			throw exception(this, "getClientList()", e);
		}
	}

	//CheckIn
	public void createUpdateCheckIn(final CheckIn checkin) throws ApplicationException {
		try {
			if (checkin.getId() > 0)
				dao.updateCheckIn(checkin);
			else
				dao.createCheckIn(checkin);
		} catch (DAOException e) {
			throw exception(this, "createUpdateCheckIn()", ERRORS_REQUIRED_QUANTITY, e);
		}
	}

	public CheckIn readCheckIn(final int id, final int companyId) throws ApplicationException {
		try {
			return dao.readCheckIn(id, companyId);
		} catch (DAOException e) {
			throw exception(this, "readCheckIn()", e);
		}
	}

	public boolean deleteCheckIn(final int id, final int companyId) throws ApplicationException {
		try {
			return dao.deleteCheckIn(id, companyId);
		} catch (DAOException e) {
			throw exception(this, "deleteCheckIn()", e);
		}
	}

	public List<CheckIn> getCheckInList(final int companyId) throws ApplicationException {
		try {
			return dao.getCheckInList(companyId);
		} catch (DAOException e) {
			throw exception(this, "getCheckInList()", e);
		}
	}

	//CheckOut
	public void createUpdateCheckOut(final CheckOut checkout) throws ApplicationException {
		try {
			if (checkout.getId() > 0)
				dao.updateCheckOut(checkout);
			else
				dao.createCheckOut(checkout);
		} catch (DAOException e) {
			throw exception(this, "createUpdateCheckOut()", ERRORS_REQUIRED_QUANTITY, e);
		}
	}

	public CheckOut readCheckOut(final int id, final int companyId) throws ApplicationException {
		try {
			return dao.readCheckOut(id, companyId);
		} catch (DAOException e) {
			throw exception(this, "readCheckOut()", e);
		}
	}

	public boolean deleteCheckOut(final int id, final int companyId) throws ApplicationException {
		try {
			return dao.deleteCheckOut(id, companyId);
		} catch (DAOException e) {
			throw exception(this, "deleteCheckOut()", e);
		}
	}

	public List<CheckOut> getCheckOutList(final int companyId) throws ApplicationException {
		try {
			return dao.getCheckOutList(companyId);
		} catch (DAOException e) {
			throw exception(this, "getCheckOutList()", e);
		}
	}

	//Inventory
	public List<CheckIn> getInventory(final int companyId) throws ApplicationException {
		try {
			return dao.getInventory(companyId);
		} catch (DAOException e) {
			throw exception(this, "getInventory()", e);
		}
	}

	//Log
	public List<Log> getLogList() throws ApplicationException {
		try {
			return dao.getLogList();
		} catch (DAOException e) {
			throw exception(this, "getLogList()", e);
		}
	}

}