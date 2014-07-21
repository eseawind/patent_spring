package cn.edu.scut.patent.dao;

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
import cn.edu.scut.patent.model.Patent;
import cn.edu.scut.patent.service.PatentService;
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
	 * 判断PatentNumber是否存在于表CLASSIFICATION当中
	 * 
	 * @param PTT_NUM
	 * @return
	 */
	public static Boolean isPatentNumberExisted(String PTT_NUM) {
		Connection con = null;
		Statement sta = null;
		ResultSet rs = null;
		try {
			con = getConnection();
			sta = con.createStatement();
			String sql = "SELECT PTT_NUM FROM patentdb.CLASSIFICATION WHERE PTT_NUM = '"
					+ PTT_NUM + "' ;";
			rs = sta.executeQuery(sql);
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
	 * 获取所有的专利数据
	 * 
	 * @return
	 */
	public static List<Patent> getAllPatents() {
		if (!checkMySQL()) {
			return null;
		}
		Connection con = null;
		Statement sta = null;
		ResultSet rs = null;
		try {
			con = getConnection();
			sta = con.createStatement();
			String sql = "SELECT * FROM PATENTS;";
			rs = sta.executeQuery(sql);
			List<Patent> listPatent = new PatentService().transferDataToPatentDao(rs);
			if (listPatent.size() == 0) {
				return null;
			} else {
				return listPatent;
			}
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("获取所有专利数据有误 !");
			return null;
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
	 * 获取PATENTS所有专利关键属性的数据
	 * 
	 * @return
	 */
	public static ResultSet getPatentsKeys() {
		if (!checkMySQL()) {
			return null;
		}
		Connection con = null;
		Statement sta = null;
		ResultSet rs = null;
		try {
			con = getConnection();
			sta = con.createStatement();
			String sql = "SELECT PTT_NUM,PTT_NAME,PTT_DATE,CLASS_NUM_G06Q,PTT_ABSTRACT FROM patents";
			rs = sta.executeQuery(sql);
			return rs;
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("获取PATENTS所有专利关键属性的数据有误 !");
			return null;
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
	 * 从PATENTS_AFTER_WORD_DIVIDE获取专利属性的数据
	 * 
	 * @return
	 */
	public static ResultSet getPatentsFromPATENTS_AFTER_WORD_DIVIDE() {
		if (!checkMySQL()) {
			return null;
		}
		Connection con = null;
		Statement sta = null;
		ResultSet rs = null;
		try {
			con = getConnection();
			sta = con.createStatement();
			String sql = "SELECT * FROM PATENTS_AFTER_WORD_DIVIDE";
			rs = sta.executeQuery(sql);
			return rs;
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("从PATENTS_AFTER_WORD_DIVIDE获取专利属性的数据失败 !");
			return null;
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
	 * 获取具体专利文献的聚类数据，返回该专利的聚类号；如果不存在就返回-1
	 * 
	 * @return
	 */
	public static int getClusterNumber(String PTT_NUM) {
		// if (!isTableExisted("PATENT_CLUSTER")) {
		// return -1;
		// }
		Connection con = null;
		Statement sta = null;
		ResultSet rs = null;
		try {
			con = getConnection();
			sta = con.createStatement();
			String sql = "SELECT * FROM PATENT_CLUSTER WHERE PTT_NUM='"
					+ PTT_NUM + "';";
			rs = sta.executeQuery(sql);
			if (rs.next()) {
				return rs.getInt("CLUSTER");
			} else {
				return -1;
			}
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("获取专利文献聚类号有误 !");
			return -1;
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
	 * 插入Triz号码
	 * 
	 * @param PTT_NUM
	 * @param TRIZ_NUM
	 * @return
	 */
	public static Boolean insertClassificationNumber(String PTT_NUM, String[] TRIZ_NUM) {
		if (TRIZ_NUM.length < 1) {
			return false;
		}

		Connection con = null;
		Statement sta = null;
		try {
			con = getConnection();
			sta = con.createStatement();
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
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		} finally {
			try {
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
	 * 更新Triz号码
	 * 
	 * @param PTT_NUM
	 * @param TRIZ_NUM
	 * @return
	 */
	public static Boolean updateTrizNumber(String PTT_NUM, String[] TRIZ_NUM) {
		Connection con = null;
		Statement sta = null;
		try {
			con = getConnection();
			sta = con.createStatement();
			String sql_delete = "DELETE FROM patentdb.CLASSIFICATION WHERE PTT_NUM = '"
					+ PTT_NUM + "';";
			System.out.println(sql_delete);
			sta.execute(sql_delete);
			if (insertClassificationNumber(PTT_NUM, TRIZ_NUM)) {
				return true;
			} else {
				return false;
			}
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		} finally {
			try {
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
	 * 从数据库获取索引数据
	 * 
	 * @param analyzer
	 * @return
	 * @throws Exception
	 */
	public static List<Document> getDocumentFromDatabase(Analyzer analyzer)
			throws Exception {
		List<Patent> listPatent = getAllPatents();
		if (listPatent == null || listPatent.size() == 0) {
			return null;
		}
		List<Document> listDocument = new ArrayList<Document>();
		for (int i = 0; i < listPatent.size(); i++) {
			Patent pttDao = listPatent.get(i);
			Document document = new Document();
			document.add(new TextField("PTT_TYPE", pttDao.getPttType(),
					Field.Store.YES));
			document.add(new TextField("APPLY_NUM", pttDao.getApplyNum(),
					Field.Store.YES));
			document.add(new TextField("APPLY_DATE", pttDao.getApplyDate()
					.toString(), Field.Store.YES));
			System.out.println(pttDao.getPttName());
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
	 * 
	 * @return
	 */
	public static List<String> getAllTRIZ() {
		if (!checkMySQL()) {
			return null;
		}
		Connection con = null;
		Statement sta = null;
		ResultSet rs = null;
		try {
			con = getConnection();
			sta = con.createStatement();
			String sql = "SELECT * FROM TRIZ;";
			rs = sta.executeQuery(sql);
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
	 * CLASSIFICATION统计TRIZ的个数
	 * 
	 * @return
	 */
	public static List<String> getCount() {
		if (!checkMySQL()) {
			return null;
		}
		Connection con = null;
		Statement sta = null;
		ResultSet rs = null;
		try {
			con = getConnection();
			sta = con.createStatement();
			List<String> listCount = new ArrayList<String>();
			for (int i = 1; i <= 40; i++) {
				String sql = "SELECT count(*) FROM `CLASSIFICATION` WHERE `TRIZ_NUM`="
						+ i + " group by `TRIZ_NUM`";
				rs = sta.executeQuery(sql);
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
	 * 
	 */
	public static void generateTDate() {
		Connection con = null;
		Statement sta = null;
		ResultSet rs = null;
		try {
			Class.forName("com.mysql.jdbc.Driver");
			con = getConnection();
			sta = con.createStatement();
			String sql = "SELECT DISTINCT PTT_DATE FROM patents";
			rs = sta.executeQuery(sql);
			while (rs.next()) {
				Date date = rs.getDate(1);
				String year = date.toString().substring(0, 4);
				String month = date.toString().substring(5, 7);
				String day = date.toString().substring(8, 10);

				sta.execute("insert into t_date (TIME_KEY,YEAR,MONTH,DATE) VALUES ('"
						+ date
						+ "','"
						+ year
						+ "','"
						+ month
						+ "','"
						+ day
						+ "')");
				System.out.println(rs.getDate(1).toString());
			}
		} catch (Exception e) {
			e.printStackTrace();
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
		Connection con = null;
		Statement sta = null;
		ResultSet rs = null;
		try {
			con = getConnection();
			sta = con.createStatement();
			rs = sta.executeQuery("select count(*) from " + dbName);
			rs.first();
			size = rs.getInt(1);
			return size;
		} catch (Exception e) {
			e.printStackTrace();
			return -1;
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

	public static void main(String[] args) {
		checkMySQL();
		if(PatentService.isEmpty()){
			System.out.println("patent空的");
		}else{
			System.out.println("patent非空");
		}
		if(TrizService.isEmpty()){
			System.out.println("triz空的");
		}else{
			System.out.println("triz非空");
		}
		PatentService.cleanTable();
		if(PatentService.isEmpty()){
			System.out.println("patent空的");
		}else{
			System.out.println("patent非空");
		}
	}
}
