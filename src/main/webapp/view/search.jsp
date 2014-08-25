<%@ page contentType="text/html; charset=utf-8" %>
<%@ page pageEncoding="utf-8"%>
<%@ page language="java" import="java.sql.*,java.util.*,java.net.*"%>
<%@ include file="path&check.jsp" %>
<%@ include file="/permission/userPermission.jsp" %>
<%
String noResultFoundError = (String)request.getAttribute("NoResultFoundError");
String noResultFound;
if(noResultFoundError == null){
	noResultFound = "hide";
}else{
	noResultFound = "show";
}
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>专利检索</title>
<base href="<%=basePath%>">
<script type="text/javascript" src="js/jquery-1.11.1.min.js"></script>
<script type="text/javascript" src="js/jquery-ui.min.js"></script>
<script type="text/javascript" src="js/useful_function.js"></script>
<script type="text/javascript">
$(document).ready(function() {
    //让div居中
	mediate($('.searchDivClass'));
	//加入日期选择控件
	$('#PTT_DATE, #APPLY_DATE, #INTO_DATE').datepicker({
		changeMonth:true,
		changeYear:true,
		dateFormat:'yy-mm-dd',
		yearRange:'-100:+100'
	});
	//设置waitingDialog和noResultFound的效果
	$('#waitingDialog, #noResultFoundDialog').dialog({
		autoOpen:false,
		closeOnEscape:false,
		draggable:false,
		modal:true,
		title:"查询"
	});
	//判断noResultFound的dialog应该展示还是隐藏
    if($('#noResultFound').val()=='hide'){
    	$('#noResultFoundDialog').dialog('close');
    }else{
    	$('#noResultFoundDialog').dialog('open');
    }
	//在searchForm提交后显示等待的dialog
	$('#searchForm').submit(function(){
		$('#waitingDialog').dialog('open');
	});
	//跳转到指标页面
	$('#indicator').click(function(){
		location.href='view/indicator.jsp';
	});
});
</script>
<link href="css/search.css" rel="stylesheet" type="text/css" />
<link href="css/jquery-ui.min.css" rel="stylesheet" type="text/css" />
</head>
<body>
<%@ include file="client.jsp" %>
<input type="hidden" id="noResultFound" value='<%=noResultFound%>' />
<div class="searchDivClass">
<form id="searchForm" name="searchForm" method="post" action="search">
<table cellspacing="10px">
<tr>
<td colspan="4">
<div>
<span><input type="checkbox" name="FMZL" value="11" checked="checked"><label for="FMZL">发明</label>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</span>
<span><input type="checkbox" name="SYXX" value="22" checked="checked"><label for="SYXX">实用新型</label>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</span>
<span><input type="checkbox" name="WGSJ" value="33" checked="checked"><label for="WGZL">外观设计</label>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</span>
</div>
</td>
</tr>
<tr><td></td></tr>
<tr><td></td></tr>
<tr><td width="15%"><label class="RowLabel">申请号</label></td>
<td width="35%"><input type="text" name="APPLY_NUM" value="" class="RowInput"/></td>
<td width="15%"><label class="RowLabel">申请日期</label></td>
<td width="35%"><input type="text" id="APPLY_DATE" name="APPLY_DATE" value="" class="RowInput"/></td></tr>
<tr><td><label class="RowLabel">专利名称</label></td>
<td><input type="text" name="PTT_NAME" value="" class="RowInput"/></td>
<td><label class="RowLabel">专利公开号</label></td>
<td><input type="text" name="PTT_NUM" value="" class="RowInput"/></td></tr>
<tr><td><label class="RowLabel">专利公开日</label></td>
<td><input type="text" id="PTT_DATE" name="PTT_DATE" value="" class="RowInput"/></td>
<td><label class="RowLabel">主分类号</label></td>
<td><input type="text" name="PTT_MAIN_CLASS_NUM" value="" class="RowInput"/></td></tr>
<tr><td><label class="RowLabel">分类号</label></td>
<td><input type="text" name="PTT_CLASS_NUM" value="" class="RowInput"/></td>
<td><label class="RowLabel">申请（专利权）人</label></td>
<td><input type="text" name="PROPOSER" value="" class="RowInput"/></td></tr>
<tr><td><label class="RowLabel">申请人地址</label></td>
<td><input type="text" name="PROPOSER_ADDRESS" value="" class="RowInput"/></td>
<td><label class="RowLabel">发明（设计）人</label></td>
<td><input type="text" name="INVENTOR" value="" class="RowInput"/></td></tr>
<tr><td><label class="RowLabel">国际申请</label></td>
<td><input type="text" name="INTERNATIONAL_APPLY" value="" class="RowInput"/></td>
<td><label class="RowLabel">国际公布</label></td>
<td><input type="text" name="INTERNATIONAL_PUBLICATION" value="" class="RowInput"/></td></tr>
<tr><td><label class="RowLabel">初始日</label></td>
<td><input type="text" id="INTO_DATE" name="INTO_DATE" value="" class="RowInput"/></td>
<td><label class="RowLabel">专利代理机构</label></td>
<td><input type="text" name="PTT_AGENCY_ORG" value="" class="RowInput"/></td></tr>
<tr><td><label class="RowLabel">专利代理人</label></td>
<td><input type="text" name="PTT_AGENCY_PERSON" value="" class="RowInput"/></td>
<td><label class="RowLabel">专利摘要</label></td>
<td><input type="text" name="PTT_ABSTRACT" value="" class="RowInput"/></td></tr>
<tr><td colspan="4">
<input type="submit" class="buttonClass" value="提交">&nbsp;&nbsp;&nbsp;&nbsp;<input type="reset" class="buttonClass" value="重置">&nbsp;&nbsp;&nbsp;&nbsp;<input type="button" id="indicator" class="buttonClass" value="查看指标"></td></tr>
</table>
</form>
</div>
<div id="waitingDialog">
正在查询中，请稍候...
</div>
<div id="noResultFoundDialog">
对不起，没有找到结果。
</div>
</body>
</html>