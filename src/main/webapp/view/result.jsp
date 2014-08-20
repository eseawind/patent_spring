<%@ page contentType="text/html; charset=utf-8"%>
<%@ page pageEncoding="utf-8"%>
<%@ page language="java"
	import="java.sql.*,java.util.*,java.net.*,cn.edu.scut.patent.model.Patent" %>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://"
	+ request.getServerName() + ":" + request.getServerPort()
	+ path + "/";
%>
<%
	int count = 10;
	String jsonArray = (String) session.getAttribute("PATENTLIST");
	String timeConsume = (String) session.getAttribute("TIMECONSUME");
	String pageString = (String)request.getParameter("PAGE");
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>检索结果</title>
<base href="<%=basePath%>">
<script type="text/javascript" src="js/jquery-1.11.1.min.js"></script>
<script type="text/javascript" src="js/useful_function.js"></script>
<script type="text/javascript">
$(document).ready(function(){
	//让div居中
	mediate($('.resultDivClass'));
	
	var patentsString=eval('('+$('#jsonArray').val()+')');
	var timeConsume=$('#timeConsume').val();
	var count=$('#count').val();
	
	var totalItem=getTotalItem(patentsString);
	var pageTotal=getPageTotal(totalItem,count);
	var pageNumber=getPageNumber();
	var startItem=getStartItem(pageNumber,count);
	var finalItem=getFinalItem(pageNumber,pageTotal,count,totalItem);
	fillResult(patentsString,startItem,finalItem);
	fillFoot(totalItem,pageNumber,pageTotal,timeConsume);
	
	//页码跳转时刷新函数(必须要绑定函数以适应新产生的动态元素)
	$('#fillFoot').on('click','a',function(){
		pageNumber=parseInt($(this).attr('href'));
		startItem=getStartItem(pageNumber,count);
		finalItem=getFinalItem(pageNumber,pageTotal,count,totalItem);
		fillResult(patentsString,startItem,finalItem);
		fillFoot(totalItem,pageNumber,pageTotal,timeConsume);
		return false;
	});
	$('#fillFoot').on('change','#selectPage',function(){
		pageNumber=parseInt($('#selectPage :selected').val());
		startItem=getStartItem(pageNumber,count);
		finalItem=getFinalItem(pageNumber,pageTotal,count,totalItem);
		fillResult(patentsString,startItem,finalItem);
		fillFoot(totalItem,pageNumber,pageTotal,timeConsume);
		return false;
	});
});
//获取检索结果的总数
function getTotalItem(patentsString){
	return patentsString.length;
};
//获取总页数
function getPageTotal(totalItem,count){
	//向上取整
	return Math.ceil(totalItem/count);
};
//获取当前页码
function getPageNumber(){
	return 1;
};
//获取开始项的号码
function getStartItem(pageNumber,count){
	return (pageNumber-1)*count;
};
//获取最后一项的号码
function getFinalItem(pageNumber,pageTotal,count,totalItem){
	if(pageNumber<pageTotal){
		return pageNumber*count;
	}else{
		return totalItem;
	}
};
//填充数据表格
function fillResult(patentsString,startItem,finalItem){
	var htmlString='';
	for(var i=startItem; i<finalItem; i++){
		htmlString += '<tr>';
		htmlString += '<td width="5%">' + (i + 1) + '</td>';
		htmlString += '<td width="24%">' + '<a href="/patent_spring/view/showFile.jsp?&APPLY_NUM=' + patentsString[i].ApplyNum + '&PTT_NUM=' + patentsString[i].PttNum + '&PTT_TYPE=' + patentsString[i].PttType + '">' + patentsString[i].PttName + '</a>' + '</td>';
		htmlString += '<td width="15%">' + patentsString[i].PttNum + '</td>';
		htmlString += '<td width="15%">' + patentsString[i].Inventor + '</td>';
		htmlString += '<td width="15%">' + patentsString[i].Proposer + '</td>';
		htmlString += '<td width="26%">' + patentsString[i].PttDate + '</td>';
		htmlString += '</tr>';
	}
	$('#fillTable').html(htmlString);
};
//填充页脚
function fillFoot(totalItem,pageNumber,pageTotal,timeConsume){
	var htmlString='';
	htmlString += '<td>一共搜索到' + totalItem + '条结果</td><td>&nbsp;</td>';
	if (pageNumber > 1) {
		htmlString += '<td><a href="' + (pageNumber - 1) + '">上一页</a></td><td>&nbsp;</td>';
	}
	if (pageNumber - 2 > 0) {
		htmlString += '<td><a href="' + (pageNumber - 2) + '">' + (pageNumber - 2) + '</a></td><td>&nbsp;</td>';
	}
	if (pageNumber - 1 > 0) {
		htmlString += '<td><a href="' + (pageNumber - 1) + '">' + (pageNumber - 1) + '</a></td><td>&nbsp;</td>';
	}
	htmlString += '<td>' + pageNumber + '</td><td>&nbsp;</td>';
	if (pageNumber + 1 <= pageTotal) {
		htmlString += '<td><a href="' + (pageNumber + 1) + '">' + (pageNumber + 1) + '</a></td><td>&nbsp;</td>';
	}
	if (pageNumber + 2 <= pageTotal) {
		htmlString += '<td><a href="' + (pageNumber + 2) + '">' + (pageNumber + 2) + '</a></td><td>&nbsp;</td>';
	}
	if (pageNumber < pageTotal) {
		htmlString += '<td><a href="' + (pageNumber + 1) + '">下一页</a></td><td>&nbsp;</td>';
	}
	htmlString += '<td>共' + pageTotal + '页</td><td>&nbsp;</td>';
	htmlString += '<td>转到第';
	htmlString += '<select id="selectPage">';
	for (var i = 1; i <= pageTotal; i++) {
		htmlString += '<option value="' + i + '"';
		if (i == pageNumber) {
			htmlString += 'selected';
		}
		htmlString += '>' + i + '</option>';
	}
	htmlString += '</select>';
	htmlString += '页</td>';
	htmlString += '<td>&nbsp;</td><td>耗时' + timeConsume + '</td>';
	$('#fillFoot').html(htmlString);
};
</script>
<link href="css/result.css" rel="stylesheet" type="text/css" />
</head>
<body>
<%@ include file="client.jsp" %>
<input type="hidden" id="jsonArray" value='<%=jsonArray%>' />
<input type="hidden" id="basePath" value='<%=basePath%>' />
<input type="hidden" id="count" value='<%=count%>' />
<input type="hidden" id="timeConsume" value='<%=timeConsume%>' />
<input type="hidden" id="page" value='<%=pageString%>' />
<div class="resultDivClass">
<div>
<table class='mainTableClass' border=0 cellspacing='10px' cellpadding='10px'>
<tr>
<th width='5%'>序号</th>
<th width='24%'>专利名称</th>
<th width='15%'>公开号</th>
<th width='15%'>发明人</th>
<th width='15%'>申请人</th>
<th width='26%'>专利公开日</th>
</tr>
<tr><td colspan='6'>
<table id="fillTable" class='mainTableClass' border=0 cellspacing='10px' cellpadding='10px'>
</table>
</td></tr>
</table>
</div>
<div align='right'>
<table>
<tr>
<div id="fillFoot">
</div>
</tr>
</table>
</div>
</div>
</body>
</html>