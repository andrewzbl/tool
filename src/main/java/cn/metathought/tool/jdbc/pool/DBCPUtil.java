package cn.metathought.tool.jdbc.pool;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

import javax.sql.DataSource;
import org.apache.commons.dbcp2.BasicDataSourceFactory;


/**
 * dbcp连接池
 * <p>
 * 注：需要在classpath下创建名为dbconfig.properties的dbcp配置文件
 * 
 * @author zbl
 * @date 2016.06.04
 *
 */
public class DBCPUtil {
	private static DataSource dataSource;

	static {
		try {
			Properties props = new Properties();
			props.load(DBCPUtil.class.getClassLoader().getResourceAsStream(
					"dbconfig.properties"));
			dataSource = BasicDataSourceFactory.createDataSource(props);
		} catch (Exception e) {
			throw new ExceptionInInitializerError("init dbcp connection error");
		}
	}

	public static Connection getConnection() {
		try {
			return dataSource.getConnection();
		} catch (SQLException e) {
			throw new RuntimeException("get dbcp connection error");
		}
	}

	public static void release(ResultSet rs, Statement stmt, Connection conn) {
		if (rs != null) {
			try {
				rs.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
			rs = null;
		}
		if (stmt != null) {
			try {
				stmt.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
			stmt = null;
		}
		if (conn != null) {
			try {
				conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
			conn = null;
		}
	}
}
