package cn.edu.scut.patent.controller;

import java.util.List;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import cn.edu.scut.patent.core.Indicator;
import cn.edu.scut.patent.model.IndicatorData;
import cn.edu.scut.patent.model.IndicatorParam;
import cn.edu.scut.patent.model.IndicatorValueItem;
import cn.edu.scut.patent.util.StringHelper;

@Controller
public class TechnicalGrowthRateDataChartController {

	/**
	 * 展示技术生长率
	 * 
	 * @throws Exception
	 */
	@RequestMapping(value = "technicalGrowthRateDataChart")
	public void technicalGrowthRateDataChart(HttpServletRequest request,
			HttpServletResponse response) throws Exception {

		String keyWord = request.getParameter("KEY_WORD");
		IndicatorParam param = new IndicatorParam();
		param.keyWord = keyWord;
		IndicatorData indicatorData = Indicator
				.getTechnicalGrowthRateData(param);
		if(indicatorData == null){
			
		}
		List<IndicatorValueItem> value11 = indicatorData.value11;
		String X_Categories = "";
		for (int i = 0; i < value11.size(); i++) {
			X_Categories += "'" + value11.get(i).year + "'";
			if (i != value11.size() - 1) {
				X_Categories += ",";
			}
		}

		String Data = "{name: '技术生长率',data: [";
		for (int i = 0; i < value11.size(); i++) {
			Data += value11.get(i).value;
			if (i != value11.size() - 1) {
				Data += ",";
			}
		}
		Data += "]}";

		String dateString = StringHelper.getTime();

		HttpSession session = request.getSession();
		session.setAttribute("Chart_Type", "spline");
		session.setAttribute("Title", "技术生长率");
		session.setAttribute("Subtitle", dateString);
		session.setAttribute("X_Text", "年份");
		session.setAttribute("Y_Text", "百分比");
		session.setAttribute("Unit", "");
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
