package cn.edu.scut.patent.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import cn.edu.scut.patent.service.TrizService;
import cn.edu.scut.patent.util.Constants;

public class DatabaseHelper {

	/**
	 * 连接数据库
	 * 
	 * @return
	 * @throws Exception
	 */
	public static Connection getConnection() throws Exception {
		Class.forName("com.mysql.jdbc.Driver");
		return DriverManager.getConnection(Constants.MYSQL_URL,
				Constants.MYSQL_ACCOUNT, Constants.MYSQL_PASSWORD);
	}

	/**
	 * 检查MySQL数据库是否完整
	 * 
	 * @return
	 */
	public static Boolean checkMySQL() {
		if (!isDatabaseExisted("patentdb")) {
			if (!createDatabase()) {
				return false;
			}
		}
		if (!createTableTRIZ()) {
			return false;
		}
		return true;
	}

	/**
	 * 判断数据库是否存在
	 * 
	 * @param db_name
	 * @return
	 */
	public static Boolean isDatabaseExisted(String db_name) {
		Connection con = null;
		Statement sta = null;
		ResultSet rs = null;
		try {
			con = getConnection();
			sta = con.createStatement();
			String sql = "SELECT SCHEMA_NAME FROM INFORMATION_SCHEMA.SCHEMATA WHERE SCHEMA_NAME = '"
					+ db_name + "' ;";
			rs = sta.executeQuery(sql);
			if (rs.next()) {
				System.out.println("数据库" + db_name + "已经存在");
				return true;
			} else {
				System.out.println("数据库" + db_name + "不存在");
				return false;
			}
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("isDatabaseExisted Error !");
			return false;
		} finally {
			try {
				if (rs != null) {
					rs.close();
				}
				if (sta != null) {
					sta.close();
				}
				if (con != null) {
					con.close();
				}
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	/**
	 * 创建数据库patentdb
	 * 
	 * @param
	 * @return Boolean
	 */
	public static Boolean createDatabase() {
		Connection con = null;
		PreparedStatement ps = null;
		try {
			con = getConnection();
			String sql_create_database = "create database patentdb;";
			System.out.println(sql_create_database);
			ps = con.prepareStatement(sql_create_database);
			ps.executeUpdate();
			System.out.println("创建数据库patentdb成功");
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("创建数据库patentdb失败");
			return false;
		} finally {
			try {
				if (ps != null) {
					ps.close();
				}
				if (con != null) {
					con.close();
				}
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	/**
	 * 创建TRIZ数据表
	 * 
	 * @param
	 * @return Boolean
	 */
	public static Boolean createTableTRIZ() {
		new TrizService().fillTriz();
		return true;
	}

	/**
	 * 得到resultSet的行数
	 * 
	 * @param rs
	 * @return
	 */
	public static int getSize(ResultSet rs) {
		try {
			rs.last();
			return rs.getRow();
		} catch (SQLException e) {
			e.printStackTrace();
			return -1;
		}
	}

	public static void main(String[] args) {

	}
}
