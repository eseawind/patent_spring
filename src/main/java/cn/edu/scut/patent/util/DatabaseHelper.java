package cn.edu.scut.patent.util;

import java.io.IOException;
import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
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
	 * 判断数据库是否存在
	 * 
	 * @param db_name
	 * @return
	 */
	private static Boolean isDatabaseExisted(String db_name) {
		try {
			Connection con = getConnection();
			Statement sta = con.createStatement();
			String sql = "SELECT SCHEMA_NAME FROM INFORMATION_SCHEMA.SCHEMATA WHERE SCHEMA_NAME = '"
					+ db_name + "' ;";
			ResultSet rs = sta.executeQuery(sql);
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
		}
	}

	/**
	 * 判断数据表是否存在
	 * 
	 * @param table_name
	 * @return
	 */
	private static Boolean isTableExisted(String table_name) {
		try {
			Connection con = getConnection();
			Statement sta = con.createStatement();
			String sql = "SELECT TABLE_NAME FROM INFORMATION_SCHEMA.TABLES WHERE TABLE_NAME = '"
					+ table_name + "' ;";
			ResultSet rs = sta.executeQuery(sql);
			if (rs.next()) {
				System.out.println("数据表" + table_name + "已经存在");
				return true;
			} else {
				System.out.println("数据表" + table_name + "不存在");
				return false;
			}
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("isTableExisted Error !");
			return false;
		}
	}

	/**
	 * 判断PatentNumber是否存在于表CLASSIFICATION当中
	 * 
	 * @param PTT_NUM
	 * @return
	 */
	public static Boolean isPatentNumberExisted(String PTT_NUM) {
		try {
			Connection con = getConnection();
			Statement sta = con.createStatement();
			String sql = "SELECT PTT_NUM FROM patentdb.CLASSIFICATION WHERE PTT_NUM = '"
					+ PTT_NUM + "' ;";
			ResultSet rs = sta.executeQuery(sql);
			if (rs.next()) {
				System.out.println("专利" + PTT_NUM + "已经分类！");
				return true;
			} else {
				System.out.println("专利" + PTT_NUM + "还没有分类！");
				return false;
			}
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("isPatentNumberExisted Error !");
			return false;
		}
	}

	/**
	 * 创建数据库patentdb
	 * 
	 * @param
	 * @return Boolean
	 */
	private static Boolean createDatabase() {
		try {
			Connection con = getConnection();
			String sql_create_database = "create database patentdb;";
			System.out.println(sql_create_database);
			PreparedStatement ps = con.prepareStatement(sql_create_database);
			ps.executeUpdate();
			ps.close();
			con.close();
			System.out.println("创建数据库patentdb成功");
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("创建数据库patentdb失败");
			return false;
		}
	}

	/**
	 * 创建PATENTS和CLASSIFICATION数据表
	 * 
	 * @param
	 * @return Boolean
	 */
	private static Boolean createTables() {
		try {
			Connection con = getConnection();
			String sql_create_table_patents = "CREATE TABLE PATENTS ("
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
					+ ", CLASS_NUM_G06Q VARCHAR(200)" + ", PTT_TYPE VARCHAR(4)"
					+ ", FILE_NAME VARCHAR(200) NOT NULL)" + " ENGINE = InnoDB"
					+ ";";

			String sql_create_table_triz = "CREATE TABLE TRIZ ("
					+ "TRIZ_NUM INT" + ", TRIZ_TEXT VARCHAR(200) NOT NULL"
					+ ", PRIMARY KEY(TRIZ_NUM))" + " ENGINE = InnoDB" + ";";

			String sql_create_table_classification = "CREATE TABLE CLASSIFICATION ("
					+ "PTT_NUM VARCHAR(20)"
					+ ", TRIZ_NUM INT"
					+ ", PRIMARY KEY(PTT_NUM, TRIZ_NUM)"
					+ ", FOREIGN KEY (PTT_NUM) REFERENCES PATENTS(PTT_NUM) ON DELETE RESTRICT ON UPDATE RESTRICT"
					+ ", FOREIGN KEY (TRIZ_NUM) REFERENCES TRIZ(TRIZ_NUM) ON DELETE RESTRICT ON UPDATE RESTRICT)"
					+ " ENGINE = InnoDB" + ";";

			String sql_insert_into_triz = "INSERT INTO patentdb.TRIZ (TRIZ_NUM, TRIZ_TEXT) VALUES "
					+ "('"
					+ "1"
					+ "','"
					+ "分割原则"
					+ "')"
					+ ", "
					+ "('"
					+ "2"
					+ "','"
					+ "拆出原则"
					+ "')"
					+ ", "
					+ "('"
					+ "3"
					+ "','"
					+ "局部性质原则"
					+ "')"
					+ ", "
					+ "('"
					+ "4"
					+ "','"
					+ "不对称原则"
					+ "')"
					+ ", "
					+ "('"
					+ "5"
					+ "','"
					+ "组合原则"
					+ "')"
					+ ", "
					+ "('"
					+ "6"
					+ "','"
					+ "多功能原则"
					+ "')"
					+ ", "
					+ "('"
					+ "7"
					+ "','"
					+ "玛特廖什卡原则"
					+ "')"
					+ ", "
					+ "('"
					+ "8"
					+ "','"
					+ "重量补偿原则"
					+ "')"
					+ ", "
					+ "('"
					+ "9"
					+ "','"
					+ "预先反作用原则"
					+ "')"
					+ ", "
					+ "('"
					+ "10"
					+ "','"
					+ "预先作用原则"
					+ "')"
					+ ", "
					+ "('"
					+ "11"
					+ "','"
					+ "予先放枕头原则"
					+ "')"
					+ ", "
					+ "('"
					+ "12"
					+ "','"
					+ "等势原则"
					+ "')"
					+ ", "
					+ "('"
					+ "13"
					+ "','"
					+ "相反原则"
					+ "')"
					+ ", "
					+ "('"
					+ "14"
					+ "','"
					+ "球形原则"
					+ "')"
					+ ", "
					+ "('"
					+ "15"
					+ "','"
					+ "动态原则"
					+ "')"
					+ ", "
					+ "('"
					+ "16"
					+ "','"
					+ "局部作用或过量作用原则"
					+ "')"
					+ ", "
					+ "('"
					+ "17"
					+ "','"
					+ "向另一维度过渡的原则"
					+ "')"
					+ ", "
					+ "('"
					+ "18"
					+ "','"
					+ "机械振动原则"
					+ "')"
					+ ", "
					+ "('"
					+ "19"
					+ "','"
					+ "周期作用原则"
					+ "')"
					+ ", "
					+ "('"
					+ "20"
					+ "','"
					+ "连续有益作用原则"
					+ "')"
					+ ", "
					+ "('"
					+ "21"
					+ "','"
					+ "跃过原则"
					+ "')"
					+ ", "
					+ "('"
					+ "22"
					+ "','"
					+ "变害为利原则"
					+ "')"
					+ ", "
					+ "('"
					+ "23"
					+ "','"
					+ "反向联系原则"
					+ "')"
					+ ", "
					+ "('"
					+ "24"
					+ "','"
					+ "中介原则"
					+ "')"
					+ ", "
					+ "('"
					+ "25"
					+ "','"
					+ "自我服务原则"
					+ "')"
					+ ", "
					+ "('"
					+ "26"
					+ "','"
					+ "复制原则"
					+ "')"
					+ ", "
					+ "('"
					+ "27"
					+ "','"
					+ "用廉价的不持久性代替昂贵的持久性原则"
					+ "')"
					+ ", "
					+ "('"
					+ "28"
					+ "','"
					+ "代替力学原理原则"
					+ "')"
					+ ", "
					+ "('"
					+ "29"
					+ "','"
					+ "利用气动和液：压结构的原则"
					+ "')"
					+ ", "
					+ "('"
					+ "30"
					+ "','"
					+ "利用软壳和薄膜原则"
					+ "')"
					+ ", "
					+ "('"
					+ "31"
					+ "','"
					+ "利用多孔材料原则"
					+ "')"
					+ ", "
					+ "('"
					+ "32"
					+ "','"
					+ "改变颜色原则"
					+ "')"
					+ ", "
					+ "('"
					+ "33"
					+ "','"
					+ "一致原则"
					+ "')"
					+ ", "
					+ "('"
					+ "34"
					+ "','"
					+ "部分剔除和再生原则"
					+ "')"
					+ ", "
					+ "('"
					+ "35"
					+ "','"
					+ "改变物体聚合态原则"
					+ "')"
					+ ", "
					+ "('"
					+ "36"
					+ "','"
					+ "相变原则"
					+ "')"
					+ ", "
					+ "('"
					+ "37"
					+ "','"
					+ "利用热膨胀原则"
					+ "')"
					+ ", "
					+ "('"
					+ "38"
					+ "','"
					+ "利用强氧化剂原则"
					+ "')"
					+ ", "
					+ "('"
					+ "39"
					+ "','"
					+ "采用惰性介质原则"
					+ "')"
					+ ", "
					+ "('"
					+ "40"
					+ "','"
					+ "利用混合材料原则" + "');";

			System.out.println(sql_create_table_patents);
			System.out.println(sql_create_table_triz);
			System.out.println(sql_create_table_classification);
			System.out.println(sql_insert_into_triz);
			PreparedStatement ps = con
					.prepareStatement(sql_create_table_patents);
			ps.executeUpdate();
			ps = con.prepareStatement(sql_create_table_triz);
			ps.executeUpdate();
			ps = con.prepareStatement(sql_create_table_classification);
			ps.executeUpdate();
			ps = con.prepareStatement(sql_insert_into_triz);
			ps.executeUpdate();
			ps.close();
			con.close();
			System.out.println("创建数据表PATENTS、TRIZ和CLASSIFICATION，输入TRIZ数据成功");
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("创建数据表PATENTS、TRIZ和CLASSIFICATION，输入TRIZ数据失败");
			return false;
		}
	}

	/**
	 * 插入Triz号码
	 * 
	 * @param PTT_NUM
	 * @param TRIZ_NUM
	 * @return
	 */
	public static Boolean insertTrizNumber(String PTT_NUM, String[] TRIZ_NUM) {
		try {
			if (TRIZ_NUM.length < 1) {
				return false;
			}
			Connection con = getConnection();
			Statement sta = con.createStatement();
			String sql_insert = "INSERT INTO patentdb.CLASSIFICATION (PTT_NUM, TRIZ_NUM) VALUES ";
			for (int i = 0; i < TRIZ_NUM.length; i++) {
				sql_insert += "('" + PTT_NUM + "','" + TRIZ_NUM[i] + "')";
				if (i < TRIZ_NUM.length - 1) {
					sql_insert += ",";
				}
			}
			sql_insert += ";";
			System.out.println(sql_insert);
			sta.execute(sql_insert);
			sta.close();
			con.close();
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	/**
	 * 更新Triz号码
	 * 
	 * @param PTT_NUM
	 * @param TRIZ_NUM
	 * @return
	 */
	public static Boolean updateTrizNumber(String PTT_NUM, String[] TRIZ_NUM) {
		try {
			Connection con = getConnection();
			Statement sta = con.createStatement();
			String sql_delete = "DELETE FROM patentdb.CLASSIFICATION WHERE PTT_NUM = '"
					+ PTT_NUM + "';";
			System.out.println(sql_delete);
			sta.execute(sql_delete);
			sta.close();
			con.close();
			if (insertTrizNumber(PTT_NUM, TRIZ_NUM)) {
				return true;
			} else {
				return false;
			}
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	/**
	 * 按照Map<String, String>的格式写入数据库
	 * 
	 * @param map
	 * @throws IOException
	 */
	public static void saveToDatabase(PatentDao patentdao) throws IOException {
		if (patentdao != null) {
			if (writeToDb(patentdao)) {
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

		if (!isDatabaseExisted("patentdb")) {
			if (!createDatabase()) {
				return false;
			}
		}
		if (!isTableExisted("PATENTS")) {
			if (!createTables()) {
				return false;
			}
		}
		try {
			Connection con = getConnection();
			Statement sta = con.createStatement();

			String sql_insert_into_patents = "INSERT INTO patentdb.PATENTS (PTT_NUM,APPLY_NUM,APPLY_DATE,PTT_NAME,PTT_DATE,PTT_MAIN_CLASS_NUM,PTT_CLASS_NUM,PROPOSER,"
					+ "PROPOSER_ADDRESS,INVENTOR,PTT_AGENCY_ORG,PTT_AGENCY_PERSON,PTT_ABSTRACT,CLASS_NUM_G06Q,INTERNATIONAL_APPLY,INTERNATIONAL_PUBLICATION,INTO_DATE,PTT_TYPE,FILE_NAME) VALUES ('"
					+ StringHelper.replaceSpecialCharacters(pttDao.getPttNum())
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
					+ pttDao.getIntoDate()
					+ "','"
					+ pttDao.getPttType()
					+ "','" + pttDao.getFileName() + "')";

			String sql_insert_into_classification = "INSERT INTO patentdb.CLASSIFICATION (PTT_NUM, TRIZ_NUM) VALUES ('"
					+ StringHelper.replaceSpecialCharacters(pttDao.getPttNum())
					+ "','" + "23" + "')";

			System.out.println(sql_insert_into_patents);
			System.out.println(sql_insert_into_classification);
			sta.execute(sql_insert_into_patents);
			sta.execute(sql_insert_into_classification);

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
