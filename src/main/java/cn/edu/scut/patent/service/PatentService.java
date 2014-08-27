package cn.edu.scut.patent.service;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.TextField;
import ICTCLAS2014.Nlpir;
import cn.edu.scut.patent.dao.PatentDao;
import cn.edu.scut.patent.model.Patent;
import cn.edu.scut.patent.model.PatentClassification;

public class PatentService extends TotalService {

	/**
	 * 检查表是否为空
	 * 
	 * @return
	 */
	public static Boolean isEmpty() {
		return PatentDao.isEmpty(session);
	}

	/**
	 * 清空表
	 */
	public static void cleanTable() {
		PatentDao.cleanTable(session);
	}

	/**
	 * 保存
	 * 
	 * @param patent
	 */
	public void save(Patent patent) {
		new PatentDao().save(session, patent);
	}

	/**
	 * 获取所有的pttNum
	 * 
	 * @return
	 */
	public List<String> getAllPttNum() {
		return new PatentDao().getAllPttNum(session);
	}

	/**
	 * 获取所有的专利数据
	 * 
	 * @return
	 */
	public List<Patent> getAllPatents() {
		return new PatentDao().getAllPatents(session);
	}

	/**
	 * 获取所有的专利数据,包含分类的数据
	 * 
	 * @return
	 */
	public List<PatentClassification> getAllPatentsWithClassification() {
		return new PatentDao().getAllPatentsWithClassification(session);
	}

	/**
	 * 获取PATENTS所有专利关键属性的数据
	 * 
	 * @return
	 */
	public List<Patent> getPatentsKey() {
		return new PatentDao().getPatentsKey(session);
	}

	/**
	 * 使用ICTCLAS中文分词系统过滤Patent
	 * 
	 * @param patent
	 * @return
	 */
	public Patent getNlpirPatent(Patent patent) {
		if (patent.getPttName() != null) {
			patent.setPttName(Nlpir.doNlpirString(patent.getPttName(), 0, null,
					null));
		}
		if (patent.getPttMainClassNum() != null) {
			patent.setPttMainClassNum(Nlpir.doNlpirString(
					patent.getPttMainClassNum(), 0, null, null));
		}
		if (patent.getPttClassNum() != null) {
			patent.setPttClassNum(Nlpir.doNlpirString(patent.getPttClassNum(),
					0, null, null));
		}
		if (patent.getProposer() != null) {
			patent.setProposer(Nlpir.doNlpirString(patent.getProposer(), 0,
					null, null));
		}
		if (patent.getProposerAddress() != null) {
			patent.setProposerAddress(Nlpir.doNlpirString(
					patent.getProposerAddress(), 0, null, null));
		}
		if (patent.getInventor() != null) {
			patent.setInventor(Nlpir.doNlpirString(patent.getInventor(), 0,
					null, null));
		}
		if (patent.getInternationalApply() != null) {
			patent.setInternationalApply(Nlpir.doNlpirString(
					patent.getInternationalApply(), 0, null, null));
		}
		if (patent.getInternationalPublication() != null) {
			patent.setInternationalPublication(Nlpir.doNlpirString(
					patent.getInternationalPublication(), 0, null, null));
		}
		if (patent.getPttAgencyOrg() != null) {
			patent.setPttAgencyOrg(Nlpir.doNlpirString(
					patent.getPttAgencyOrg(), 0, null, null));
		}
		if (patent.getPttAgencyPerson() != null) {
			patent.setPttAgencyPerson(Nlpir.doNlpirString(
					patent.getPttAgencyPerson(), 0, null, null));
		}
		if (patent.getPttAbstract() != null) {
			patent.setPttAbstract(Nlpir.doNlpirString(patent.getPttAbstract(),
					0, null, null));
		}
		return patent;
	}

	/**
	 * 从数据库获取索引数据
	 * 
	 * @param analyzer
	 * @return
	 * @throws Exception
	 */
	public List<Document> getDocumentsFromPatents(Analyzer analyzer)
			throws Exception {
		List<Patent> listPatent = getAllPatents();
		if (listPatent == null || listPatent.size() == 0) {
			return null;
		}
		List<Document> listDocument = new ArrayList<Document>();
		for (Patent patent : listPatent) {
			// 使用ICTCLAS中文分词系统过滤Patent
			patent = getNlpirPatent(patent);
			Document document = new Document();
			document.add(new TextField("PTT_TYPE", patent.getPttType(),
					Field.Store.YES));
			document.add(new TextField("APPLY_NUM", patent.getApplyNum(),
					Field.Store.YES));
			document.add(new TextField("APPLY_DATE", patent.getApplyDate()
					.toString(), Field.Store.YES));
			document.add(new TextField("PTT_NAME", patent.getPttName(),
					Field.Store.YES));
			document.add(new TextField("PTT_NUM", patent.getPttNum(),
					Field.Store.YES));
			document.add(new TextField("PTT_DATE", patent.getPttDate()
					.toString(), Field.Store.YES));
			document.add(new TextField("PTT_MAIN_CLASS_NUM", patent
					.getPttMainClassNum(), Field.Store.YES));
			document.add(new TextField("PTT_CLASS_NUM",
					patent.getPttClassNum(), Field.Store.YES));
			document.add(new TextField("PROPOSER", patent.getProposer(),
					Field.Store.YES));
			document.add(new TextField("PROPOSER_ADDRESS", patent
					.getProposerAddress(), Field.Store.YES));
			document.add(new TextField("INVENTOR", patent.getInventor(),
					Field.Store.YES));
			document.add(new TextField("INTERNATIONAL_APPLY", patent
					.getInternationalApply(), Field.Store.YES));
			document.add(new TextField("INTERNATIONAL_PUBLICATION", patent
					.getInternationalPublication(), Field.Store.YES));
			document.add(new TextField("INTO_DATE", patent.getIntoDate()
					.toString(), Field.Store.YES));
			document.add(new TextField("PTT_AGENCY_ORG", patent
					.getPttAgencyOrg(), Field.Store.YES));
			document.add(new TextField("PTT_AGENCY_PERSON", patent
					.getPttAgencyPerson(), Field.Store.YES));
			document.add(new TextField("PTT_ABSTRACT", patent.getPttAbstract(),
					Field.Store.YES));
			listDocument.add(document);
		}
		return listDocument;
	}

	/**
	 * 以Map<String, String>的形式返回Patent的所有属性
	 * 
	 * @param patent
	 * @return
	 */
	public Map<String, String> getAllPatentProperties(Patent patent) {
		Map<String, String> map = new HashMap<String, String>();
		if (patent.getApplyNum() != null) {
			map.put("APPLY_NUM", patent.getApplyNum());
		}
		if (patent.getApplyDate() != null) {
			map.put("APPLY_DATE", patent.getApplyDate().toString());
		}
		if (patent.getPttName() != null) {
			map.put("PTT_NAME", patent.getPttName());
		}
		if (patent.getPttNum() != null) {
			map.put("PTT_NUM", patent.getPttNum());
		}
		if (patent.getPttDate() != null) {
			map.put("PTT_DATE", patent.getPttDate().toString());
		}
		if (patent.getPttMainClassNum() != null) {
			map.put("PTT_MAIN_CLASS_NUM", patent.getPttMainClassNum());
		}
		if (patent.getPttClassNum() != null) {
			map.put("PTT_CLASS_NUM", patent.getPttClassNum());
		}
		if (patent.getProposer() != null) {
			map.put("PROPOSER", patent.getProposer());
		}
		if (patent.getProposerAddress() != null) {
			map.put("PROPOSER_ADDRESS", patent.getProposerAddress());
		}
		if (patent.getInventor() != null) {
			map.put("INVENTOR", patent.getInventor());
		}
		if (patent.getInternationalApply() != null) {
			map.put("INTERNATIONAL_APPLY", patent.getInternationalApply());
		}
		if (patent.getInternationalPublication() != null) {
			map.put("INTERNATIONAL_PUBLICATION",
					patent.getInternationalPublication());
		}
		if (patent.getIntoDate() != null) {
			map.put("INTO_DATE", patent.getIntoDate().toString());
		}
		if (patent.getPttAgencyOrg() != null) {
			map.put("PTT_AGENCY_ORG", patent.getPttAgencyOrg());
		}
		if (patent.getPttAgencyPerson() != null) {
			map.put("PTT_AGENCY_PERSON", patent.getPttAgencyPerson());
		}
		if (patent.getPttAbstract() != null) {
			map.put("PTT_ABSTRACT", patent.getPttAbstract());
		}
		if (patent.getPttType() != null) {
			map.put("pttType", patent.getPttType());
		}
		return map;
	}

	/**
	 * 把ResultSet转化为List<PatentDao>
	 * 
	 * @param rs
	 * @return
	 * @throws SQLException
	 */
	public List<Patent> transferDataToPatentDao(ResultSet rs)
			throws SQLException {
		List<Patent> listPatent = new ArrayList<Patent>();
		while (rs.next()) {
			Patent pttDao = new Patent();
			pttDao.setPttType(rs.getString("PTT_TYPE"));
			pttDao.setApplyNum(rs.getString("APPLY_NUM"));
			pttDao.setApplyDate(rs.getDate("APPLY_DATE"));
			pttDao.setPttName(rs.getString("PTT_NAME"));
			pttDao.setPttNum(rs.getString("PTT_NUM"));
			pttDao.setPttDate(rs.getDate("PTT_DATE"));
			pttDao.setPttMainClassNum(rs.getString("PTT_MAIN_CLASS_NUM"));
			pttDao.setPttClassNum(rs.getString("PTT_CLASS_NUM"));
			pttDao.setProposer(rs.getString("PROPOSER"));
			pttDao.setProposerAddress(rs.getString("PROPOSER_ADDRESS"));
			pttDao.setInventor(rs.getString("INVENTOR"));
			pttDao.setInternationalApply(rs.getString("INTERNATIONAL_APPLY"));
			pttDao.setInternationalPublication(rs
					.getString("INTERNATIONAL_PUBLICATION"));
			pttDao.setIntoDate(rs.getDate("INTO_DATE"));
			pttDao.setPttAgencyOrg(rs.getString("PTT_AGENCY_ORG"));
			pttDao.setPttAgencyPerson(rs.getString("PTT_AGENCY_PERSON"));
			pttDao.setPttAbstract(rs.getString("PTT_ABSTRACT"));
			listPatent.add(pttDao);
		}
		return listPatent;
	}
}
