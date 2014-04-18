package cn.edu.scut.patent.controller;

import java.util.List;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import cn.edu.scut.patent.util.DatabaseHelper;
import cn.edu.scut.patent.util.StringHelper;

@Controller
public class ChartController {

	/**
	 * 统计数据入口
	 * 
	 * @throws Exception
	 */
	@RequestMapping(value = "chart")
	public void chart(HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		
		String dateString = StringHelper.getTime();
		String X_Categories = "";
		List<String> listTRIZ = DatabaseHelper.getAllTRIZ();
		for(int i = 0; i < listTRIZ.size(); i++){
			X_Categories += "'" + listTRIZ.get(i) + "'";
			if(i != listTRIZ.size() - 1){
				X_Categories += ",";
			}
		}
		
		String Data = "{name: 'TRIZ',data: [";
		List<String> listCount = DatabaseHelper.getCount();
		for(int i = 0; i < listCount.size(); i++){
			Data += listCount.get(i);
			if(i != listCount.size() - 1){
				Data += ",";
			}
		}
		Data += "]}";
		
		HttpSession session = request.getSession();
		session.setAttribute("Chart_Type", "column");
		session.setAttribute("Title", "TRIZ原理使用频率统计");
		session.setAttribute("Subtitle", dateString);
		session.setAttribute("X_Text", "40个创新原理");
		session.setAttribute("Y_Text", "使用次数");
		session.setAttribute("Unit", "次");
		session.setAttribute("Credits_Text", "华南理工大学");
		session.setAttribute("Credits_Href", "http://www.scut.edu.cn");
		session.setAttribute("X_Categories", X_Categories);
		session.setAttribute("Data", Data);
		
		RequestDispatcher re = request.getRequestDispatcher("view/chart.jsp");
		try {
			re.forward(request, response);
		} catch (ServletException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
