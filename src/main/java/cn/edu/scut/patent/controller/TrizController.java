package cn.edu.scut.patent.controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import cn.edu.scut.patent.model.Triz;
import cn.edu.scut.patent.service.TrizService;

@Controller
public class TrizController {

	/**
	 * 获取Triz原理
	 * 
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "getTriz")
	public void getTriz(HttpServletRequest request, HttpServletResponse response) {
		try {
			request.setCharacterEncoding("utf-8");
			response.setContentType("text/html;charset=utf-8");
		} catch (UnsupportedEncodingException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		List<Triz> trizList = new TrizService().getAllTriz();
		JSONArray jsonArray = new JSONArray();
		for (Triz triz : trizList) {
			JSONObject jsonObj = new JSONObject();
			jsonObj.put("Text", triz.getTrizText());
			jsonArray.put(jsonObj);
		}

		PrintWriter out;
		try {
			out = response.getWriter();
			out.write(jsonArray.toString());
			out.flush();
			out.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
