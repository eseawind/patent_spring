package cn.edu.scut.patent.util;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.imageio.ImageIO;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.TextField;
import org.apache.pdfbox.pdfparser.PDFParser;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDResources;
import org.apache.pdfbox.pdmodel.graphics.xobject.PDXObjectImage;
import org.apache.pdfbox.util.PDFTextStripper;

public class PDFHelper {

	/**
	 * 获取PDF文档 请先把已加密的PDF文档解密 采用fontbox-1.8.4和pdfbox-1.8.4支持
	 * 
	 * @throws Exception
	 */
	public static Document getDocumentFromPDF(File pdf) throws Exception {
		// Document document = LucenePDFDocument.getDocument(pdf);
		String pdfpath = pdf.getAbsolutePath();
		// 创建输入流读取pdf文件
		String title = pdf.getName();
		String result = "";
		FileInputStream is = null;
		PDDocument doc = null;
		Map<String, String> map = null;
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
			transferPDFToImages(doc, title, Constants.TYPE);
			// 获取专利的各项属性
			map = getPatentDetails(result);
			DatabaseHelper.saveToDatabase(map);

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
		document.add(new TextField("title", title, Field.Store.YES));
		document.add(new TextField("contents", result, Field.Store.YES));// 最后需要删除
		document.add(new TextField("path", pdfpath, Field.Store.YES));
		// 遍历map把各项专利的属性加入到document当中
		if (map != null) {
			Set<String> keySet = map.keySet();
			Iterator it = keySet.iterator();
			while (it.hasNext()) {
				String key = (String) it.next();
				String value = map.get(key);
				document.add(new TextField(key, value, Field.Store.YES));
			}
		}
		return document;
	}

	/**
	 * 获取PDF文档当中的图片
	 * 
	 * @throws IOException
	 */
	@SuppressWarnings({ "unchecked", "deprecation", "unused" })
	private static void getImagesInPDF(PDDocument doc, String prefix)
			throws IOException {
		List<PDPage> pages = doc.getDocumentCatalog().getAllPages();
		Iterator<PDPage> iter = pages.iterator();
		while (iter.hasNext()) {
			System.out.println("@@@@@@@@@@");
			PDPage page = (PDPage) iter.next();
			PDResources resources = page.getResources();
			Map<String, PDXObjectImage> images = resources.getImages();
			if (images != null) {
				System.out.println("#########");
				Iterator<String> imageIter = images.keySet().iterator();
				while (imageIter.hasNext()) {
					System.out.println("%%%%%%%%%%");
					String key = (String) imageIter.next();
					PDXObjectImage image = (PDXObjectImage) images.get(key);
					// String name = getUniqueFileName( key,
					// image.getSuffix() );
					// System.out.println( "Writing image:" + name );
					// PDStream pdfstream=image.getPDStream();
					String name = prefix + "_" + key;
					image.write2file(Constants.GET_IMAGES_FROM_PDF_DIR_STRING
							+ "\\" + name);
				}
			}
		}
	}

	/**
	 * 把PDF文档转化为图片
	 * 
	 * @throws IOException
	 */
	@SuppressWarnings("unchecked")
	private static void transferPDFToImages(PDDocument doc, String prefix,
			String type) throws IOException {
		List<PDPage> pages = doc.getDocumentCatalog().getAllPages();
		if (pages.size() > 0) {
			for (int i = 0; i < pages.size(); i++) {
				PDPage page = (PDPage) pages.get(i);
				BufferedImage image = page.convertToImage();
				String name = prefix + "_" + i;
				File file = new File(
						Constants.TRANSFER_PDF_TO_IMAGES_DIR_STRING + "\\"
								+ name + "." + type);
				ImageIO.write(image, type, file);
			}
		}
	}

	/**
	 * 获取专利的各个属性
	 */
	private static Map<String, String> getPatentDetails(String text) {
		String[] result = text.split("\n");
		Map<String, String> map = new HashMap<String, String>();

		// 获取专利名称
		for (int i = 0; i < result.length; i++) {
			if (result[i].startsWith("SooPAT")) {
				map.put("pttName", result[i + 1]);
			}
		}
		// 获取申请号
		for (int i = 0; i < result.length; i++) {
			if (result[i].startsWith("申请号：")) {
				map.put("applyNum", result[i].substring(4));
			}
		}
		// 获取申请日
		for (int i = 0; i < result.length; i++) {
			if (result[i].startsWith("申请日：")) {
				map.put("applyDate", result[i].substring(4));
			}
		}
		// 获取申请(专利权)人
		for (int i = 0; i < result.length; i++) {
			if (result[i].startsWith("申请(专利权)人")) {
				map.put("proposer", result[i].substring(8));
			}
		}
		// 获取地址
		for (int i = 0; i < result.length; i++) {
			if (result[i].startsWith("地址")) {
				map.put("proposerAddress", result[i].substring(3));
			}
		}
		// 获取发明(设计)人
		for (int i = 0; i < result.length; i++) {
			if (result[i].startsWith("发明(设计)人")) {
				map.put("inventor", result[i].substring(7));
			}
		}
		// 获取主分类号
		for (int i = 0; i < result.length; i++) {
			if (result[i].startsWith("主分类号")) {
				map.put("pttMainClassNum", result[i].substring(5));
			}
		}
		// 获取分类号
		for (int i = 0; i < result.length; i++) {
			if (result[i].startsWith("分类号")) {
				map.put("pttClassNum", result[i].substring(4));
			}
		}
		// 获取公开(公告)号
		for (int i = 0; i < result.length; i++) {
			if (result[i].startsWith("公开(公告)号")) {
				map.put("pttNum", result[i].substring(7));
			}
		}
		// 获取公开(公告)日
		for (int i = 0; i < result.length; i++) {
			if (result[i].startsWith("公开(公告)日")) {
				map.put("pttDate", result[i].substring(7));
			}
		}
		// 获取专利代理机构
		for (int i = 0; i < result.length; i++) {
			if (result[i].startsWith("专利代理机构")) {
				map.put("pttAgencyOrg", result[i].substring(7));
			}
		}
		// 获取代理人
		for (int i = 0; i < result.length; i++) {
			if (result[i].startsWith("代理人")) {
				map.put("pttAgencyPerson", result[i].substring(4));
			}
		}
		// 获取进入国家日期
		for (int i = 0; i < result.length; i++) {
			map.put("intoDate", "2000-01-01");
		}
		return map;
	}
}
