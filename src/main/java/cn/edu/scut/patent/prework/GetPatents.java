package cn.edu.scut.patent.prework;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import cn.edu.scut.patent.util.Constants;

/**
 * 专利抓取GetPatents
 * 
 * 根据国家知识产权局专利检索，将检索到的专利页面保存在本地
 */
public class GetPatents {

	public static void main(String[] args) {
		doGetPatents();
	}

	/**
	 * 开放给索引调用的接口
	 */
	public static void doGetPatents() {
		GetPatents getPatents = new GetPatents();
		// 获取商业专利方法专利列表页面，已知商业方法专利中发明型专利11525/27048个，实用新型专利888/3311个
		// 在这里用了最笨的方法，一个页面专利数100/20，总共有发明型专利页面116/1353页，实用新型专利9/166页，所以用了两个for循环抓取专利列表页面
		for (int i = 1; i <= 2; i++) {
			getPatents.getPatentListPage("11", i);
		}
		for (int j = 1; j <= 2; j++) {
			getPatents.getPatentListPage("22", j);
		}
		// 提取抓取下来的专利列表页面中每个专利的专利基本信息网页的URL
		try {
			getPatents.getAllUrls("11");
			getPatents.getAllUrls("22");
		} catch (IOException e) {
			e.printStackTrace();
		}
		// 根据提取出来的专利基本信息页面的URL，抓取各专利基本信息网页
		getPatents.downloadHtmls("11");
		getPatents.downloadHtmls("22");
	}

	/**
	 * 取得专利列表
	 * 
	 * @param type
	 *            (专利类型)
	 * @param page
	 *            (页码)
	 */
	private void getPatentListPage(String type, int page) {

		// 中国知识局的查询接口
		String url = Constants.CHINA_SIPO_URL;
		PostMethod postMethod = new UTF8PostMethod(url);

		// 填入各个表单域的值
		NameValuePair[] data = { new NameValuePair("recshu", "20"),
				new NameValuePair("searchword", "分类号=('%G06Q%')"),
				new NameValuePair("flag3", "1"),
				new NameValuePair("selectbase", type),
				new NameValuePair("pg", String.valueOf(page)),
				new NameValuePair("sign", "0"), };

		// 将表单的值放入postMethod中
		postMethod.setRequestBody(data);

		// 执行postMethod
		HttpClient httpClient = new HttpClient();
		httpClient.getHttpConnectionManager().getParams()
				.setConnectionTimeout(30000);
		httpClient.getHttpConnectionManager().getParams().setSoTimeout(300000);

		try {
			int statusCode = httpClient.executeMethod(postMethod);
			if (statusCode != HttpStatus.SC_OK) {
				System.err.println("Method failed: "
						+ postMethod.getStatusLine());
			}
			// 读取内容
			InputStream responseBody = postMethod.getResponseBodyAsStream();
			// 这里的编码规则要与上面的相对应
			BufferedReader br = new BufferedReader(new InputStreamReader(
					responseBody, "gb2312"));
			String tempbf;
			StringBuffer html = new StringBuffer(100);

			while ((tempbf = br.readLine()) != null) {
				html.append(tempbf + "\n");
			}

			if (html.length() < 100) {
				throw new Exception("页面错误！" + html.length());
			}
			String filepath = Constants.WEBSITE_DIRECTORY_PATH + type
					+ "/patentListPage" + page + ".html";
			File f = new File(filepath);
			if (!f.exists()) {
				// f.mkdir();
				f.createNewFile();
			}
			FileWriter wt = new FileWriter(f);
			BufferedWriter bw = new BufferedWriter(wt);

			bw.write(html.toString());
			bw.flush();
			bw.close();
			System.out.println("专利列表页面保存成功！---" + type + "  " + page);

		} catch (SocketTimeoutException e) {
			System.err.println("页面无法访问,重试！  错误信息1：" + e.getMessage()
					+ "    type:" + type + "    page:" + page);
			postMethod.releaseConnection();
			getPatentListPage(type, page);
		} catch (Exception e) {
			System.err.println("页面无法访问,重试！  错误信息2：" + e.getMessage()
					+ "    type:" + type + "    page:" + page);
			postMethod.releaseConnection();
			getPatentListPage(type, page);
		}
		postMethod.releaseConnection();
	}

	/**
	 * 获取HTML字节流
	 * 
	 * @param pageURL
	 * @param encoding
	 * @return
	 */
	private StringBuilder getHTML(String pageURL, String encoding) {
		StringBuilder pageHTML = new StringBuilder();
		try {
			URL url = new URL(pageURL);
			HttpURLConnection connection = (HttpURLConnection) url
					.openConnection();
			connection.setRequestProperty("User-Agent", "MSIE 7.0");
			BufferedReader br = new BufferedReader(new InputStreamReader(
					connection.getInputStream(), encoding));
			String line = null;
			while ((line = br.readLine()) != null) {
				pageHTML.append(line);
				pageHTML.append("\r\n");
			}
			connection.disconnect();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return pageHTML;
	}

	/**
	 * 从soopat取得专利列表
	 * 
	 * @param type
	 * @param page
	 */
	private void getPatentListPage2(String type, int page) {

		// soopat的查询接口
		String url = "http://www2.soopat.com/Home/Result?SearchWord=FLH%3A(G06Q)%20&FMZL=Y&SYXX=Y&WGZL=Y&PatentIndex=10";
		try {
			StringBuilder html = getHTML(url, "utf-8");
			if (html.length() < 100) {
				throw new Exception("页面错误！" + html.length());
			}
			String filepath = "E:/dir/patents/patentlist/patentListPage/"
					+ type + "/patentListPage" + page + ".html";
			File f = new File(filepath);
			if (!f.exists()) {
				f.createNewFile();
			}
			FileWriter wt = new FileWriter(f);
			BufferedWriter bw = new BufferedWriter(wt);
			bw.write(html.toString());
			bw.flush();
			bw.close();
			System.out.println("专利列表页面保存成功！---" + type + "  " + page);

		} catch (SocketTimeoutException e) {
			System.err.println("页面无法访问,重试！  错误信息1：" + e.getMessage()
					+ "    type:" + type + "    page:" + page);
			getPatentListPage(type, page);
		} catch (Exception e) {
			System.err.println("页面无法访问,重试！  错误信息2：" + e.getMessage()
					+ "    type:" + type + "    page:" + page);
			getPatentListPage(type, page);
		}
	}

	/**
	 * 把获取专利检索结果页面的URL存入TXT文件
	 * 
	 * @param type
	 * @throws IOException
	 */
	private void getAllUrls(String type) throws IOException {
		File urlsfile = new File(Constants.URL_TXT_PATH + "patentList" + type
				+ "Url.txt");
		if (urlsfile.exists()) {
			urlsfile.delete();
		}
		FileWriter wt = new FileWriter(urlsfile, true);
		BufferedWriter bw = new BufferedWriter(wt);

		File file = new File(Constants.WEBSITE_DIRECTORY_PATH + type + "/");
		File[] name = file.listFiles();
		int l = name.length;
		for (int i = 0; i < l; i++) {
			currentFileName = name[i].toString();
			getUrls(name[i].toString(), bw);
		}
		bw.flush();
		bw.close();
	}

	private String currentFileName;

	/**
	 * 从网页目录中获取URL
	 * 
	 * @param filePath
	 * @param bw
	 */
	private void getUrls(String filePath, BufferedWriter bw) {
		List<URL> results = new ArrayList<URL>();
		try {
			FileInputStream content = new FileInputStream(new File(filePath));
			BufferedReader br = new BufferedReader(new InputStreamReader(
					content, "gb2312"));
			String tempbf;
			StringBuffer html = new StringBuffer(100);
			while ((tempbf = br.readLine()) != null) {
				html.append(tempbf + "\n");
			}
			URL u = new URL(Constants.CHINA_SIPO_SHOW_URL);
			results = parseLinksInDocument(u, html.toString());
			for (Iterator<URL> i = results.iterator(); i.hasNext();) {
				URL url = (URL) i.next();
				String s = url.toExternalForm();
				String match = "zljs/hyjs-yx-new.jsp";
				if (s.indexOf(match) != -1) {
					bw.write(s + "\n");
				}
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (MalformedURLException e1) {
			e1.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 根据TXT下载网页页面
	 * 
	 * @param type
	 */
	private void downloadHtmls(String type) {

		try {
			File f = new File(Constants.WEBSITE_PATH);
			if (!f.exists()) {
				f.mkdir();
			}
			File f2 = new File(Constants.WEBSITE_PATH + type);
			if (!f2.exists()) {
				f2.mkdir();
			}
			FileInputStream fis = new FileInputStream(Constants.URL_TXT_PATH
					+ "patentList" + type + "Url.txt");
			BufferedReader br = new BufferedReader(new InputStreamReader(fis,
					"gb2312"));
			String tempbf;

			int n = 1;
			try {
				while ((tempbf = br.readLine()) != null) {

					URL url = new URL(tempbf);
					String s = "" + n;
					n++;
					File urlsfile = new File(Constants.WEBSITE_PATH + type
							+ "/" + s + ".html");
					if (urlsfile.exists()) {
						continue;
					} else {
						down(url, s, type);
					}
				}
			} catch (MalformedURLException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}

	}

	/**
	 * 单个下载URL页面
	 * 
	 * @param url
	 * @param filename
	 * @param type
	 */
	private void down(URL url, String filename, String type) {
		try {
			URLConnection uc = url.openConnection();

			uc.setConnectTimeout(300000);
			uc.setReadTimeout(600000);
			BufferedReader br = new BufferedReader(new InputStreamReader(
					uc.getInputStream(), "gb2312"));
			String tempbf;
			StringBuffer html = new StringBuffer(100);

			while ((tempbf = br.readLine()) != null) {
				html.append(tempbf + "\n");
			}

			if (html.toString().indexOf("数据库中无符合条件的专利记录") != -1) {
				throw new Exception("页面错误，重新下载！" + url.toString());
			}
			File urlsfile = new File(Constants.WEBSITE_PATH + type + "/"
					+ filename + ".html");
			FileWriter wt = new FileWriter(urlsfile);
			BufferedWriter bw = new BufferedWriter(wt);

			bw.write(html.toString());

			bw.flush();
			bw.close();
			System.out.println(filename + "\t" + url.toString());
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (SocketTimeoutException e) {
			System.err.println("页面无法下载,重试" + e.getMessage() + "    url:"
					+ url.toString());
			// down(url, filename, type);
		} catch (SocketException e) {
			// e.printStackTrace();
			System.err.println("页面无法下载,重试" + e.getMessage() + "    url:"
					+ url.toString());
			// down(url, filename, type);
		} catch (IOException e) {
			System.err.println(e.getMessage() + "    url:" + url.toString());
			down(repalceUrl(url), filename, type);
		} catch (Exception e) {
			System.err.println(e.getMessage());
			down(url, filename, type);
		}

	}

	/**
	 * 替换URL
	 * 
	 * @param url
	 * @return
	 */
	private URL repalceUrl(URL url) {
		String s = url.toExternalForm();
		s = s.replaceAll(" ", "%20");
		URL temp = null;
		try {
			temp = new URL(s);
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
		return temp;

	}

	private List<URL> parseLinksInDocument(URL sourceURL, String textContent) {
		return parseAsHTML(sourceURL, textContent);
	}

	private List<URL> parseAsHTML(URL sourceURL, String textContent) {
		ArrayList<URL> newURLs = new ArrayList<URL>();
		HashSet<URL> newURLSet = new HashSet<URL>();

		extractAttributesFromTags("img", "src", sourceURL, newURLs, newURLSet,
				textContent);
		extractAttributesFromTags("a", "href", sourceURL, newURLs, newURLSet,
				textContent);
		extractAttributesFromTags("body", "background", sourceURL, newURLs,
				newURLSet, textContent);
		extractAttributesFromTags("frame", "src", sourceURL, newURLs,
				newURLSet, textContent);
		extractAttributesFromTags("IMG", "SRC", sourceURL, newURLs, newURLSet,
				textContent);
		extractAttributesFromTags("A", "HREF", sourceURL, newURLs, newURLSet,
				textContent);
		extractAttributesFromTags("BODY", "BACKGROUND", sourceURL, newURLs,
				newURLSet, textContent);
		extractAttributesFromTags("FRAME", "SRC", sourceURL, newURLs,
				newURLSet, textContent);

		System.out.println("Returning " + newURLs.size()
				+ " urls extracted from page " + currentFileName);
		return newURLs;
	}

	private void extractAttributesFromTags(String tag, String attr,
			URL sourceURL, List<URL> newURLs, Set<URL> newURLSet, String input) {

		int startPos = 0;
		String startTag = "<" + tag + " ";
		String attrStr = attr + "=\"";
		while (true) {
			int tagPos = input.indexOf(startTag, startPos);
			if (tagPos < 0) {
				return;
			}
			int attrPos = input.indexOf(attrStr, tagPos + 1);
			if (attrPos < 0) {
				startPos = tagPos + 1;
				continue;
			}
			int nextClosePos = input.indexOf(">", tagPos + 1);
			if (attrPos < nextClosePos) {

				int closeQuotePos = input.indexOf("\"",
						attrPos + attrStr.length() + 1);
				if (closeQuotePos > 0) {
					String urlStr = input.substring(attrPos + attrStr.length(),
							closeQuotePos);
					if (urlStr.indexOf('#') != -1) {
						urlStr = urlStr.substring(0, urlStr.indexOf('#'));
					}

					if (isMailTo(urlStr)) {
						logMailURL(urlStr);
					} else {
						try {

							URL u = new URL(sourceURL, urlStr);
							if (newURLSet.contains(u)) {
								// _logClass.debug("Already found URL on page: "
								// + u);
							} else {
								newURLs.add(u);
								newURLSet.add(u);
								// _logClass.debug("Found new URL on page: " +
								// u);
							}
						} catch (MalformedURLException murle) {
						}
					}
				}
				startPos = tagPos + 1;
				continue;
			} else {
				startPos = tagPos + 1;
				continue;
			}
		}
	}

	private void logMailURL(String url) {

		try {
			FileWriter appendedFile = new FileWriter("d:/logmail.txt", true);
			PrintWriter pW = new PrintWriter(appendedFile);
			pW.println(url);
			pW.flush();
			pW.close();
		} catch (IOException ioe) {
			System.out.println("Caught IO exception writing mailto URL:"
					+ ioe.getMessage());
		}
	}

	/**
	 * Check if a particular URL looks like it's a mailto: style link.
	 * 
	 * @param url
	 * @return
	 */
	private boolean isMailTo(String url) {
		if (url == null) {
			return false;
		}

		url = url.toUpperCase();
		return (url.indexOf("MAILTO:") != -1);
	}

	/**
	 * Inner class for UTF-8 support
	 * 
	 * @author Vincent_Melancholy
	 * 
	 */
	public static class UTF8PostMethod extends PostMethod {
		public UTF8PostMethod(String url) {
			super(url);
		}

		@Override
		public String getRequestCharSet() {
			// return super.getRequestCharSet();
			return "gb2312";
		}
	}

	/**
	 * Inner class for UTF-8 support
	 * 
	 * @author Vincent_Melancholy
	 * 
	 */
	public static class UTF8GetMethod extends GetMethod {
		public UTF8GetMethod(String url) {
			super(url);
		}

		@Override
		public String getRequestCharSet() {
			// return super.getRequestCharSet();
			return "utf-8";
		}
	}

}
