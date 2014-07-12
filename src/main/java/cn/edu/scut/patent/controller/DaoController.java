package cn.edu.scut.patent.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import cn.edu.scut.patent.dao.DatabaseHelper;

@Controller
public class DaoController {

	/**
	 * 插入Triz号码
	 * 
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "insertTrizNumber")
	public ModelAndView insertTrizNumber(HttpServletRequest request,
			HttpServletResponse response) {
		String PTT_NUM = request.getParameter("PTT_NUM");
		String[] TRIZ_NUM = request.getParameterValues("triz");
		String result;
		if (DatabaseHelper.insertTrizNumber(PTT_NUM, TRIZ_NUM)) {
			result = "专利分类信息已存入数据库！感谢您的支持！";
		} else {
			result = "专利分类信息已存入数据库失败！";
		}
		ModelAndView mv = new ModelAndView("/index", "command", result);
		return mv;
	}

	/**
	 * 更新Triz号码
	 * 
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "updateTrizNumber")
	public ModelAndView updateTrizNumber(HttpServletRequest request,
			HttpServletResponse response) {
		String PTT_NUM = request.getParameter("PTT_NUM");
		String[] TRIZ_NUM = request.getParameterValues("triz");
		for (int i = 0; i < TRIZ_NUM.length; i++) {
			System.out.println(i + "@@" + TRIZ_NUM[i]);
		}
		System.out.println();
		String result;
		if (DatabaseHelper.updateTrizNumber(PTT_NUM, TRIZ_NUM)) {
			result = "专利分类信息已存入数据库！感谢您的支持！";
		} else {
			result = "专利分类信息已存入数据库失败！";
		}
		ModelAndView mv = new ModelAndView("alert", "command", result);
		return mv;
	}
}
