package com.solanasystems.jstuff.dao;

public class DAOFactory {

	public static UserDAO getUserDAO() {
		return new UserDAO();
	}

	public static AdminDAO getAdminDAO() {
		return new AdminDAO();
	}

}
