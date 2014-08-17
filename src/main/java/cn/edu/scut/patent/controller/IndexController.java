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
import cn.edu.scut.patent.core.Index;
import cn.edu.scut.patent.util.StringHelper;

@Controller
public class IndexController {

	/**
	 * 索引入口
	 * 
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "index")
	public void index(HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		try {
			request.setCharacterEncoding("utf-8");
			response.setContentType("text/html;charset=utf-8");
		} catch (UnsupportedEncodingException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		// 索引开始的时间
		long startTime = new Date().getTime();
		System.out.println("进入index啦！");
		new Index().doIndexFromDatabase();
		String timeConsume = StringHelper.timer(startTime);

		String result = "索引建立成功！耗时" + timeConsume;
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
