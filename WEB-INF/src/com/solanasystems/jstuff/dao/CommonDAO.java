package com.solanasystems.jstuff.dao;

import java.sql.Connection;
import java.sql.SQLException;

import com.solanasystems.jstuff.dao.query.CommonQuery;
import com.solanasystems.utils.dao.DAOUtils;

public class CommonDAO extends DAOUtils implements CommonQuery {

	protected Connection getConnection() throws SQLException {
		return DataSource.getConnection();
	}

}
