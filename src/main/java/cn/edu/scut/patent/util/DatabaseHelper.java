package cn.edu.scut.patent.util;

import java.io.IOException;
import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.TextField;
import ICTCLAS2014.Nlpir;
import cn.edu.scut.patent.model.PatentDao;

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
	private static Boolean checkMySQL() {
		if (!isDatabaseExisted("patentdb")) {
			if (!createDatabase()) {
				return false;
			}
		}
		if (!isTableExisted("PATENTS")) {
			if (!createTablePATENTS()) {
				return false;
			}
		}
		if (!isTableExisted("TRIZ")) {
			if (!createTableTRIZ()) {
				return false;
			}
		}
		if (!isTableExisted("CLASSIFICATION")) {
			if (!createTableCLASSIFICATION()) {
				return false;
			}
		}
		return true;
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
	public static Boolean isTableExisted(String table_name) {
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
	 * 创建PATENTS数据表
	 * 
	 * @param
	 * @return Boolean
	 */
	private static Boolean createTablePATENTS() {
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
					+ ", PTT_ABSTRACT VARCHAR(10000) NOT NULL"
					+ ", PTT_TYPE VARCHAR(4) NOT NULL"
					+ ", CLASS_NUM_G06Q VARCHAR(200) NOT NULL)"
					+ " ENGINE = InnoDB" + ";";
			System.out.println(sql_create_table_patents);
			PreparedStatement ps = con
					.prepareStatement(sql_create_table_patents);
			ps.executeUpdate();
			ps.close();
			con.close();
			System.out.println("创建数据表PATENTS数据成功");
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("创建数据表PATENTS失败");
			return false;
		}
	}

	/**
	 * 创建TRIZ数据表
	 * 
	 * @param
	 * @return Boolean
	 */
	private static Boolean createTableTRIZ() {
		try {
			Connection con = getConnection();
			String sql_create_table_triz = "CREATE TABLE TRIZ ("
					+ "TRIZ_NUM INT" + ", TRIZ_TEXT VARCHAR(200) NOT NULL"
					+ ", PRIMARY KEY(TRIZ_NUM))" + " ENGINE = InnoDB" + ";";
			String sql_insert_into_triz = "INSERT INTO patentdb.TRIZ (TRIZ_NUM, TRIZ_TEXT) VALUES "
					+ "('"
					+ "1"
					+ "','"
					+ "分割原理"
					+ "')"
					+ ", "
					+ "('"
					+ "2"
					+ "','"
					+ "抽出原理"
					+ "')"
					+ ", "
					+ "('"
					+ "3"
					+ "','"
					+ "局部特性原理"
					+ "')"
					+ ", "
					+ "('"
					+ "4"
					+ "','"
					+ "不对称原理"
					+ "')"
					+ ", "
					+ "('"
					+ "5"
					+ "','"
					+ "组合原理"
					+ "')"
					+ ", "
					+ "('"
					+ "6"
					+ "','"
					+ "多用性原理"
					+ "')"
					+ ", "
					+ "('"
					+ "7"
					+ "','"
					+ "嵌套原理"
					+ "')"
					+ ", "
					+ "('"
					+ "8"
					+ "','"
					+ "反重力原理"
					+ "')"
					+ ", "
					+ "('"
					+ "9"
					+ "','"
					+ "预先反作用原理"
					+ "')"
					+ ", "
					+ "('"
					+ "10"
					+ "','"
					+ "预先作用原理"
					+ "')"
					+ ", "
					+ "('"
					+ "11"
					+ "','"
					+ "预先防范原理"
					+ "')"
					+ ", "
					+ "('"
					+ "12"
					+ "','"
					+ "等势原理"
					+ "')"
					+ ", "
					+ "('"
					+ "13"
					+ "','"
					+ "反向作用原理"
					+ "')"
					+ ", "
					+ "('"
					+ "14"
					+ "','"
					+ "曲面化原理"
					+ "')"
					+ ", "
					+ "('"
					+ "15"
					+ "','"
					+ "动态性原理"
					+ "')"
					+ ", "
					+ "('"
					+ "16"
					+ "','"
					+ "不足或过量作用原理"
					+ "')"
					+ ", "
					+ "('"
					+ "17"
					+ "','"
					+ "多维化原理"
					+ "')"
					+ ", "
					+ "('"
					+ "18"
					+ "','"
					+ "振动原理"
					+ "')"
					+ ", "
					+ "('"
					+ "19"
					+ "','"
					+ "周期性动作原理"
					+ "')"
					+ ", "
					+ "('"
					+ "20"
					+ "','"
					+ "有效持续作用原理"
					+ "')"
					+ ", "
					+ "('"
					+ "21"
					+ "','"
					+ "急速作用原理"
					+ "')"
					+ ", "
					+ "('"
					+ "22"
					+ "','"
					+ "变害为益原理"
					+ "')"
					+ ", "
					+ "('"
					+ "23"
					+ "','"
					+ "反馈原理"
					+ "')"
					+ ", "
					+ "('"
					+ "24"
					+ "','"
					+ "中介原理"
					+ "')"
					+ ", "
					+ "('"
					+ "25"
					+ "','"
					+ "自服务原理"
					+ "')"
					+ ", "
					+ "('"
					+ "26"
					+ "','"
					+ "复制原理"
					+ "')"
					+ ", "
					+ "('"
					+ "27"
					+ "','"
					+ "一次性用品替代原理"
					+ "')"
					+ ", "
					+ "('"
					+ "28"
					+ "','"
					+ "替换机械系统原理"
					+ "')"
					+ ", "
					+ "('"
					+ "29"
					+ "','"
					+ "气压或液压结构原理"
					+ "')"
					+ ", "
					+ "('"
					+ "30"
					+ "','"
					+ "柔性壳体或薄膜结构原理"
					+ "')"
					+ ", "
					+ "('"
					+ "31"
					+ "','"
					+ "多孔材料原理"
					+ "')"
					+ ", "
					+ "('"
					+ "32"
					+ "','"
					+ "变换颜色原理"
					+ "')"
					+ ", "
					+ "('"
					+ "33"
					+ "','"
					+ "同质原理"
					+ "')"
					+ ", "
					+ "('"
					+ "34"
					+ "','"
					+ "自弃与修复原理"
					+ "')"
					+ ", "
					+ "('"
					+ "35"
					+ "','"
					+ "状态和参数变化原理"
					+ "')"
					+ ", "
					+ "('"
					+ "36"
					+ "','"
					+ "相变原理"
					+ "')"
					+ ", "
					+ "('"
					+ "37"
					+ "','"
					+ "热膨胀原理"
					+ "')"
					+ ", "
					+ "('"
					+ "38"
					+ "','"
					+ "强氧化作用原理"
					+ "')"
					+ ", "
					+ "('"
					+ "39"
					+ "','"
					+ "惰性介质原理"
					+ "')" + ", " + "('" + "40" + "','" + "复合材料原理" + "');";
			System.out.println(sql_create_table_triz);
			System.out.println(sql_insert_into_triz);
			PreparedStatement ps = con.prepareStatement(sql_create_table_triz);
			ps.executeUpdate();
			ps = con.prepareStatement(sql_insert_into_triz);
			ps.executeUpdate();
			ps.close();
			con.close();
			System.out.println("创建数据表TRIZ、输入TRIZ数据成功");
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("创建数据表TRIZ、输入TRIZ数据失败");
			return false;
		}
	}

	/**
	 * 创建CLASSIFICATION数据表
	 * 
	 * @param
	 * @return Boolean
	 */
	private static Boolean createTableCLASSIFICATION() {
		try {
			Connection con = getConnection();
			String sql_create_table_classification = "CREATE TABLE CLASSIFICATION ("
					+ "PTT_NUM VARCHAR(20)"
					+ ", TRIZ_NUM INT"
					+ ", PRIMARY KEY(PTT_NUM, TRIZ_NUM)"
					+ ", FOREIGN KEY (PTT_NUM) REFERENCES PATENTS(PTT_NUM) ON DELETE RESTRICT ON UPDATE RESTRICT"
					+ ", FOREIGN KEY (TRIZ_NUM) REFERENCES TRIZ(TRIZ_NUM) ON DELETE RESTRICT ON UPDATE RESTRICT)"
					+ " ENGINE = InnoDB" + ";";
			System.out.println(sql_create_table_classification);
			PreparedStatement ps = con
					.prepareStatement(sql_create_table_classification);
			ps.executeUpdate();
			ps.close();
			con.close();
			System.out.println("创建数据表CLASSIFICATION成功");
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("创建数据表CLASSIFICATION失败");
			return false;
		}
	}

	/**
	 * 获取所有的专利数据
	 * 
	 * @return
	 */
	private static List<PatentDao> getAllPatents() {
		if (!checkMySQL()) {
			return null;
		}
		try {
			Connection con = getConnection();
			Statement sta = con.createStatement();
			String sql = "SELECT * FROM PATENTS;";
			ResultSet rs = sta.executeQuery(sql);
			List<PatentDao> listPatent = transferDataToPatentDao(rs);
			if (listPatent.size() == 0) {
				return null;
			} else {
				return listPatent;
			}
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("获取所有专利数据有误 !");
			return null;
		}
	}

	/**
	 * 把ResultSet转化为List<PatentDao>
	 * 
	 * @param rs
	 * @return
	 * @throws SQLException
	 */
	private static List<PatentDao> transferDataToPatentDao(ResultSet rs)
			throws SQLException {
		List<PatentDao> listPatent = new ArrayList<PatentDao>();
		while (rs.next()) {
			PatentDao pttDao = new PatentDao();
			pttDao.setPttType(rs.getString("PTT_TYPE"));
			pttDao.setApplyNum(rs.getString("APPLY_NUM"));
			pttDao.setApplyDate(rs.getDate("APPLY_DATE"));
			pttDao.setPttName(rs.getString("PTT_NAME"));
			pttDao.setPttNum(rs.getString("PTT_NUM"));
			pttDao.setPttDate(rs.getDate("PTT_DATE"));
			pttDao.setPttMainClassNum(rs.getString("PTT_MAIN_CLASS_NUM"));
			pttDao.setPttClassNum(rs.getString("PTT_CLASS_NUM"));
			pttDao.setProposer(rs.getString("PROPOSER"));
			pttDao.setProposerAddress(rs.getString("PROPOSER_ADDRESS"));
			pttDao.setInventor(rs.getString("INVENTOR"));
			pttDao.setInternationalApply(rs.getString("INTERNATIONAL_APPLY"));
			pttDao.setInternationalPublication(rs
					.getString("INTERNATIONAL_PUBLICATION"));
			pttDao.setIntoDate(rs.getDate("INTO_DATE"));
			pttDao.setPttAgencyOrg(rs.getString("PTT_AGENCY_ORG"));
			pttDao.setPttAgencyPerson(rs.getString("PTT_AGENCY_PERSON"));
			pttDao.setPttAbstract(rs.getString("PTT_ABSTRACT"));
			listPatent.add(pttDao);
		}
		return listPatent;
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
	 * 按照专利的格式写入数据库的外部接口
	 * 
	 * @param patentdao
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
		if (!checkMySQL()) {
			return false;
		}
		try {
			Connection con = getConnection();
			Statement sta = con.createStatement();

			String sql_insert_into_patents = "INSERT INTO patentdb.PATENTS (PTT_NUM,APPLY_NUM,APPLY_DATE,PTT_NAME,PTT_DATE,PTT_MAIN_CLASS_NUM,PTT_CLASS_NUM,PROPOSER,"
					+ "PROPOSER_ADDRESS,INVENTOR,PTT_AGENCY_ORG,PTT_AGENCY_PERSON,PTT_ABSTRACT,CLASS_NUM_G06Q,INTERNATIONAL_APPLY,INTERNATIONAL_PUBLICATION,INTO_DATE,PTT_TYPE) VALUES ('"
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
					+ pttDao.getIntoDate() + "','" + pttDao.getPttType() + "')";

			System.out.println(sql_insert_into_patents);
			sta.execute(sql_insert_into_patents);
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
	 * 从数据库获取索引数据
	 * 
	 * @throws Exception
	 */
	public static List<Document> getDocumentFromDatabase(Analyzer analyzer)
			throws Exception {
		List<PatentDao> listPatent = getAllPatents();
		if (listPatent == null || listPatent.size() == 0) {
			return null;
		}
		List<Document> listDocument = new ArrayList<Document>();
		for (int i = 0; i < listPatent.size(); i++) {
			PatentDao pttDao = listPatent.get(i);
			Document document = new Document();
			document.add(new TextField("PTT_TYPE", pttDao.getPttType(),
					Field.Store.YES));
			document.add(new TextField("APPLY_NUM", pttDao.getApplyNum(),
					Field.Store.YES));
			document.add(new TextField("APPLY_DATE", pttDao.getApplyDate()
					.toString(), Field.Store.YES));
			document.add(new TextField("PTT_NAME", Nlpir.doNlpirString(
					pttDao.getPttName(), 0, null, null), Field.Store.YES));
			document.add(new TextField("PTT_NUM", pttDao.getPttNum(),
					Field.Store.YES));
			document.add(new TextField("PTT_DATE", pttDao.getPttDate()
					.toString(), Field.Store.YES));
			document.add(new TextField("PTT_MAIN_CLASS_NUM", Nlpir
					.doNlpirString(pttDao.getPttMainClassNum(), 0, null, null),
					Field.Store.YES));
			document.add(new TextField("PTT_CLASS_NUM", Nlpir.doNlpirString(
					pttDao.getPttClassNum(), 0, null, null), Field.Store.YES));
			document.add(new TextField("PROPOSER", Nlpir.doNlpirString(
					pttDao.getProposer(), 0, null, null), Field.Store.YES));
			document.add(new TextField("PROPOSER_ADDRESS", Nlpir.doNlpirString(
					pttDao.getProposerAddress(), 0, null, null),
					Field.Store.YES));
			document.add(new TextField("INVENTOR", Nlpir.doNlpirString(
					pttDao.getInventor(), 0, null, null), Field.Store.YES));
			document.add(new TextField("INTERNATIONAL_APPLY", Nlpir
					.doNlpirString(pttDao.getInternationalApply(), 0, null,
							null), Field.Store.YES));
			document.add(new TextField("INTERNATIONAL_PUBLICATION", Nlpir
					.doNlpirString(pttDao.getInternationalPublication(), 0,
							null, null), Field.Store.YES));
			document.add(new TextField("INTO_DATE", pttDao.getIntoDate()
					.toString(), Field.Store.YES));
			document.add(new TextField("PTT_AGENCY_ORG", Nlpir.doNlpirString(
					pttDao.getPttAgencyOrg(), 0, null, null), Field.Store.YES));
			document.add(new TextField("PTT_AGENCY_PERSON", Nlpir
					.doNlpirString(pttDao.getPttAgencyPerson(), 0, null, null),
					Field.Store.YES));
			document.add(new TextField("PTT_ABSTRACT", Nlpir.doNlpirString(
					pttDao.getPttAbstract(), 0, null, null), Field.Store.YES));
			listDocument.add(document);
		}
		return listDocument;
	}

	/**
	 * 获取所有的TRIZ
	 */
	public static List<String> getAllTRIZ() {
		if (!checkMySQL()) {
			return null;
		}
		try {
			Connection con = getConnection();
			Statement sta = con.createStatement();
			String sql = "SELECT * FROM TRIZ;";
			ResultSet rs = sta.executeQuery(sql);
			List<String> listTRIZ = new ArrayList<String>();
			while (rs.next()) {
				String temp = rs.getString("TRIZ_NUM")
						+ rs.getString("TRIZ_TEXT");
				listTRIZ.add(temp);
			}
			return listTRIZ;
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("获取TRIZ原理有误 !");
			return null;
		}
	}

	/**
	 * CLASSIFICATION统计TRIZ的个数
	 */
	public static List<String> getCount() {
		if (!checkMySQL()) {
			return null;
		}
		try {
			Connection con = getConnection();
			Statement sta = con.createStatement();
			List<String> listCount = new ArrayList<String>();
			for (int i = 1; i <= 40; i++) {
				String sql = "SELECT count(*) FROM `CLASSIFICATION` WHERE `TRIZ_NUM`="
						+ i + " group by `TRIZ_NUM`";
				ResultSet rs = sta.executeQuery(sql);
				if (rs.next()) {
					String temp = rs.getString("count(*)");
					listCount.add(temp);
				} else {
					listCount.add("0");
				}
			}
			return listCount;
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("获取Count有误 !");
			return null;
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
