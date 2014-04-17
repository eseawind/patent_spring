package cn.edu.scut.patent.util;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StringHelper {

	/**
	 * 把String转换成Date
	 */
	public static java.sql.Date stringToDate(String str) {
		DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		java.util.Date date = null;
		java.sql.Date dateSQL = null;
		try {
			date = format.parse(str);
			dateSQL = new java.sql.Date(date.getTime());
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return dateSQL;
	}

	/**
	 * 去除String字符串中的空格、回车、换行符、制表符
	 */
	public static String replaceSpecialCharacters(String str) {
		String dest = "";
		if (str != null) {
			Pattern p = Pattern.compile("\\s*|\t|\r|\n");
			Matcher m = p.matcher(str);
			dest = m.replaceAll("");
		}
		return dest;
	}

	/**
	 * 计时器
	 * 
	 * @param startTime
	 *            开始的时间（毫秒）
	 * @return
	 */
	public static String timer(long startTime) {
		// 结束的时间
		long endTime = new Date().getTime();
		int time = (int) ((endTime - startTime) / 1000);
		int second = time % 60;
		time /= 60;
		int minute = time % 60;
		time /= 60;
		int hour = time % 24;
		int day = time / 24;
		String result = "一共花费了" + day + "天" + hour + "小时" + minute + "分钟"
				+ second + "秒完成";
		return result;
	}
}
