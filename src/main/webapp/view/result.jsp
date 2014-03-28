<%@ page contentType="text/html; charset=utf-8"%>
<%@ page pageEncoding="utf-8"%>
<%@ page language="java"
	import="java.sql.*,java.util.*,java.net.*,cn.edu.scut.patent.model.PatentDao"%>
<%
	String htmlString = "";
	int count = 10;
	List<PatentDao> patentList = (ArrayList<PatentDao>) request
			.getSession().getAttribute("PATENTLIST");
	String pageString = (String) request.getAttribute("PAGE");
	int pageNumber;
	int pageTotal;
	int totalItem;
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>检索结果</title>
</head>
<body>
	<%
		if (patentList == null) {
			htmlString += "找不到任何检索结果";
		} else {
			totalItem = patentList.size();
			if (totalItem % count == 0) {
				pageTotal = totalItem / count;
			} else {
				pageTotal = totalItem / count + 1;
			}
			if (pageString == null) {
				pageNumber = 1;
			} else {
				pageNumber = Integer.parseInt(pageString);
			}
			if (pageNumber > totalItem) {
				htmlString += "页面溢出！";
			} else {
				int final_item;
				if (pageNumber < pageTotal) {
					final_item = pageNumber * count;
				} else {
					final_item = totalItem;
				}
				htmlString += "<table><th><td>序号</td><td>专利名称</td><td>公开号</td><td>发明人</td><td>申请人</td><td>专利公开日</td></th></th>";
				for (int i = (pageNumber - 1) * count; i < final_item; i++) {
					PatentDao pttDao = patentList.get(i);

					htmlString += "<tr>";
					htmlString += "<td>" + (i + 1) + "</td>";
					htmlString += "<td>" + "<a href='/patent_spring/view/showFile.jsp?FILE_NAME=200810142584.pdf" + "&PTT_NUM=" + pttDao.getPttNum() + "'>" + pttDao.getPttName() + "</a>" + "</td>";
					htmlString += "<td>" + pttDao.getPttNum() + "</td>";
					htmlString += "<td>" + pttDao.getInventor() + "</td>";
					htmlString += "<td>" + pttDao.getProposer() + "</td>";
					htmlString += "<td>" + pttDao.getPttDate() + "</td>";
					htmlString += "</tr>";
				}
				htmlString += "</table>";
				htmlString += "</br>一共搜索到" + totalItem + "条结果";
				if (pageNumber > 1) {
					htmlString += "<a href='http://www.baidu.com'>上一页</a>";
				}
				if (pageNumber - 2 > 0) {
					htmlString += "<a href='http://www.baidu.com'>"
							+ (pageNumber - 2) + "</a>";
				}
				if (pageNumber - 1 > 0) {
					htmlString += "<a href='http://www.baidu.com'>"
							+ (pageNumber - 1) + "</a>";
				}
				htmlString += pageNumber;
				if (pageNumber + 1 <= pageTotal) {
					htmlString += "<a href='http://www.baidu.com'>"
							+ (pageNumber + 1) + "</a>";
				}
				if (pageNumber + 2 <= pageTotal) {
					htmlString += "<a href='http://www.baidu.com'>"
							+ (pageNumber + 2) + "</a>";
				}
				if (pageNumber < pageTotal) {
					htmlString += "<a href='/patent_spring/view/result.jsp'>下一页</a>";
				}
				htmlString += "共" + pageTotal + "页";
			}
		}
		out.print(htmlString);
	%>
</body>
</html>