<%@ page contentType="text/html; charset=utf-8" %>
<%@ page pageEncoding="utf-8"%>
<%@ page language="java" import="java.sql.*,java.util.*,java.net.*"%>
<%@ include file="path&check.jsp" %>
<%@ include file="/permission/administratorPermission.jsp" %>
<%
	int count = 10;
	String jsonArray = (String) session.getAttribute("UNCHECKACCOUNT");
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>管理员工作页</title>
<base href="<%=basePath%>">
<script type="text/javascript" src="js/jquery-1.11.1.min.js"></script>
<script type="text/javascript" src="js/jquery-ui.min.js"></script>
<script type="text/javascript" src="js/useful_function.js"></script>
<script type="text/javascript">
$(document).ready(function() {
	//让div居中
	mediate($('.administratorDivClass'));
	//让welcomeDiv达到屏幕最大宽度
	maxWidth($('#welcomeDiv'));
	//提取自动审核功能的参数
	$.getJSON('getAutopass',function(data){
		if(data.result=='0'){
			$('#labelCheckAuto').text('自动审核关闭中');
			$('#checkAuto').prop('checked', false);
		}else{
			$('#labelCheckAuto').text('自动审核启用中');
			$('#checkAuto').prop('checked', true);
		}
	});
	//当点击自动审核按钮时自动变换文字并弹出确认提示框
	$('#checkAuto').change(function(){
		if($('#labelCheckAuto').text()=='自动审核关闭中'){
			if(confirm('你确定要启用自动审核？')){
				$.getJSON('updateAutopass','updateFlag=1');
				$('#labelCheckAuto').text('自动审核启用中');
				$(this).prop('checked', true);
			}else{
				$(this).prop('checked', false);
			}
		}else{
			if(confirm('你确定要关闭自动审核？')){
				$.getJSON('updateAutopass','updateFlag=0');
				$('#labelCheckAuto').text('自动审核关闭中');
				$(this).prop('checked', false);
			}else{
				$(this).prop('checked', true);
			}
		}
	});
	//填充表格
	var accountString=eval('('+$('#jsonArray').val()+')');
	var count=$('#count').val();
	
	var totalItem=getTotalItem(accountString);
	var pageTotal=getPageTotal(totalItem,count);
	var pageNumber=getPageNumber();
	var startItem=getStartItem(pageNumber,count);
	var finalItem=getFinalItem(pageNumber,pageTotal,count,totalItem);
	fillResult(accountString,startItem,finalItem);
	fillFoot(totalItem,pageNumber,pageTotal);
	
	//页码跳转时刷新函数(必须要绑定函数以适应新产生的动态元素)
	$('#fillFoot').on('change','#selectPage',function(){
		pageNumber=parseInt($('#selectPage :selected').val());
		startItem=getStartItem(pageNumber,count);
		finalItem=getFinalItem(pageNumber,pageTotal,count,totalItem);
		fillResult(accountString,startItem,finalItem);
		fillFoot(totalItem,pageNumber,pageTotal);
		return false;
	});
	//当点击按钮时弹出确认提示框并在确认后修改栏目显示
	$('.mainTableClass').on('click','.buttonClass',function(){
		if($(this).val()=='通过'){
			if(confirm('你确定通过此用户的申请？')){
				$.getJSON('updateAccount','updateEmail='+$(this).parent().attr('id')+'&updatePass=1');
				accountString[$(this).attr('alt')].Pass=1;
				fillResult(accountString,startItem,finalItem);
			}
		}else{
			if(confirm('你确定否决此用户的申请？')){
				$.getJSON('updateAccount','updateEmail='+$(this).parent().attr('id')+'&updatePass=2');
				accountString[$(this).attr('alt')].Pass=2;
				fillResult(accountString,startItem,finalItem);
			}
		}
	});
	//设置indexWaitingDialog和clusterWaitingDialog的效果
	$('#indexWaitingDialog, #clusterWaitingDialog').dialog({
		autoOpen:false,
		closeOnEscape:false,
		draggable:false,
		modal:true,
		title:"请稍候"
	});
	//当点击建立索引时弹出确认窗口
	$('#index').click(function(){
		if(confirm('你确定要建立索引吗？')){
			$('#indexWaitingDialog').dialog('open');
			$.getJSON('index',function(data){
				$('#indexWaitingDialog').dialog('close');
				alert(data.result);
			});
		}
	});
	//当点击进行聚类时弹出确认窗口
	$('#cluster').click(function(){
		if(confirm('你确定要进行聚类吗？')){
			$('#clusterWaitingDialog').dialog('open');
			$.getJSON('cluster',function(data){
				$('#clusterWaitingDialog').dialog('close');
				alert(data.result);
			});
		}
	});
});
//获取检索结果的总数
function getTotalItem(accountString){
	return accountString.length;
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
function fillResult(accountString,startItem,finalItem){
	var htmlString='<tr><th width="20%">用户类型</th><th width="20%">邮箱</th><th width="20%">用户名</th><th width="20%">单位</th><th width="20%">审核</th></tr>';
	for(var i=startItem; i<finalItem; i++){
		htmlString += '<tr>';
		htmlString += '<td>' + accountString[i].AccountType + '</td>';
		htmlString += '<td>' + accountString[i].Email + '</td>';
		htmlString += '<td>' + accountString[i].Username + '</td>';
		htmlString += '<td>' + accountString[i].Department + '</td>';
		if(accountString[i].Pass=='1'){
			htmlString += '<td><div id="' + accountString[i].Email +'"><label>已通过</label></div></td>';
		}else if(accountString[i].Pass=='2'){
			htmlString += '<td><div id="' + accountString[i].Email +'"><label>已否决</label></div></td>';
		}else{
			htmlString += '<td><div id="' + accountString[i].Email + '"><input type="button" class="buttonClass" alt="' + i + '" value="通过" /><input type="button" class="buttonClass" alt="' + i + '" value="否决" /></div></td>';
		}
		htmlString += '</tr>';
	}
	$('.mainTableClass').html(htmlString);
};
//填充页脚
function fillFoot(totalItem,pageNumber,pageTotal){
	var htmlString='<table><tr>';
	htmlString += '<td><label>一共</label><input type="text" class="inputTextClass" disabled="disabled" value="' + totalItem + '" /><label>条记录</label></td>';
	htmlString += '<td><label>第</label>';
	htmlString += '<select id="selectPage">';
	for (var i = 1; i <= pageTotal; i++) {
		htmlString += '<option value="' + i + '"';
		if (i == pageNumber) {
			htmlString += 'selected';
		}
		htmlString += '>' + i + '</option>';
	}
	htmlString += '</select>';
	htmlString += '<label>页</label></td>';
	htmlString += '<td><label>共</label><input type="text" class="inputTextClass" disabled="disabled" value="' + pageTotal + '" /><label>页</label></td>';
	htmlString += '</tr></table>';
	$('#fillFoot').html(htmlString);
};
</script>
<link href="css/administrator.css" rel="stylesheet" type="text/css" />
<link href="css/jquery-ui.min.css" rel="stylesheet" type="text/css" />
</head>
<body>
<%@ include file="client.jsp" %>
<input type="hidden" id="jsonArray" value='<%=jsonArray%>' />
<input type="hidden" id="count" value='<%=count%>' />
<div class="administratorDivClass">
<div align="right">
<input type="checkbox" id="checkAuto" /><label id="labelCheckAuto">自动审核关闭中</label>
</div>
<div align="center">
<table class="mainTableClass" cellspacing="0" border="1">
</table>
</div>
<div id="fillFoot" align="right">
</div>
<div align="left">
<input type="button" id="index" value="建立索引" />
<input type="button" id="cluster" value="进行聚类" />
</div>
</div>
<div id="indexWaitingDialog">
正在建立索引，整个过程耗时大约5分钟，请稍候...
</div>
<div id="clusterWaitingDialog">
正在进行聚类，整个过程耗时大约1小时，请稍候...
</div>
</body>
</html>
