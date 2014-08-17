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
<title>华南理工大学专利检索系统</title>
<base href="<%=basePath%>">
<link rel="stylesheet" href="css/all.css" type="text/css">
</head>
<body>
<div class="image">
<img src="img/school_badge.png"/>
</div>
<div><p id="title">华南理工大学专利检索系统</p></div>
<p id="button" class="button1"><input type="button" value="专利检索" onClick="window.location.href='/patent_spring/view/search.jsp';"></input></p>
<p id="button" class="button1"><input type="button" value="统计指标" onClick="window.location.href='/patent_spring/view/indicator.jsp';"></input></p>
<p id="button" class="button1"><input type="button" value="初始化" onClick="window.location.href='/patent_spring/view/administrator.jsp';"></input></p>
</body>
</html>
