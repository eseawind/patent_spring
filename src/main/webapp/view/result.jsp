<%@ page contentType="text/html; charset=utf-8"%>
<%@ page pageEncoding="utf-8"%>
<%@ page language="java"
	import="java.sql.*,java.util.*,java.net.*,cn.edu.scut.patent.model.PatentDao"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://"
	+ request.getServerName() + ":" + request.getServerPort()
	+ path + "/";
%>
<%
	String htmlString = "";
	int count = 10;
	List<PatentDao> patentList = (ArrayList<PatentDao>) session.getAttribute("PATENTLIST");
	String pageString = (String)request.getParameter("PAGE");
	int pageNumber;
	int pageTotal;
	int totalItem;
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>检索结果</title>
<base href="<%=basePath%>">
<link rel="stylesheet" href="css/all.css" type="text/css">
</head>
<body>
<div class="image">
<img src="img/school_badge.png"/>
</div>
<hr width="100%"/>
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
				htmlString += "<table border=0 cellspacing='10px' cellpadding='10px'><tr class='search_top'><td width='5%'>序号</td><td width='24%'>专利名称</td><td width='15%'>公开号</td><td width='15%'>发明人</td><td width='15%'>申请人</td><td width='26%'>专利公开日</td></tr>";
				for (int i = (pageNumber - 1) * count; i < final_item; i++) {
					PatentDao pttDao = patentList.get(i);

					htmlString += "<tr>";
					htmlString += "<td>" + (i + 1) + "</td>";
					htmlString += "<td>" + "<a href='/patent_spring/view/showFile.jsp?&APPLY_NUM=" + pttDao.getApplyNum() + "&PTT_NUM=" + pttDao.getPttNum() + "&PTT_TYPE=" + pttDao.getPttType() + "'>" + pttDao.getPttName() + "</a>" + "</td>";
					htmlString += "<td>" + pttDao.getPttNum() + "</td>";
					htmlString += "<td>" + pttDao.getInventor() + "</td>";
					htmlString += "<td>" + pttDao.getProposer() + "</td>";
					htmlString += "<td>" + pttDao.getPttDate() + "</td>";
					htmlString += "</tr>";
				}
				htmlString += "</table>";
				htmlString += "<table><tr><td>一共搜索到" + totalItem + "条结果</td><td>&nbsp;</td>";
				String href_root = "/patent_spring/view/result.jsp?PAGE=";
				if (pageNumber > 1) {
					htmlString += "<td><a href='" + href_root + (pageNumber - 1) + "'>上一页</a></td><td>&nbsp;</td>";
				}
				if (pageNumber - 2 > 0) {
					htmlString += "<td><a href='" + href_root + (pageNumber - 2) + "'>"
							+ (pageNumber - 2) + "</a></td><td>&nbsp;</td>";
				}
				if (pageNumber - 1 > 0) {
					htmlString += "<td><a href='" + href_root + (pageNumber - 1) + "'>"
							+ (pageNumber - 1) + "</a></td><td>&nbsp;</td>";
				}
				htmlString += "<td>" + pageNumber + "</td><td>&nbsp;</td>";
				if (pageNumber + 1 <= pageTotal) {
					htmlString += "<td><a href='" + href_root + (pageNumber + 1) + "'>"
							+ (pageNumber + 1) + "</a></td><td>&nbsp;</td>";
				}
				if (pageNumber + 2 <= pageTotal) {
					htmlString += "<td><a href='" + href_root + (pageNumber + 2) + "'>"
							+ (pageNumber + 2) + "</a></td><td>&nbsp;</td>";
				}
				if (pageNumber < pageTotal) {
					htmlString += "<td><a href='" + href_root + (pageNumber + 1) + "'>下一页</a></td><td>&nbsp;</td>";
				}
				htmlString += "<td>共" + pageTotal + "页</td><td>&nbsp;</td>";
				htmlString += "<td>转到第";
				htmlString += "<select onchange='location.href=" + "this.options[this.selectedIndex].value;'>";
				for(int i = 1; i <= pageTotal; i++){
					htmlString += "<option value='"+ href_root + i + "' ";
					if(i == pageNumber){
						htmlString += "selected";
					}
					htmlString += ">" + i + "</option>";
				}
				htmlString += "</select>";
				htmlString += "页</td></tr></table>";
			}
		}
		out.print(htmlString);
	%>
</body>
</html>