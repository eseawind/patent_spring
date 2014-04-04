<%@ page contentType="text/html; charset=utf-8" %>
<%@ page pageEncoding="utf-8"%>
<%@ page language="java" import="java.sql.*,java.util.*,java.net.*"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme() + "://"
    + request.getServerName() + ":" + request.getServerPort()
    + path + "/";
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>专利检索</title>
<base href="<%=basePath%>">
<link rel="stylesheet" href="css/all.css" type="text/css">
</head>
<body>
<div class="image">
<img src="img/school_badge.png"/>
</div>
<hr width=100%/>
<form name="searchForm" method="post" action="search">
<table cellpadding="20px" cellspacing="20px" border=0>
<tr>
<td colspan="4" class="td1">
<input type="checkbox" name="FMZL" value="11" checked="checked">
<label for="FMZL">发明</label>
<input type="checkbox" name="SYXX" value="22" checked="checked">
<label for="SYXX">实用新型</label>
<input type="checkbox" name="WGSJ" value="33" checked="checked">
<label for="WGZL" style="vertical-align:middle">外观设计</label>
</td>
</tr>
<tr>
<td colspan="4">
</td>
</tr>
<tr><td><input type="button" value="申请号" class="RowButton"></td><td><input type="text" name="APPLY_NUM" value="" class="RowInput"/></td>
<td><input type="button" value="申请日期" class="RowButton"></td><td><input type="text" name="APPLY_DATE" value="" class="RowInput"/></td></tr>
<tr><td><input type="button" value="专利名称" class="RowButton"></td><td><input type="text" name="PTT_NAME" value="" class="RowInput"/></td>
<td><input type="button" value="专利公开号" class="RowButton"></td><td><input type="text" name="PTT_NUM" value="" class="RowInput"/></td></tr>
<tr><td><input type="button" value="专利公开日" class="RowButton"></td><td><input type="text" name="PTT_DATE" value="" class="RowInput"/></td>
<td><input type="button" value="主分类号" class="RowButton"></td><td><input type="text" name="PTT_MAIN_CLASS_NUM" value="" class="RowInput"/></td></tr>
<tr><td><input type="button" value="分类号" class="RowButton"></td><td><input type="text" name="PTT_CLASS_NUM" value="" class="RowInput"/></td>
<td><input type="button" value="申请（专利权）人" class="RowButton"></td><td><input type="text" name="PROPOSER" value="" class="RowInput"/></td></tr>
<tr><td><input type="button" value="申请人地址" class="RowButton"></td><td><input type="text" name="PROPOSER_ADDRESS" value="" class="RowInput"/></td>
<td><input type="button" value="发明（设计）人" class="RowButton"></td><td><input type="text" name="INVENTOR" value="" class="RowInput"/></td></tr>
<tr><td><input type="button" value="国际申请" class="RowButton"></td><td><input type="text" name="INTERNATIONAL_APPLY" value="" class="RowInput"/></td>
<td><input type="button" value="国际公布" class="RowButton"></td><td><input type="text" name="INTERNATIONAL_PUBLICATION" value="" class="RowInput"/></td></tr>
<tr><td><input type="button" value="初始日" class="RowButton"></td><td><input type="text" name="INTO_DATE" value="" class="RowInput"/></td>
<td><input type="button" value="专利代理机构" class="RowButton"></td><td><input type="text" name="PTT_AGENCY_ORG" value="" class="RowInput"/></td></tr>
<tr><td><input type="button" value="专利代理人" class="RowButton"></td><td><input type="text" name="PTT_AGENCY_PERSON" value="" class="RowInput"/></td>
<td><input type="button" value="专利摘要" class="RowButton"></td><td><input type="text" name="PTT_ABSTRACT" value="" class="RowInput"/></td></tr>
<tr><td colspan="2" class="submit1"><input type="submit" value="提交"></td><td colspan="2" class="reset1"><input type="reset" value="重置"></td></tr>
</table>
</form>
<form name="indexForm" method="post" action="index">
<table border=0 cellspacing="20px" cellpadding="20px">
<tr><td class="submit1"><input type="submit" value="Index提交"></td></tr>
</table>
</form>
<form name="chartForm" method="post" action="chart">
<table border=0 cellspacing="20px" cellpadding="20px">
<tr><td class="submit1"><input type="submit" value="统计展示"></td></tr>
</table>
</form>
<%--显示所有的专利<%
try{
// 加载驱动
Class.forName("com.mysql.jdbc.Driver").newInstance();
String url = "jdbc:mysql://localhost/patentdb";
String user = "root";
String password = "123";
Connection conn = DriverManager.getConnection(url, user, password);
//创建用于将SQL语句发送到数据库的SQLServerStatement对象。
Statement st = conn.createStatement();
//执行给定的SQL语句，该语句返回单个ResultSet对象。
String sqlString = "select * from patents";
ResultSet rs = st.executeQuery(sqlString);
String targethtml = "/patent_spring/view/showPDF.jsp";
out.println("<h1>专利列表</h1><hr><table><tr><td>专利编号</td><td>专利名称</td><td>专利申请号</td><td>专利申请</td></tr>");
while(rs.next()){
out.print("<tr>");
out.print("<td>" + "<a href='" + targethtml + "?FILE_NAME=" + java.net.URLEncoder.encode(rs.getString("FILE_NAME"), "UTF-8") + "&PTT_NUM=" + java.net.URLEncoder.encode(rs.getString("PTT_NUM"), "UTF-8") + "'>" +rs.getString("PTT_NUM") + "</a>" + "</td><td>" + rs.getString("PTT_NAME") + "</td><td>" + rs.getString("APPLY_NUM") + "</td><td>" + rs.getString("PROPOSER") + "</td>");
out.print("</tr>");
}
out.print("</table><hr>");
conn.close();
}catch(Exception e){
out.print("无数据");
out.print(e.getMessage());
}
%>--%>
</body>
</html>