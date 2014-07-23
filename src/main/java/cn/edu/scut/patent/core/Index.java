package cn.edu.scut.patent.core;

import java.io.File;
import java.util.Date;
import java.util.List;
import java.util.Map;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;
import cn.edu.scut.patent.ICTCLASAnalyzer.ICTCLASAnalyzer;
import cn.edu.scut.patent.core.impl.IndexImpl;
import cn.edu.scut.patent.util.DatabaseHelper;
import cn.edu.scut.patent.prework.GetPatentsFromNetwork;
import cn.edu.scut.patent.prework.SaveHtmlPatentsDataToMysql;
import cn.edu.scut.patent.service.PatentService;
import cn.edu.scut.patent.util.Constants;
import cn.edu.scut.patent.util.FileHelper;
import cn.edu.scut.patent.util.PDFHelper;
import cn.edu.scut.patent.util.StringHelper;

/**
 * 建立索引
 * 
 * @author Vincent_Melancholy
 * 
 */
public class Index implements IndexImpl {

	public void doIndexFromDatabase() throws Exception {
		doIndexPrework();
		// 索引开始的时间
		long startTime = new Date().getTime();
		Analyzer analyzer = new ICTCLASAnalyzer(Version.LUCENE_46);
		// 存放索引文件的位置
		Directory directory = FSDirectory.open(new File(
				Constants.INDEX_DIR_STRING));
		if (IndexWriter.isLocked(directory)) {
			IndexWriter.unlock(directory);
		}
		IndexWriterConfig config = new IndexWriterConfig(Version.LUCENE_46,
				analyzer);
		// 设置打开索引模式为创建或追加
		config.setOpenMode(IndexWriterConfig.OpenMode.CREATE);
		// config.setOpenMode(IndexWriterConfig.OpenMode.CREATE_OR_APPEND);
		IndexWriter indexWriter = new IndexWriter(directory, config);
		List<Document> listDocument = new PatentService()
				.getDocumentsFromPatents(analyzer);
		if (listDocument == null || listDocument.size() == 0) {
			return;
		}
		for (int i = 0; i < listDocument.size(); i++) {
			indexWriter.addDocument(listDocument.get(i));
		}
		// 提交事务
		indexWriter.commit();
		indexWriter.close();
		System.out.println("一共花费了" + StringHelper.timer(startTime) + "完成索引！");
	}

	public void doIndexFromPDF() throws Exception {
		Map<String, String> map = FileHelper
				.readfile(Constants.FILE_DIR_STRING);
		int i = 1;
		for (Map.Entry<String, String> entry : map.entrySet()) {
			System.out.println(i++ + "#######################################");
			// 索引文件(夹)的位置
			File fileDir = new File(entry.getValue());
			Analyzer analyzer = new ICTCLASAnalyzer(Version.LUCENE_46);
			// 存放索引文件的位置
			Directory directory = FSDirectory.open(new File(
					Constants.INDEX_DIR_STRING));
			IndexWriterConfig config = new IndexWriterConfig(Version.LUCENE_46,
					analyzer);
			// 设置打开索引模式为创建或追加
			config.setOpenMode(IndexWriterConfig.OpenMode.CREATE_OR_APPEND);
			IndexWriter indexWriter = new IndexWriter(directory, config);
			// 索引开始的时间
			long startTime = new Date().getTime();
			Document document = PDFHelper.getDocumentFromPDF(fileDir, analyzer);
			indexWriter.addDocument(document);
			// 提交事务
			indexWriter.commit();
			indexWriter.close();
			// 索引结束的时间
			long endTime = new Date().getTime();
			System.out.println("一共花费了" + (endTime - startTime) + "毫秒完成索引！");
		}
	}

	public void doIndexPrework() {
		DatabaseHelper.checkMySQL();
		if (!FileHelper.hasFiles(Constants.WEBSITE_PATH)) {
			System.out.println("开始进行GetPatents专利网页下载工作。");
			GetPatentsFromNetwork getPatentsFromNetwork = new GetPatentsFromNetwork();
			getPatentsFromNetwork.doGetPatents();
			System.out.println("GetPatents专利网页下载工作结束！");
		} else {
			System.out.println("专利网页已经存在，不需要进行GetPatents网页下载，跳过。");
		}
		if (PatentService.isEmpty()) {
			System.out.println("开始把保存下来专利网页的专利信息保存到数据库。");
			SaveHtmlPatentsDataToMysql saveHtmlPatentsDataToMysql = new SaveHtmlPatentsDataToMysql();
			saveHtmlPatentsDataToMysql.doSaveToMysql();
			System.out.println("专利信息保存到数据库完成！");
		} else {
			System.out.println("PATENTS数据库已经存在，不需要进行专利信息保存到数据库工作，跳过。");
		}
	}
}
