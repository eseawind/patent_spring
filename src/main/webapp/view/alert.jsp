<%@ page contentType="text/html; charset=UTF-8" %>
<%@ page pageEncoding="utf-8"%>
<%@ page language="java" import="java.util.*,java.io.*"%>
<%@ include file="path&check.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>执行结果</title>
<base href="<%=basePath%>">
<link rel="stylesheet" href="css/all.css" type="text/css">
</head>
<body>
<div class="image">
<img src="img/school_badge.png"/>
</div>
<div>
<p id="title"><%=request.getAttribute("command")%></p>
</div>
</body>
</html>