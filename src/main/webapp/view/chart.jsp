<%@ page contentType="text/html; charset=utf-8"%>
<%@ page pageEncoding="utf-8"%>
<%@ page language="java" import="java.sql.*"%>
<%
String Chart_Type = (String) session.getAttribute("Chart_Type");
String Title = (String) session.getAttribute("Title");
String Subtitle = (String) session.getAttribute("Subtitle");
String X_Text = (String) session.getAttribute("X_Text");
String Y_Text = (String) session.getAttribute("Y_Text");
String Unit = (String) session.getAttribute("Unit");
String Credits_Text = (String) session.getAttribute("Credits_Text");
String Credits_Href = (String) session.getAttribute("Credits_Href");
String X_Categories = (String) session.getAttribute("X_Categories");
String Data = (String) session.getAttribute("Data");
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>统计页面</title>
<script type="text/javascript" src="js/jQuery-1.11.0/jquery-1.11.0.min.js"></script>
<script type="text/javascript" src="js/Highcharts-3.0.10/highcharts.js"></script>
<script type="text/javascript" src="js/Highcharts-3.0.10/highcharts-more.js"></script>
<script src="js/Highcharts-3.0.10/modules/exporting.js"></script><!--图表导出功能-->
<script src="js/Highcharts-3.0.10/themes/gray.js"></script><!--图表主题-->
<script>
	 $(function () { //JQUERY的内置函数，表示网页加载完毕后要执行的意思
	    $('#container').highcharts({	//图表展示容器，与id保持一致
	        chart: {
				//指定图表的类型，默认是折线图（line）
	            type: '<%=Chart_Type%>',
	            zoomType: 'xy'
	        },
	        title: {
				//指定图表标题
	            text: '<%=Title%>'
	        },
			subtitle: {
				//指定图表副标题
            	text: '<%=Subtitle%>',
        	},
	        xAxis: {
	        	title: {
					//指定x轴的标题
	                text: '<%=X_Text%>',
	            },
	            categories: [<%=X_Categories%>],
	            labels: {
	            	rotation: 90,//文字竖排样式
                },
	        },
	        yAxis: {
	            title: {
					//指定y轴的标题
	                text: '<%=Y_Text%>'
	            }
	        },
			tooltip: {//数据点提示框（默认开启）
            	valueSuffix: '<%=Unit%>'	//数据的单位
        	},
			legend: {
				align: 'right',
			},
	        series: [<%=Data%>],
			credits:{
				text: '<%=Credits_Text%>',
				href: '<%=Credits_Href%>',
			}
	    });
	});
   </script>
</head>
<body>
<div id="container" style="min-width:800px;height:400px;"></div>
</body>
</html>