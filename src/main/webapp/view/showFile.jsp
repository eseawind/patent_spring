<%@ page contentType="text/html; charset=utf-8"%>
<%@ page pageEncoding="utf-8"%>
<%@ page language="java"
	import="java.util.*,java.io.*,java.sql.*,java.net.*"%>
<%@ include file="path&check.jsp" %>
<%
	String path_dir = "http://so.baiten.cn/detail/patentdetail?";
	String APPLY_NUM = java.net.URLEncoder.encode(
	request.getParameter("APPLY_NUM"), "ISO-8859-1");
	APPLY_NUM = java.net.URLDecoder.decode(APPLY_NUM, "UTF-8");
	String PTT_NUM = java.net.URLEncoder.encode(
			request.getParameter("PTT_NUM"), "ISO-8859-1");
	PTT_NUM = java.net.URLDecoder.decode(PTT_NUM, "UTF-8");
	String PTT_TYPE = java.net.URLEncoder.encode(
			request.getParameter("PTT_TYPE"), "ISO-8859-1");
	PTT_TYPE = java.net.URLDecoder.decode(PTT_TYPE, "UTF-8");
	String file_path = path_dir + "type=";
	if(PTT_TYPE.equals("11")){
		file_path += "1";
	}else if(PTT_TYPE.equals("22")){
		file_path += "2";
	}else if(PTT_TYPE.equals("33")){
		file_path += "3";
	}else{
		file_path += "63";
	}
	file_path += "&id=" + "CN" + APPLY_NUM;
	List<String> checkboxList = new ArrayList<String>();
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>专利展示</title>
<base href="<%=basePath%>">
<script type="text/javascript" src="js/jquery-1.11.1.min.js"></script>
<script type="text/javascript" src="js/jquery.fancybox.js"></script>
<script type="text/javascript" src="js/useful_function.js"></script>
<script type="text/javascript" language="javascript">
function dyniframesize(down) {
	var pTar = null;
	if (document.getElementById) {
		pTar = document.getElementById(down);
	} else {
		eval('pTar = ' + down + ';');
	}
	if (pTar && !window.opera) {
		//begin resizing iframe 
		pTar.style.display = "block"
		if (pTar.contentDocument && pTar.contentDocument.body.offsetHeight) {
			//ns6 syntax 
			pTar.height = pTar.contentDocument.body.offsetHeight + 20;
			pTar.width = pTar.contentDocument.body.scrollWidth + 20;
		} else if (pTar.Document && pTar.Document.body.scrollHeight) {
			//ie5+ syntax 
			pTar.height = pTar.Document.body.scrollHeight;
			pTar.width = pTar.Document.body.scrollWidth;
		}
	}
};
$(document).ready(function(){
	//居中
	mediate($('#showDiv'));
	//最大化宽度
	maxWidth($('.showFileTableClass'));
	//获取TRIZ原理
	$.getJSON('getTriz',function(data){
		var htmlString = '';
		var triz = data;
		//获取分类数据
		$.getJSON('getClassification','pttNum='+$('#pttNum').val(),function(data){
			var classification = data;
			htmlString += '<tr><td><table>';
			for(var i = 0; i < triz.length; i++){
				var checkString = '';
				for(var j = 0; j < classification.length; j++){
					if(classification[j].trizNum==i+1){
						checkString = "checked='checked'";
					}
				}
				htmlString += "<tr><td><input type='checkbox' name='triz' value='" + (i+1) + "' " + checkString + " />" + (i+1) + "." + triz[i].Text + "</td></tr>";
				if(i==19){
					htmlString += "</table></td><td><table>";
				}
			}
			htmlString += "</table></td></tr><tr><td colspan='2' align='center' class='sub_res'><input type='submit' value='提交'><input type='reset' value='重置'></td></tr>";
			$('#mainTable').html(htmlString);
		});
		//用于展开iframe
		$('.iframe').fancybox({
			width : '90%',
			height : '90%',
			titlePosition: 'outside'
		});
	});
	$('#submitForm').submit(function(){
		$.getJSON('updateTrizNumber',$(this).serialize(),function(data){
			alert(data.result);
		});
		return false;
	});
});
</script>
<link href="css/jquery.fancybox.css" rel="stylesheet" type="text/css" />
<link href="css/showFile.css" rel="stylesheet" type="text/css" />
</head>
<body>
<%@ include file="client.jsp" %>
<div id='showDiv'>
<table class="showFileTableClass" border=0 cellspacing="10px" cellpadding="10px">
<tr>
<td width="60%" height="100%" class="pdf"><%=PTT_NUM%>&nbsp;&nbsp;&nbsp;&nbsp;<a href="<%=file_path%>" class="iframe">查看原网页</a><iframe id="win" name="win" onload="Javascript:dyniframesize('win')" src="<%=file_path%>" width="100%" height="100%"></iframe></td>
<td width="40%" height="100%">
<div id="showdialog">
40个TRIZ原理
<form id="submitForm" name="submitForm" method="post" action="updateTrizNumber">
<input type="hidden" id="pttNum" name="PTT_NUM" value="<%=PTT_NUM%>" />
<table id="mainTable">
</table>
</form>
</div>
</td>
</tr>
</table>
</div>
</body>
</html>