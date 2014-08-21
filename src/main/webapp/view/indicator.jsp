<%@ page contentType="text/html; charset=utf-8" %>
<%@ page pageEncoding="utf-8"%>
<%@ page language="java" import="java.sql.*,java.util.*,java.net.*,cn.edu.scut.patent.util.Constants"%>
<%@ include file="path&check.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>统计指标</title>
<base href="<%=basePath%>">
<script type="text/javascript" src="js/jquery-1.11.1.min.js"></script>
<script type="text/javascript" src="js/useful_function.js"></script>
<script type="text/javascript">
$(document).ready(function() {
    //让div居中
	mediate($('.indicatorDivClass'));
});
</script>
<link href="css/indicator.css" rel="stylesheet" type="text/css" />
</head>
<body>
<%@ include file="client.jsp" %>
<div class="indicatorDivClass">
<div>
<form method="post" action="chart">
<table border="0" class="tableClass">
<tr><td>
<input type="submit" class="buttonClass" value="统计展示"></input>
</td></tr>
</table>
</form>
</div>
<div>
<form method="post" action="technicalDataChart">
<input type="hidden" name="INDICATOR_TYPE" value="<%=Constants.TECHNICAL_GROWTH_RATE_NUMBER%>" />
<table class="tableClass">
<tr><td><input type="text" name="KEY_WORD" value="" class="textClass"/></td><td><input type="submit" class="buttonClass" value="技术生长率"></input></td></tr>
</table>
</form>
</div>
<div>
<form method="post" action="technicalDataChart">
<input type="hidden" name="INDICATOR_TYPE" value="<%=Constants.TECHNICAL_MATURE_RATE_NUMBER%>" />
<table class="tableClass">
<tr><td><input type="text" name="KEY_WORD" value="" class="textClass"/></td><td><input type="submit" class="buttonClass" value="技术成熟率"></input></td></tr>
</table>
</form>
</div>
<div>
<form method="post" action="clusterDataChart">
<input type="hidden" name="INDICATOR_TYPE" value="<%=Constants.CLUSTER_NUMBER%>" />
<table class="tableClass">
<tr><td><input type="text" name="KEY_WORD" value="" class="textClass"/></td><td><input type="submit" class="buttonClass" value="聚类分析"></input></td></tr>
</table>
</form>
</div>
</div>
</body>
</html>
