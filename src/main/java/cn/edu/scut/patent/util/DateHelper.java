package cn.edu.scut.patent.util;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;

public class DateHelper {

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
}
