package cn.edu.scut.patent.core;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import cn.edu.scut.patent.model.ClusterValueItem;
import cn.edu.scut.patent.model.IndicatorData;
import cn.edu.scut.patent.model.IndicatorParam;
import cn.edu.scut.patent.model.IndicatorValueItem;
import cn.edu.scut.patent.model.Patent;
import cn.edu.scut.patent.service.PatentClusterService;
import cn.edu.scut.patent.util.StringHelper;

public class Indicator {

	/**
	 * 查询并计算技术生长率
	 * 
	 * @param param
	 */
	public static IndicatorData getTechnicalGrowthRateData(IndicatorParam param) {
		System.out.println("getTechnicalGrowthRateData");
		IndicatorData data = new IndicatorData();
		try {
			data.indicatorType = param.indicatorType;
			System.out.println(param.keyWord);

			Patent patentdao = new Patent();
			patentdao.setPttName(param.keyWord);
			List<String> pttTypeList = new ArrayList<String>();
			pttTypeList.add("11");
			pttTypeList.add("22");
			pttTypeList.add("33");
			List<Patent> patentList = new Search().doSearch(patentdao,
					pttTypeList);
			Map<String, Integer> map = new HashMap<String, Integer>();
			double sum = 0;
			if (patentList == null) {
				int thisYear = StringHelper.getThisYear();
				for (int i = 2006; i <= thisYear; i++) {
					String y = String.valueOf(i);
					// 我们统一使用List<IndicatorValueItem> value11来保存所有的数据
					data.value11.add(new IndicatorValueItem(y, 0));
				}
				return data;
			} else {
				int len = patentList.size();
				for (int i = 0; i < len; i++) {
					Patent temp = patentList.get(i);
					if (temp.getPttType().indexOf("11") >= 0) {
						String year = StringHelper.getYear(temp.getPttDate()
								.toString());
						if (map.containsKey(year)) {
							int count = map.get(year).intValue();
							count += 1;
							map.put(year, count);
						} else {
							map.put(year, 1);
						}
						sum += 1;
					}
				}

				int thisYear = StringHelper.getThisYear();
				for (int i = 2006; i <= thisYear; i++) {
					String y = String.valueOf(i);
					if (map.containsKey(y)) {
						data.value11.add(new IndicatorValueItem(y, map.get(y)
								.doubleValue() / sum));
					} else {
						data.value11.add(new IndicatorValueItem(y, 0));
					}
				}
				return data;
			}
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * 查询并计算技术成熟率
	 * 
	 * @param param
	 */
	public static IndicatorData getTechnicalMatureRateData(IndicatorParam param) {
		System.out.println("getTechnicalMatureRateData");
		IndicatorData data = new IndicatorData();
		try {
			data.indicatorType = param.indicatorType;
			System.out.println(param.keyWord);

			Patent patentdao = new Patent();
			patentdao.setPttName(param.keyWord);
			List<String> pttTypeList = new ArrayList<String>();
			pttTypeList.add("11");
			pttTypeList.add("22");
			pttTypeList.add("33");
			List<Patent> patentList = new Search().doSearch(patentdao,
					pttTypeList);
			Map<String, Integer> map11 = new HashMap<String, Integer>();
			Map<String, Integer> yearSum = new HashMap<String, Integer>();
			if (patentList == null) {
				int thisYear = StringHelper.getThisYear();
				for (int i = 2006; i <= thisYear; i++) {
					String y = String.valueOf(i);
					// 我们统一使用List<IndicatorValueItem> value11来保存所有的数据
					data.value11.add(new IndicatorValueItem(y, 0));
				}
				return data;
			} else {
				int len = patentList.size();
				for (int i = 0; i < len; i++) {
					Patent temp = patentList.get(i);
					String year = StringHelper.getYear(temp.getPttDate()
							.toString());

					if (yearSum.containsKey(year)) {
						int count = yearSum.get(year).intValue();
						count += 1;
						yearSum.put(year, count);
					} else {
						yearSum.put(year, 1);
					}

					if (temp.getPttType().indexOf("11") >= 0) {
						if (map11.containsKey(year)) {
							int count = map11.get(year).intValue();
							count += 1;
							map11.put(year, count);
						} else {
							map11.put(year, 1);
						}
					}
				}

				int thisYear = StringHelper.getThisYear();
				for (int i = 2006; i <= thisYear; i++) {
					String y = String.valueOf(i);
					if (map11.containsKey(y)) {
						data.value11.add(new IndicatorValueItem(y, map11.get(y)
								.doubleValue() / yearSum.get(y).doubleValue()));
					} else {
						data.value11.add(new IndicatorValueItem(y, 0));
					}
				}
				return data;
			}
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * 聚类查询
	 * 
	 * @param param
	 */
	public static Map<String, ClusterValueItem> getClusterData(
			IndicatorParam param) {
		System.out.println("getClusterData");
		Map<String, ClusterValueItem> map = new HashMap<String, ClusterValueItem>();
		try {
			String keyWord = param.keyWord;
			System.out.println(keyWord);

			Patent patentDao = new Patent();
			patentDao.setPttName(keyWord);
			List<String> pttTypeList = new ArrayList<String>();
			pttTypeList.add("11");
			pttTypeList.add("22");
			pttTypeList.add("33");
			List<Patent> patentList = new Search().doSearch(patentDao,
					pttTypeList);

			if (patentList == null) {
				return null;
			}
			for (Patent patentdao : patentList) {
				String temp_PTT_NUM = patentdao.getPttNum();
				int clusterNumber = new PatentClusterService()
						.find(temp_PTT_NUM);
				if (clusterNumber != -1) {
					String classNumG06Q = patentdao.getClassNumG06Q()
							.replaceAll(" ", "").substring(0, 9);
					String key = classNumG06Q + clusterNumber;
					if (map.containsKey(key)) {
						map.get(key).count += 1;
					} else {
						ClusterValueItem item = new ClusterValueItem();
						item.pttClass = getIntByClassNum(classNumG06Q);
						item.cluster = clusterNumber + 1;
						item.count = 1;
						item.keyWord = keyWord;
						map.put(key, item);
					}
				}
			}
			return map;
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * 获取ClassNumG06Q的代号
	 * 
	 * @param classnum
	 * @return
	 */
	private static int getIntByClassNum(String classnum) {
		if (classnum.equals("G06Q10/00")) {
			return 1;
		} else if (classnum.equals("G06Q20/00")) {
			return 2;
		} else if (classnum.equals("G06Q30/00")) {
			return 3;
		} else if (classnum.equals("G06Q40/00")) {
			return 4;
		} else if (classnum.equals("G06Q50/00")) {
			return 5;
		} else if (classnum.equals("G06Q90/00")) {
			return 6;
		} else if (classnum.equals("G06Q99/00")) {
			return 7;
		} else {
			return 0;
		}
	}
}
