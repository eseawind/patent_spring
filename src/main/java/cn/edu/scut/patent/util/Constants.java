package cn.edu.scut.patent.util;

public class Constants {
	// 下述部分文件夹位置的根目录
	private static String DIRECTORY_ROOT = "E:/dir";
	// PDF索引文件(夹)的位置
	public static String FILE_DIR_STRING = "E:/code/Juno workplace/patent_spring/src/main/webapp/file";
	// 存放索引文件的位置
	public static String INDEX_DIR_STRING = DIRECTORY_ROOT + "/index";
	// 获取PDF文档当中图片的存放位置
	public static String GET_IMAGES_FROM_PDF_DIR_STRING = DIRECTORY_ROOT
			+ "/index";
	// 把PDF文档转化为图片的存放位置
	public static String TRANSFER_PDF_TO_IMAGES_DIR_STRING = DIRECTORY_ROOT
			+ "/images";
	// 转化图片的类型
	public static String TYPE = "TIF";
	// 存放NLPIR.dll的绝对路径
	public static String NLPIR_DLL_STRING = DIRECTORY_ROOT
			+ "/ICTCLAS2014/NLPIR";
	// ICTCLAS词库library(即Data文件夹)的绝对路径
	public static String ICTCLAS_LIBRARY_STRING = DIRECTORY_ROOT
			+ "/ICTCLAS2014";
	// MySql的访问路径
	public static String MYSQL_URL = "jdbc:mysql://localhost/patentdb";
	// MySql的账户名
	public static String MYSQL_ACCOUNT = "root";
	// MySql的账户密码
	public static String MYSQL_PASSWORD = "123";
	// 中国知识局的查询接口
	public static String CHINA_SIPO_URL = "http://211.157.104.87:8080/sipo/zljs/hyjs-jieguo.jsp";
	// 中国知识局单个专利展示接口
	public static String CHINA_SIPO_SHOW_URL = "http://211.157.104.87:8080/sipo/zljs/";
	// 存放网页目录的地址
	public static String WEBSITE_DIRECTORY_PATH = DIRECTORY_ROOT
			+ "/patents/patentlist/patentListPage/";
	// 获取专利检索结果页面URL的TXT文件存放地址
	public static String URL_TXT_PATH = DIRECTORY_ROOT
			+ "/patents/patentlist/patentListPage/";
	// 存放专利网页的地址
	public static String WEBSITE_PATH = DIRECTORY_ROOT
			+ "/patents/patentPages/";
	// 技术生长率的指标号码
	public static int TECHNICAL_GROWTH_RATE_NUMBER = 101;
	// 技术成熟率的指标号码
	public static int TECHNICAL_MATURE_RATE_NUMBER = 102;
}
