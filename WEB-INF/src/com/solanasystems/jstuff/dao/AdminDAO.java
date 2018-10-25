package com.solanasystems.jstuff.dao;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.apache.commons.io.FileUtils;

import com.solanasystems.jstuff.om.Category;
import com.solanasystems.jstuff.om.CheckIn;
import com.solanasystems.jstuff.om.CheckOut;
import com.solanasystems.jstuff.om.Client;
import com.solanasystems.jstuff.om.Company;
import com.solanasystems.jstuff.om.ItemMaster;
import com.solanasystems.jstuff.om.Location;
import com.solanasystems.jstuff.om.Log;
import com.solanasystems.jstuff.om.Supplier;
import com.solanasystems.utils.exception.DAOException;

public class AdminDAO extends CommonDAO {

	private static final String JAPANESE_NAME_AND_ADDRESS = "`nameJA`, `addressJA`";

	private static final String JAPANESE_NAME_AND_DESCRIPTION = "`nameJA`, `descriptionJA`";

	private static final String COMPANY_ALL_FIELDS = "`id`, `name`, `address`";

	private static final String INVENTORY_COMMON_FIELDS_HEADER = "`id`, `roleId`, `itemId`, `clientId`, `locationId`";

	private static final String CHECKIN_COMMON_FIELDS = "`id`, `roleId`, `itemId`, `clientId`, `supplierId`, `locationId`, `quantity`, `comment`, `date`";

	private static final String CHECKOUT_COMMON_FIELDS = CHECKIN_COMMON_FIELDS;

	private static final String LOCATION_COMMON_FIELDS = COMPANY_ALL_FIELDS;

	private static final String CATEGORY_COMMON_FIELDS = "`id`, `name`, `description`";

	private static final String CLIENT_COMMON_FIELDS = CATEGORY_COMMON_FIELDS;

	private static final String ITEM_MASTER_COMMON_FIELDS = "`id`, `categoryId`, `name`, `code`, `description`, `nameJA`, `descriptionJA`";

	private static final String SUPPLIER_COMMON_FIELDS = CATEGORY_COMMON_FIELDS;

	private static final String LOG_COMMON_FIELDS = "`id`, `companyId`, `userName`, `dateTime`, `ipAddress`, `tableName`, `details`";

	private static final String LOG_COMMON_FIELDS_FOOTER = "`userName`, `dateTime`, `ipAddress`";

	private static final String COMPANY_ID_QUERY = " AND `companyId` = ?";

	// Company
	public void createCompany(final Company company) throws DAOException {
		try {
			final PreparedStatement ps = prepareStatement("INSERT INTO `Company` ("
					+ COMPANY_ALL_FIELDS + ", " + JAPANESE_NAME_AND_ADDRESS 
					+ ") VALUES (NULL, ?, ?, ?, ?)");
			populateCompany(ps, company);

			checkInsert(ps, "Error inserting company: " + company.getName());
		} catch (SQLException e) {
			throw exception(this, "Error inserting company: " + company.getName(), e);
		} finally {
			cleanup();
		}
	}

	public Company readCompany(final int id) throws DAOException {
		try {
			final PreparedStatement ps = prepareStatement("SELECT "
					+ COMPANY_ALL_FIELDS + ", " + JAPANESE_NAME_AND_ADDRESS 
					+ " FROM `Company` WHERE `id` = ?");
			ps.setInt(1, id);

			final ResultSet rs = ps.executeQuery();
			final Company company = new Company();
			if (rs.next()) {
				loadCompany(rs, company);
			}
			return company;
		} catch (SQLException e) {
			throw exception(this, "Error reading company: " + id, e);
		} finally {
			cleanup();
		}
	}

	public void updateCompany(final Company company) throws DAOException {
		try {
			final PreparedStatement ps = prepareStatement("UPDATE `Company` SET `name` = ?, `address` = ?, `nameJA` = ?, `addressJA` = ? WHERE `id` = ?");
			int index = populateCompany(ps, company);
			ps.setInt(index++, company.getId());

			ps.executeUpdate();
		} catch (SQLException e) {
			throw exception(this,
					"Error updating company: " + company.getName(), e);
		} finally {
			cleanup();
		}
	}

	public boolean deleteCompany(final int id) throws DAOException {
		Connection conn = null;
		PreparedStatement ps = null;
		try {
			conn = getConnection();
			ps = conn
					.prepareStatement("SELECT COUNT(`companyId`) = 0 FROM `UserCompanyIds` WHERE `companyId` = ?");
			ps.setInt(1, id);

			final ResultSet rs = ps.executeQuery();
			final boolean canDelete = rs.next() && rs.getBoolean(1);
			if (canDelete) {
				ps.close();
				ps = conn
						.prepareStatement("DELETE FROM `Company` WHERE `id` = ?");
				ps.setInt(1, id);

				checkDelete(ps, "Error deleting company: " + id);
			}
			return canDelete;
		} catch (SQLException e) {
			throw exception(this, "Error deleting company: " + id, e);
		} finally {
			cleanup(conn, ps);
		}
	}

	public List<Company> getCompanyList(final String query) throws DAOException {
		try {
			final PreparedStatement ps = prepareStatement("SELECT "
					+ COMPANY_ALL_FIELDS + ", " + JAPANESE_NAME_AND_ADDRESS 
					+ ", (SELECT COUNT(`companyId`) FROM `UserCompanyIds` WHERE `companyId` = co.`id`) = 0 AS canDelete FROM `Company` co"
					+ query + " ORDER BY `name`, `nameJA` ASC");

			final ResultSet rs = ps.executeQuery();
			final List<Company> list = newArrayList();
			while (rs.next()) {
				Company company = new Company();
				int index = loadCompany(rs, company);
				company.setCanDelete(rs.getBoolean(index++));
				list.add(company);
			}
			return list;
		} catch (SQLException e) {
			throw exception(this, "Error getting company list", e);
		} finally {
			cleanup();
		}
	}

	private int populateCompany(final PreparedStatement ps, final Company company) throws SQLException {
		int index = 1;
		ps.setString(index++, company.getName());
		ps.setString(index++, company.getAddress());
		if(isBlank(company.getNameJA())) ps.setString(index++, company.getName());
		else if((company.getName().equalsIgnoreCase(company.getNameJA())) && (isNotBlank(company.getName()) && isNotBlank(company.getNameJA()))) ps.setString(index++, company.getName());
		else ps.setString(index++, company.getNameJA());
		if(isBlank(company.getAddressJA())) ps.setString(index++, company.getAddress());
		else if((company.getAddress().equalsIgnoreCase(company.getAddressJA())) && (isNotBlank(company.getAddress()) && isNotBlank(company.getAddressJA()))) ps.setString(index++, company.getAddress());
		else ps.setString(index++, company.getAddressJA());
		return index;
	}

	private int loadCompany(final ResultSet rs, final Company company) throws SQLException {
		int index = 1;
		company.setId(rs.getInt(index++));
		company.setName(rs.getString(index++));
		company.setAddress(rs.getString(index++));
		company.setNameJA(rs.getString(index++));
		company.setAddressJA(rs.getString(index++));
		return index;
	}

	// Location
	public void createLocation(final Location location) throws DAOException {
		try {
			final PreparedStatement ps = prepareStatement("INSERT INTO `Location` ("
					+ LOCATION_COMMON_FIELDS + ", " + JAPANESE_NAME_AND_ADDRESS 
					+ ", `companyId`) VALUES (NULL, ?, ?, ?, ?, ?)");
			int index = populateLocation(ps, location);
			ps.setInt(index++, location.getCompanyId());

			checkInsert(ps, "Error inserting location: " + location.getName());
		} catch (SQLException e) {
			throw exception(this,
					"Error inserting location: " + location.getName(), e);
		} finally {
			cleanup();
		}
	}

	public Location readLocation(final int id, final int companyId) throws DAOException {
		try {
			final PreparedStatement ps = prepareStatement("SELECT "
					+ LOCATION_COMMON_FIELDS + ", " + JAPANESE_NAME_AND_ADDRESS 
					+ " FROM `Location` WHERE `id` = ?" + COMPANY_ID_QUERY);
			int index = 1;
			ps.setInt(index++, id);
			ps.setInt(index++, companyId);

			final ResultSet rs = ps.executeQuery();
			final Location location = new Location();
			if (rs.next()) {
				loadLocation(rs, location);
			}
			return location;
		} catch (SQLException e) {
			throw exception(this, "Error reading location: " + id, e);
		} finally {
			cleanup();
		}
	}

	public void updateLocation(final Location location) throws DAOException {
		try {
			final PreparedStatement ps = prepareStatement("UPDATE `Location` SET `name` = ?, `address` = ?, `nameJA` = ?, `addressJA` = ? WHERE `id` = ?"
					+ COMPANY_ID_QUERY);
			int index = populateLocation(ps, location);
			ps.setInt(index++, location.getId());
			ps.setInt(index++, location.getCompanyId());

			ps.executeUpdate();
		} catch (SQLException e) {
			throw exception(this,
					"Error updating location: " + location.getName(), e);
		} finally {
			cleanup();
		}
	}

	public boolean deleteLocation(final int id, final int companyId) throws DAOException {
		Connection conn = null;
		PreparedStatement ps = null;
		try {
			conn = getConnection();
			ps = conn.prepareStatement("SELECT COUNT(`locationId`) = 0 FROM `Inventory` WHERE `locationId` = ?"
							+ COMPANY_ID_QUERY);
			int index = 1;
			ps.setInt(index++, id);
			ps.setInt(index++, companyId);

			final ResultSet rs = ps.executeQuery();
			final boolean canDelete = rs.next() && rs.getBoolean(1);
			if (canDelete) {
				ps.close();
				ps = conn.prepareStatement("DELETE FROM `Location` WHERE `id` = ?"
								+ COMPANY_ID_QUERY);
				index = 1;
				ps.setInt(index++, id);
				ps.setInt(index++, companyId);

				checkDelete(ps, "Error deleting location: " + id);
			}
			return canDelete;
		} catch (SQLException e) {
			throw exception(this, "Error deleting location: " + id, e);
		} finally {
			cleanup(conn, ps);
		}
	}

	public List<Location> getLocationList(final int companyId) throws DAOException {
		try {
			final PreparedStatement ps = prepareStatement("SELECT "
					+ LOCATION_COMMON_FIELDS + ", " + JAPANESE_NAME_AND_ADDRESS 
					+ ", (SELECT COUNT(`locationId`) FROM `Inventory` WHERE `locationId` = l.`id`) = 0 AS canDelete FROM `Location` l"
					+ " WHERE `companyId` = ? ORDER BY `name`, `nameJA` ASC");
			ps.setInt(1, companyId);

			final ResultSet rs = ps.executeQuery();
			final List<Location> list = newArrayList();
			while (rs.next()) {
				Location location = new Location();
				int index = loadLocation(rs, location);
				location.setCanDelete(rs.getBoolean(index++));
				list.add(location);
			}
			return list;
		} catch (SQLException e) {
			throw exception(this, "Error getting location list: " + companyId, e);
		} finally {
			cleanup();
		}
	}

	private int populateLocation(final PreparedStatement ps, final Location location) throws SQLException {
		int index = 1;
		ps.setString(index++, location.getName());
		ps.setString(index++, location.getAddress());
		if(isBlank(location.getNameJA())) ps.setString(index++, location.getName());
		else if((location.getName().equalsIgnoreCase(location.getNameJA())) && (isNotBlank(location.getName()) && isNotBlank(location.getNameJA()))) ps.setString(index++, location.getName());
		else ps.setString(index++, location.getNameJA());
		if(isBlank(location.getAddressJA())) ps.setString(index++, location.getAddress());
		else if((location.getAddress().equalsIgnoreCase(location.getAddressJA())) && (isNotBlank(location.getAddress()) && isNotBlank(location.getAddressJA()))) ps.setString(index++, location.getAddress());
		else ps.setString(index++, location.getAddressJA());
		return index;
	}

	private int loadLocation(final ResultSet rs, final Location location) throws SQLException {
		int index = 1;
		location.setId(rs.getInt(index++));
		location.setName(rs.getString(index++));
		location.setAddress(rs.getString(index++));
		location.setNameJA(rs.getString(index++));
		location.setAddressJA(rs.getString(index++));
		return index;
	}

	// Category
	public void createCategory(final Category category) throws DAOException {
		try {
				final PreparedStatement ps = prepareStatement("INSERT INTO `Category` (" 
						+ CATEGORY_COMMON_FIELDS + ", " + JAPANESE_NAME_AND_DESCRIPTION 
						+ ", `companyId`) VALUES (NULL, ?, ?, ?, ?, ?)");
				int index = populateCategory(ps, category);
				ps.setInt(index++, category.getCompanyId());
				
				checkInsert(ps, "Error inserting category: " + category.getName());
		} catch (SQLException e) {
			throw exception(this, "Error inserting category: " + category.getName(), e);
		} finally {
			cleanup();
		}
	}

	public Category readCategory(final int id, final int companyId) throws DAOException {
		try {
				final PreparedStatement ps = prepareStatement("SELECT " 
						+ CATEGORY_COMMON_FIELDS + ", " + JAPANESE_NAME_AND_DESCRIPTION
						+ " FROM `Category` WHERE `id` = ?" 
						+ COMPANY_ID_QUERY);
				int index = 1;
				ps.setInt(index++, id);
				ps.setInt(index++, companyId);

				final ResultSet rs = ps.executeQuery();
				final Category category = new Category();
				if (rs.next()) {
					loadCategory(rs, category);
				}
				return category;
		} catch (SQLException e) {
			throw exception(this, "Error reading category: " + id, e);
		} finally {
			cleanup();
		}
	}

	public void updateCategory(final Category category) throws DAOException {
		try {
				final PreparedStatement ps = prepareStatement("UPDATE `Category` SET `name` = ?, `description` = ?, `nameJA` = ?, `descriptionJA` = ? WHERE `id` = ?"
						+ COMPANY_ID_QUERY);
				int index = populateCategory(ps, category);
				ps.setInt(index++, category.getId());
				ps.setInt(index++, category.getCompanyId());
				ps.executeUpdate();
				
		} catch (SQLException e) {
			throw exception(this, "Error updating category: " + category.getName(), e);
		} finally {
			cleanup();
		}
	}

	public boolean deleteCategory(final int id, final int companyId) throws DAOException {
		Connection conn = null;
		PreparedStatement ps = null;
		try {
			conn = getConnection();
			ps = conn.prepareStatement("SELECT COUNT(`categoryId`) = 0 FROM `ItemMaster` WHERE `categoryId` = ?" + COMPANY_ID_QUERY);
			int index = 1;
			ps.setInt(index++, id);
			ps.setInt(index++, companyId);

			final ResultSet rs = ps.executeQuery();
			final boolean canDelete = rs.next() && rs.getBoolean(1);
			if (canDelete) {
				ps.close();
				ps = conn.prepareStatement("DELETE FROM `Category` WHERE `id` = ?" + COMPANY_ID_QUERY);
				index = 1;
				ps.setInt(index++, id);
				ps.setInt(index++, companyId);

				checkDelete(ps, "Error deleting category: " + id);
			}
			return canDelete;
		} catch (SQLException e) {
			throw exception(this, "Error deleting category: " + id, e);
		} finally {
			cleanup(conn, ps);
		}
	}

	public List<Category> getCategoryList(final int companyId) throws DAOException {
		try {
				final PreparedStatement ps = prepareStatement("SELECT " + CATEGORY_COMMON_FIELDS + ", " + JAPANESE_NAME_AND_DESCRIPTION
						+ ", (SELECT COUNT(`categoryId`) FROM `ItemMaster`"
						+ " WHERE `categoryId` = ca.`id`) = 0 AS canDelete FROM `Category` ca WHERE `companyId` = ? ORDER BY `name`, `nameJA` ASC");
				ps.setInt(1, companyId);
				final ResultSet rs = ps.executeQuery();
				final List<Category> list = newArrayList();
				while (rs.next()) {
					Category category = new Category();
					int index = loadCategory(rs, category);
					category.setCanDelete(rs.getBoolean(index++));
					list.add(category);
				}
				return list;
		} catch (SQLException e) {
			throw exception(this, "Error getting category list: " + companyId, e);
		} finally {
			cleanup();
		}
	}

	private int populateCategory(final PreparedStatement ps, final Category category) throws SQLException {
		int index = 1;
		ps.setString(index++, category.getName());
		ps.setString(index++, category.getDescription());
		if(isBlank(category.getNameJA())) ps.setString(index++, category.getName());
		else if((category.getName().equalsIgnoreCase(category.getNameJA())) && (isNotBlank(category.getName()) && isNotBlank(category.getNameJA()))) ps.setString(index++, category.getName());
		else ps.setString(index++, category.getNameJA());
		ps.setString(index++, category.getDescriptionJA());
		return index;
	}

	private int loadCategory(final ResultSet rs, final Category category) throws SQLException {
		int index = 1;
		category.setId(rs.getInt(index++));
		category.setName(rs.getString(index++));
		category.setDescription(rs.getString(index++));
		category.setNameJA(rs.getString(index++));
		category.setDescriptionJA(rs.getString(index++));
		return index;
	}

	// ItemMaster
	public void createItemMaster(final ItemMaster itemMaster) throws DAOException {
		try {
			final PreparedStatement ps = prepareStatement("INSERT INTO `ItemMaster` ("
					+ ITEM_MASTER_COMMON_FIELDS
					+ ", `companyId`) VALUES (NULL, ?, ?, ?, ?, ?, ?, ?)");
			int index = populateItemMaster(ps, itemMaster);
			ps.setInt(index++, itemMaster.getCompanyId());

			checkInsert(ps, "Error inserting itemMaster: " + itemMaster.getName());
		} catch (SQLException e) {
			throw exception(this, "Error inserting itemMaster: " + itemMaster.getName(), e);
		} finally {
			cleanup();
		}
	}

	public ItemMaster readItemMaster(final int id, final int companyId) throws DAOException {
		try {
			final PreparedStatement ps = prepareStatement("SELECT "
					+ ITEM_MASTER_COMMON_FIELDS
					+ " FROM `ItemMaster` WHERE `id` = ?" + COMPANY_ID_QUERY);
			int index = 1;
			ps.setInt(index++, id);
			ps.setInt(index++, companyId);

			final ResultSet rs = ps.executeQuery();
			final ItemMaster itemMaster = new ItemMaster();
			if (rs.next()) {
				loadItemMaster(rs, itemMaster);
			}
			return itemMaster;
		} catch (SQLException e) {
			throw exception(this, "Error reading itemMaster: " + id, e);
		} finally {
			cleanup();
		}
	}

	public void updateItemMaster(final ItemMaster itemMaster) throws DAOException {
		try {
			final PreparedStatement ps = prepareStatement("UPDATE `ItemMaster` SET `categoryId` = ?, `name` = ?, `code` = ?, `description` = ?, `nameJA` = ?, `descriptionJA` = ? WHERE `id` = ?"
					+ COMPANY_ID_QUERY);
			int index = populateItemMaster(ps, itemMaster);
			ps.setInt(index++, itemMaster.getId());
			ps.setInt(index++, itemMaster.getCompanyId());

			ps.executeUpdate();
		} catch (SQLException e) {
			throw exception(this,
					"Error updating itemMaster: " + itemMaster.getName(), e);
		} finally {
			cleanup();
		}
	}

	public boolean deleteItemMaster(final int id, final int companyId) throws DAOException {
		Connection conn = null;
		PreparedStatement ps = null;
		try {
			conn = getConnection();
			ps = conn.prepareStatement("SELECT COUNT(`itemId`) = 0 FROM `Inventory` WHERE `itemId` = ?"
							+ COMPANY_ID_QUERY);
			int index = 1;
			ps.setInt(index++, id);
			ps.setInt(index++, companyId);

			final ResultSet rs = ps.executeQuery();
			final boolean canDelete = rs.next() && rs.getBoolean(1);
			if (canDelete) {
				ps.close();
				ps = conn.prepareStatement("DELETE FROM `ItemMaster` WHERE `id` = ?"
								+ COMPANY_ID_QUERY);
				index = 1;
				ps.setInt(index++, id);
				ps.setInt(index++, companyId);

				checkDelete(ps, "Error deleting itemMaster: " + id);
			}
			return canDelete;
		} catch (SQLException e) {
			throw exception(this, "Error deleting itemMaster: " + id, e);
		} finally {
			cleanup(conn, ps);
		}
	}

	public List<ItemMaster> getItemMaster(final int companyId) throws DAOException {
		try {
			final String fields = ITEM_MASTER_COMMON_FIELDS.replace("`, `", "`, im.`");
			final PreparedStatement ps = prepareStatement("SELECT im."
					+ fields
					+ ", ca.`name`, ca.`nameJA`"
					+ ", (SELECT COUNT(`itemId`) FROM `Inventory` WHERE `itemId` = im.`id`) = 0 AS canDelete FROM `ItemMaster` im"
					+ " LEFT JOIN `Category` ca ON im.`categoryId` = ca.`id`"
					+ " WHERE im.`companyId` = ? ORDER BY ca.`name`, ca.`nameJA`, im.`name`, im.`nameJA`");
			ps.setInt(1, companyId);

			final ResultSet rs = ps.executeQuery();
			final List<ItemMaster> list = newArrayList();
			while (rs.next()) {
				ItemMaster itemMaster = new ItemMaster();
				int index = loadItemMaster(rs, itemMaster);
				itemMaster.setCategoryName(rs.getString(index++));
				itemMaster.setCategoryNameJA(rs.getString(index++));
				itemMaster.setCanDelete(rs.getBoolean(index++));
				list.add(itemMaster);
			}
			return list;
		} catch (SQLException e) {
			throw exception(this, "Error getting itemMaster: " + companyId, e);
		} finally {
			cleanup();
		}
	}

	private int populateItemMaster(final PreparedStatement ps, final ItemMaster itemMaster) throws SQLException {
		int index = 1;
		ps.setInt(index++, itemMaster.getCategoryId());
		ps.setString(index++, itemMaster.getName());
		ps.setString(index++, itemMaster.getCode());
		ps.setString(index++, itemMaster.getDescription());
		if(isBlank(itemMaster.getNameJA())) ps.setString(index++, itemMaster.getName());
		else if((itemMaster.getName().equalsIgnoreCase(itemMaster.getNameJA())) && (isNotBlank(itemMaster.getName()) && isNotBlank(itemMaster.getNameJA()))) ps.setString(index++, itemMaster.getName());
		else ps.setString(index++, itemMaster.getNameJA());
		ps.setString(index++, itemMaster.getDescriptionJA());
		return index;
	}

	private int loadItemMaster(final ResultSet rs, final ItemMaster itemMaster) throws SQLException {
		int index = 1;
		itemMaster.setId(rs.getInt(index++));
		itemMaster.setCategoryId(rs.getInt(index++));
		itemMaster.setName(rs.getString(index++));
		itemMaster.setCode(rs.getString(index++));
		itemMaster.setDescription(rs.getString(index++));
		itemMaster.setNameJA(rs.getString(index++));
		itemMaster.setDescriptionJA(rs.getString(index++));
		return index;
	}

	// Supplier
	public void createSupplier(final Supplier supplier) throws DAOException {
		try {
			final PreparedStatement ps = prepareStatement("INSERT INTO `Supplier` ("
					+ SUPPLIER_COMMON_FIELDS
					+ ", " + JAPANESE_NAME_AND_DESCRIPTION
					+ ", `companyId`) VALUES (NULL, ?, ?, ?, ?, ?)");
			int index = populateSupplier(ps, supplier);
			ps.setInt(index++, supplier.getCompanyId()); 
			
			checkInsert(ps, "Error inserting supplier: " + supplier.getName());
		} catch (SQLException e) {
			throw exception(this, "Error inserting supplier: " + supplier.getName(), e);
		} finally {
			cleanup();
		}
	}

	public Supplier readSupplier(final int id, final int companyId) throws DAOException {
		try {
			final PreparedStatement ps = prepareStatement("SELECT "
					+ SUPPLIER_COMMON_FIELDS
					+ ", " + JAPANESE_NAME_AND_DESCRIPTION
					+ " FROM `Supplier` WHERE `id` = ?" + COMPANY_ID_QUERY);
			int index = 1;
			ps.setInt(index++, id);
			ps.setInt(index++, companyId);
			final ResultSet rs = ps.executeQuery();
			final Supplier supplier = new Supplier();
			if (rs.next()) {
				loadSupplier(rs, supplier);
			}
			return supplier;
		} catch (SQLException e) {
			throw exception(this, "Error reading supplier: " + id, e);
		} finally {
			cleanup();
		}
	}

	public void updateSupplier(final Supplier supplier) throws DAOException {
		try {
					final PreparedStatement ps = prepareStatement("UPDATE `Supplier` SET `name` = ?, `description` = ?, `nameJA` = ?, `descriptionJA` = ? WHERE `id` = ?" + COMPANY_ID_QUERY);
					int index = populateSupplier(ps, supplier);
					ps.setInt(index++, supplier.getId());
					ps.setInt(index++, supplier.getCompanyId());
					ps.executeUpdate();
		} catch (SQLException e) {
			throw exception(this, "Error updating supplier: " + supplier.getName(), e);
		} finally {
			cleanup();
		}
	}

	public boolean deleteSupplier(final int id, final int companyId) throws DAOException {
		Connection conn = null;
		PreparedStatement ps = null;
		try {
			conn = getConnection();
			ps = conn.prepareStatement("SELECT COUNT(`supplierId`) = 0 FROM `Inventory` WHERE `supplierId` = ?"
							+ COMPANY_ID_QUERY);
			int index = 1;
			ps.setInt(index++, id);
			ps.setInt(index++, companyId);

			final ResultSet rs = ps.executeQuery();
			final boolean canDelete = rs.next() && rs.getBoolean(1);
			if (canDelete) {
				ps.close();
				ps = conn.prepareStatement("DELETE FROM `Supplier` WHERE `id` = ?"
								+ COMPANY_ID_QUERY);
				index = 1;
				ps.setInt(index++, id);
				ps.setInt(index++, companyId);

				checkDelete(ps, "Error deleting supplier: " + id);
			}
			return canDelete;
		} catch (SQLException e) {
			throw exception(this, "Error deleting supplier: " + id, e);
		} finally {
			cleanup(conn, ps);
		}
	}

	public List<Supplier> getSupplierList(final int companyId) throws DAOException {
		try {
			final PreparedStatement ps = prepareStatement("SELECT "
					+ SUPPLIER_COMMON_FIELDS + ", " + JAPANESE_NAME_AND_DESCRIPTION
					+ ", (SELECT COUNT(`supplierId`) FROM `Inventory`"
					+ " WHERE `supplierId` = supp.`id`) = 0 AS canDelete FROM `Supplier` supp WHERE `companyId` = ? ORDER BY `name`, `nameJA` ASC");
			ps.setInt(1, companyId);

			final ResultSet rs = ps.executeQuery();
			
			final List<Supplier> list = newArrayList();
			while (rs.next()) {
				Supplier supplier = new Supplier();
				int index = loadSupplier(rs, supplier);
				supplier.setCanDelete(rs.getBoolean(index++));
				list.add(supplier);
			}
			return list;
		} catch (SQLException e) {
			throw exception(this, "Error getting supplier list: " + companyId, e);
		} finally {
			cleanup();
		}
	}

	private int populateSupplier(final PreparedStatement ps, final Supplier supplier) throws SQLException {
		int index = 1;
		ps.setString(index++, supplier.getName());
		ps.setString(index++, supplier.getDescription());
		if(isBlank(supplier.getNameJA())) ps.setString(index++, supplier.getName());
		else if((supplier.getName().equalsIgnoreCase(supplier.getNameJA())) && (isNotBlank(supplier.getName()) && isNotBlank(supplier.getNameJA()))) ps.setString(index++, supplier.getName());
		else ps.setString(index++, supplier.getNameJA());
		ps.setString(index++, supplier.getDescriptionJA());
		return index;
	}

	private int loadSupplier(final ResultSet rs, final Supplier supplier) throws SQLException {
		int index = 1;
		supplier.setId(rs.getInt(index++));
		supplier.setName(rs.getString(index++));
		supplier.setDescription(rs.getString(index++));
		supplier.setNameJA(rs.getString(index++));
		supplier.setDescriptionJA(rs.getString(index++));
		return index;
	}

	// Client
	public void createClient(final Client client) throws DAOException {
		Connection conn = null;
		PreparedStatement ps = null;
		try {
			conn = getConnection();
			ps = conn.prepareStatement("SELECT COUNT(`id` > 0 OR NULL) = 0 FROM `Client` WHERE `companyId` = ?");
			ps.setInt(1, client.getCompanyId());

			final ResultSet rs = ps.executeQuery();
			final boolean cntZero = rs.next() && rs.getBoolean(1);
			if (cntZero) {
				ps.close();
				ps = conn.prepareStatement("INSERT INTO `Client` (" 
						+ CLIENT_COMMON_FIELDS + ", " + JAPANESE_NAME_AND_DESCRIPTION 
						+ ", `companyId`) VALUES (NULL, ?, ?, ?, ?, ?)");
				int index = populateFirstClient(ps);
				ps.setInt(index++, client.getCompanyId());
				
				checkInsert(ps, "Error inserting client: 1st`Client`.`id` of this.`companyId`");
			}
			ps.close();
			ps = conn.prepareStatement("INSERT INTO `Client` (" 
					+ CLIENT_COMMON_FIELDS + ", " + JAPANESE_NAME_AND_DESCRIPTION 
					+ ", `companyId`) VALUES (NULL, ?, ?, ?, ?, ?)");
			int index = populateClient(ps, client);
			ps.setInt(index++, client.getCompanyId());
			
			checkInsert(ps, "Error inserting client: " + client.getName());
		} catch (SQLException e) {
			throw exception(this, "Error inserting client: " + client.getName(), e);
		} finally {
			cleanup(conn, ps);
		}
	}

	public Client readClient(final int id, final int companyId) throws DAOException {
		try {
				final PreparedStatement ps = prepareStatement("SELECT " 
						+ CLIENT_COMMON_FIELDS + ", " + JAPANESE_NAME_AND_DESCRIPTION
						+ " FROM `Client` WHERE `id` = ?" 
						+ COMPANY_ID_QUERY);
				int index = 1;
				ps.setInt(index++, id);
				ps.setInt(index++, companyId);

				final ResultSet rs = ps.executeQuery();
				final Client client = new Client();
				if (rs.next()) {
					loadClient(rs, client);
				}
				return client;
		} catch (SQLException e) {
			throw exception(this, "Error reading client: " + id, e);
		} finally {
			cleanup();
		}
	}

	public void updateClient(final Client client) throws DAOException {
		try {
				final PreparedStatement ps = prepareStatement("UPDATE `Client` SET `name` = ?, `description` = ?, `nameJA` = ?, `descriptionJA` = ? WHERE `id` = ?"
						+ COMPANY_ID_QUERY);
				int index = populateClient(ps, client);
				ps.setInt(index++, client.getId());
				ps.setInt(index++, client.getCompanyId());
				ps.executeUpdate();
				
		} catch (SQLException e) {
			throw exception(this, "Error updating client: " + client.getName(), e);
		} finally {
			cleanup();
		}
	}

	public boolean deleteClient(final int id, final int companyId) throws DAOException {
		Connection conn = null;
		PreparedStatement ps = null;
		try {
			conn = getConnection();
			ps = conn.prepareStatement("SELECT COUNT(`clientId`) = 0 FROM `Inventory` WHERE `clientId` = ?" + COMPANY_ID_QUERY);
			int index = 1;
			ps.setInt(index++, id);
			ps.setInt(index++, companyId);

			final ResultSet rs = ps.executeQuery();
			final boolean canDelete = rs.next() && rs.getBoolean(1);
			if (canDelete) {
				ps.close();
				ps = conn.prepareStatement("DELETE FROM `Client` WHERE `id` = ?" + COMPANY_ID_QUERY);
				index = 1;
				ps.setInt(index++, id);
				ps.setInt(index++, companyId);

				checkDelete(ps, "Error deleting client: " + id);
			}
			return canDelete;
		} catch (SQLException e) {
			throw exception(this, "Error deleting client: " + id, e);
		} finally {
			cleanup(conn, ps);
		}
	}

	public List<Client> getClientList(final int companyId) throws DAOException {
		try {
				final PreparedStatement ps = prepareStatement("SELECT " + CLIENT_COMMON_FIELDS + ", " + JAPANESE_NAME_AND_DESCRIPTION
						+ ", (SELECT COUNT(`clientId`) FROM `Inventory` WHERE `clientId` = cl.`id`) = 0 AS canDelete"
						+ " FROM `Client` cl WHERE `companyId` = ? ORDER BY `name`, `nameJA` ASC");
				ps.setInt(1, companyId);
				final ResultSet rs = ps.executeQuery();
				final List<Client> list = newArrayList();
				while (rs.next()) {
					Client client = new Client();
					int index = loadClient(rs, client);
					client.setCanDelete(rs.getBoolean(index++));
					list.add(client);
				}
				return list;
		} catch (SQLException e) {
			throw exception(this, "Error getting client list: " + companyId, e);
		} finally {
			cleanup();
		}
	}

	private int populateFirstClient(final PreparedStatement ps) throws SQLException {
		int index = 1;
		ps.setString(index++, null);
		ps.setString(index++, "");
		ps.setString(index++, null);
		ps.setString(index++, "");
		return index;
	}

	private int populateClient(final PreparedStatement ps, final Client client) throws SQLException {
		int index = 1;
		if(isBlank(client.getName())) ps.setString(index++, null);
		else ps.setString(index++, client.getName());
		ps.setString(index++, client.getDescription());
		if(isBlank(client.getNameJA())) ps.setString(index++, client.getName());
		else if((client.getName().equalsIgnoreCase(client.getNameJA())) && (isNotBlank(client.getName()) && isNotBlank(client.getNameJA()))) ps.setString(index++, client.getName());
		else ps.setString(index++, client.getNameJA());
		ps.setString(index++, client.getDescriptionJA());
		return index;
	}

	private int loadClient(final ResultSet rs, final Client client) throws SQLException {
		int index = 1;
		client.setId(rs.getInt(index++));
		client.setName(rs.getString(index++));
		client.setDescription(rs.getString(index++));
		client.setNameJA(rs.getString(index++));
		client.setDescriptionJA(rs.getString(index++));
		return index;
	}

	// CheckIn
	public void createCheckIn(final CheckIn checkin) throws DAOException {
		Connection conn = null;
		PreparedStatement ps = null;
		try {
			conn = getConnection();
			ps = conn.prepareStatement("SELECT "
					+ "COUNT(ISNULL(`nameJA`)) = 1 FROM `Client`"
					+ " WHERE `id` = ?" + COMPANY_ID_QUERY);
			int index = 1;
			ps.setInt(index++, checkin.getClientId());
			ps.setInt(index, checkin.getCompanyId());

			final ResultSet rs = ps.executeQuery();
			final boolean nameless = rs.next() && rs.getBoolean(1);
			
			if (nameless) {
				ps.close();
				rs.close();
				ps = conn.prepareStatement("INSERT INTO `Inventory` ("
						+ CHECKIN_COMMON_FIELDS
						+ ", `companyId`) VALUES (NULL, ?, ?, 0, ?, ?, ?, ?, ?, ?)");
				index = populateCheckInForNoClient(ps, checkin, nameless);
				ps.setInt(index++, checkin.getCompanyId());

				checkInsert(ps, "Error inserting check-in: " + checkin.getQuantity());
			}
			else {
				ps.close();
				rs.close();
				ps = conn.prepareStatement("INSERT INTO `Inventory` ("
						+ CHECKIN_COMMON_FIELDS
						+ ", `companyId`) VALUES (NULL, ?, ?, ?, ?, ?, ?, ?, ?, ?)");
				index = populateCheckIn(ps, checkin);
				ps.setInt(index++, checkin.getCompanyId());

				checkInsert(ps, "Error inserting check-in: " + checkin.getQuantity());
			}
		} catch (SQLException e) {
			throw exception(this, "Error inserting check-in: " + checkin.getQuantity(), e);
		} finally {
			cleanup(conn, ps);
		}
	}

	public CheckIn readCheckIn(final int id, final int companyId) throws DAOException {
		try {
			final PreparedStatement ps = prepareStatement("SELECT "
					+ CHECKIN_COMMON_FIELDS
					+ " FROM `Inventory` WHERE `id` = ?" + COMPANY_ID_QUERY);
			int index = 1;
			ps.setInt(index++, id);
			ps.setInt(index++, companyId);

			final ResultSet rs = ps.executeQuery();
			final CheckIn checkin = new CheckIn();
			if (rs.next()) {
				loadCheckIn(rs, checkin);
			}
			return checkin;
		} catch (SQLException e) {
			throw exception(this, "Error reading check-in: " + id, e);
		} finally {
			cleanup();
		}
	}

	public void updateCheckIn(final CheckIn checkin) throws DAOException {
		try {
			final PreparedStatement ps = prepareStatement("UPDATE `Inventory` SET `roleId` = ?, `itemId` = ?, `clientId` = ?, `supplierId` = ?, `locationId` = ?, `quantity` = ?, `comment` = ?, `date` = ? WHERE `id` = ?"
					+ COMPANY_ID_QUERY);
			int index = populateCheckIn(ps, checkin);
			ps.setInt(index++, checkin.getId());
			ps.setInt(index++, checkin.getCompanyId());

			ps.executeUpdate();
		} catch (SQLException e) {
			throw exception(this,
					"Error updating check-in: " + checkin.getQuantity(), e);
		} finally {
			cleanup();
		}
	}

	public boolean deleteCheckIn(final int id, final int companyId) throws DAOException {
		Connection conn = null;
		PreparedStatement ps = null;
		try {
			conn = getConnection();
			ps = conn.prepareStatement("SELECT COUNT(`roleId` = 1 OR NULL) <> 0 FROM `Inventory` WHERE `id` = ?"
							+ COMPANY_ID_QUERY);
			int index = 1;
			ps.setInt(index++, id);
			ps.setInt(index++, companyId);

			final ResultSet rs = ps.executeQuery();
			final boolean canDelete = rs.next() && rs.getBoolean(1);
			if (canDelete) {
				ps.close();
				ps = conn
						.prepareStatement("DELETE FROM `Inventory` WHERE `id` = ?"
								+ COMPANY_ID_QUERY);
				index = 1;
				ps.setInt(index++, id);
				ps.setInt(index++, companyId);

				checkDelete(ps, "Error deleting check-in: " + id);
			}
			return canDelete;
		} catch (SQLException e) {
			throw exception(this, "Error deleting check-in: " + id, e);
		} finally {
			cleanup(conn, ps);
		}
	}

	public List<CheckIn> getCheckInList(final int companyId)
			throws DAOException {
		try {
			final String fields = CHECKIN_COMMON_FIELDS.replace("`, `", "`, check_in.`");
			final PreparedStatement ps = prepareStatement("SELECT check_in."
					+ fields
					+ ", cl.`name`, cl.`nameJA`, l.`name`, l.`nameJA`, im.`name`, im.`nameJA`, s.`name`, s.`nameJA`"
					+ ", (SELECT COUNT(cin.`roleId` = 1 OR NULL) <> 0 FROM `Inventory` cin) <> 0 AS canDelete FROM `Inventory` check_in"
					+ " LEFT JOIN `Client` cl ON check_in.`clientId` = cl.`id`"
					+ " LEFT JOIN `Location` l ON check_in.`locationId` = l.`id`"
					+ " LEFT JOIN `ItemMaster` im ON check_in.`itemId` = im.`id`"
					+ " LEFT JOIN `Supplier` s ON check_in.`supplierId` = s.`id`"
					+ " WHERE check_in.`roleId` = 1 AND check_in.`companyId` = ?"
					+ " ORDER BY check_in.`date` DESC, check_in.`clientId` ASC");
			ps.setInt(1, companyId);

			final ResultSet rs = ps.executeQuery();
			final List<CheckIn> list = newArrayList();
			while (rs.next()) {
				CheckIn checkin = new CheckIn();
				int index = loadCheckIn(rs, checkin);
				checkin.setClientName(rs.getString(index++));
				checkin.setClientNameJA(rs.getString(index++));
				checkin.setLocationName(rs.getString(index++));
				checkin.setLocationNameJA(rs.getString(index++));
				checkin.setItemName(rs.getString(index++));
				checkin.setItemNameJA(rs.getString(index++));
				checkin.setSupplierName(rs.getString(index++));
				checkin.setSupplierNameJA(rs.getString(index++));
				checkin.setCanDelete(rs.getBoolean(index++));
				list.add(checkin);
			}
			return list;
		} catch (SQLException e) {
			throw exception(this, "Error getting check-in list: " + companyId, e);
		} finally {
			cleanup();
		}
	}

	private int populateCheckInForNoClient(final PreparedStatement ps, final CheckIn checkin, final boolean nameless) throws SQLException {
		int index = 1;
		ps.setInt(index++, checkin.getRoleId());
		ps.setInt(index++, checkin.getItemId());
		ps.setInt(index++, checkin.getSupplierId());
		ps.setInt(index++, checkin.getLocationId());
		ps.setInt(index++, checkin.getQuantity());
		ps.setString(index++, checkin.getComment());
		setDate(ps, index++, checkin.getDate());
		return index;
	}

	private int populateCheckIn(final PreparedStatement ps, final CheckIn checkin) throws SQLException {
		int index = 1;
		ps.setInt(index++, checkin.getRoleId());
		ps.setInt(index++, checkin.getItemId());
		ps.setInt(index++, checkin.getClientId());
		ps.setInt(index++, checkin.getSupplierId());
		ps.setInt(index++, checkin.getLocationId());
		ps.setInt(index++, checkin.getQuantity());
		ps.setString(index++, checkin.getComment());
		setDate(ps, index++, checkin.getDate());
		return index;
	}

	private int loadCheckIn(final ResultSet rs, final CheckIn checkin) throws SQLException {
		int index = 1;
		checkin.setId(rs.getInt(index++));
		checkin.setRoleId(rs.getInt(index++));
		checkin.setItemId(rs.getInt(index++));
		checkin.setClientId(rs.getInt(index++));
		checkin.setSupplierId(rs.getInt(index++));
		checkin.setLocationId(rs.getInt(index++));
		checkin.setQuantity(rs.getInt(index++));
		checkin.setComment(rs.getString(index++));
		checkin.setDate(rs.getDate(index++));
		return index;
	}

	// CheckOut
	public void createCheckOut(final CheckOut checkout) throws DAOException {
		Connection conn = null;
		PreparedStatement ps = null;
		try {
			conn = getConnection();
			ps = conn.prepareStatement("SELECT "
					+ "COUNT(ISNULL(`nameJA`)) = 1 FROM `Client`"
					+ " WHERE `id` = ?" + COMPANY_ID_QUERY);
			int index = 1;
			ps.setInt(index++, checkout.getClientId());
			ps.setInt(index, checkout.getCompanyId());

			final ResultSet rs = ps.executeQuery();
			final boolean nameless = rs.next() && rs.getBoolean(1);
			
			if (nameless) {
				ps.close();
				rs.close();
				ps = conn.prepareStatement("INSERT INTO `Inventory` ("
						+ CHECKOUT_COMMON_FIELDS
						+ ", `companyId`) VALUES (NULL, ?, ?, 0, ?, ?, ?, ?, ?, ?)");
				index = populateCheckOutForNoClient(ps, checkout, nameless);
				ps.setInt(index++, checkout.getCompanyId());

				checkInsert(ps, "Error inserting check-in: " + checkout.getQuantity());
			}
			else {
				ps.close();
				rs.close();
				ps = conn.prepareStatement("INSERT INTO `Inventory` ("
						+ CHECKOUT_COMMON_FIELDS
						+ ", `companyId`) VALUES (NULL, ?, ?, ?, ?, ?, ?, ?, ?, ?)");
				index = populateCheckOut(ps, checkout);
				ps.setInt(index++, checkout.getCompanyId());

				checkInsert(ps, "Error inserting check-in: " + checkout.getQuantity());
			}
		} catch (SQLException e) {
			throw exception(this, "Error inserting check-in: " + checkout.getQuantity(), e);
		} finally {
			cleanup(conn, ps);
		}
	}

	public CheckOut readCheckOut(final int id, final int companyId) throws DAOException {
		try {
			final PreparedStatement ps = prepareStatement("SELECT "
					+ CHECKOUT_COMMON_FIELDS
					+ " FROM `Inventory` WHERE `id` = ?" + COMPANY_ID_QUERY);
			int index = 1;
			ps.setInt(index++, id);
			ps.setInt(index++, companyId);

			final ResultSet rs = ps.executeQuery();
			final CheckOut checkout = new CheckOut();
			if (rs.next()) {
				loadCheckOut(rs, checkout);
			}
			return checkout;
		} catch (SQLException e) {
			throw exception(this, "Error reading check-out: " + id, e);
		} finally {
			cleanup();
		}
	}

	public void updateCheckOut(final CheckOut checkout) throws DAOException {
		try {
			final PreparedStatement ps = prepareStatement("UPDATE `Inventory` SET `roleId` = ?, `itemId` = ?, `clientId` = ?, `supplierId` = ?, `locationId` = ?, `quantity` = ?, `comment` = ?, `date` = ? WHERE `id` = ?"
					+ COMPANY_ID_QUERY);
			int index = populateCheckOut(ps, checkout);
			ps.setInt(index++, checkout.getId());
			ps.setInt(index++, checkout.getCompanyId());

			ps.executeUpdate();
		} catch (SQLException e) {
			throw exception(this, "Error updating check-out: " + checkout.getQuantity(), e);
		} finally {
			cleanup();
		}
	}

	public boolean deleteCheckOut(final int id, final int companyId) throws DAOException {
		Connection conn = null;
		PreparedStatement ps = null;
		try {
			conn = getConnection();
			ps = conn.prepareStatement("SELECT COUNT(`roleId` = 2 OR NULL) <> 0 FROM `Inventory` WHERE `id` = ?"
							+ COMPANY_ID_QUERY);
			int index = 1;
			ps.setInt(index++, id);
			ps.setInt(index, companyId);

			final ResultSet rs = ps.executeQuery();
			final boolean canDelete = rs.next() && rs.getBoolean(1);
			if (canDelete) {
				ps.close();
				ps = conn.prepareStatement("DELETE FROM `Inventory` WHERE `id` = ?"
								+ COMPANY_ID_QUERY);
				index = 1;
				ps.setInt(index++, id);
				ps.setInt(index, companyId);

				checkDelete(ps, "Error deleting check-out: " + id);
			}
			return canDelete;
		} catch (SQLException e) {
			throw exception(this, "Error deleting check-out: " + id, e);
		} finally {
			cleanup(conn, ps);
		}
	}

	public List<CheckOut> getCheckOutList(final int companyId) throws DAOException {
		try {
			final String fields = CHECKOUT_COMMON_FIELDS.replace("`, `", "`, check_out.`");
			final PreparedStatement ps = prepareStatement("SELECT check_out."
					+ fields
					+ ", cl.`name`, cl.`nameJA`, l.`name`, l.`nameJA`, im.`name`, im.`nameJA`, s.`name`, s.`nameJA`"
					+ ", (SELECT COUNT(cout.`roleId` = 1 OR NULL) <> 0 FROM `Inventory` cout) <> 0 AS canDelete FROM `Inventory` check_out"
					+ " LEFT JOIN `Client` cl ON check_out.`clientId` = cl.`id`"
					+ " LEFT JOIN `Location` l ON check_out.`locationId` = l.`id`"
					+ " LEFT JOIN `ItemMaster` im ON check_out.`itemId` = im.`id`"
					+ " LEFT JOIN `Supplier` s ON check_out.`supplierId` = s.`id`"
					+ " WHERE check_out.`roleId` = 2 AND check_out.`companyId` = ?"
					+ " ORDER BY check_out.`date` DESC, check_out.`clientId` ASC");
			ps.setInt(1, companyId);

			final ResultSet rs = ps.executeQuery();
			final List<CheckOut> list = newArrayList();
			while (rs.next()) {
				CheckOut checkout = new CheckOut();
				int index = loadCheckOut(rs, checkout);
				checkout.setClientName(rs.getString(index++));
				checkout.setClientNameJA(rs.getString(index++));
				checkout.setLocationName(rs.getString(index++));
				checkout.setLocationNameJA(rs.getString(index++));
				checkout.setItemName(rs.getString(index++));
				checkout.setItemNameJA(rs.getString(index++));
				checkout.setSupplierName(rs.getString(index++));
				checkout.setSupplierNameJA(rs.getString(index++));
				checkout.setCanDelete(rs.getBoolean(index++));
				list.add(checkout);
			}
			return list;
		} catch (SQLException e) {
			throw exception(this, "Error getting check-out list: " + companyId, e);
		} finally {
			cleanup();
		}
	}

	private int populateCheckOutForNoClient(final PreparedStatement ps, final CheckOut checkout, final boolean nameless) throws SQLException {
		int index = 1;
		ps.setInt(index++, checkout.getRoleId());
		ps.setInt(index++, checkout.getItemId());
		ps.setInt(index++, checkout.getSupplierId());
		ps.setInt(index++, checkout.getLocationId());
		ps.setInt(index++, checkout.getQuantity());
		ps.setString(index++, checkout.getComment());
		setDate(ps, index++, checkout.getDate());
		return index;
	}

	private int populateCheckOut(final PreparedStatement ps, final CheckOut checkout) throws SQLException {
		int index = 1;
		ps.setInt(index++, checkout.getRoleId());
		ps.setInt(index++, checkout.getItemId());
		ps.setInt(index++, checkout.getClientId());
		ps.setInt(index++, checkout.getSupplierId());
		ps.setInt(index++, checkout.getLocationId());
		ps.setInt(index++, checkout.getQuantity());
		ps.setString(index++, checkout.getComment());
		setDate(ps, index++, checkout.getDate());
		return index;
	}

	private int loadCheckOut(final ResultSet rs, final CheckOut checkout) throws SQLException {
		int index = 1;
		checkout.setId(rs.getInt(index++));
		checkout.setRoleId(rs.getInt(index++));
		checkout.setItemId(rs.getInt(index++));
		checkout.setClientId(rs.getInt(index++));
		checkout.setSupplierId(rs.getInt(index++));
		checkout.setLocationId(rs.getInt(index++));
		checkout.setQuantity(rs.getInt(index++));
		checkout.setComment(rs.getString(index++));
		checkout.setDate(rs.getDate(index++));
		return index;
	}

	// Inventory
	public List<CheckIn> getInventory(final int companyId) throws DAOException {
		try {
			final String fields = INVENTORY_COMMON_FIELDS_HEADER.replace("`, `", "`, inv.`");
			final PreparedStatement ps = prepareStatement("SELECT inv."
					+ fields
					+ ", im.`categoryId`, SUM(CASE WHEN (inv.`roleId` = 1) THEN inv.`quantity` WHEN (inv.`roleId` = 2) THEN -(inv.`quantity`) END) AS qty_sum, MAX(inv.`date`) AS lastupdate,"
					+ " cl.`name`, cl.`nameJA`, l.`name`, l.`nameJA`, im.`name`, im.`nameJA`, ca.`name`, ca.`nameJA` FROM `Inventory` inv"
					+ " LEFT JOIN `Client` cl ON inv.`clientId` = cl.`id`"
					+ " LEFT JOIN `Location` l ON inv.`locationId` = l.`id`"
					+ " LEFT JOIN `ItemMaster` im ON inv.`itemId` = im.`id`"
					+ " LEFT JOIN `Category` ca ON im.`categoryId` = ca.`id`"
					+ " WHERE inv.`companyId` = ? GROUP BY cl.`name`, cl.`nameJA`, im.name, im.nameJA, l.name, l.nameJA ORDER BY lastupdate DESC, cl.`name`, cl.`nameJA`, l.name, l.nameJA ASC");
			ps.setInt(1, companyId);

			final ResultSet rs = ps.executeQuery();
			final List<CheckIn> list = newArrayList();
			while (rs.next()) {
				CheckIn checkin = new CheckIn();
				int index = loadInventory(rs, checkin);
				checkin.setClientName(rs.getString(index++));
				checkin.setClientNameJA(rs.getString(index++));
				checkin.setLocationName(rs.getString(index++));
				checkin.setLocationNameJA(rs.getString(index++));
				checkin.setItemName(rs.getString(index++));
				checkin.setItemNameJA(rs.getString(index++));
				checkin.setCategoryName(rs.getString(index++));
				checkin.setCategoryNameJA(rs.getString(index++));
				list.add(checkin);
			}
			return list;
		} catch (SQLException e) {
			throw exception(this, "Error getting check-in list: " + companyId, e);
		} finally {
			cleanup();
		}
	}

	private int loadInventory(final ResultSet rs, final CheckIn checkin) throws SQLException {
		int index = 1;
		checkin.setId(rs.getInt(index++));
		checkin.setRoleId(rs.getInt(index++));
		checkin.setItemId(rs.getInt(index++));
		checkin.setClientId(rs.getInt(index++));
		checkin.setCategoryId(rs.getInt(index++));
		checkin.setLocationId(rs.getInt(index++));
		checkin.setQuantity(rs.getInt(index++));
		checkin.setDate(rs.getDate(index++));
		return index;
	}

	// Log
	public void createLog(final Log log) throws DAOException, IOException {
		try {
			@SuppressWarnings("deprecation")
			final String logcontent = FileUtils.readFileToString(new File("/home/dai/\u30C7\u30B9\u30AF\u30C8\u30C3\u30D7/apache-tomcat-7.0.69/logs/localhost_access_log.log"));
			final String[] logdata = logcontent.split("\u0020", -1);
			final PreparedStatement ps = prepareStatement("INSERT INTO `Log` ("
					 + LOG_COMMON_FIELDS_FOOTER
					 + ") VALUES (?, ?, ?)");
			int index = 1;
			for(int i = 0; i > logdata.length; i += 7){
				if(index > 3) index = 1;
				ps.setString(index++, logdata[i + 1]);
				ps.setString(index++, logdata[i + 2]);
				ps.setString(index++, logdata[i]);
			}

			checkInsert(ps, "Error inserting log: " + log.getUserName());
		} catch (SQLException e) {
			throw exception(this, "Error inserting log: " + log.getUserName(), e);
		} finally {
			cleanup();
		}
	}
	
	public List<Log> getLogList() throws DAOException {
		//final Logger logger = Logger.getLogger(AdminDAO.class);
		//final String ip = request.getRemoteAddr();
		try {
			final String fields = LOG_COMMON_FIELDS.replace("`, `", "`, log.`");
			final PreparedStatement ps = prepareStatement("SELECT log."
					+ fields
					+ ", co.`name`, co.`nameJA` FROM `Log` log"
					+ " LEFT JOIN `Company` co ON log.`companyId` = co.`id`"
					+ " GROUP BY co.`name`, co.`nameJA` ORDER BY dateTime DESC");

			final ResultSet rs = ps.executeQuery();
			final List<Log> list = newArrayList();
			while (rs.next()) {
				Log log = new Log();
				int index = loadLog(rs, log);
				log.setCompanyName(rs.getString(index++));
				log.setCompanyNameJA(rs.getString(index++));
				list.add(log);
				//if(isBlank(ip))logger.error("didn't inspect remote IP address: " + ip);
				//else logger.info("remote IP address: " + ip);
			}
			return list;
		} catch (SQLException e) {
			throw exception(this, "Error getting log list: ", e);
		} finally {
			cleanup();
		}
	}

	/*private int populateLog(final PreparedStatement ps, final Log log) throws SQLException {
		int index = 1;
		ps.setString(index++, log.getUserName());
		ps.setString(index++, log.getDateTime());
		ps.setString(index++, log.getIpAddress());
		ps.setString(index++, log.getTableName());
		ps.setString(index++, log.getDetails());
		return index;
	}*/

	private int loadLog(final ResultSet rs, final Log log) throws SQLException {
		int index = 1;
		log.setId(rs.getInt(index++));
		log.setCompanyId(rs.getInt(index++));
		log.setUserName(rs.getString(index++));
		log.setDateTime(rs.getString(index++));
		log.setIpAddress(rs.getString(index++));
		log.setTableName(rs.getString(index++));
		log.setDetails(rs.getString(index++));
		return index;
	}

}
