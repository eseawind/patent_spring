package cn.edu.scut.patent.prework;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import ICTCLAS2014.Nlpir;
import cn.edu.scut.patent.model.Data;
import cn.edu.scut.patent.model.Patent;
import cn.edu.scut.patent.model.PatentCluster;
import cn.edu.scut.patent.model.PatentFeatureWord;
import cn.edu.scut.patent.model.PatentMatrix;
import cn.edu.scut.patent.model.PatentWordTfDf;
import cn.edu.scut.patent.model.PatentsAfterWordDivide;
import cn.edu.scut.patent.model.WordInfo;
import cn.edu.scut.patent.prework.impl.ClusterImpl;
import cn.edu.scut.patent.service.DataService;
import cn.edu.scut.patent.service.PatentClusterService;
import cn.edu.scut.patent.service.PatentFeatureWordService;
import cn.edu.scut.patent.service.PatentService;
import cn.edu.scut.patent.service.PatentWordTfDfService;
import cn.edu.scut.patent.service.PatentsAfterWordDivideService;
import cn.edu.scut.patent.service.StopwordService;
import cn.edu.scut.patent.service.WordInfoService;
import cn.edu.scut.patent.service.WordSmarkService;
import cn.edu.scut.patent.util.Constants;
import cn.edu.scut.patent.util.DatabaseHelper;
import cn.edu.scut.patent.util.StringHelper;

/**
 * 对专利文本进行K-means文本聚类处理
 * 
 * @author CJX
 * 
 */
public class Cluster implements ClusterImpl {

	public static Connection con;
	public Map<String, PatentWordTfDf> titleWordDic;
	public Map<String, PatentWordTfDf> abstractWordDic;
	public Map<String, PatentWordTfDf> contentWordDic;
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

		// 如果数据表PATENT_WORD_TF_DF不为空，则跳过下述函数，不再浪费资源重复计算。
		if (PatentWordTfDfService.isEmpty()) {
			PatentsAfterWordDivideService.cleanTable();
			StopwordService.cleanTable();
			WordSmarkService.cleanTable();

			long startTime = new Date().getTime();// 开始的时间
			System.out.println("将名称和摘要分词并存入patent_word_after_divide中");
			// 将名称和摘要分词并存入patent_word_after_divide
			cluster.divideWordToDb();
			String timeConsume1 = "1.花费了" + StringHelper.timer(startTime)
					+ "完成专利名称和专利摘要的分词和过滤！";
			System.out.println(timeConsume1);
			result += timeConsume1 + "\n";
		}

		// 如果数据表T_WORD_INFO不为空，则跳过下述函数，不再浪费资源重复计算。
		if (WordInfoService.isEmpty()) {
			PatentWordTfDfService.cleanTable();

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

		// 如果数据表PATENT_FEATURE_WORD不为空，则跳过下述函数，不再浪费资源重复计算。
		if (PatentFeatureWordService.isEmpty()) {
			WordInfoService.cleanTable();

			long startTime = new Date().getTime();// 开始的时间
			// 保存（word,maxTF,DF）值到t_word_info
			cluster.extractFeatureWord();
			String timeConsume3 = "3.花费了" + StringHelper.timer(startTime)
					+ "完成计算所有词权重MaxTf，根据权重提取特证词并保存到数据表 T_WORD_INFO中！";
			System.out.println(timeConsume3);
			result += timeConsume3 + "\n";
		}

		// 如果数据表PATENT_CLUSTER不为空，则跳过下述函数，不再浪费资源重复计算。
		if (PatentClusterService.isEmpty()) {
			PatentFeatureWordService.cleanTable();

			long startTime = new Date().getTime();// 开始的时间
			cluster.countAndSaveToDb(20);
			cluster.countStandardTFIDF();
			String timeConsume4 = "4.花费了" + StringHelper.timer(startTime)
					+ "完成计算并规范化TF-IDF值，提取前20位存入数据表PATENT_FEATURE_WORD中！";
			System.out.println(timeConsume4);
			result += timeConsume4 + "\n";
		}

		// 如果数据表PATENT_CLUSTER不为空，则跳过下述函数，不再浪费资源重复计算。
		if (PatentClusterService.isEmpty()) {
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

		List<Patent> list = new PatentService().getPatentsKey();
		PatentsAfterWordDivide pttAWDM;
		int count = 0;
		for (Patent patent : list) {
			pttAWDM = new PatentsAfterWordDivide();
			pttAWDM.setPttNum(patent.getPttNum());
			pttAWDM.setPttDate(patent.getPttDate());
			pttAWDM.setClassNumG06Q(patent.getClassNumG06Q());

			// 将专利名称和摘要分词
			pttAWDM.setPttNameDivided(Nlpir.doNlpirString(patent.getPttName(),
					0, null, null));
			pttAWDM.setPttAbstractDivided(Nlpir.doNlpirString(
					patent.getPttAbstract(), 0, null, null));

			// 存入到patents_after_word_divide数据表
			new PatentsAfterWordDivideService().save(pttAWDM);
			count++;

			// *************************************
			// 用于限制聚类的个数
			if (Constants.CLUSTER_LIMIT > 0) {
				if (count >= Constants.CLUSTER_LIMIT) {
					break;
				}
			}
			// *************************************
		}
	}

	public void countTF() {
		List<PatentsAfterWordDivide> list = new PatentsAfterWordDivideService()
				.getAllPatentsAfterWordDivide();

		PatentsAfterWordDivide pawd;
		String[] titleArr;
		String[] abstractArr;
		String[] contentArr;
		titleWordDic = new HashMap<String, PatentWordTfDf>();
		abstractWordDic = new HashMap<String, PatentWordTfDf>();
		contentWordDic = new HashMap<String, PatentWordTfDf>();

		// 数据集循环
		for (PatentsAfterWordDivide patentsAfterWordDivide : list) {
			pawd = patentsAfterWordDivide;

			String tempStr;
			// 以空格切开专利名词（折扣/n 卡/n 系统/n ）
			titleArr = pawd.getPttNameDivided().split(" ");
			for (int i = 0; i < titleArr.length; i++) {
				tempStr = titleArr[i];
				PatentWordTfDf p;
				// 以word_专利名称为格式判断是否唯一，如果是唯一的tf为1
				if (titleWordDic.get(tempStr + "_" + pawd.getPttNum()) == null) {
					p = new PatentWordTfDf();
					p.setFlag(1); // 1为标题
					p.setWord(tempStr);
					p.setPttNum(pawd.getPttNum());
					titleWordDic.put(tempStr + "_" + pawd.getPttNum(), p);
				}
				// 如果不唯一，tf+1
				else {
					p = titleWordDic.get(tempStr + "_" + pawd.getPttNum());
					p.setTf(p.getTf() + 1);
				}
			}

			// 以空格切开专利摘要
			abstractArr = pawd.getPttAbstractDivided().split(" ");
			for (int j = 0; j < abstractArr.length; j++) {
				tempStr = abstractArr[j];
				if (tempStr.lastIndexOf("/") != -1)
					tempStr = tempStr.substring(0, tempStr.lastIndexOf("/"));
				PatentWordTfDf p;
				if (abstractWordDic.get(tempStr + "_" + pawd.getPttNum()) == null) {
					p = new PatentWordTfDf();
					p.setFlag(0); // 0为摘要
					p.setWord(tempStr);
					p.setPttNum(pawd.getPttNum());
					abstractWordDic.put(tempStr + "_" + pawd.getPttNum(), p);
				} else {
					p = abstractWordDic.get(tempStr + "_" + pawd.getPttNum());
					p.setTf(p.getTf() + 1);
				}
			}

			// 以空格切开专利说明书
			contentArr = pawd.getPttContentDivided().split(" ");
			for (int k = 0; k < contentArr.length; k++) {
				tempStr = contentArr[k];
				if (tempStr.lastIndexOf("/") != -1) {
					tempStr = tempStr.substring(0, tempStr.lastIndexOf("/"));
				}
				PatentWordTfDf p;
				if (contentWordDic.get(tempStr + "_" + pawd.getPttNum()) == null) {
					p = new PatentWordTfDf();
					p.setFlag(2); // 设定2为专利说明书的内容
					p.setWord(tempStr);
					p.setPttNum(pawd.getPttNum());
					contentWordDic.put(tempStr + "_" + pawd.getPttNum(), p);
				} else {
					p = contentWordDic.get(tempStr + "_" + pawd.getPttNum());
					p.setTf(p.getTf() + 1);
				}
			}

			// 判断哈希表长度，防止内存溢出。
			if (abstractWordDic.size() > 60000 || titleWordDic.size() > 60000
					|| contentWordDic.size() > 60000) {
				saveWordDicToDatabase();
				abstractWordDic = new HashMap<String, PatentWordTfDf>();
				titleWordDic = new HashMap<String, PatentWordTfDf>();
				contentWordDic = new HashMap<String, PatentWordTfDf>();
				System.out.println("长度过长，保存数据，清空哈希表，继续统计！");
			}
		}
	}

	public void saveWordDicToDatabase() {
		Iterator<Map.Entry<String, PatentWordTfDf>> iterator1 = titleWordDic
				.entrySet().iterator();
		Iterator<Map.Entry<String, PatentWordTfDf>> iterator2 = abstractWordDic
				.entrySet().iterator();
		Iterator<Map.Entry<String, PatentWordTfDf>> iterator3 = contentWordDic
				.entrySet().iterator();
		Map.Entry<String, PatentWordTfDf> entry;
		while (iterator1.hasNext()) {
			entry = iterator1.next();
			new PatentWordTfDfService().save(entry.getValue());
		}
		System.out.println("成功把title的TF保存到数据表PATENT_WORD_TF_DF中!");
		while (iterator2.hasNext()) {
			entry = iterator2.next();
			new PatentWordTfDfService().save(entry.getValue());
		}
		System.out.println("成功把abstract的TF保存到数据表PATENT_WORD_TF_DF中!");
		while (iterator3.hasNext()) {
			entry = iterator3.next();
			new PatentWordTfDfService().save(entry.getValue());
		}
		System.out.println("成功把content的TF保存到数据表PATENT_WORD_TF_DF中!");
	}

	public void countDF() {
		List<String> list = new PatentWordTfDfService().getAllWord();
		int count = 0;
		for (String word : list) {
			System.out.println(count++ + "\t" + word);
			List<PatentWordTfDf> listPatentWordTfDf = new PatentWordTfDfService()
					.getAllFromWord(word);
			// 统计某词出现的文档频率
			List<String> listPttNum = new PatentWordTfDfService()
					.getAllPttNumFromWord(word);
			int df = listPttNum.size();
			System.out.println("DF : " + df);

			for (PatentWordTfDf patentWordTfDf : listPatentWordTfDf) {
				patentWordTfDf.setDf(df);
				new PatentWordTfDfService().update(patentWordTfDf);
			}
		}
	}

	public void extractFeatureWord() {
		Map<String, Number> wordMaxTFmap = new HashMap<String, Number>();
		int count = 0;
		List<String> listWord = new PatentWordTfDfService().getAllWord();
		int tempNum;
		WordInfo tempWim;
		for (String word : listWord) {
			tempNum = 0;
			List<PatentWordTfDf> listPatentWordTfDf = new PatentWordTfDfService()
					.getAllFromWord(word);
			int df = 1;
			for (PatentWordTfDf patentWordTfDf : listPatentWordTfDf) {
				df = patentWordTfDf.getDf();
				tempNum = Math.max(tempNum, patentWordTfDf.getTf());
			}
			wordMaxTFmap.put(word, tempNum);

			// 保存进数据库
			tempWim = new WordInfo();
			tempWim.setWord(word);
			tempWim.setMaxTf(tempNum);
			tempWim.setDf(df);
			new WordInfoService().save(tempWim);
			count++;
			System.out.println("map count:" + count + "\tkey:" + word
					+ "\tvalue:" + tempNum);
		}
		System.out.println("map保存成功！");
	}

	public void countAndSaveToDb(int size) {
		int n = new PatentsAfterWordDivideService().getTableSize();
		int count = 0;
		Map<String, Number> wordMaxTFmap = new HashMap<String, Number>();
		List<WordInfo> listWordInfo = new WordInfoService().getAll();

		for (WordInfo wordInfo : listWordInfo) {
			wordMaxTFmap.put(wordInfo.getWord(), wordInfo.getMaxTf());
		}
		System.out.println(wordMaxTFmap.get("web"));
		System.out.println(wordMaxTFmap.size());

		List<String> listPttNum = new PatentWordTfDfService().getAllPttNum();
		ArrayList<PatentFeatureWord> tempArr;
		for (String pttNum : listPttNum) {
			tempArr = new ArrayList<PatentFeatureWord>();

			List<PatentWordTfDf> listPatentWordTfDf = new PatentWordTfDfService()
					.getAllFromPttNum(pttNum);
			for (PatentWordTfDf patentWordTfDf : listPatentWordTfDf) {
				int tf = patentWordTfDf.getTf();
				int maxTf = 0;
				if (wordMaxTFmap.get(patentWordTfDf.getWord()) == null) {
					System.out.println(patentWordTfDf.getWord()
							+ "\tnullpointerexception!");
					continue;
				} else {
					maxTf = wordMaxTFmap.get(patentWordTfDf.getWord())
							.intValue();
				}
				int df = patentWordTfDf.getDf();
				Number tempValue = (0.5 + 0.5 * tf / maxTf)
						* (Math.log(n / df));
				PatentFeatureWord pfwm = new PatentFeatureWord(pttNum,
						patentWordTfDf.getWord(), tempValue.doubleValue());
				tempArr.add(pfwm);
			}

			for (int i = 0; i < tempArr.size() - 1; i++) {
				for (int j = i + 1; j < tempArr.size(); j++) {
					if (tempArr.get(i).getTfIdfValue() < tempArr.get(j)
							.getTfIdfValue()) {
						PatentFeatureWord tempPfwm = tempArr.get(i);
						tempArr.set(i, tempArr.get(j));
						tempArr.set(j, tempPfwm);
					}
				}
			}

			int len = Math.min(size, tempArr.size());
			for (int m = 0; m < len; m++) {
				// 写入数据库
				new PatentFeatureWordService().save(tempArr.get(m));
			}
			count++;
			System.out.println("第" + count + "个专利" + pttNum + "的特征词数量为" + len);
		}
	}

	public void countStandardTFIDF() {
		List<String> listPttNum = new PatentService().getAllPttNum();
		for (String pttNum : listPttNum) {
			List<PatentFeatureWord> listPatentFeatureWord = new PatentFeatureWordService()
					.getAllFromPttNum(pttNum);
			double sum = 0;
			for (int i = 0; i < listPatentFeatureWord.size(); i++) {
				sum += Math
						.pow(listPatentFeatureWord.get(i).getTfIdfValue(), 2);
			}

			sum = Math.sqrt(sum);
			for (int j = 0; j < listPatentFeatureWord.size(); j++) {
				PatentFeatureWord patentFeatureWord = listPatentFeatureWord
						.get(j);
				patentFeatureWord.setTfIdfValueStandard(patentFeatureWord
						.getTfIdfValue() / sum);
				new PatentFeatureWordService().update(patentFeatureWord);
			}
			System.out.println(pttNum);
		}
	}

	@SuppressWarnings("unchecked")
	public void clusterByCJX(int k) {
		List<PatentMatrix> pttMatrix = new ArrayList<PatentMatrix>();
		int n = 0;

		List<String> listPttNum = new PatentService().getAllPttNum();
		for (String pttNum : listPttNum) {
			PatentMatrix pm = new PatentMatrix();
			pm.pttNum = pttNum;

			List<PatentFeatureWord> listPatentFeatureWord = new PatentFeatureWordService()
					.getAllFromPttNum(pttNum);
			int index = 0;
			double sum = 0;
			for (PatentFeatureWord patentFeatureWord : listPatentFeatureWord) {
				pm.value[index] = patentFeatureWord.getTfIdfValueStandard();
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

		for (int i = 0; i < n; i++) {
			new PatentClusterService().save(new PatentCluster(
					pttMatrix.get(i).pttNum, clusterAssignments[i]));
		}
		System.out.println("聚类成功！");
	}

	@SuppressWarnings("unchecked")
	public void clusterByQYJ(int k) {
		// 以专利为单位，存储所有专利所有特征词的权重
		System.out.println("以专利为单位，存储所有专利所有特征词的权重");
		List<PatentMatrix> pttMatrix = new ArrayList<PatentMatrix>();
		int n = 0;
		List<String> listPttNum = new PatentFeatureWordService().getAllPttNum();
		for (String pttNum : listPttNum) {
			PatentMatrix pm = new PatentMatrix();
			pm.pttNum = pttNum;
			List<PatentFeatureWord> listPatentFeatureWord = new PatentFeatureWordService()
					.getAllFromPttNum(pttNum);
			int index = 0;
			double sum = 0;
			for (PatentFeatureWord patentFeatureWord : listPatentFeatureWord) {
				pm.value[index] = patentFeatureWord.getTfIdfValueStandard();
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

		for (int i = 0; i < n; i++) {
			new PatentClusterService().save(new PatentCluster(
					pttMatrix.get(i).pttNum, clusterAssignments[i]));
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

		DataService.cleanTable();
		for (int j = 0; j < len; j++) {
			for (int i = j; i < len; i++) {
				if (i == j) {
					new DataService().save(new Data(i, j, 0));
				} else {
					double dis = getDistance(cluster.get(i), cluster.get(j));
					new DataService().save(new Data(i, j, dis));
					new DataService().save(new Data(j, i, dis));
				}
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
		List<Double> distance = new DataService().getAllDistanceFromI(n);
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
