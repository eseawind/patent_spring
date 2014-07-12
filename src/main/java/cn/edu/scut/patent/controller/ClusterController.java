package cn.edu.scut.patent.controller;

import java.util.Date;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import cn.edu.scut.patent.prework.Cluster;
import cn.edu.scut.patent.util.StringHelper;

@Controller
public class ClusterController {

	/**
	 * 进行聚类入口
	 * 
	 * @throws Exception
	 */
	@RequestMapping(value = "cluster")
	public ModelAndView cluster() throws Exception {
		// 聚类开始的时间
		long startTime = new Date().getTime();
		System.out.println("进行聚类啦！");
		new Cluster().doCluster();
		String timeConsume = StringHelper.timer(startTime);

		ModelAndView mv = new ModelAndView("alert", "command", "聚类成功！耗时"
				+ timeConsume);
		return mv;
	}
}
