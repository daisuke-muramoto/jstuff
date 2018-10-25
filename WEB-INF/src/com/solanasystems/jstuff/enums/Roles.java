package com.solanasystems.jstuff.enums;

public class Roles {

	public static final String ROLE_ADMIN = Role.ADMIN.getName();

	public static final String ROLE_MANAGER = Role.MANAGER.getName();

	public static final String ROLE_USER = Role.USER.getName();

	public static final String[] ROLES_ALL = { ROLE_ADMIN, ROLE_MANAGER, ROLE_USER };

	public enum Role {

		ADMIN, MANAGER, USER;

		public String getName() {
			return super.name().toLowerCase();
		}

	}

}
