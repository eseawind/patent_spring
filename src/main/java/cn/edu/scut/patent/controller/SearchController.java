package cn.edu.scut.patent.controller;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import cn.edu.scut.patent.core.Search;
import cn.edu.scut.patent.model.Patent;
import cn.edu.scut.patent.util.StringHelper;

@Controller
public class SearchController {

	/**
	 * 查询入口
	 * 
	 * @throws IOException
	 */
	@RequestMapping(value = "search")
	public void search(HttpServletRequest request, HttpServletResponse response)
			throws IOException {
		try {
			request.setCharacterEncoding("utf-8");
			response.setContentType("text/html;charset=utf-8");
		} catch (UnsupportedEncodingException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		// 检索开始的时间
		long startTime = new Date().getTime();
		List<String> pttTypeList = new ArrayList<String>();
		Patent patent = new Patent();

		String flag0 = null;
		String flag = null;
		String FMZL = request.getParameter("FMZL");
		String SYXX = request.getParameter("SYXX");
		String WGSJ = request.getParameter("WGSJ");
		String applyNum = request.getParameter("APPLY_NUM");
		String applyDate = request.getParameter("APPLY_DATE");
		String pttName = request.getParameter("PTT_NAME");
		String pttNum = request.getParameter("PTT_NUM");
		String pttDate = request.getParameter("PTT_DATE");
		String pttMainClassNum = request.getParameter("PTT_MAIN_CLASS_NUM");
		String pttClassNum = request.getParameter("PTT_CLASS_NUM");
		String proposer = request.getParameter("PROPOSER");
		String proposerAddress = request.getParameter("PROPOSER_ADDRESS");
		String inventor = request.getParameter("INVENTOR");
		String internationalApply = request.getParameter("INTERNATIONAL_APPLY");
		String internationalPublication = request
				.getParameter("INTERNATIONAL_PUBLICATION");
		String intoDate = request.getParameter("INTO_DATE");
		String pttAgencyOrg = request.getParameter("PTT_AGENCY_ORG");
		String pttAgencyPerson = request.getParameter("PTT_AGENCY_PERSON");
		String pttAbstract = request.getParameter("PTT_ABSTRACT");

		if (FMZL != null) {
			pttTypeList.add(FMZL);
			flag0 = "1";
		}
		if (SYXX != null) {
			pttTypeList.add(SYXX);
			flag0 = "1";
		}
		if (WGSJ != null) {
			pttTypeList.add(WGSJ);
			flag0 = "1";
		}
		if (applyNum != null && StringHelper.replaceSpace(applyNum) != "") {
			// 如果不是以CN中国专利开头的，则添加上CN字段
			if (applyNum.indexOf("CN") == -1) {
				applyNum = "CN" + applyNum;
			}
			patent.setApplyNum(StringHelper.replaceSpace(applyNum));
			flag = "1";
		}
		if (applyDate != null && StringHelper.replaceSpace(applyDate) != "") {
			patent.setApplyDate(StringHelper.stringToDate(StringHelper
					.replaceSpace(applyDate)));
			flag = "1";
		}
		if (pttName != null && StringHelper.replaceSpace(pttName) != "") {
			patent.setPttName(StringHelper.replaceSpace(pttName));
			flag = "1";
		}
		if (pttNum != null && StringHelper.replaceSpace(pttNum) != "") {
			patent.setPttNum(StringHelper.replaceSpace(pttNum));
			flag = "1";
		}
		if (pttDate != null && StringHelper.replaceSpace(pttDate) != "") {
			patent.setPttDate(StringHelper.stringToDate(StringHelper
					.replaceSpace(pttDate)));
			flag = "1";
		}
		if (pttMainClassNum != null
				&& StringHelper.replaceSpace(pttMainClassNum) != "") {
			patent.setPttMainClassNum(StringHelper
					.replaceSpace(pttMainClassNum));
			flag = "1";
		}
		if (pttClassNum != null && StringHelper.replaceSpace(pttClassNum) != "") {
			patent.setPttClassNum(StringHelper.replaceSpace(pttClassNum));
			flag = "1";
		}
		if (proposer != null && StringHelper.replaceSpace(proposer) != "") {
			patent.setProposer(StringHelper.replaceSpace(proposer));
			flag = "1";
		}
		if (proposerAddress != null
				&& StringHelper.replaceSpace(proposerAddress) != "") {
			patent.setProposerAddress(StringHelper
					.replaceSpace(proposerAddress));
			flag = "1";
		}
		if (inventor != null && StringHelper.replaceSpace(inventor) != "") {
			patent.setInventor(StringHelper.replaceSpace(inventor));
			flag = "1";
		}
		if (internationalApply != null
				&& StringHelper.replaceSpace(internationalApply) != "") {
			patent.setInternationalApply(StringHelper
					.replaceSpace(internationalApply));
			flag = "1";
		}
		if (internationalPublication != null
				&& StringHelper.replaceSpace(internationalPublication) != "") {
			patent.setInternationalPublication(StringHelper
					.replaceSpace(internationalPublication));
			flag = "1";
		}
		if (intoDate != null && StringHelper.replaceSpace(intoDate) != "") {
			patent.setIntoDate(StringHelper.stringToDate(StringHelper
					.replaceSpace(intoDate)));
			flag = "1";
		}
		if (pttAgencyOrg != null
				&& StringHelper.replaceSpace(pttAgencyOrg) != "") {
			patent.setPttAgencyOrg(StringHelper.replaceSpace(pttAgencyOrg));
			flag = "1";
		}
		if (pttAgencyPerson != null
				&& StringHelper.replaceSpace(pttAgencyPerson) != "") {
			patent.setPttAgencyPerson(StringHelper
					.replaceSpace(pttAgencyPerson));
			flag = "1";
		}
		if (pttAbstract != null && StringHelper.replaceSpace(pttAbstract) != "") {
			patent.setPttAbstract(StringHelper.replaceSpace(pttAbstract));
			flag = "1";
		}
		if (flag0 == null || flag == null) {
			try {
				System.out.println("缺乏参数，非法进入！");
				response.sendRedirect("view/search.jsp");
				return;
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		System.out.println("进入search啦！");
		List<Patent> patentList = new Search().doSearch(patent, pttTypeList);
		String timeConsume = StringHelper.timer(startTime);

		RequestDispatcher re;
		if (patentList != null && patentList.size() > 0) {
			JSONArray jsonArray = new JSONArray();
			for (Patent temp : patentList) {
				JSONObject jsonObj = new JSONObject();
				// 去除applyNum里面的CN字段以适应佰腾网的查询要求
				jsonObj.put("ApplyNum", temp.getApplyNum().replaceAll("CN", ""));
				jsonObj.put("Inventor", temp.getInventor());
				jsonObj.put("Proposer", temp.getProposer());
				jsonObj.put("PttDate", temp.getPttDate());
				jsonObj.put("PttName", temp.getPttName());
				jsonObj.put("PttNum", temp.getPttNum());
				jsonObj.put("PttType", temp.getPttType());
				jsonArray.put(jsonObj);
			}
			request.getSession().setAttribute("PATENTLIST",
					jsonArray.toString());
			request.getSession().setAttribute("TIMECONSUME", timeConsume);
			re = request.getRequestDispatcher("view/result.jsp");
		} else {
			request.setAttribute("NoResultFoundError", "NoResult");
			re = request.getRequestDispatcher("view/search.jsp");
			System.out.println("没有找到结果");
		}

		try {
			re.forward(request, response);
		} catch (ServletException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
