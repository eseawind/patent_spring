package cn.edu.scut.patent.prework.impl;

import java.io.BufferedWriter;
import java.io.IOException;
import java.net.URL;
import java.util.List;

public interface GetPatentsFromNetworkImpl {

	/**
	 * 获取专利页面
	 */
	void doGetPatents();

	/**
	 * 取得专利列表
	 * 
	 * @param type
	 *            (专利类型)
	 * @param page
	 *            (页码)
	 */
	void getPatentsListPage(String type, int page);

	/**
	 * 把获取专利检索结果页面的URL存入TXT文件
	 * 
	 * @param type
	 * @throws IOException
	 */
	void saveAllUrlsToTxt(String type) throws IOException;

	/**
	 * 从网页列表中获取该页面所有的URL
	 * 
	 * @param filePath
	 * @param bw
	 */
	void getUrls(String filePath, BufferedWriter bw);

	/**
	 * 根据TXT下载所有的网页页面
	 * 
	 * @param type
	 */
	void downloadHtmls(String type);

	/**
	 * 下载单个URL页面
	 * 
	 * @param url
	 * @param filename
	 * @param type
	 */
	void downloadHtml(URL url, String filename, String type);

	/**
	 * 把网页中的URL整理为List<URL>的形式返回
	 * 
	 * @param sourceURL
	 *            用于识别URL的模板
	 * @param textContent
	 *            已转化为String形式的网页代码
	 * @return
	 */
	List<URL> parseAsHTML(URL sourceURL, String textContent);
}
