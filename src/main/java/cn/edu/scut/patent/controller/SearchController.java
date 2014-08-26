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
		Patent patentdao = new Patent();

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
		if (applyNum != null && applyNum.replaceAll(" ", "") != "") {
			patentdao.setApplyNum(applyNum);
			flag = "1";
		}
		if (applyDate != null && applyDate.replaceAll(" ", "") != "") {
			patentdao.setApplyDate(StringHelper.stringToDate(applyDate));
			flag = "1";
		}
		if (pttName != null && pttName.replaceAll(" ", "") != "") {
			patentdao.setPttName(pttName);
			flag = "1";
		}
		if (pttNum != null && pttNum.replaceAll(" ", "") != "") {
			patentdao.setPttNum(pttNum);
			flag = "1";
		}
		if (pttDate != null && pttDate.replaceAll(" ", "") != "") {
			patentdao.setPttDate(StringHelper.stringToDate(pttDate));
			flag = "1";
		}
		if (pttMainClassNum != null
				&& pttMainClassNum.replaceAll(" ", "") != "") {
			patentdao.setPttMainClassNum(pttMainClassNum);
			flag = "1";
		}
		if (pttClassNum != null && pttClassNum.replaceAll(" ", "") != "") {
			patentdao.setPttClassNum(pttClassNum);
			flag = "1";
		}
		if (proposer != null && proposer.replaceAll(" ", "") != "") {
			patentdao.setProposer(proposer);
			flag = "1";
		}
		if (proposerAddress != null
				&& proposerAddress.replaceAll(" ", "") != "") {
			patentdao.setProposerAddress(proposerAddress);
			flag = "1";
		}
		if (inventor != null && inventor.replaceAll(" ", "") != "") {
			patentdao.setInventor(inventor);
			flag = "1";
		}
		if (internationalApply != null
				&& internationalApply.replaceAll(" ", "") != "") {
			patentdao.setInternationalApply(internationalApply);
			flag = "1";
		}
		if (internationalPublication != null
				&& internationalPublication.replaceAll(" ", "") != "") {
			patentdao.setInternationalPublication(internationalPublication);
			flag = "1";
		}
		if (intoDate != null && intoDate.replaceAll(" ", "") != "") {
			patentdao.setPttDate(StringHelper.stringToDate(intoDate));
			flag = "1";
		}
		if (pttAgencyOrg != null && pttAgencyOrg.replaceAll(" ", "") != "") {
			patentdao.setPttAgencyOrg(pttAgencyOrg);
			flag = "1";
		}
		if (pttAgencyPerson != null
				&& pttAgencyPerson.replaceAll(" ", "") != "") {
			patentdao.setPttAgencyPerson(pttAgencyPerson);
			flag = "1";
		}
		if (pttAbstract != null && pttAbstract.replaceAll(" ", "") != "") {
			patentdao.setPttAbstract(pttAbstract);
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
		List<Patent> patentList = new Search().doSearch(patentdao, pttTypeList);
		String timeConsume = StringHelper.timer(startTime);

		RequestDispatcher re;
		if (patentList != null && patentList.size() > 0) {
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
