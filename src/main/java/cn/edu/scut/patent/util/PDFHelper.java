package cn.edu.scut.patent.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import net.paoding.analysis.analyzer.PaodingAnalyzer;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.cjk.CJKAnalyzer;
import org.apache.lucene.analysis.cn.ChineseAnalyzer;
import org.apache.lucene.analysis.cn.smart.SmartChineseAnalyzer;
import org.apache.lucene.analysis.core.StopAnalyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.TextField;
import org.apache.lucene.util.Version;
import org.apache.pdfbox.pdfparser.PDFParser;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.util.PDFTextStripper;
import org.wltea.analyzer.lucene.IKAnalyzer;
import ICTCLAS2014.Nlpir;
import cn.edu.scut.patent.dao.DatabaseHelper;
import cn.edu.scut.patent.model.PatentDao;

public class PDFHelper {

	/**
	 * 获取PDF文档 请先把已加密的PDF文档解密 采用fontbox-1.8.4和pdfbox-1.8.4支持
	 * 
	 * @throws Exception
	 */
	public static Document getDocumentFromPDF(File pdf, Analyzer analyzer)
			throws Exception {
		String pdfpath = pdf.getAbsolutePath();
		// 创建输入流读取pdf文件
		String title = pdf.getName();
		String result = "";
		FileInputStream is = null;
		PDDocument doc = null;
		PatentDao patentdao = null;
		try {
			is = new FileInputStream(pdf);
			PDFParser parser = new PDFParser(is);
			parser.parse();
			doc = parser.getPDDocument();
			PDFTextStripper stripper = new PDFTextStripper();
			result = stripper.getText(doc);
			System.out.println(result);
			// 获取图片
			// getImagesInPDF(doc, title);
			// 转化图片
			// transferPDFToImages(doc, title, Constants.TYPE);
			// 获取专利的各项属性
			patentdao = getPatentDetails(result);
			DatabaseHelper.savePatentsToDatabase(patentdao);

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (is != null) {
				try {
					is.close();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			if (doc != null) {
				try {
					doc.close();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
		Document document = new Document();
		document.add(new TextField("title", Nlpir.doNlpirString(title, 0, null,
				null), Field.Store.YES));
		document.add(new TextField("path", pdfpath, Field.Store.YES));

		Map<String, String> map = patentdao.getAll();
		// 遍历map把各项专利的属性加入到document当中
		if (map != null) {
			Set<String> keySet = map.keySet();
			Iterator<String> it = keySet.iterator();
			while (it.hasNext()) {
				String key = it.next();
				String value = Nlpir.doNlpirString(map.get(key), 0, null, null);
				document.add(new TextField(key, value, Field.Store.YES));
			}
		}
		// 打印分词结果
		CheckHelper.printKeyWords(analyzer, patentdao);
		// 测试所有Analyzer的分词结果
		// testAllAnalyzer(patentdao);
		return document;
	}

	/**
	 * 从SOOPAT的PDF专利文档获取专利的各个属性
	 */
	private static PatentDao getPatentDetails(String text) {
		String[] result = text.split("\n");
		PatentDao patentdao = new PatentDao();

		// 获取专利名称
		for (int i = 0; i < result.length; i++) {
			if (result[i].startsWith("SooPAT")) {
				patentdao.setPttName(result[i + 1]);
			}
		}
		// 获取申请号
		for (int i = 0; i < result.length; i++) {
			if (result[i].startsWith("申请号：")) {
				patentdao.setApplyNum(result[i].substring(4));
			}
		}
		// 获取申请日
		for (int i = 0; i < result.length; i++) {
			if (result[i].startsWith("申请日：")) {
				patentdao.setApplyDate(StringHelper.stringToDate(result[i]
						.substring(4)));
			}
		}
		// 获取申请(专利权)人
		for (int i = 0; i < result.length; i++) {
			if (result[i].startsWith("申请(专利权)人")) {
				patentdao.setProposer(result[i].substring(8));
			}
		}
		// 获取地址
		for (int i = 0; i < result.length; i++) {
			if (result[i].startsWith("地址")) {
				patentdao.setProposerAddress(result[i].substring(3));
			}
		}
		// 获取发明(设计)人
		for (int i = 0; i < result.length; i++) {
			if (result[i].startsWith("发明(设计)人")) {
				patentdao.setInventor(result[i].substring(7));
			}
		}
		// 获取主分类号
		for (int i = 0; i < result.length; i++) {
			if (result[i].startsWith("主分类号")) {
				patentdao.setPttMainClassNum(result[i].substring(5));
			}
		}
		// 获取分类号
		for (int i = 0; i < result.length; i++) {
			if (result[i].startsWith("分类号")) {
				patentdao.setPttClassNum(result[i].substring(4));
			}
		}
		// 获取公开(公告)号
		for (int i = 0; i < result.length; i++) {
			if (result[i].startsWith("公开(公告)号")) {
				patentdao.setPttNum(result[i].substring(7));
			}
		}
		// 获取公开(公告)日
		for (int i = 0; i < result.length; i++) {
			if (result[i].startsWith("公开(公告)日")) {
				patentdao.setPttDate(StringHelper.stringToDate(result[i]
						.substring(7)));
			}
		}
		// 获取专利代理机构
		for (int i = 0; i < result.length; i++) {
			if (result[i].startsWith("专利代理机构")) {
				patentdao.setPttAgencyOrg(result[i].substring(7));
			}
		}
		// 获取代理人
		for (int i = 0; i < result.length; i++) {
			if (result[i].startsWith("代理人")) {
				patentdao.setPttAgencyPerson(result[i].substring(4));
			}
		}
		// 获取进入国家日期
		for (int i = 0; i < result.length; i++) {
			patentdao.setIntoDate(StringHelper.stringToDate("2000-01-01"));
		}
		return patentdao;
	}

	/**
	 * 测试所有的analyzer
	 * 
	 * @throws IOException
	 */
	private static void testAllAnalyzer(PatentDao patentdao) throws IOException {

		Analyzer standardAnalyzer = new StandardAnalyzer(Version.LUCENE_46);
		CheckHelper.printKeyWords(standardAnalyzer, patentdao);

		Analyzer stopAnalyzer = new StopAnalyzer(Version.LUCENE_46);
		CheckHelper.printKeyWords(stopAnalyzer, patentdao);

		Analyzer cjkAnalyzer = new CJKAnalyzer(Version.LUCENE_46);
		CheckHelper.printKeyWords(cjkAnalyzer, patentdao);

		Analyzer chineseAnalyzer = new ChineseAnalyzer();
		CheckHelper.printKeyWords(chineseAnalyzer, patentdao);

		Analyzer paodingAnalyzer = new PaodingAnalyzer();
		CheckHelper.printKeyWords(paodingAnalyzer, patentdao);

		Analyzer ikAnalyzer = new IKAnalyzer();
		CheckHelper.printKeyWords(ikAnalyzer, patentdao);

		Analyzer smartChineseAnalyzer = new SmartChineseAnalyzer(
				Version.LUCENE_46);
		CheckHelper.printKeyWords(smartChineseAnalyzer, patentdao);

		// Analyzer ictclasAnalyzer = new ICTCLASAnalyzer(Version.LUCENE_46);
		// CheckHelper.printKeyWords(ictclasAnalyzer,
		// Nlpir.doNlpirString(text, null, null));
	}
}
