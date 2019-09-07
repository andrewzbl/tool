package cn.metathought.tool.jdbc;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Properties;
import lombok.extern.slf4j.Slf4j;

/**
 * jdbc工具类
 * <p>
 * 注：创建名为dbconfig.properties的jdbc配置文件，名称为driverClassName、url、username、password
 * 
 * @author zbl
 * @date 2016.05.20
 *
 */
@Slf4j
public class JDBCUtil {
	private static String dbDriver;
	private static String dbUrl;
	private static String dbUser;
	private static String dbPwd;

	static {
		try {
			Properties props = new Properties();
			props.load(JDBCUtil.class.getClassLoader().getResourceAsStream(
					"dbconfig.properties"));
			dbDriver = props.getProperty("driverClassName");
			dbUrl = props.getProperty("url");
			dbUser = props.getProperty("username");
			dbPwd = props.getProperty("password");
		} catch (Exception e) {
			throw new ExceptionInInitializerError("init jdbc connection error");
		}
	}

	public static Connection getConnection() {
		try {
			Class.forName(dbDriver);
			Connection conn = DriverManager.getConnection(dbUrl, dbUser, dbPwd);

			return conn;
		} catch (Exception e) {
			log.error("get jdbc connection error", e);
			return null;
		}
	}

	public void closeConnection(Connection conn) {
		if (conn != null) {
			try {
				conn.close();
			} catch (SQLException e) {
				log.error("close jdbc connection error", e);
			}
		}
	}

	public void closePreparedStatement(PreparedStatement ps) {
		if (ps != null) {
			try {
				ps.close();
			} catch (SQLException e) {
				log.error("close statement error", e);
			}
		}
	}

	public void closeResultSet(ResultSet rs) {
		if (rs != null) {
			try {
				rs.close();
			} catch (SQLException e) {
				log.error("close resultset error", e);
			}
		}
	}
}
