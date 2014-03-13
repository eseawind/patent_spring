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
<title>专利汇总页</title>
<base href="<%=basePath%>">
</head>
<body>
<form name="searchForm" method="post" action="search">
<table>
<tr><td>申请号</td><td><input type="text" name="applyNum" value=""/></td></tr>
<tr><td>申请日期</td><td><input type="text" name="applyDate" value=""/></td></tr>
<tr><td>专利名称</td><td><input type="text" name="pttName" value=""/></td></tr>
<tr><td>专利公开号</td><td><input type="text" name="pttNum" value=""/></td></tr>
<tr><td>专利公开日</td><td><input type="text" name="pttDate" value=""/></td></tr>
<tr><td>主分类号</td><td><input type="text" name="pttMainClassNum" value=""/></td></tr>
<tr><td>分类号</td><td><input type="text" name="pttClassNum" value=""/></td></tr>
<tr><td>申请（专利权）人</td><td><input type="text" name="proposer" value=""/></td></tr>
<tr><td>申请人地址</td><td><input type="text" name="proposerAddress" value=""/></td></tr>
<tr><td>发明（设计）人</td><td><input type="text" name="inventor" value=""/></td></tr>
<tr><td>专利代理机构</td><td><input type="text" name="pttAgencyOrg" value=""/></td></tr>
<tr><td>专利代理人</td><td><input type="text" name="pttAgencyPerson" value=""/></td></tr>
<tr><td>专利摘要</td><td><input type="text" name="pttAbstract" value=""/></td></tr>
<tr><td>商业方法类下的分类号</td><td><input type="text" name="classNumG06Q" value=""/></td></tr>
<tr><td>国际申请</td><td><input type="text" name="internationalApply" value=""/></td></tr>
<tr><td>国际公布</td><td><input type="text" name="internationalPublication" value=""/></td></tr>
<tr><td><input type="submit" value="Search提交"></td><td><input type="reset" value="重置"></td></tr>
</table>
</form>
<form name="indexForm" method="post" action="index">
<table>
<tr><td><input type="submit" value="Index提交"></td><td><input type="reset" value="重置"></td></tr>
</table>
</form>
<%
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
%>
</body>
</html>