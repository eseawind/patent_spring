package cn.edu.scut.patent.controller;

import java.io.IOException;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import cn.edu.scut.patent.core.IndexAndSearch;
import cn.edu.scut.patent.model.PatentDao;
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
		PatentDao patentdao = new PatentDao();
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
		List<PatentDao> patentList = IndexAndSearch.doSearch(patentdao);
		
		request.getSession().setAttribute("PATENTLIST", patentList);
		//request.setAttribute("PATENTLIST", patentList);
		RequestDispatcher re = request.getRequestDispatcher("view/searchresult.jsp");
		try {
			re.forward(request, response);
		} catch (ServletException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
