package cn.edu.scut.patent.controller;

import java.io.IOException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import cn.edu.scut.patent.model.PatentDao;
import cn.edu.scut.patent.testing.IndexAndSearch;
import cn.edu.scut.patent.util.DateHelper;

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

		if (request.getParameter("applyNum").replaceAll(" ", "") != "") {
			patentdao.setApplyNum(request.getParameter("applyNum"));
		}
		if (request.getParameter("applyDate").replaceAll(" ", "") != "") {
			patentdao.setApplyDate(DateHelper.stringToDate(request
					.getParameter("applyDate")));
		}
		if (request.getParameter("pttName").replaceAll(" ", "") != "") {
			patentdao.setPttName(request.getParameter("pttName"));
		}
		if (request.getParameter("pttNum").replaceAll(" ", "") != "") {
			patentdao.setPttNum(request.getParameter("pttNum"));
		}
		if (request.getParameter("pttDate").replaceAll(" ", "") != "") {
			patentdao.setPttDate(DateHelper.stringToDate(request
					.getParameter("pttDate")));
		}
		if (request.getParameter("pttMainClassNum").replaceAll(" ", "") != "") {
			patentdao.setPttMainClassNum(request
					.getParameter("pttMainClassNum"));
		}
		if (request.getParameter("pttClassNum").replaceAll(" ", "") != "") {
			patentdao.setPttClassNum(request.getParameter("pttClassNum"));
		}
		if (request.getParameter("proposer").replaceAll(" ", "") != "") {
			patentdao.setProposer(request.getParameter("proposer"));
		}
		if (request.getParameter("proposerAddress").replaceAll(" ", "") != "") {
			patentdao.setProposerAddress(request
					.getParameter("proposerAddress"));
		}
		if (request.getParameter("inventor").replaceAll(" ", "") != "") {
			patentdao.setInventor(request.getParameter("inventor"));
		}
		if (request.getParameter("pttAgencyOrg").replaceAll(" ", "") != "") {
			patentdao.setPttAgencyOrg(request.getParameter("pttAgencyOrg"));
		}
		if (request.getParameter("pttAgencyPerson").replaceAll(" ", "") != "") {
			patentdao.setPttAgencyPerson(request
					.getParameter("pttAgencyPerson"));
		}
		if (request.getParameter("pttAbstract").replaceAll(" ", "") != "") {
			patentdao.setPttAbstract(request.getParameter("pttAbstract"));
		}
		if (request.getParameter("classNumG06Q").replaceAll(" ", "") != "") {
			patentdao.setClassNumG06Q(request.getParameter("classNumG06Q"));
		}
		if (request.getParameter("internationalApply").replaceAll(" ", "") != "") {
			patentdao.setInternationalApply(request
					.getParameter("internationalApply"));
		}
		if (request.getParameter("internationalPublication")
				.replaceAll(" ", "") != "") {
			patentdao.setInternationalPublication(request
					.getParameter("internationalPublication"));
		}

		System.out.println("进入search啦！");
		IndexAndSearch.doSearch(patentdao);
	}
}
