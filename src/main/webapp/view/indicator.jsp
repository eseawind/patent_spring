<%@ page contentType="text/html; charset=utf-8" %>
<%@ page pageEncoding="utf-8"%>
<%@ page language="java" import="java.sql.*,java.util.*,java.net.*,cn.edu.scut.patent.util.Constants"%>
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
<title>统计指标</title>
<base href="<%=basePath%>">
<link rel="stylesheet" href="css/all.css" type="text/css">
</head>
<body>
<div class="image">
<img src="img/school_badge.png"/>
</div>
<form name="chartForm" method="post" action="chart">
<p id="button" class="button1"><input type="submit" value="统计展示"></input></p>
</form>
<form name="technicalGrowthRateDataForm" method="post" action="technicalDataChart">
<input type="hidden" name="INDICATOR_TYPE" value="<%=Constants.TECHNICAL_GROWTH_RATE_NUMBER%>" />
<table border=0 cellspacing="20px" cellpadding="20px">
<tr><td><input type="text" name="KEY_WORD" value="" class="RowInput"/></td><td><p id="button" class="button1"><input type="submit" value="技术生长率"></input></p></td></tr>
</table>
</form>
<form name="technicalMatureRateDataForm" method="post" action="technicalDataChart">
<input type="hidden" name="INDICATOR_TYPE" value="<%=Constants.TECHNICAL_MATURE_RATE_NUMBER%>" />
<table border=0 cellspacing="20px" cellpadding="20px">
<tr><td><input type="text" name="KEY_WORD" value="" class="RowInput"/></td><td><p id="button" class="button1"><input type="submit" value="技术成熟率"></input></p></td></tr>
</table>
</form>
</body>
</html>
