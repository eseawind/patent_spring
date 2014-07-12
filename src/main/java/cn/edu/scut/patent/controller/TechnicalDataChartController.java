package cn.edu.scut.patent.controller;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import cn.edu.scut.patent.model.ClusterValueItem;
import cn.edu.scut.patent.model.IndicatorData;
import cn.edu.scut.patent.model.IndicatorParam;
import cn.edu.scut.patent.model.IndicatorValueItem;
import cn.edu.scut.patent.service.Indicator;
import cn.edu.scut.patent.util.Constants;
import cn.edu.scut.patent.util.StringHelper;

@Controller
public class TechnicalDataChartController {

	/**
	 * 展示各种技术指标
	 * 
	 * @throws Exception
	 */
	@RequestMapping(value = "technicalDataChart")
	public void technicalGrowthRateDataChart(HttpServletRequest request,
			HttpServletResponse response) throws Exception {

		String keyWord = request.getParameter("KEY_WORD");
		int indicatorType = Integer.parseInt(request
				.getParameter("INDICATOR_TYPE"));
		IndicatorParam param = new IndicatorParam();
		param.keyWord = keyWord;
		param.indicatorType = indicatorType;
		IndicatorData indicatorData = new IndicatorData();
		String Data = "";
		if (indicatorType == Constants.TECHNICAL_GROWTH_RATE_NUMBER) {
			indicatorData = Indicator.getTechnicalGrowthRateData(param);
			Data += "{name: '技术生长率',data: [";
		} else if (indicatorType == Constants.TECHNICAL_MATURE_RATE_NUMBER) {
			indicatorData = Indicator.getTechnicalMatureRateData(param);
			Data += "{name: '技术成熟率',data: [";
		}
		List<IndicatorValueItem> value11 = indicatorData.value11;
		String X_Categories = "";
		for (int i = 0; i < value11.size(); i++) {
			X_Categories += "'" + value11.get(i).year + "'";
			if (i != value11.size() - 1) {
				X_Categories += ",";
			}
		}

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
		if (indicatorType == Constants.TECHNICAL_GROWTH_RATE_NUMBER) {
			session.setAttribute("Title", "技术生长率");
		} else if (indicatorType == Constants.TECHNICAL_MATURE_RATE_NUMBER) {
			session.setAttribute("Title", "技术成熟率");
		}
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

	/**
	 * 展示聚类情况
	 * 
	 * @throws Exception
	 */
	@RequestMapping(value = "clusterDataChart")
	public void clusterDataChart(HttpServletRequest request,
			HttpServletResponse response) throws Exception {

		String keyWord = request.getParameter("KEY_WORD");
		int indicatorType = Integer.parseInt(request
				.getParameter("INDICATOR_TYPE"));
		IndicatorParam param = new IndicatorParam();
		param.keyWord = keyWord;
		param.indicatorType = indicatorType;
		Map<String, ClusterValueItem> map = new HashMap<String, ClusterValueItem>();
		String Data = "";

		String X_Categories = "";
		for (int i = 1; i <= 20; i++) {
			X_Categories += "'" + i + "'";
			if (i != 20) {
				X_Categories += ",";
			}
		}
		if (indicatorType == Constants.CLUSTER_NUMBER) {
			map = Indicator.getClusterData(param);
			if (map != null) {
				Data += "{data: [";
				if (map.size() > 0) {
					Set<String> keySet = map.keySet();
					Iterator<String> it = keySet.iterator();
					while (it.hasNext()) {
						String key = it.next();
						ClusterValueItem value = map.get(key);
						Data += "[" + value.cluster + "," + value.pttClass
								+ "," + value.count + "]";
						if (it.hasNext()) {
							Data += ",";
						}
					}
				}
				Data += "]}";
			}
		}

		String dateString = StringHelper.getTime();

		HttpSession session = request.getSession();
		session.setAttribute("Chart_Type", "bubble");
		session.setAttribute("Title", "聚类情况");
		session.setAttribute("Subtitle", dateString);
		session.setAttribute("X_Text", "聚类群");
		session.setAttribute("Y_Text", "ClassNumG06Q");
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
