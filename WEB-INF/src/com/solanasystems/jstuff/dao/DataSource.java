package com.solanasystems.jstuff.dao;

import java.sql.Connection;
import java.sql.SQLException;

import javax.naming.InitialContext;
import javax.naming.NamingException;

import com.javaranch.unittest.helper.sql.pool.JNDIUnitTestHelper;
import com.solanasystems.utils.common.Log4J;

public class DataSource {

	private static javax.sql.DataSource ds = null;

	public static synchronized void init() {
		initDS();
	}

	public static Connection getConnection() throws SQLException {
		if (ds == null)
			init();
		return ds.getConnection();
	}

	public static void closeDataSource() throws Exception {
	}

	private static void initDS() {
		try {
			String name = "java:/comp/env/jdbc/jstuffDB";
			if (!JNDIUnitTestHelper.notInitialized()) {
				final String jndiName = JNDIUnitTestHelper.getJndiName();
				if (jndiName != null) {
					Log4J.warn(DataSource.class, "using test datasource : " + jndiName);
					name = jndiName;
				}
			}
			ds = (javax.sql.DataSource) new InitialContext().lookup(name);
		} catch (NamingException e) {
			Log4J.error(DataSource.class, "getting jetty datasource", e);
		}
	}

}
