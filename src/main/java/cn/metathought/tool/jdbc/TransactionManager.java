package cn.metathought.tool.jdbc;

import cn.metathought.tool.jdbc.pool.C3P0Util;
import cn.metathought.tool.jdbc.pool.DBCPUtil;
import java.sql.Connection;
import java.sql.SQLException;

public class TransactionManager {
	public static ThreadLocal<Connection> tl = new ThreadLocal<Connection>();

	/**
	 * 获取数据库连接
	 * 
	 * @param type
	 *            数据库连接方式
	 *            <p>
	 *            0:jdbc,1:c3p0,2:dbcp
	 * 
	 * @return
	 */
	public static Connection getConnection(int type) {
		Connection conn = tl.get();
		if (conn == null) {
			switch (type) {
			case 0:
				conn = JDBCUtil.getConnection();
				break;
			case 1:
				conn = C3P0Util.getConnection();
				break;
			case 2:
				conn = DBCPUtil.getConnection();
				break;
			}

			tl.set(conn);
		}
		return conn;
	}

	public static void startTransaction(Connection conn) {
		try {
			conn.setAutoCommit(false);
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}

	public static void commit(Connection conn) {
		try {
			conn.commit();
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}

	public static void rollback(Connection conn) {
		try {
			conn.rollback();
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}

	public static void release(Connection conn) {
		try {
			conn.close();
			tl.remove();
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}
}
