package cn.edu.scut.patent.model;

import java.sql.Date;
import java.util.HashMap;
import java.util.Map;

public class PatentDao {
	// 申请号
	private String applyNum;
	// 申请日期
	private Date applyDate;
	// 专利名称
	private String pttName;
	// 专利公开号（主键）
	private String pttNum;
	// 专利公开日
	private Date pttDate;
	// 主分类号
	private String pttMainClassNum;
	// 分类号
	private String pttClassNum;
	// 申请（专利权）人
	private String proposer;
	// 申请人地址
	private String proposerAddress;
	// 发明（设计）人
	private String inventor;
	// 专利代理机构
	private String pttAgencyOrg;
	// 专利代理人
	private String pttAgencyPerson;
	// 专利摘要
	private String pttAbstract;//
	// 商业方法类下的分类号
	private String classNumG06Q;//
	// 国际申请
	private String internationalApply;//
	// 国际公布
	private String internationalPublication;//
	// 进入国家日期
	private Date intoDate;//
	// 专利类型（自定义）
	private String pttType;//
	// 专利内容
	private String content;//

	public Map<String, String> getAll(){
		Map<String, String> map = new HashMap<String, String>();
		if(applyNum != null){
			map.put("applyNum", applyNum);
		}
		if(applyDate != null){
			map.put("applyDate", applyDate.toString());
		}
		if(pttName != null){
			map.put("pttName", pttName);
		}
		if(pttNum != null){
			map.put("pttNum", pttNum);
		}
		if(pttDate != null){
			map.put("pttDate", pttDate.toString());
		}
		if(pttMainClassNum != null){
			map.put("pttMainClassNum", pttMainClassNum);
		}
		if(pttClassNum != null){
			map.put("pttClassNum", pttClassNum);
		}
		if(proposer != null){
			map.put("proposer", proposer);
		}
		if(proposerAddress != null){
			map.put("proposerAddress", proposerAddress);
		}
		if(inventor != null){
			map.put("inventor", inventor);
		}
		if(pttAgencyOrg != null){
			map.put("pttAgencyOrg", pttAgencyOrg);
		}
		if(pttAgencyPerson != null){
			map.put("pttAgencyPerson", pttAgencyPerson);
		}
		if(pttAbstract != null){
			map.put("pttAbstract", pttAbstract);
		}
		if(classNumG06Q != null){
			map.put("classNumG06Q", classNumG06Q);
		}
		if(internationalApply != null){
			map.put("internationalApply", internationalApply);
		}
		if(internationalPublication != null){
			map.put("internationalPublication", internationalPublication);
		}
		if(intoDate != null){
			map.put("intoDate", intoDate.toString());
		}
		if(pttType != null){
			map.put("pttType", pttType);
		}
		if(content != null){
			map.put("content", content);
		}
		return map;
	}
	
	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getPttType() {
		return pttType;
	}

	public void setPttType(String pttType) {
		this.pttType = pttType;
	}

	public String getInternationalApply() {
		return internationalApply;
	}

	public void setInternationalApply(String internationalApply) {
		this.internationalApply = internationalApply;
	}

	public String getInternationalPublication() {
		return internationalPublication;
	}

	public void setInternationalPublication(String internationalPublication) {
		this.internationalPublication = internationalPublication;
	}

	public Date getIntoDate() {
		return intoDate;
	}

	public void setIntoDate(Date intoDate) {
		this.intoDate = intoDate;
	}

	public String getApplyNum() {
		return applyNum;
	}

	public void setApplyNum(String applyNum) {
		this.applyNum = applyNum;
	}

	public Date getApplyDate() {
		return applyDate;
	}

	public void setApplyDate(java.sql.Date date) {
		this.applyDate = date;
	}

	public String getPttName() {
		return pttName;
	}

	public void setPttName(String pttName) {
		this.pttName = pttName;
	}

	public String getPttNum() {
		return pttNum;
	}

	public void setPttNum(String pttNum) {
		this.pttNum = pttNum;
	}

	public Date getPttDate() {
		return pttDate;
	}

	public void setPttDate(Date pttDate) {
		this.pttDate = pttDate;
	}

	public String getPttMainClassNum() {
		return pttMainClassNum;
	}

	public void setPttMainClassNum(String pttMainClassNum) {
		this.pttMainClassNum = pttMainClassNum;
	}

	public String getPttClassNum() {
		return pttClassNum;
	}

	public void setPttClassNum(String pttClassNum) {
		this.pttClassNum = pttClassNum;

		this.classNumG06Q = getG06QClass(pttClassNum);
	}

	public String getProposer() {
		return proposer;
	}

	public void setProposer(String proposer) {
		this.proposer = proposer;
	}

	public String getProposerAddress() {
		return proposerAddress;
	}

	public void setProposerAddress(String proposerAddress) {
		this.proposerAddress = proposerAddress;
	}

	public String getInventor() {
		return inventor;
	}

	public void setInventor(String inventor) {
		this.inventor = inventor;
	}

	public String getPttAgencyOrg() {
		return pttAgencyOrg;
	}

	public void setPttAgencyOrg(String pttAgencyOrg) {
		this.pttAgencyOrg = pttAgencyOrg;
	}

	public String getPttAgencyPerson() {
		return pttAgencyPerson;
	}

	public void setPttAgencyPerson(String pttAgencyPerson) {
		this.pttAgencyPerson = pttAgencyPerson;
	}

	public String getPttAbstract() {
		return pttAbstract;
	}

	public void setPttAbstract(String pttAbstract) {
		this.pttAbstract = pttAbstract;
	}

	public String getClassNumG06Q() {
		return classNumG06Q;
	}

	public void setClassNumG06Q(String classNumG06Q) {
		this.classNumG06Q = classNumG06Q;
	}

	private String getG06QClass(String classStr) {
		String[] ss = classStr.split(";");

		int len = ss.length;
		for (int i = 0; i < len; i++) {
			if (ss[i].indexOf("G06Q") != -1) {
				return ss[i];
			}
		}
		return "";
	}
}
