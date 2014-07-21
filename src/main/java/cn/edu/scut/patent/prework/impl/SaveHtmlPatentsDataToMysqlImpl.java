package cn.edu.scut.patent.prework.impl;

import java.io.File;
import java.io.IOException;
import org.htmlparser.util.ParserException;
import cn.edu.scut.patent.model.Patent;

public interface SaveHtmlPatentsDataToMysqlImpl {

	/**
	 * 保存专利信息到数据库
	 */
	void doSaveToMysql();

	/**
	 * 按照专利的类型执行保存工作
	 * 
	 * @param type
	 */
	void save(String type);

	/**
	 * 把单个网页的数据保存到数据库中
	 * 
	 * @param file
	 * @param type
	 * @throws IOException
	 */
	void savePatentToDatabase(File file, String type) throws IOException;

	/**
	 * 把HTML中的数据格式化为PatentDao类型
	 * 
	 * @param html
	 * @param type
	 * @return
	 * @throws ParserException
	 */
	Patent parseHtmlToPatentDao(String html, String type)
			throws ParserException;
}
