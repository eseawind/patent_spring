package cn.edu.scut.patent.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import cn.edu.scut.patent.prework.PatentsPreprocess;

@Controller
public class ClusterController {

	/**
	 * 进行聚类入口
	 * 
	 * @throws Exception
	 */
	@RequestMapping(value = "cluster")
	public ModelAndView cluster() throws Exception {
		System.out.println("进行聚类啦！");
		PatentsPreprocess.doPatentsPreprocess();

		ModelAndView mv = new ModelAndView("alert", "command", "聚类成功！");
		return mv;
	}
}
