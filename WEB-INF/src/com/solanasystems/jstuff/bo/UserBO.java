package com.solanasystems.jstuff.bo;

import static com.solanasystems.jstuff.common.ErrorConstants.ERRORS_UNIQUE_NAME;
import static com.solanasystems.jstuff.dao.query.CommonQuery.EXCLUDE_ADMIN_USER_QUERY;
import static com.solanasystems.jstuff.dao.query.CommonQuery.TABLE_JOIN_USER_COMPANY_IDS;

import java.util.List;

import com.solanasystems.jstuff.dao.DAOFactory;
import com.solanasystems.jstuff.dao.UserDAO;
import com.solanasystems.jstuff.om.Role;
import com.solanasystems.jstuff.om.User;
import com.solanasystems.jstuff.util.QueryUtils;
import com.solanasystems.utils.bo.BOUtils;
import com.solanasystems.utils.exception.ApplicationException;
import com.solanasystems.utils.exception.DAOException;

public class UserBO extends BOUtils {

	private final UserDAO dao;

	private UserBO(final UserDAO dao) {
		this.dao = dao;
	}

	public static UserBO getInstance() {
		return new UserBO(DAOFactory.getUserDAO());
	}

	public void createUpdateUser(final User user) throws ApplicationException {
		try {
			if (user.getId() > 0)
				dao.update(user);
			else
				dao.create(user);
			dao.updateUserCompanyIds(user);
		} catch (DAOException e) {
			throw exception(this, "createUpdateUser()", ERRORS_UNIQUE_NAME, e);
		}
	}

	public User readUser(final int userId) throws ApplicationException {
		return read(" u.`id` = " + userId);
	}

	public User readUser(final String userName) throws ApplicationException {
		return read(" u.`name` = ".concat(string(userName)));
	}

	public User read(final String query) throws ApplicationException {
		try {
			final User user = dao.read(query);
			dao.setUserCompanyIds(user);
			return user;
		} catch (DAOException e) {
			throw exception(this, "read()", e);
		}
	}

	public void deleteUser(final int userId) throws ApplicationException {
		try {
			dao.delete(userId);
		} catch (DAOException e) {
			throw exception(this, "deleteUser()", e);
		}
	}

	public boolean canAccessUser(final int userId, final int companyId) throws ApplicationException {
		try {
			return dao.canAccessUser(userId, companyId);
		} catch (DAOException e) {
			throw exception(this, "canAccessUser()", e);
		}
	}

	public List<User> getUserList(final int companyId) throws ApplicationException {
		try {
			String query = "";
			if (companyId > 0) {
				final StringBuilder sb = sb();
				sb.append(TABLE_JOIN_USER_COMPANY_IDS);
				sb.append(" WHERE").append(EXCLUDE_ADMIN_USER_QUERY).append(AND).append("uc.`companyId` = ").append(companyId);
				query = sb.toString();
			}
			return dao.getUserList(query);
		} catch (DAOException e) {
			throw exception(this, "getUserList()", e);
		}
	}

	public List<Role> getRoleList(final boolean isAdmin) throws ApplicationException {
		try {
			return dao.getRoleList(QueryUtils.excludeAdminQuery(isAdmin));
		} catch (DAOException e) {
			throw exception(this, "getRoleList()", e);
		}
	}

	public void updateLastUsedCompanyId(final int userId, final int companyId) throws ApplicationException {
		try {
			dao.updateLastUsedCompanyId(userId, companyId);
		} catch (DAOException e) {
			throw exception(this, "updateLastUsedCompanyId()", e);
		}
	}

}
