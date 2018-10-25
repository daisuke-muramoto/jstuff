package com.solanasystems.jstuff.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import com.solanasystems.jstuff.om.Role;
import com.solanasystems.jstuff.om.User;
import com.solanasystems.jstuff.util.UserUtils;
import com.solanasystems.utils.exception.DAOException;

public class UserDAO extends CommonDAO {

	private static final String ROLE_SELECT_QUERY = "SELECT `id`, `name` FROM `Role` WHERE ";

	private static final String PASS_QUERY = ", `pass` =" + PASSWORD_QUERY;

	private static final String CAN_ACCESS_USER_QUERY = " AND" + EXCLUDE_ADMIN_USER_QUERY;

	private static final String SORT_QUERY = asc("r.`name`", "u.`name`");

	public void create(final User user) throws DAOException {
		Connection conn = null;
		PreparedStatement ps = null;
		try {
			conn = getConnection();
			ps = conn.prepareStatement("INSERT INTO `User` (`id`, `name`, `pass`, `lastUsedCompanyId`) VALUES (NULL, ?," + PASSWORD_QUERY + ", ?)",
					RETURN_GENERATED_KEYS);
			int index = 1;
			ps.setString(index++, user.getName());
			ps.setString(index++, user.getPass());
			ps.setInt(index++, user.getCompanyIds()[0]);

			user.setId(checkInsertAndGetId(ps, "Error inserting user: " + user.getName()));
		} catch (SQLException e) {
			cleanup(conn, ps);
			throw exception(this, "Error inserting user: " + user.getName(), e);
		}
		try {
			ps = conn.prepareStatement("INSERT INTO `UserRole` (`userId`, `roleId`) VALUES (?, ?)");
			int index = 1;
			ps.setInt(index++, user.getId());
			ps.setInt(index++, user.getRoleId());

			ps.executeUpdate();
		} catch (SQLException e) {
			throw exception(this, "Error creating user role: " + user.getName(), e);
		} finally {
			cleanup(conn, ps);
		}
	}

	public User read(final String query) throws DAOException {
		try {
			final PreparedStatement ps = prepareStatement("SELECT u.`id`, u.`name`, u.`lastUsedCompanyId`, ur.`roleId`, c.`name`" + TABLES_USER_ROLE
					+ " LEFT JOIN `Company` c ON u.`lastUsedCompanyId` = c.`id` WHERE " + query);

			final ResultSet rs = ps.executeQuery();
			final User user = new User();
			if (rs.next()) {
				int index = load(rs, user);
				user.setCompanyName(rs.getString(index++));
			}
			return user;
		} catch (SQLException e) {
			throw exception(this, "Error reading user: " + query, e);
		} finally {
			cleanup();
		}
	}

	public void setUserCompanyIds(final User user) throws DAOException {
		try {
			final PreparedStatement ps = prepareStatement("SELECT `companyId` FROM `UserCompanyIds` WHERE `userId` = ?");
			ps.setInt(1, user.getId());

			final ResultSet rs = ps.executeQuery();
			final List<Integer> list = newArrayList();
			while (rs.next()) {
				list.add(rs.getInt(1));
			}
			user.setCompanyIds(idArrayFromList(list));
		} catch (SQLException e) {
			throw exception(this, "Error setting user company ids: " + user.getName(), e);
		} finally {
			cleanup();
		}
	}

	public void update(final User user) throws DAOException {
		Connection conn = null;
		PreparedStatement ps = null;
		try {
			final boolean isUpdatePass = isNotBlank(user.getPass());
			final String query = isUpdatePass ? PASS_QUERY : "";
			conn = getConnection();
			ps = conn.prepareStatement("UPDATE `User` SET `name` = ?" + query + " WHERE `id` = ?");
			int index = 1;
			ps.setString(index++, user.getName());
			if (isUpdatePass)
				ps.setString(index++, user.getPass());
			ps.setInt(index++, user.getId());

			ps.executeUpdate();
			ps.close();
		} catch (SQLException e) {
			cleanup(conn, ps);
			throw exception(this, "Error updating user: " + user.getName(), e);
		}
		try {
			ps = conn.prepareStatement("UPDATE `UserRole` SET `roleId` = ? WHERE `userId` = ?");
			int index = 1;
			ps.setInt(index++, user.getRoleId());
			ps.setInt(index++, user.getId());

			ps.executeUpdate();
		} catch (SQLException e) {
			throw exception(this, "Error updating user role: " + user.getName(), e);
		} finally {
			cleanup(conn, ps);
		}
	}

	public void updateUserCompanyIds(final User user) throws DAOException {
		PreparedStatement ps = null;
		try {
			final int userId = user.getId();
			if (UserUtils.isNotAdminUser(user.getRoleId())) {
				ps = prepareStatement("INSERT INTO `UserCompanyIds` (`userId`, `companyId`) VALUES (?, ?)");
				ps.addBatch("DELETE FROM `UserCompanyIds` WHERE `userId` = " + userId);

				final int[] companyIds = user.getCompanyIds();
				for (int i = 0, length = companyIds.length; i < length; i++) {
					int index = 1;
					ps.setInt(index++, userId);
					ps.setInt(index++, companyIds[i]);
					ps.addBatch();
				}
				ps.executeBatch();
			} else {
				ps = prepareStatement("DELETE FROM `UserCompanyIds` WHERE `userId` = ?");
				ps.setInt(1, userId);

				ps.executeUpdate();
			}
		} catch (SQLException e) {
			cleanup();
			throw exception(this, "Error updating user company ids: " + user.getId(), e);
		}
	}

	public void delete(final int userId) throws DAOException {
		try {
			final PreparedStatement ps = prepareStatement("DELETE u, ur" + TABLES_USER_ROLE + " WHERE u.`id` = ?");
			ps.setInt(1, userId);

			checkDelete(ps, "Error deleting user: " + userId);
		} catch (SQLException e) {
			throw exception(this, "Error deleting user: " + userId, e);
		} finally {
			cleanup();
		}
	}

	public boolean canAccessUser(final int userId, final int companyId) throws DAOException {
		try {
			final PreparedStatement ps = prepareStatement("SELECT u.`id`" + TABLES_ALL_USER_ROLE + TABLE_JOIN_USER_COMPANY_IDS
					+ " WHERE u.`id` = ? AND uc.`companyId` = ?" + CAN_ACCESS_USER_QUERY);
			int index = 1;
			ps.setInt(index++, userId);
			ps.setInt(index++, companyId);

			final ResultSet rs = ps.executeQuery();
			final boolean canAccess = rs.next();
			return canAccess;
		} catch (SQLException e) {
			throw exception(this, "Error checking user access: ", e);
		} finally {
			cleanup();
		}
	}

	public List<User> getUserList(final String query) throws DAOException {
		try {
			final PreparedStatement ps = prepareStatement("SELECT u.`id`, u.`name`, u.`lastUsedCompanyId`, r.`id`, r.`name`" + TABLES_ALL_USER_ROLE + query
					+ SORT_QUERY);

			final ResultSet rs = ps.executeQuery();
			final List<User> list = newArrayList();
			while (rs.next()) {
				User user = new User();
				int index = load(rs, user);
				user.setRoleName(rs.getString(index++));
				list.add(user);
			}
			return list;
		} catch (SQLException e) {
			throw exception(this, "Error getting user list: " + query, e);
		} finally {
			cleanup();
		}
	}

	private int load(final ResultSet rs, final User user) throws SQLException {
		int index = 1;
		user.setId(rs.getInt(index++));
		user.setName(rs.getString(index++));
		user.setCompanyId(rs.getInt(index++));
		user.setRoleId(rs.getInt(index++));
		return index;
	}

	public List<Role> getRoleList(final String query) throws DAOException {
		try {
			final PreparedStatement ps = prepareStatement(ROLE_SELECT_QUERY + query);

			final ResultSet rs = ps.executeQuery();
			final List<Role> list = newArrayList();
			while (rs.next()) {
				list.add(load(rs));
			}
			return list;
		} catch (SQLException e) {
			throw exception(this, "Error getting role list: ", e);
		} finally {
			cleanup();
		}
	}

	private Role load(final ResultSet rs) throws SQLException {
		int index = 1;
		Role role = new Role();
		role.setId(rs.getInt(index++));
		role.setName(rs.getString(index++));
		return role;
	}

	public void updateLastUsedCompanyId(final int userId, final int companyId) throws DAOException {
		try {
			final PreparedStatement ps = prepareStatement("UPDATE `User` SET `lastUsedCompanyId` = ? WHERE `id` = ?");
			int index = 1;
			ps.setInt(index++, companyId);
			ps.setInt(index++, userId);

			ps.executeUpdate();
		} catch (SQLException e) {
			throw exception(this, "Error updating last used company id: " + userId, e);
		} finally {
			cleanup();
		}
	}

}
