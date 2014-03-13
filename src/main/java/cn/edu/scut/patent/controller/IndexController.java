package cn.edu.scut.patent.controller;

import java.util.Map;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import cn.edu.scut.patent.core.IndexAndSearch;
import cn.edu.scut.patent.util.Constants;
import cn.edu.scut.patent.util.FileHelper;

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
		Map<String, String> map = FileHelper
				.readfile(Constants.FILE_DIR_STRING);
		int i = 1;
		for (Map.Entry<String, String> entry : map.entrySet()) {
			System.out
					.println(i++
							+ "##########################################################");
			IndexAndSearch.doIndex(entry.getValue());
		}
	}
}
