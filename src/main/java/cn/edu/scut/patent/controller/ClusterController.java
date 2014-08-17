package cn.edu.scut.patent.controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.Date;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.json.JSONObject;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import cn.edu.scut.patent.prework.Cluster;
import cn.edu.scut.patent.util.StringHelper;

@Controller
public class ClusterController {

	/**
	 * 进行聚类入口
	 * 
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	@RequestMapping(value = "cluster")
	public void cluster(HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		try {
			request.setCharacterEncoding("utf-8");
			response.setContentType("text/html;charset=utf-8");
		} catch (UnsupportedEncodingException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		// 聚类开始的时间
		long startTime = new Date().getTime();
		System.out.println("进行聚类啦！");
		new Cluster().doCluster();
		String timeConsume = StringHelper.timer(startTime);

		String result = "聚类成功！耗时" + timeConsume;
		JSONObject jsonObj = new JSONObject();
		jsonObj.put("result", result);
		PrintWriter out;
		try {
			out = response.getWriter();
			out.write(jsonObj.toString());
			out.flush();
			out.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
