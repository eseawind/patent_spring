package cn.edu.scut.patent.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import cn.edu.scut.patent.util.DatabaseHelper;

public class DaoController {

	/**
	 * 更新Triz号码
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "updateTrizNumber")
	public ModelAndView updateTrizNumber(HttpServletRequest request,
			HttpServletResponse response){
		String PTT_NUM = request.getParameter("PTT_NUM");
		String TRIZ_NUM = request.getParameter("TRIZ_NUM");
		String result;
		if(DatabaseHelper.updateTrizNumber(PTT_NUM, TRIZ_NUM)){
			result = "UPDATE SUCCESS!";
		}else{
			result = "UPDATE FAILED!";
		}
		ModelAndView mv = new ModelAndView("/index", "command",
				result);
		return mv;
	}
}