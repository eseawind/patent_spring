package cn.edu.scut.patent.util;

import java.io.IOException;
import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Map;

import cn.edu.scut.patent.model.PatentDao;

public class DatabaseHelper {

	/**
	 * 连接数据库
	 * 
	 * @return
	 * @throws Exception
	 */
	private static Connection getConnection() throws Exception {
		Class.forName("com.mysql.jdbc.Driver");
		return DriverManager.getConnection("jdbc:mysql://"
				+ Constants.MYSQL_URL, Constants.MYSQL_ACCOUNT,
				Constants.MYSQL_PASSWORD);
	}

	/**
	 * 判断数据表是否存在
	 * 
	 * @param
	 * @return Boolean
	 */
	private static Boolean isExisted(String table_name) {
		try {
			Connection con = getConnection();
			Statement sta = con.createStatement();
			String sql = "SELECT table_name FROM information_schema.TABLES WHERE table_name = '"
					+ table_name + "' ;";
			ResultSet rs = sta.executeQuery(sql);
			if (rs.next()) {
				System.out.println("数据表PATENTS已经存在");
				return true;
			} else {
				System.out.println("数据表PATENTS不存在");
				return false;
			}
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("isExisted Error !");
			return false;
		}
	}

	/**
	 * 创建PATENTS数据表
	 * 
	 * @param
	 * @return Boolean
	 */
	private static Boolean createTablePATENTS() {
		try {
			Connection con = getConnection();
			String sql = "CREATE TABLE PATENTS ("
					+ "APPLY_NUM VARCHAR(20) NOT NULL"
					+ ", APPLY_DATE DATE NOT NULL"
					+ ", PTT_NAME VARCHAR(200) NOT NULL"
					+ ", PTT_NUM VARCHAR(20) NOT NULL PRIMARY KEY"
					+ ", PTT_DATE DATE NOT NULL"
					+ ", PTT_MAIN_CLASS_NUM VARCHAR(20) NOT NULL"
					+ ", PTT_CLASS_NUM VARCHAR(1000) NOT NULL"
					+ ", PROPOSER VARCHAR(300) NOT NULL"
					+ ", PROPOSER_ADDRESS VARCHAR(500) NOT NULL"
					+ ", INVENTOR VARCHAR(200) NOT NULL"
					+ ", INTERNATIONAL_APPLY VARCHAR(200)"
					+ ", INTERNATIONAL_PUBLICATION VARCHAR(50)"
					+ ", INTO_DATE DATE"
					+ ", PTT_AGENCY_ORG VARCHAR(500) NOT NULL"
					+ ", PTT_AGENCY_PERSON VARCHAR(200) NOT NULL"
					+ ", PTT_ABSTRACT VARCHAR(10000)"
					+ ", CLASS_NUM_G06Q VARCHAR(200)"
					+ ", PTT_TYPE VARCHAR(4)) " + ";";

			System.out.println(sql);
			PreparedStatement ps = con.prepareStatement(sql);
			ps.executeUpdate();
			ps.close();
			con.close();
			System.out.println("创建数据表PATENTS成功");
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("不能创建数据表PATENTS");
			return false;
		}
	}

	/**
	 * 按照Map<String, String>的格式写入数据库
	 * 
	 * @param map
	 * @throws IOException
	 */
	public static void saveToDatabase(Map<String, String> map)
			throws IOException {
		if (map != null) {
			PatentDao pttDao = new PatentDao();
			pttDao.setPttName(map.get("pttName"));
			pttDao.setApplyNum(map.get("applyNum"));
			pttDao.setApplyDate(DateHelper.stringToDate(map.get("applyDate")
					.toString()));
			pttDao.setProposer(map.get("proposer"));
			pttDao.setProposerAddress(map.get("proposerAddress"));
			pttDao.setInventor(map.get("inventor"));
			pttDao.setPttMainClassNum(map.get("pttMainClassNum"));
			pttDao.setPttClassNum(map.get("pttClassNum"));
			pttDao.setPttNum(map.get("pttNum"));
			pttDao.setPttDate(DateHelper.stringToDate(map.get("pttDate")));
			pttDao.setPttAgencyOrg(map.get("pttAgencyOrg"));
			pttDao.setPttAgencyPerson(map.get("pttAgencyPerson"));
			pttDao.setIntoDate(DateHelper.stringToDate(map.get("intoDate")));

			if (writeToDb(pttDao)) {
				System.out.println("写入数据库成功");
			} else {
				System.out.println("写入数据库失败");
			}
		}
	}

	/**
	 * 按照专利的格式写入数据库
	 * 
	 * @param pttDao
	 * @return
	 */
	private static Boolean writeToDb(PatentDao pttDao) {

		if (!isExisted("PATENTS")) {
			if (!createTablePATENTS()) {
				return false;
			}
		}
		try {
			Connection con = getConnection();
			Statement sta = con.createStatement();

			String sql = "INSERT INTO patents (PTT_NUM,APPLY_NUM,APPLY_DATE,PTT_NAME,PTT_DATE,PTT_MAIN_CLASS_NUM,PTT_CLASS_NUM,PROPOSER,"
					+ "PROPOSER_ADDRESS,INVENTOR,PTT_AGENCY_ORG,PTT_AGENCY_PERSON,PTT_ABSTRACT,CLASS_NUM_G06Q,INTERNATIONAL_APPLY,INTERNATIONAL_PUBLICATION,INTO_DATE,PTT_TYPE) VALUES ('"
					+ pttDao.getPttNum()
					+ "','"
					+ pttDao.getApplyNum()
					+ "','"
					+ pttDao.getApplyDate()
					+ "','"
					+ pttDao.getPttName()
					+ "','"
					+ pttDao.getPttDate()
					+ "','"
					+ pttDao.getPttMainClassNum()
					+ "','"
					+ pttDao.getPttClassNum()
					+ "','"
					+ pttDao.getProposer()
					+ "','"
					+ pttDao.getProposerAddress()
					+ "','"
					+ pttDao.getInventor()
					+ "','"
					+ pttDao.getPttAgencyOrg()
					+ "','"
					+ pttDao.getPttAgencyPerson()
					+ "','"
					+ pttDao.getPttAbstract()
					+ "','"
					+ pttDao.getClassNumG06Q()
					+ "','"
					+ pttDao.getInternationalApply()
					+ "','"
					+ pttDao.getInternationalPublication()
					+ "','"
					+ pttDao.getIntoDate() + "','" + pttDao.getPttType() + "')";

			System.out.println(sql);
			sta.execute(sql);
			sta.close();
			con.close();
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println(pttDao.getPttNum());
			return false;
		}
	}

	/**
	 * 
	 */
	public static void generateTDate() {
		Connection con;
		try {
			Class.forName("com.mysql.jdbc.Driver");
			con = getConnection();
			Statement sta = con.createStatement();
			String sql = "SELECT DISTINCT PTT_DATE FROM patents";
			ResultSet reset = sta.executeQuery(sql);

			while (reset.next()) {
				Date date = reset.getDate(1);
				String year = date.toString().substring(0, 4);
				String month = date.toString().substring(5, 7);
				String day = date.toString().substring(8, 10);

				Statement sta2 = con.createStatement();
				sta2.execute("insert into t_date (TIME_KEY,YEAR,MONTH,DATE) VALUES ('"
						+ date
						+ "','"
						+ year
						+ "','"
						+ month
						+ "','"
						+ day
						+ "')");
				sta2.close();

				System.out.println(reset.getDate(1).toString());
			}
			reset.close();
			sta.close();
			con.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
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

	/**
	 * 返回数据表的长度
	 * 
	 * @param dbName
	 * @return
	 */
	public static int getSize(String dbName) {
		int size;
		Connection con;
		try {
			con = getConnection();
			Statement sta = con.createStatement();
			ResultSet rs = sta.executeQuery("select count(*) from " + dbName);
			rs.first();
			size = rs.getInt(1);
			rs.close();
			sta.close();
			con.close();
			return size;
		} catch (Exception e) {
			e.printStackTrace();
			return -1;
		}
	}
}
