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
<title>初始化</title>
<base href="<%=basePath%>">
<link rel="stylesheet" href="css/all.css" type="text/css">
</head>
<body>
<div class="image">
<img src="img/school_badge.png"/>
</div>
<form name="indexForm" method="post" action="index">
<p id="button" class="button1"><input type="submit" value="建立索引"></input></p>
</form>
<form name="clusterForm" method="post" action="cluster">
<p id="button" class="button1"><input type="submit" value="进行聚类"></input></p>
</form>
</body>
</html>
