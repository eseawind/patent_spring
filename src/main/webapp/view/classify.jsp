<%@ page contentType="text/html; charset=utf-8"%>
<%@ page pageEncoding="utf-8"%>
<%@ page language="java"
	import="java.sql.*,java.util.*,java.net.*,cn.edu.scut.patent.model.Patent" %>
<%@ include file="path&check.jsp" %>
<%@ include file="/permission/classifierPermission.jsp" %>
<%
	int count = 10;
	String jsonArray = (String) session.getAttribute("PATENTCLASSIFICATION");
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>分类页</title>
<base href="<%=basePath%>">
<script type="text/javascript" src="js/jquery-1.11.1.min.js"></script>
<script type="text/javascript" src="js/useful_function.js"></script>
<script type="text/javascript">
$(document).ready(function(){
	//让div居中
	mediate($('.resultDivClass'));
	//填充表格
	var patentclassification=eval('('+$('#jsonArray').val()+')');
	var count=$('#count').val();
	
	var totalItem=getTotalItem(patentclassification);
	var pageTotal=getPageTotal(totalItem,count);
	var pageNumber=getPageNumber();
	var startItem=getStartItem(pageNumber,count);
	var finalItem=getFinalItem(pageNumber,pageTotal,count,totalItem);
	fillResult(patentclassification,startItem,finalItem);
	fillFoot(totalItem,pageNumber,pageTotal);
	
	//页码跳转时刷新函数(必须要绑定函数以适应新产生的动态元素)
	$('#fillFoot').on('click','a',function(){
		pageNumber=parseInt($(this).attr('href'));
		startItem=getStartItem(pageNumber,count);
		finalItem=getFinalItem(pageNumber,pageTotal,count,totalItem);
		fillResult(patentclassification,startItem,finalItem);
		fillFoot(totalItem,pageNumber,pageTotal);
		return false;
	});
	$('#fillFoot').on('change','#selectPage',function(){
		pageNumber=parseInt($('#selectPage :selected').val());
		startItem=getStartItem(pageNumber,count);
		finalItem=getFinalItem(pageNumber,pageTotal,count,totalItem);
		fillResult(patentclassification,startItem,finalItem);
		fillFoot(totalItem,pageNumber,pageTotal);
		return false;
	});
});
//获取检索结果的总数
function getTotalItem(patentclassification){
	return patentclassification.length;
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
function fillResult(patentclassification,startItem,finalItem){
	var htmlString='';
	for(var i=startItem; i<finalItem; i++){
		htmlString += '<tr>';
		htmlString += '<td width="5%">' + (i + 1) + '</td>';
		htmlString += '<td width="24%">' + '<a href="/patent_spring/view/showFile.jsp?&APPLY_NUM=' + patentclassification[i].ApplyNum + '&PTT_NUM=' + patentclassification[i].PttNum + '&PTT_TYPE=' + patentclassification[i].PttType + '">' + patentclassification[i].PttName + '</a>' + '</td>';
		htmlString += '<td width="15%">' + patentclassification[i].PttNum + '</td>';
		htmlString += '<td width="15%">' + patentclassification[i].PttDate + '</td>';
		htmlString += '<td width="15%">' + patentclassification[i].ClassNumG06Q + '</td>';
		htmlString += '<td width="26%">'
		if(patentclassification[i].TrizNum == null){
			htmlString += "未分类";
		}else{
			//htmlString += patentclassification[i].TrizNum;
			htmlString += "已分类";
		}
		htmlString += '</td>';
		htmlString += '</tr>';
	}
	$('#fillTable').html(htmlString);
};
//填充页脚
function fillFoot(totalItem,pageNumber,pageTotal){
	var htmlString='';
	htmlString += '<td>一共有' + totalItem + '条结果</td><td>&nbsp;</td>';
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
	htmlString += '<td>&nbsp;</td>';
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
<div class="resultDivClass">
<div>
<table class='mainTableClass' border=0 cellspacing='10px' cellpadding='10px'>
<tr>
<th width='5%'>序号</th>
<th width='24%'>专利名称</th>
<th width='15%'>专利号</th>
<th width='15%'>发明日期</th>
<th width='15%'>分类号</th>
<th width='26%'>分类情况</th>
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