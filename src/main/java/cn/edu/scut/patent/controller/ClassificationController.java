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
import cn.edu.scut.patent.service.ClassificationService;

@Controller
public class ClassificationController {

	/**
	 * 更新Triz号码
	 * 
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "updateTrizNumber")
	public void updateTrizNumber(HttpServletRequest request,
			HttpServletResponse response) {
		try {
			request.setCharacterEncoding("utf-8");
			response.setContentType("text/html;charset=utf-8");
		} catch (UnsupportedEncodingException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		String PTT_NUM = request.getParameter("PTT_NUM");
		String[] TRIZ_NUM = request.getParameterValues("triz");
		for (int i = 0; i < TRIZ_NUM.length; i++) {
			System.out.println(i + "@@" + TRIZ_NUM[i]);
		}
		System.out.println();
		String result;
		if (new ClassificationService().updateClassificationNumber(PTT_NUM,
				TRIZ_NUM)) {
			result = "专利分类信息已存入数据库！感谢您的支持！";
		} else {
			result = "专利分类信息已存入数据库失败！";
		}
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

	/**
	 * 获取分类号
	 * 
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "getClassification")
	public void getClassification(HttpServletRequest request,
			HttpServletResponse response) {
		try {
			request.setCharacterEncoding("utf-8");
			response.setContentType("text/html;charset=utf-8");
		} catch (UnsupportedEncodingException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		String pttNum = request.getParameter("pttNum");
		List<Integer> trizNumList = new ClassificationService()
				.getAllFromPttNum(pttNum);

		JSONArray jsonArray = new JSONArray();
		for (int trizNum : trizNumList) {
			JSONObject jsonObj = new JSONObject();
			jsonObj.put("trizNum", trizNum);
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
