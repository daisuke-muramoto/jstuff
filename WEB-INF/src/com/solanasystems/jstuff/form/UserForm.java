package com.solanasystems.jstuff.form;

import static com.solanasystems.jstuff.common.Constants.PASSWORD_MIN_LENGTH;
import static com.solanasystems.jstuff.common.Constants.PASSWORD_PATTERN;

import javax.servlet.http.HttpServletRequest;

import com.solanasystems.jstuff.util.UserUtils;

public class UserForm extends CustomValidatorForm {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private int id;

	private String name;

	private String pass;

	private String pass2;

	private int roleId;

	private int[] companyIds;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPass() {
		return pass;
	}

	public void setPass(String pass) {
		this.pass = pass;
	}

	public String getPass2() {
		return pass2;
	}

	public void setPass2(String pass2) {
		this.pass2 = pass2;
	}

	public int getRoleId() {
		return roleId;
	}

	public void setRoleId(int roleId) {
		this.roleId = roleId;
	}

	public int[] getCompanyIds() {
		return companyIds;
	}

	public void setCompanyIds(int[] companyIds) {
		this.companyIds = companyIds;
	}

	public boolean isContainsCompanyId(int companyId) {
		return contains(companyIds, companyId);
	}

	protected void setActionErrors(final HttpServletRequest request) {
		if (isBlank(name))
			addError(ERRORS_REQUIRED_NAME);
		if (id == 0) {
			if (isBlank(pass))
				addError(ERRORS_REQUIRED_PASS);
			if (isBlank(pass2))
				addError(ERRORS_REQUIRED_PASS2);
		}
		if (isNotBlank(pass)) {
			if (!pass.equals(pass2))
				addError(ERRORS_PASS_MISMATCH);
			else if (!pass.matches(PASSWORD_PATTERN))
				addError(ERRORS_PASS_PATTERN, PASSWORD_MIN_LENGTH);
		}
		if (roleId == 0)
			addError(ERRORS_REQUIRED_ROLE);
		if (UserUtils.isNotAdminUser(roleId) && isEmpty(companyIds))
			addError(ERRORS_REQUIRED_COMPANY);
	}

}
