package cn.edu.scut.patent.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import cn.edu.scut.patent.core.IndexAndSearch;

@Controller
public class IndexController {

	/**
	 * 索引入口
	 * 
	 * @throws Exception
	 */
	@RequestMapping(value = "index")
	public void index() throws Exception {
		System.out.println("进入index啦！");
		// IndexAndSearch.doIndexFromPDF(entry.getValue());
		IndexAndSearch.doIndexFromDatabase();
	}
}
