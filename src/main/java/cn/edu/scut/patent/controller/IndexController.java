package cn.edu.scut.patent.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import cn.edu.scut.patent.core.IndexAndSearch;

@Controller
public class IndexController {

	/**
	 * 索引入口
	 * 
	 * @throws Exception
	 */
	@RequestMapping(value = "index")
	public ModelAndView index() throws Exception {
		System.out.println("进入index啦！");
		// IndexAndSearch.doIndexFromPDF(entry.getValue());
		IndexAndSearch.doIndexFromDatabase();

		ModelAndView mv = new ModelAndView("alert", "command", "索引建立成功！");
		return mv;
	}
}
