package cn.edu.scut.patent.controller;

import java.io.IOException;
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
		// 检索开始的时间
		long startTime = new Date().getTime();
		List<String> pttTypeList = new ArrayList<String>();
		Patent patentdao = new Patent();
		if (request.getParameter("FMZL") != null) {
			pttTypeList.add(request.getParameter("FMZL"));
		}
		if (request.getParameter("SYXX") != null) {
			pttTypeList.add(request.getParameter("SYXX"));
		}
		if (request.getParameter("WGSJ") != null) {
			pttTypeList.add(request.getParameter("WGSJ"));
		}
		if (request.getParameter("APPLY_NUM").replaceAll(" ", "") != "") {
			patentdao.setApplyNum(request.getParameter("APPLY_NUM"));
		}
		if (request.getParameter("APPLY_DATE").replaceAll(" ", "") != "") {
			patentdao.setApplyDate(StringHelper.stringToDate(request
					.getParameter("APPLY_DATE")));
		}
		if (request.getParameter("PTT_NAME").replaceAll(" ", "") != "") {
			patentdao.setPttName(request.getParameter("PTT_NAME"));
		}
		if (request.getParameter("PTT_NUM").replaceAll(" ", "") != "") {
			patentdao.setPttNum(request.getParameter("PTT_NUM"));
		}
		if (request.getParameter("PTT_DATE").replaceAll(" ", "") != "") {
			patentdao.setPttDate(StringHelper.stringToDate(request
					.getParameter("PTT_DATE")));
		}
		if (request.getParameter("PTT_MAIN_CLASS_NUM").replaceAll(" ", "") != "") {
			patentdao.setPttMainClassNum(request
					.getParameter("PTT_MAIN_CLASS_NUM"));
		}
		if (request.getParameter("PTT_CLASS_NUM").replaceAll(" ", "") != "") {
			patentdao.setPttClassNum(request.getParameter("PTT_CLASS_NUM"));
		}
		if (request.getParameter("PROPOSER").replaceAll(" ", "") != "") {
			patentdao.setProposer(request.getParameter("PROPOSER"));
		}
		if (request.getParameter("PROPOSER_ADDRESS").replaceAll(" ", "") != "") {
			patentdao.setProposerAddress(request
					.getParameter("PROPOSER_ADDRESS"));
		}
		if (request.getParameter("INVENTOR").replaceAll(" ", "") != "") {
			patentdao.setInventor(request.getParameter("INVENTOR"));
		}
		if (request.getParameter("INTERNATIONAL_APPLY").replaceAll(" ", "") != "") {
			patentdao.setInternationalApply(request
					.getParameter("INTERNATIONAL_APPLY"));
		}
		if (request.getParameter("INTERNATIONAL_PUBLICATION").replaceAll(" ",
				"") != "") {
			patentdao.setInternationalPublication(request
					.getParameter("INTERNATIONAL_PUBLICATION"));
		}
		if (request.getParameter("INTO_DATE").replaceAll(" ", "") != "") {
			patentdao.setPttDate(StringHelper.stringToDate(request
					.getParameter("INTO_DATE")));
		}
		if (request.getParameter("PTT_AGENCY_ORG").replaceAll(" ", "") != "") {
			patentdao.setPttAgencyOrg(request.getParameter("PTT_AGENCY_ORG"));
		}
		if (request.getParameter("PTT_AGENCY_PERSON").replaceAll(" ", "") != "") {
			patentdao.setPttAgencyPerson(request
					.getParameter("PTT_AGENCY_PERSON"));
		}
		if (request.getParameter("PTT_ABSTRACT").replaceAll(" ", "") != "") {
			patentdao.setPttAbstract(request.getParameter("PTT_ABSTRACT"));
		}
		System.out.println("进入search啦！");
		List<Patent> patentList = new Search().doSearch(patentdao, pttTypeList);
		String timeConsume = StringHelper.timer(startTime);

		JSONArray jsonArray = new JSONArray();
		for (Patent patent : patentList) {
			JSONObject jsonObj = new JSONObject();
			jsonObj.put("ApplyNum", patent.getApplyNum());
			jsonObj.put("Inventor", patent.getInventor());
			jsonObj.put("Proposer", patent.getProposer());
			jsonObj.put("PttDate", patent.getPttDate());
			jsonObj.put("PttName", patent.getPttName());
			jsonObj.put("PttNum", patent.getPttNum());
			jsonObj.put("PttType", patent.getPttType());
			jsonArray.put(jsonObj);
		}
		request.getSession().setAttribute("PATENTLIST", jsonArray.toString());
		request.getSession().setAttribute("TIMECONSUME", timeConsume);
		RequestDispatcher re = request.getRequestDispatcher("view/result.jsp");

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
