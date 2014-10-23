package socket.io.common;
/**
 * @author QingWang
 * @date 2014-10-1 20:48:22
 */
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.apache.log4j.Logger;

import socket.io.config.ResourceManager;

public class MySQL {

	private static Logger logger = Logger.getLogger(MySQL.class);

	private static String host = ResourceManager.getInstance().getMySQLHost();
	private static String port = ResourceManager.getInstance().getMySQLPort();
	private static String dbName = ResourceManager.getInstance().getMySQLDB();
	private static String username = ResourceManager.getInstance()
			.getMySQLUserName();
	private static String password = ResourceManager.getInstance()
			.getMySQLPassword();
	private static String drivername = "com.mysql.jdbc.Driver";
	private static String url = "jdbc:mysql://" + host + ":" + port + "/" + dbName;
	
	private static Connection conn = null;
	private static Statement stmt = null;
	private static ResultSet rs = null;

	// create connection 
	public static void connect() {
		try {
			Class.forName(drivername);
			conn = DriverManager.getConnection(url, username, password);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			logger.error(e.getMessage());
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			logger.error(e.getMessage());
		}
	}

	// query£ºreturn a collection of record
	public static void query(String sql) {
		if (null != conn) {
			try {
				stmt = (Statement) conn.prepareStatement(sql);
				rs = stmt.executeQuery(sql);
				if (null != rs) {
					rs.next();
				} else {
					logger.info("No Result£¡");
				}
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				logger.error(e.getMessage());
			}
		} else {
			logger.info("Connect Failed£¡");
		}
	}

	// get column field value
	public static Object getObjectValue(String columnName) {
		if (null != rs) {
			try {
				Object columnValue = rs.getObject(columnName);
				logger.info(url+" Query Result£º"+columnName+" = "+columnValue);
				return columnValue;
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				logger.error(e.getMessage());
			}
		}
		return null;
	}
	
	// close current connection
	public static void close(){
		try {
			if(null != rs){
				rs.close();
			}
			if(null != stmt){
				stmt.close();
			}
			if(null != conn){
				conn.close();
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			logger.error(e.getMessage());
		}
	}
}
