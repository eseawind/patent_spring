<%@ page contentType="text/html; charset=utf-8" %>
<%@ page pageEncoding="utf-8"%>
<%
String account = (String) session.getAttribute("Account");
String email;
if(account == null){
	email = "";
}else {
	email = account;
}
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<script type="text/javascript" src="js/useful_function.js"></script>
<script type="text/javascript">
$(document).ready(function(){
	//最大化宽度
	maxWidth($('#welcome'));
	if($('#welcome').val()==''){
		$('#exit').hide();
	}else {
		$('#exit').show();
	}
});
</script>
<link href="css/client.css" rel="stylesheet" type="text/css" />
</head>
<body>
<input type="hidden" id="welcome" value='<%=email%>' />
<div id="welcomeDiv">
<form method="post" id="exitForm" action="exit">
<label><%=email%></label>&nbsp;&nbsp;</label><label>欢迎来到华南理工大学专利检索系统</label>
<input type="submit" id="exit" value="退出" />
</form>
</div>
</body>
</html>