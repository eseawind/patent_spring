<%
	request.getRequestDispatcher("/view/showall.jsp")
			.forward(request, response);
%>
<%@ page language="java" pageEncoding="utf-8"%>
<%@ page contentType="text/html; charset=utf-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<title>华南理工大学商业方法专利检索系统</title>
</head>

<body>
<div>华南理工大学商业方法专利检索系统</div>
<p>
<form method=get action="http://www.google.com/search">
<table bgcolor="#FFFFFF"><tr><td>
<a href="http://www.google.com/intl/zh-CN/">
<img src="http://www.google.com/logos/Logo_40wht.gif" 
border="0" alt="Google" align="absmiddle" width="81" height="27"></a>
<input type=text name=q size=20 maxlength=255 value="">
<input type=hidden name=hl  value=zh-CN>
<input type=submit name=btnG  value="Google 搜索">
</td></tr></table>
</form>
</p>
<form name="request_form" method="post">
<table>
<tr>
<td>申请号</td>
<td><input type="text" name="apply_number"/></td>
</tr>
<tr>
<td>申请时间</td>
<td><input type="text" name="apply_time"/></td>
</tr>
<tr>
<td>申请人</td>
<td><input type="text" name="apply_member"/></td>
</tr>
<tr>
<td>专利名称</td>
<td><input type="text" name="patent_name"/></td>
</tr>
<tr>
<td>摘要</td>
<td><input type="text" name="summary"/></td>
</tr>
<tr>
<td align="center" colspan="2"><input type="submit" value="提交查询"/></td>
</tr>
</table>
</form>
</body>
</html>