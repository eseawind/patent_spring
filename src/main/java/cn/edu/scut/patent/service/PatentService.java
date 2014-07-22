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
	 * 获取所有的专利数据
	 * 
	 * @return
	 */
	public List<Patent> getAllPatents() {
		return new PatentDao().getAllPatents(session);
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
		for (int i = 0; i < listPatent.size(); i++) {
			Patent pttDao = listPatent.get(i);
			Document document = new Document();
			document.add(new TextField("PTT_TYPE", pttDao.getPttType(),
					Field.Store.YES));
			document.add(new TextField("APPLY_NUM", pttDao.getApplyNum(),
					Field.Store.YES));
			document.add(new TextField("APPLY_DATE", pttDao.getApplyDate()
					.toString(), Field.Store.YES));
			System.out.println(pttDao.getPttName());
			document.add(new TextField("PTT_NAME", Nlpir.doNlpirString(
					pttDao.getPttName(), 0, null, null), Field.Store.YES));
			document.add(new TextField("PTT_NUM", pttDao.getPttNum(),
					Field.Store.YES));
			document.add(new TextField("PTT_DATE", pttDao.getPttDate()
					.toString(), Field.Store.YES));
			document.add(new TextField("PTT_MAIN_CLASS_NUM", Nlpir
					.doNlpirString(pttDao.getPttMainClassNum(), 0, null, null),
					Field.Store.YES));
			document.add(new TextField("PTT_CLASS_NUM", Nlpir.doNlpirString(
					pttDao.getPttClassNum(), 0, null, null), Field.Store.YES));
			document.add(new TextField("PROPOSER", Nlpir.doNlpirString(
					pttDao.getProposer(), 0, null, null), Field.Store.YES));
			document.add(new TextField("PROPOSER_ADDRESS", Nlpir.doNlpirString(
					pttDao.getProposerAddress(), 0, null, null),
					Field.Store.YES));
			document.add(new TextField("INVENTOR", Nlpir.doNlpirString(
					pttDao.getInventor(), 0, null, null), Field.Store.YES));
			document.add(new TextField("INTERNATIONAL_APPLY", Nlpir
					.doNlpirString(pttDao.getInternationalApply(), 0, null,
							null), Field.Store.YES));
			document.add(new TextField("INTERNATIONAL_PUBLICATION", Nlpir
					.doNlpirString(pttDao.getInternationalPublication(), 0,
							null, null), Field.Store.YES));
			document.add(new TextField("INTO_DATE", pttDao.getIntoDate()
					.toString(), Field.Store.YES));
			document.add(new TextField("PTT_AGENCY_ORG", Nlpir.doNlpirString(
					pttDao.getPttAgencyOrg(), 0, null, null), Field.Store.YES));
			document.add(new TextField("PTT_AGENCY_PERSON", Nlpir
					.doNlpirString(pttDao.getPttAgencyPerson(), 0, null, null),
					Field.Store.YES));
			document.add(new TextField("PTT_ABSTRACT", Nlpir.doNlpirString(
					pttDao.getPttAbstract(), 0, null, null), Field.Store.YES));
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
