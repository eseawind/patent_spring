package cn.edu.scut.patent.prework.impl;

import java.util.ArrayList;
import cn.edu.scut.patent.model.PatentMatrix;

public interface ClusterImpl {

	/**
	 * 进行聚类
	 */
	void doCluster();

	/**
	 * 将专利名称和专利摘要分词并过滤（根据t_stopword(具体某字词)和t_word_smark(词性)），
	 * 之后将处理好的专利名称和摘要等信息传入到patents_after_word_divide。
	 * 
	 * 在数据表PATENTS_AFTER_WORD_DIVIDE中输入所有的分词内容
	 */
	void divideWordToDb();

	/**
	 * 分开统计在名称和摘要中某个词语出现的频率，以（词语_专利号，PatentWordTFIDFModel（id,pttNum,word,tf,df,
	 * flag））为一个元素保存于HashMap中，以词语_专利号为唯一标识。
	 * 
	 * 统计词语在某个文档中出现的频率TF，并保存到HashMap中
	 */
	void countTF();

	/**
	 * 将两个字典存入数据库
	 * 
	 * 把HashMap中统计词语在某个文档中出现的频率TF并保存到数据表PATENT_WORD_TF_DF中
	 */
	void saveWordDicToDatabase();

	/**
	 * 计算所有词的文档频数DF，并保存到数据表PATENT_WORD_TF_DF中
	 */
	void countDF();

	/**
	 * 计算所有词权重MaxTf，按权重提取特证词，并保存到数据表 T_WORD_INFO中
	 */
	void extractFeatureWord();

	/**
	 * 计算TF-IDF值并提取前size位存入数据表PATENT_FEATURE_WORD中
	 * 
	 * @param size
	 *            为前多少位词语作为特征词。
	 */
	void countAndSaveToDb(int size);

	/**
	 * 规范化TF-IDF值，使同一篇文档中所有特征权重的平方和等于1
	 * 
	 * 更新数据表PATENT_FEATURE_WORD中的TFIDF_VALUE_STANDARD
	 */
	void countStandardTFIDF();

	/**
	 * 根据patent_feature_word表对专利进行分类和k-Means聚类
	 * 
	 * @param k
	 *            为聚类中心个数
	 * @author CJX
	 */
	void clusterByCJX(int k);

	/**
	 * 根据patent_feature_word表对专利进行分类和k-Means聚类
	 * 
	 * @param k
	 *            为聚类中心个数
	 * @author Vincent_Melancholy
	 */
	void clusterByQYJ(int k);

	/**
	 * 获取聚类的中心点
	 * 
	 * @param cluster
	 * @return
	 */
	PatentMatrix getClusterCenter(ArrayList<PatentMatrix> cluster);

	/**
	 * 计算两个专利之间的距离
	 * 
	 * @param pm
	 * @param center
	 * @return
	 */
	double getDistance(PatentMatrix pm, PatentMatrix center);

	/**
	 * 获取某个专利距离最近的聚类群的编号
	 * 
	 * @param distances
	 * @return
	 */
	int getNearestCluster(double[] distances);

	/**
	 * 某一点与其他点距离的标准差
	 * 
	 * @param distance
	 * @return
	 * @author CJX
	 */
	double countStandardDiviationByCJX(double[] distance);

	/**
	 * 某一点与其他点距离的标准差
	 * 
	 * @param n
	 * @return
	 * @author Vincent_Melancholy
	 */
	double countStandardDiviationByQYJ(int n);
}
