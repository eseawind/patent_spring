package cn.edu.scut.patent.prework;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import ICTCLAS2014.Nlpir;
import cn.edu.scut.patent.model.PatentFeatureWordModel;
import cn.edu.scut.patent.model.PatentMatrix;
import cn.edu.scut.patent.model.PatentWordTFIDFModel;
import cn.edu.scut.patent.model.PatentsAfterWordDivideModel;
import cn.edu.scut.patent.model.WordInfoModel;
import cn.edu.scut.patent.prework.impl.ClusterImpl;
import cn.edu.scut.patent.util.Constants;
import cn.edu.scut.patent.dao.DatabaseHelper;
import cn.edu.scut.patent.util.StringHelper;

/**
 * 对专利文本进行K-means文本聚类处理
 * 
 * @author CJX
 * 
 */
public class Cluster implements ClusterImpl {

	public static Connection con;
	public Map<String, PatentWordTFIDFModel> titleWordDic;
	public Map<String, PatentWordTFIDFModel> abstractWordDic;
	public Map<String, PatentWordTFIDFModel> contentWordDic;
	int count = 0;

	public Cluster() {
		try {
			con = DatabaseHelper.getConnection();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void doCluster() {
		long totalStartTime = new Date().getTime();// 总的开始的时间
		String result = "";
		Cluster cluster = new Cluster();

		// 如果数据表PATENT_WORD_TF_DF已经存在的话，则跳过下述函数，不再浪费资源重复计算。
		if (!DatabaseHelper.isTableExisted("PATENT_WORD_TF_DF")) {
			DatabaseHelper.dropTable("PATENTS_AFTER_WORD_DIVIDE");
			DatabaseHelper.dropTable("T_STOPWORD");
			DatabaseHelper.dropTable("T_WORD_SMARK");

			long startTime = new Date().getTime();// 开始的时间
			System.out.println("将名称和摘要分词并存入patent_word_after_divide中");
			// 将名称和摘要分词并存入patent_word_after_divide
			cluster.divideWordToDb();
			String timeConsume1 = "1.花费了" + StringHelper.timer(startTime)
					+ "完成专利名称和专利摘要的分词和过滤！";
			System.out.println(timeConsume1);
			result += timeConsume1 + "\n";
		}

		// 如果数据表T_WORD_INFO已经存在的话，则跳过下述函数，不再浪费资源重复计算。
		if (!DatabaseHelper.isTableExisted("T_WORD_INFO")) {
			DatabaseHelper.dropTable("PATENT_WORD_TF_DF");

			long startTime = new Date().getTime();// 开始的时间
			// 计算TF，存入Map中
			cluster.countTF();
			// 将Map中的数据(即TF)存入到patent_word_tf_df
			cluster.saveWordDicToDatabase();
			// 更新patent_word_tf_df中的DF值
			cluster.countDF();
			String timeConsume2 = "2.花费了" + StringHelper.timer(startTime)
					+ "完成统计在名称和摘要中某个词语出现的频率、存入数据库、和计算文档频数DF！";
			System.out.println(timeConsume2);
			result += timeConsume2 + "\n";
		}

		// 如果数据表PATENT_FEATURE_WORD已经存在的话，则跳过下述函数，不再浪费资源重复计算。
		if (!DatabaseHelper.isTableExisted("PATENT_FEATURE_WORD")) {
			DatabaseHelper.dropTable("T_WORD_INFO");

			long startTime = new Date().getTime();// 开始的时间
			// 保存（word,maxTF,DF）值到t_word_info
			cluster.extractFeatureWord();
			String timeConsume3 = "3.花费了" + StringHelper.timer(startTime)
					+ "完成计算所有词权重MaxTf，根据权重提取特证词并保存到数据表 T_WORD_INFO中！";
			System.out.println(timeConsume3);
			result += timeConsume3 + "\n";
		}

		// 如果数据表PATENT_CLUSTER已经存在的话，则跳过下述函数，不再浪费资源重复计算。
		if (!DatabaseHelper.isTableExisted("PATENT_CLUSTER")) {
			DatabaseHelper.dropTable("PATENT_FEATURE_WORD");

			long startTime = new Date().getTime();// 开始的时间
			cluster.countAndSaveToDb(20);
			cluster.countStandardTFIDF();
			String timeConsume4 = "4.花费了" + StringHelper.timer(startTime)
					+ "完成计算并规范化TF-IDF值，提取前20位存入数据表PATENT_FEATURE_WORD中！";
			System.out.println(timeConsume4);
			result += timeConsume4 + "\n";
		}

		// 如果数据表PATENT_CLUSTER已经存在的话，则跳过下述函数，不再浪费资源重复计算。
		if (!DatabaseHelper.isTableExisted("PATENT_CLUSTER")) {
			long startTime = new Date().getTime();// 开始的时间
			cluster.clusterByQYJ(20);
			String timeConsume5 = "5.花费了" + StringHelper.timer(startTime)
					+ "完成聚类过程！";
			System.out.println(timeConsume5);
			result += timeConsume5 + "\n";
		}

		String timeConsumeTotal = "总共花费了" + StringHelper.timer(totalStartTime)
				+ "完成整个过程，great！";
		result += timeConsumeTotal + "\n";
		System.out.println(result);
	}

	public void divideWordToDb() {
		ResultSet rs = null;
		try {
			if (!DatabaseHelper.isTableExisted("PATENTS_AFTER_WORD_DIVIDE")) {
				DatabaseHelper.createTablePATENTS_AFTER_WORD_DIVIDE();
			}
			if (!DatabaseHelper.isTableExisted("T_STOPWORD")) {
				DatabaseHelper.createTableT_STOPWORD();
			}
			if (!DatabaseHelper.isTableExisted("T_WORD_SMARK")) {
				DatabaseHelper.createTableT_WORD_SMARK();
			}

			rs = DatabaseHelper.getPatentsKeys();

			PatentsAfterWordDivideModel pttAWDM;
			while (rs.next()) {
				pttAWDM = new PatentsAfterWordDivideModel();
				pttAWDM.setPtt_num(rs.getString("PTT_NUM"));
				pttAWDM.setPtt_date(rs.getDate("PTT_DATE"));
				pttAWDM.setClass_num_g06q(rs.getString("CLASS_NUM_G06Q"));

				// 将专利名称和摘要分词
				pttAWDM.setPtt_name(Nlpir.doNlpirString(
						rs.getString("PTT_NAME"), 0, null, null));
				pttAWDM.setPtt_abstract(Nlpir.doNlpirString(
						rs.getString("PTT_ABSTRACT"), 0, null, null));

				// 存入到patents_after_word_divide数据表
				DatabaseHelper.insertPATENTS_AFTER_WORD_DIVIDE(pttAWDM);
				// *************************************
				if (rs.getRow() >= 1000) {
					break;
				}
				// *************************************
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (rs != null) {
					rs.close();
				}
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	public void countTF() {
		ResultSet rs = null;
		try {
			rs = DatabaseHelper.getPatentsFromPATENTS_AFTER_WORD_DIVIDE();

			PatentsAfterWordDivideModel pawd;
			String[] titleArr;
			String[] abstractArr;
			String[] contentArr;
			titleWordDic = new HashMap<String, PatentWordTFIDFModel>();
			abstractWordDic = new HashMap<String, PatentWordTFIDFModel>();
			contentWordDic = new HashMap<String, PatentWordTFIDFModel>();

			// 数据集循环
			while (rs.next()) {
				pawd = new PatentsAfterWordDivideModel();
				pawd.read(rs);

				String tempStr;
				// 以空格切开专利名词（折扣/n 卡/n 系统/n ）
				titleArr = pawd.getPtt_name().split(" ");
				for (int i = 0; i < titleArr.length; i++) {
					tempStr = titleArr[i];
					PatentWordTFIDFModel p;
					// 以word_专利名称为格式判断是否唯一，如果是唯一的tf为1
					if (titleWordDic.get(tempStr + "_" + pawd.getPtt_num()) == null) {
						p = new PatentWordTFIDFModel();
						p.setFlag(1); // 1为标题
						p.setWord(tempStr);
						p.setPttNum(pawd.getPtt_num());
						titleWordDic.put(tempStr + "_" + pawd.getPtt_num(), p);
					}
					// 如果不唯一，tf+1
					else {
						p = titleWordDic.get(tempStr + "_" + pawd.getPtt_num());
						p.setTf(p.getTf() + 1);
					}
				}

				// 以空格切开专利摘要
				abstractArr = pawd.getPtt_abstract().split(" ");
				for (int j = 0; j < abstractArr.length; j++) {
					tempStr = abstractArr[j];
					if (tempStr.lastIndexOf("/") != -1)
						tempStr = tempStr
								.substring(0, tempStr.lastIndexOf("/"));
					PatentWordTFIDFModel p;
					if (abstractWordDic.get(tempStr + "_" + pawd.getPtt_num()) == null) {
						p = new PatentWordTFIDFModel();
						p.setFlag(0); // 0为摘要
						p.setWord(tempStr);
						p.setPttNum(pawd.getPtt_num());
						abstractWordDic.put(tempStr + "_" + pawd.getPtt_num(),
								p);
					} else {
						p = abstractWordDic.get(tempStr + "_"
								+ pawd.getPtt_num());
						p.setTf(p.getTf() + 1);
					}
				}

				// 以空格切开专利说明书
				contentArr = pawd.getPtt_content().split(" ");
				for (int k = 0; k < contentArr.length; k++) {
					tempStr = contentArr[k];
					if (tempStr.lastIndexOf("/") != -1) {
						tempStr = tempStr
								.substring(0, tempStr.lastIndexOf("/"));
					}
					PatentWordTFIDFModel p;
					if (contentWordDic.get(tempStr + "_" + pawd.getPtt_num()) == null) {
						p = new PatentWordTFIDFModel();
						p.setFlag(2); // 设定2为专利说明书的内容
						p.setWord(tempStr);
						p.setPttNum(pawd.getPtt_num());
						contentWordDic
								.put(tempStr + "_" + pawd.getPtt_num(), p);
					} else {
						p = contentWordDic.get(tempStr + "_"
								+ pawd.getPtt_num());
						p.setTf(p.getTf() + 1);
					}
				}

				// 判断哈希表长度，防止内存溢出。
				if (abstractWordDic.size() > 60000
						|| titleWordDic.size() > 60000
						|| contentWordDic.size() > 60000) {
					saveWordDicToDatabase();
					abstractWordDic = new HashMap<String, PatentWordTFIDFModel>();
					titleWordDic = new HashMap<String, PatentWordTFIDFModel>();
					contentWordDic = new HashMap<String, PatentWordTFIDFModel>();
					System.out.println("长度过长，保存数据，清空哈希表，继续统计！");
				}

				System.out.println(rs.getRow() + "\t" + "ok!\t"
						+ pawd.getPtt_num() + "\t" + pawd.getPtt_name());
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				if (rs != null) {
					rs.close();
				}
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	public void saveWordDicToDatabase() {
		if (!DatabaseHelper.isTableExisted("PATENT_WORD_TF_DF")) {
			DatabaseHelper.createTablePATENT_WORD_TF_DF();
		}
		Iterator<Map.Entry<String, PatentWordTFIDFModel>> iterator1 = titleWordDic
				.entrySet().iterator();
		Iterator<Map.Entry<String, PatentWordTFIDFModel>> iterator2 = abstractWordDic
				.entrySet().iterator();
		Iterator<Map.Entry<String, PatentWordTFIDFModel>> iterator3 = contentWordDic
				.entrySet().iterator();
		Map.Entry<String, PatentWordTFIDFModel> entry;
		while (iterator1.hasNext()) {
			entry = iterator1.next();
			entry.getValue().write(con);
		}
		System.out.println("成功把title的TF保存到数据表PATENT_WORD_TF_DF中!");
		while (iterator2.hasNext()) {
			entry = iterator2.next();
			entry.getValue().write(con);
		}
		System.out.println("成功把abstract的TF保存到数据表PATENT_WORD_TF_DF中!");
		while (iterator3.hasNext()) {
			entry = iterator3.next();
			entry.getValue().write(con);
		}
		System.out.println("成功把content的TF保存到数据表PATENT_WORD_TF_DF中!");
	}

	public void countDF() {
		Statement sta = null;
		ResultSet rs = null;
		Statement tempSta1 = null;
		Statement tempSta2 = null;
		ResultSet tempRs1 = null;
		ResultSet tempRs2 = null;
		try {
			sta = con.createStatement();
			rs = sta.executeQuery("SELECT DISTINCT WORD FROM PATENT_WORD_TF_DF");

			// int count = 0;
			while (rs.next()) {
				System.out.println(rs.getRow() + "\t" + rs.getString("WORD"));

				tempSta1 = con.createStatement(ResultSet.TYPE_FORWARD_ONLY,
						ResultSet.CONCUR_UPDATABLE);
				tempRs1 = tempSta1
						.executeQuery("SELECT * FROM PATENT_WORD_TF_DF WHERE WORD='"
								+ rs.getString(1) + "'");

				// 统计某词出现的文档频率
				tempSta2 = con.createStatement();
				tempRs2 = tempSta2
						.executeQuery("SELECT DISTINCT PTT_NUM FROM PATENT_WORD_TF_DF WHERE WORD='"
								+ rs.getString(1) + "'");
				int f = DatabaseHelper.getSize(tempRs2);

				System.out.println("DF : " + f);
				while (tempRs1.next()) {
					tempRs1.updateInt(5, f);
					tempRs1.updateRow();
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				if (rs != null) {
					rs.close();
				}
				if (tempRs1 != null) {
					tempRs1.close();
				}
				if (tempRs2 != null) {
					tempRs2.close();
				}
				if (sta != null) {
					sta.close();
				}
				if (tempSta1 != null) {
					tempSta1.close();
				}
				if (tempSta2 != null) {
					tempSta2.close();
				}
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	public void extractFeatureWord() {
		Map<String, Number> wordMaxTFmap = new HashMap<String, Number>();
		Statement sta = null;
		ResultSet rs1 = null;
		ResultSet rs2 = null;
		try {
			int count = 0;
			sta = con.createStatement();
			rs1 = sta
					.executeQuery("SELECT DISTINCT WORD FROM patent_word_tf_df");
			int tempNum;
			String word;
			WordInfoModel tempWim;
			while (rs1.next()) {
				tempNum = 0;
				sta = con.createStatement();
				word = rs1.getString(1);
				rs2 = sta
						.executeQuery("select * from patent_word_tf_df where WORD='"
								+ word + "'");
				int df = 1;
				while (rs2.next()) {
					df = rs2.getInt(5);
					tempNum = Math.max(tempNum, rs2.getInt(4));
				}
				wordMaxTFmap.put(word, tempNum);

				// 保存进数据库
				tempWim = new WordInfoModel();
				tempWim.setWord(word);
				tempWim.setMaxTf(tempNum);
				tempWim.setDf(df);
				if (!DatabaseHelper.isTableExisted("T_WORD_INFO")) {
					DatabaseHelper.createTableT_WORD_INFO();
				}
				tempWim.write();
				count++;
				System.out.println("map count:" + count + "\tkey:" + word
						+ "\tvalue:" + tempNum);
			}
			System.out.println("map保存成功！");
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (rs1 != null) {
					rs1.close();
				}
				if (rs2 != null) {
					rs2.close();
				}
				if (sta != null) {
					sta.close();
				}
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	public void countAndSaveToDb(int size) {
		if (!DatabaseHelper.isTableExisted("PATENT_FEATURE_WORD")) {
			DatabaseHelper.createTablePATENT_FEATURE_WORD();
		}
		Statement sta = null;
		ResultSet rs = null;
		ResultSet pttnumRs = null;
		try {
			int n = DatabaseHelper.getSize("patents_after_word_divide");
			int count = 0;
			Map<String, Number> wordMaxTFmap = new HashMap<String, Number>();
			sta = con.createStatement();
			rs = sta.executeQuery("select * from t_word_info");
			while (rs.next()) {
				wordMaxTFmap.put(rs.getString("WORD"), rs.getInt("MAX_TF"));
			}
			System.out.println(wordMaxTFmap.get("web"));
			System.out.println(wordMaxTFmap.size());

			sta = con.createStatement();
			rs = sta.executeQuery("SELECT DISTINCT PTT_NUM FROM patent_word_tf_df");
			String pttnum;
			ArrayList<PatentFeatureWordModel> tempArr;
			while (rs.next()) {
				tempArr = new ArrayList<PatentFeatureWordModel>();
				sta = con.createStatement();
				pttnum = rs.getString(1);
				pttnumRs = sta
						.executeQuery("select * from patent_word_tf_df where PTT_NUM='"
								+ pttnum + "'");

				while (pttnumRs.next()) {
					int tf = pttnumRs.getInt(4);
					int maxTf = 0;
					if (wordMaxTFmap.get(pttnumRs.getString(3)) == null) {
						System.out.println(pttnumRs.getString(3)
								+ "\tnullpointerexception!");
						continue;
					} else {
						maxTf = wordMaxTFmap.get(pttnumRs.getString(3))
								.intValue();
					}
					int df = pttnumRs.getInt(5);
					Number tempValue = (0.5 + 0.5 * tf / maxTf)
							* (Math.log(n / df));
					PatentFeatureWordModel pfwm = new PatentFeatureWordModel(
							pttnum, pttnumRs.getString(3),
							tempValue.doubleValue());
					tempArr.add(pfwm);
				}

				for (int i = 0; i < tempArr.size() - 1; i++) {
					for (int j = i + 1; j < tempArr.size(); j++) {
						if (tempArr.get(i).getTfidfValue() < tempArr.get(j)
								.getTfidfValue()) {
							PatentFeatureWordModel tempPfwm = tempArr.get(i);
							tempArr.set(i, tempArr.get(j));
							tempArr.set(j, tempPfwm);
						}
					}
				}

				int len = Math.min(size, tempArr.size());
				for (int m = 0; m < len; m++) {
					// 写入数据库
					tempArr.get(m).write();
				}
				count++;
				System.out.println("第" + count + "个专利" + pttnum + "的特征词数量为"
						+ len);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (sta != null) {
					sta.close();
				}
				if (rs != null) {
					rs.close();
				}
				if (pttnumRs != null) {
					pttnumRs.close();
				}
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	public void countStandardTFIDF() {
		Statement sta = null;
		ResultSet rs = null;
		Statement sta2 = null;
		ResultSet rs2 = null;
		try {
			sta = con.createStatement();
			rs = sta.executeQuery("select PTT_NUM from patents");
			while (rs.next()) {
				String pttNum = rs.getString(1);
				sta2 = con.createStatement();
				rs2 = sta2
						.executeQuery("select * from patent_feature_word where PTT_NUM='"
								+ pttNum + "'");
				List<PatentFeatureWordModel> pfwnList = new ArrayList<PatentFeatureWordModel>();
				while (rs2.next()) {
					PatentFeatureWordModel pfwm = new PatentFeatureWordModel();
					pfwm.read(rs2);
					pfwnList.add(pfwm);
				}
				double sum = 0;
				for (int i = 0; i < pfwnList.size(); i++) {
					sum += Math.pow(pfwnList.get(i).getTfidfValue(), 2);
				}
				sum = Math.sqrt(sum);
				for (int j = 0; j < pfwnList.size(); j++) {
					PatentFeatureWordModel p = pfwnList.get(j);
					p.setTfidfValueStandard(p.getTfidfValue() / sum);
					p.updateTfidfValueStandard();
				}
				System.out.println(pttNum);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				if (rs2 != null) {
					rs2.close();
				}
				if (rs != null) {
					rs.close();
				}
				if (sta2 != null) {
					sta2.close();
				}
				if (sta != null) {
					sta.close();
				}
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	@SuppressWarnings("unchecked")
	public void clusterByCJX(int k) {
		if (!DatabaseHelper.isTableExisted("PATENT_CLUSTER")) {
			DatabaseHelper.createTablePATENT_CLUSTER();
		}
		List<PatentMatrix> pttMatrix = new ArrayList<PatentMatrix>();
		int n = 0;
		Statement sta = null;
		ResultSet rs = null;
		Statement sta2 = null;
		ResultSet rs2 = null;
		try {
			sta = con.createStatement();
			rs = sta.executeQuery("select PTT_NUM from patents");
			while (rs.next()) {
				String pttNum = rs.getString(1);
				PatentMatrix pm = new PatentMatrix();
				pm.pttNum = pttNum;

				sta2 = con.createStatement();
				rs2 = sta2
						.executeQuery("select * from patent_feature_word where PTT_NUM='"
								+ pttNum + "'");
				int index = 0;
				double sum = 0;
				while (rs2.next()) {
					PatentFeatureWordModel pfwm = new PatentFeatureWordModel();
					pfwm.read(rs2);
					pm.value[index] = pfwm.getTfidfValueStandard();
					sum += pm.value[index] * pm.value[index];
					index += 1;
				}
				for (int t = index; t < 20; t++) {
					pm.value[t] = 0;
				}
				pttMatrix.add(pm);

				System.out.println(":" + n);
				System.out.println("" + sum);
				// if(n == 100)
				// {
				// break;
				// }
				n++;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				if (rs2 != null) {
					rs2.close();
				}
				if (rs != null) {
					rs.close();
				}
				if (sta2 != null) {
					sta2.close();
				}
				if (sta != null) {
					sta.close();
				}
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		// 变量定义
		ArrayList<PatentMatrix>[] clusters = new ArrayList[k]; // k个聚类
		List<PatentMatrix> clustersCenter = new ArrayList<PatentMatrix>(); // k个聚类的中心点
		for (int z = 0; z < k; z++) {
			clusters[z] = new ArrayList<PatentMatrix>();
			clusters[z].add(pttMatrix.get(z));
			clustersCenter.add(pttMatrix.get(z));
		}

		int[] clusterAssignments = new int[n]; // 各个数据属于那个聚类
		int[] nearestCluster = new int[n]; // 各个数据离那个聚类最近
		double[][] distance = new double[n][k]; // 各个数据与各个聚类的距离

		int count = 1;
		while (true) {
			System.out.println(":" + count);
			// 1.重新计算每个聚类的中心点
			System.out.println("clustersCenter:");
			for (int i = 0; i < 20; i++) {
				clustersCenter.set(i, getClusterCenter(clusters[i]));
			}

			// 2.计算每个数据和每个聚类中心的距离
			System.out.println("distance:");
			for (int m = 0; m < k; m++) {
				for (int j = 0; j < n; j++) {
					distance[j][m] = getDistance(pttMatrix.get(j),
							clustersCenter.get(m));
					System.out.print(" " + distance[j][m]);
				}
				System.out.print("\n");
			}

			// 3.计算每个数据离哪个聚类最近
			System.out.print("nearestCluster:");
			for (int f = 0; f < n; f++) {
				nearestCluster[f] = getNearestCluster(distance[f]);
				System.out.print(" " + nearestCluster[f]);
			}
			System.out.print("\n");

			// 4.比较每个数据最近的聚类是否就是它所属的聚类
			// 如果全相等表示所有的点已经是最佳距离了，直接返回；
			System.out.print("clusterAssignments:");
			int r = 0;
			for (int w = 0; w < n; w++) {
				System.out.print(" " + clusterAssignments[w]);
				if (nearestCluster[w] == clusterAssignments[w]) {
					r++;
				} else {
					clusterAssignments[w] = nearestCluster[w];
				}
			}
			System.out.print("\n");
			if (r == n) {
				break;
			}
			System.out.println("第四步完成");

			// 5.否则需要重新调整资料点和群聚类的关系，调整完毕后再重新开始循环；
			// 需要修改每个聚类的成员和表示某个数据属于哪个聚类的变量
			for (int q = 0; q < k; q++) {
				clusters[q].clear();
			}
			for (int p = 0; p < n; p++) {
				clusters[clusterAssignments[p]].add(pttMatrix.get(p));
			}
			System.out.println("第五步完成");
			count++;
		}

		Statement sta3 = null;
		for (int ii = 0; ii < n; ii++) {
			try {
				sta3 = con.createStatement();
				sta3.execute("INSERT INTO patent_cluster (PTT_NUM,CLUSTER) VALUES ('"
						+ pttMatrix.get(ii).pttNum
						+ "',"
						+ clusterAssignments[ii] + ")");
			} catch (SQLException e) {
				e.printStackTrace();
			} finally {
				try {
					if (sta3 != null) {
						sta3.close();
					}
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		System.out.println("聚类成功！");
	}

	@SuppressWarnings("unchecked")
	public void clusterByQYJ(int k) {
		if (!DatabaseHelper.isTableExisted("PATENT_CLUSTER")) {
			DatabaseHelper.createTablePATENT_CLUSTER();
		}
		// 以专利为单位，存储所有专利所有特征词的权重
		System.out.println("以专利为单位，存储所有专利所有特征词的权重");
		List<PatentMatrix> pttMatrix = new ArrayList<PatentMatrix>();
		int n = 0;
		Statement sta = null;
		ResultSet rs = null;
		Statement sta2 = null;
		ResultSet rs2 = null;
		try {
			sta = con.createStatement();
			rs = sta.executeQuery("SELECT DISTINCT PTT_NUM FROM PATENT_FEATURE_WORD");
			while (rs.next()) {
				String pttNum = rs.getString(1);
				PatentMatrix pm = new PatentMatrix();
				pm.pttNum = pttNum;
				sta2 = con.createStatement();
				rs2 = sta2
						.executeQuery("select * from patent_feature_word where PTT_NUM='"
								+ pttNum + "'");
				int index = 0;
				double sum = 0;
				while (rs2.next()) {
					PatentFeatureWordModel pfwm = new PatentFeatureWordModel();
					pfwm.read(rs2);
					pm.value[index] = pfwm.getTfidfValueStandard();
					sum += pm.value[index] * pm.value[index];
					index += 1;
				}
				for (int t = index; t < 20; t++) {
					pm.value[t] = 0;
				}
				pttMatrix.add(pm);
				System.out.println("第" + n + "个专利");
				System.out.println("权重平方和为:" + sum);
				n++;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				if (rs2 != null) {
					rs2.close();
				}
				if (rs != null) {
					rs.close();
				}
				if (sta2 != null) {
					sta2.close();
				}
				if (sta != null) {
					sta.close();
				}
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		// 变量定义
		ArrayList<PatentMatrix>[] clusters = new ArrayList[k]; // k个聚类(每一个聚类下是一个装有所有点的ArrayList<PatentMatrix>())
		List<PatentMatrix> clustersCenter = new ArrayList<PatentMatrix>(); // k个聚类的中心点
		for (int z = 0; z < k; z++) {
			clusters[z] = new ArrayList<PatentMatrix>();
			clusters[z].add(pttMatrix.get(z));
			clustersCenter.add(pttMatrix.get(z));
		}

		int[] clusterAssignments = new int[n]; // 上一次各个数据所属的聚类号
		int[] nearestCluster = new int[n]; // 本次各个数据所属的聚类号
		double[][] distance = new double[n][k]; // 各个数据与各个聚类的距离

		int count = 1;
		while (true) {
			System.out.println("第" + count + "次聚类");
			// 1.重新计算每个聚类的中心点
			System.out.println("第一步：重新计算每个聚类的中心点");
			for (int i = 0; i < 20; i++) {
				clustersCenter.set(i, getClusterCenter(clusters[i]));
			}
			System.out.println("第一步完成");

			// 2.计算每个数据和每个聚类中心的距离
			System.out.println("第二步：计算每个数据和每个聚类中心的距离");
			for (int m = 0; m < k; m++) {
				for (int j = 0; j < n; j++) {
					distance[j][m] = getDistance(pttMatrix.get(j),
							clustersCenter.get(m));
				}
				System.out.println();
			}
			System.out.println("第二步完成");

			// 3.计算每个数据离哪个聚类最近
			System.out.println("第三步：计算每个数据离哪个聚类最近");
			for (int f = 0; f < n; f++) {
				nearestCluster[f] = getNearestCluster(distance[f]);
				System.out.println("第" + f + "个点离聚类群" + nearestCluster[f]
						+ "最近");
			}
			System.out.println("第三步完成");

			// 4.比较每个数据最近的聚类是否就是它所属的聚类
			// 如果全相等表示所有的点已经是最佳距离或达到了聚类的最大次数，直接返回；
			System.out.println("第四步：比较每个数据最近的聚类是否就是它原本所属的聚类");
			int r = 0;
			for (int w = 0; w < n; w++) {
				System.out.println("第" + w + "个点原本所属的聚类群为"
						+ clusterAssignments[w]);
				if (nearestCluster[w] == clusterAssignments[w]) {
					r++;
					System.out.println("相同");
				} else {
					clusterAssignments[w] = nearestCluster[w];
					System.out.println("不同");
				}
			}
			System.out.println();
			if (r == n || count >= Constants.CLUSTER_MAX_NUMBER) {
				break;
			}
			System.out.println("第四步完成");

			// 5.否则需要重新调整资料点和群聚类的关系，调整完毕后再重新开始循环；
			// 需要修改每个聚类的成员和表示某个数据属于哪个聚类的变量
			System.out.println("第五步：重新调整资料点和群聚类的关系");
			for (int q = 0; q < k; q++) {
				clusters[q].clear();
			}
			for (int p = 0; p < n; p++) {
				clusters[clusterAssignments[p]].add(pttMatrix.get(p));
			}
			System.out.println("第五步完成");
			count++;
		}

		for (int ii = 0; ii < n; ii++) {
			Statement sta3 = null;
			try {
				sta3 = con.createStatement();
				sta3.execute("INSERT INTO patent_cluster (PTT_NUM,CLUSTER) VALUES ('"
						+ pttMatrix.get(ii).pttNum
						+ "',"
						+ clusterAssignments[ii] + ")");
			} catch (SQLException e) {
				e.printStackTrace();
			} finally {
				try {
					if (sta3 != null) {
						sta3.close();
					}
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		System.out.println("聚类成功！");
	}

	public PatentMatrix getClusterCenter(ArrayList<PatentMatrix> cluster) {
		int len = cluster.size();
		System.out.println("当前聚类群中含有的点数：" + len);//
		// double[][] distance = new double[len][len]; // 存储各数据间相互距离的二维数组
		// for (int j = 0; j < len; j++) {
		// for (int i = j; i < len; i++) {
		// if (i == j) {
		// distance[i][j] = 0;
		// } else {
		// distance[i][j] = distance[j][i] = getDistance(
		// cluster.get(i), cluster.get(j));
		// }
		// }
		// }

		// *********************************
		Statement sta = null;
		try {
			sta = con.createStatement();
			if (!DatabaseHelper.isTableExisted("DATA")) {
				String sql_create_table_DATA = "CREATE TABLE DATA ("
						+ "I INT NOT NULL" + ", J INT NOT NULL"
						+ ", DISTANCE DOUBLE NOT NULL" + ", PRIMARY KEY(I, J))"
						+ " ENGINE = InnoDB" + ";";
				sta.execute(sql_create_table_DATA);
			}
			String sql_drop_table_DATA = "TRUNCATE TABLE DATA;";
			sta.execute(sql_drop_table_DATA);

			for (int j = 0; j < len; j++) {
				for (int i = j; i < len; i++) {
					String sql_insert_table_DATA = "";
					if (i == j) {
						sql_insert_table_DATA = "INSERT INTO DATA (I, J, DISTANCE) VALUES ("
								+ i + ", " + j + ", " + 0 + ");";
						sta.execute(sql_insert_table_DATA);
					} else {
						double dis = getDistance(cluster.get(i), cluster.get(j));
						sql_insert_table_DATA = "INSERT INTO DATA (I, J, DISTANCE) VALUES ("
								+ i + ", " + j + ", " + dis + ");";
						sta.execute(sql_insert_table_DATA);
						sql_insert_table_DATA = "INSERT INTO DATA (I, J, DISTANCE) VALUES ("
								+ j + ", " + i + ", " + dis + ");";
						sta.execute(sql_insert_table_DATA);
					}
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				if (sta != null) {
					sta.close();
				}
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		// *********************************
		double[] standardDiviation = new double[len];
		for (int n = 0; n < len; n++) {
			standardDiviation[n] = countStandardDiviationByQYJ(n);
		}

		double min = standardDiviation[0];
		int index = 0;
		for (int m = 0; m < len; m++) {
			if (standardDiviation[m] < min) {
				min = standardDiviation[m];
				index = m;
			}
		}
		System.out.println("聚类中心为第" + index + "点");
		return cluster.get(index);
	}

	public double getDistance(PatentMatrix pm, PatentMatrix center) {
		double value = 0;
		// 规范化后的距离 |dx|*|dy|=1 distance=dx*dy/|dx|*|dy|=dx*dy
		if (pm.pttNum.equals(center.pttNum)) {
			return 0;
		}
		for (int i = 0; i < 20; i++) {
			value += center.value[i] * pm.value[i];
		}
		System.out.println("这是" + pm.pttNum + "和" + center.pttNum + "之间的距离,为"
				+ Math.abs(1 - value));
		return Math.abs(1 - value);
	}

	public int getNearestCluster(double[] distances) {
		int i = 0;
		double min = distances[0];
		for (int j = 1; j < distances.length; j++) {
			if (distances[j] < min) {
				min = distances[j];
				i = j;
			}
		}
		return i;
	}

	public double countStandardDiviationByCJX(double[] distance) {
		int len = distance.length;
		double sum = 0;
		for (int i = 0; i < len; i++) {
			sum += distance[i];
		}
		double average = sum / len;
		double s2 = 0;
		for (int j = 0; j < len; j++) {
			s2 += Math.pow(distance[j] - average, 2);
		}
		return Math.sqrt(s2 / len);
	}

	/**
	 * 某一点与其他点距离的标准差
	 * 
	 * @param n
	 * @return
	 * @author Vincent_Melancholy
	 */
	public double countStandardDiviationByQYJ(int n) {
		List<Double> distance = new ArrayList<Double>();
		Statement sta = null;
		ResultSet rs = null;
		try {
			sta = con.createStatement();
			if (!DatabaseHelper.isTableExisted("DATA")) {
				String sql_create_table_DATA = "CREATE TABLE DATA ("
						+ "I INT NOT NULL" + ", J INT NOT NULL"
						+ ", DISTANCE DOUBLE NOT NULL" + ", PRIMARY KEY(I, J))"
						+ " ENGINE = InnoDB" + ";";
				sta.execute(sql_create_table_DATA);
			}
			String sql_select_from_DATA = "SELECT DISTANCE FROM DATA WHERE I="
					+ n;
			rs = sta.executeQuery(sql_select_from_DATA);
			while (rs.next()) {
				distance.add(rs.getDouble(1));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				if (rs != null) {
					rs.close();
				}
				if (sta != null) {
					sta.close();
				}
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		int len = distance.size();
		double sum = 0;
		for (int i = 0; i < len; i++) {
			sum += distance.get(i);
		}
		double average = sum / len;

		double s2 = 0;
		for (int j = 0; j < len; j++) {
			s2 += Math.pow(distance.get(j) - average, 2);
		}
		return Math.sqrt(s2 / len);
	}
}
