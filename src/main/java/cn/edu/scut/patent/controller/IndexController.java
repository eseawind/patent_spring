package cn.edu.scut.patent.controller;

import java.util.Date;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import cn.edu.scut.patent.service.Index;
import cn.edu.scut.patent.util.StringHelper;

@Controller
public class IndexController {

	/**
	 * 索引入口
	 * 
	 * @throws Exception
	 */
	@RequestMapping(value = "index")
	public ModelAndView index() throws Exception {
		// 索引开始的时间
		long startTime = new Date().getTime();
		System.out.println("进入index啦！");
		new Index().doIndexFromDatabase();
		String timeConsume = StringHelper.timer(startTime);

		ModelAndView mv = new ModelAndView("alert", "command", "索引建立成功！耗时" + timeConsume);
		return mv;
	}
}
