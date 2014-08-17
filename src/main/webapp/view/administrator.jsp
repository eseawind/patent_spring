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
	//当点击自动审核按钮时自动变换文字并弹出确认提示框
	$('#checkAuto').change(function(){
		if($('#labelCheckAuto').text()=='自动审核关闭中'){
			if(confirm('你确定要启用自动审核？')){
				$('#labelCheckAuto').text('自动审核启用中');
				$(this).prop('checked', true);
			}else{
				$(this).prop('checked', false);
			}
		}else{
			if(confirm('你确定要关闭自动审核？')){
				$('#labelCheckAuto').text('自动审核关闭中');
				$(this).prop('checked', false);
			}else{
				$(this).prop('checked', true);
			}
		}
	});
	//当点击按钮时弹出确认提示框并在确认后修改栏目显示
	$('.buttonClass').click(function(){
		if($(this).val()=='通过'){
			if(confirm('你确定通过此用户的申请？')){
				$(this).parent().html('<label>已通过</label>');
				//do something here
			}
		}else{
			if(confirm('你确定否决此用户的申请？')){
				$(this).parent().html('<label>已否决</label>')
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
</script>
<link href="css/administrator.css" rel="stylesheet" type="text/css" />
<link href="css/jquery-ui.min.css" rel="stylesheet" type="text/css" />
</head>
<body>
<div id="welcomeDiv">
<label>欢迎来到华南理工大学专利检索系统</label>
<input type="button" value="退出" />
</div>
<div class="administratorDivClass">
<div align="right">
<input type="checkbox" id="checkAuto" /><label id="labelCheckAuto">自动审核关闭中</label>
</div>
<div align="center">
<table class="mainTableClass" cellspacing="0" border="1">
<tr>
<th width="20%">用户类型</th><th width="20%">邮箱</th><th width="20%">用户名</th><th width="20%">单位</th><th width="20%">审核</th>
</tr>
<tr>
<td>用户</td><td>1353015987@qq.com</td><td>Vincent_Qiu</td><td>华南理工大学</td><td><div><input type="button" class="buttonClass" value="通过" /><input type="button" class="buttonClass" value="否决" /></div></td>
</tr>
<tr>
<td>用户</td><td>1353015987@qq.com</td><td>Vincent_Qiu</td><td>华南理工大学</td><td><input type="button" class="buttonClass" value="通过" /><input type="button" class="buttonClass" value="否决" /></td>
</tr>
<tr>
<td>用户</td><td>1353015987@qq.com</td><td>Vincent_Qiu</td><td>华南理工大学</td><td><input type="button" class="buttonClass" value="通过" /><input type="button" class="buttonClass" value="否决" /></td>
</tr>
<tr>
<td>用户</td><td>1353015987@qq.com</td><td>Vincent_Qiu</td><td>华南理工大学</td><td><input type="button" class="buttonClass" value="通过" /><input type="button" class="buttonClass" value="否决" /></td>
</tr>
<tr>
<td>用户</td><td>1353015987@qq.com</td><td>Vincent_Qiu</td><td>华南理工大学</td><td><input type="button" class="buttonClass" value="通过" /><input type="button" class="buttonClass" value="否决" /></td>
</tr>
<tr>
<td>用户</td><td>1353015987@qq.com</td><td>Vincent_Qiu</td><td>华南理工大学</td><td><input type="button" class="buttonClass" value="通过" /><input type="button" class="buttonClass" value="否决" /></td>
</tr>
<tr>
<td>用户</td><td>1353015987@qq.com</td><td>Vincent_Qiu</td><td>华南理工大学</td><td><input type="button" class="buttonClass" value="通过" /><input type="button" class="buttonClass" value="否决" /></td>
</tr>
<tr>
<td>用户</td><td>1353015987@qq.com</td><td>Vincent_Qiu</td><td>华南理工大学</td><td><input type="button" class="buttonClass" value="通过" /><input type="button" class="buttonClass" value="否决" /></td>
</tr>
<tr>
<td>用户</td><td>1353015987@qq.com</td><td>Vincent_Qiu</td><td>华南理工大学</td><td><input type="button" class="buttonClass" value="通过" /><input type="button" class="buttonClass" value="否决" /></td>
</tr>
<tr>
<td>用户</td><td>1353015987@qq.com</td><td>Vincent_Qiu</td><td>华南理工大学</td><td><input type="button" class="buttonClass" value="通过" /><input type="button" class="buttonClass" value="否决" /></td>
</tr>
</table>
</div>
<div align="right">
<table>
<tr>
<td><label>一共</label>
<input type="text" class="inputTextClass" disabled="disabled" value="20" /><label>条记录</label></td>
<td><label>第</label>
<select>
<option value="1">1</option>
<option value="2">2</option>
</select>
<label>页</label></td>
<td><label>共</label>
<input type="text" class="inputTextClass" disabled="disabled" value="2" />
<label>页</label></td>
</tr>
</table>
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
