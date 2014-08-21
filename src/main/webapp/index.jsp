<%@ page contentType="text/html; charset=utf-8" %>
<%@ page pageEncoding="utf-8"%>
<%@ page language="java" import="java.sql.*,java.util.*,java.net.*"%>
<%@ include file="view/path&check.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>华南理工大学专利检索系统</title>
<base href="<%=basePath%>">
<script type="text/javascript" src="js/jquery-1.11.1.min.js"></script>
<script type="text/javascript" src="js/jquery-ui.min.js"></script>
<script type="text/javascript" src="js/jquery.validate.min.js"></script>
<script type="text/javascript" src="js/jquery.cookie.js"></script>
<script type="text/javascript" src="js/useful_function.js"></script>
<script type="text/javascript">
$(document).ready(function() {
	//让div居中
	mediate($('.divClass'));
	$('#login').show();
    $('#register').hide();
	$('#loginErrorLabel').hide();
	//设置载入后用cookie自动填充用户名和密码
	fillCookie();
	//用于登录和注册界面的切换
	$('#loginButton').mouseover(function(){
		$('#login').show();
    	$('#register').hide();	
	});
	$('#registerButton').mouseover(function(){
		$('#register').show();
    	$('#login').hide();	
	});
	//用于密码输入时input类型的转换
	$('#loginPassword,#registerPassword,#registerConfirmPassword').focus(function(){
		$(this).attr('type','password');
	}).blur(function(){
		if(this.value == ''){
			$(this).attr('type','text');
		}
	});
	//用于文本框内的提示字体被点击后自动消除和还原
	$('.tableCss').focus(function(){
		if(this.value == this.defaultValue){
			this.value = '';
			$(this).css('color','#000')
		}
	}).blur(function(){
		if(this.value == ''){
			this.value = this.defaultValue;
			$(this).css('color','#CCC')
		}
	});
	//用于登录文本框内选择账户类型时消除第一个选项和改变字体的颜色
	$('select:first').change(function(){
		if($('select:first > option:first').val()==''){
			$(this).css('color','#000')
			$('select:first > option:first').remove();
		}
	});
	//用于注册文本框内选择账户类型时消除第一个选项和改变字体的颜色
	$('select:last').change(function(){
		if($('select:last > option:first').val()==''){
			$(this).css('color','#000')
			$('select:last > option:first').remove();
		}
	});
	//只能包含英文字母或者数字的自定义validation验证方法  
	jQuery.validator.addMethod("onlyLetterNumber", function(value,element){
		return this.optional(element) || /^[a-zA-Z0-9]+$/.test(value);
},"只能包括英文字母和数字"); 
	//验证登录表单
	$('#loginForm').validate({
		rules:{
			loginEmail:{
				email:true
			},
			loginPassword:{
				rangelength:[8,16]
			},
			loginSelect:{
				required:true
			}
		},
		messages:{
			loginEmail:{
				email:"请输入合法的email地址"
			},
			loginPassword:{
				rangelength:"请输入8-16位密码"
			},
			loginSelect:{
				required:"请选择一个用户类型"
			}
		}
	});
	//验证注册表单
	$('#registerForm').validate({
		rules:{
			registerEmail:{
				email:true
			},
			registerSelect:{
				required:true
			},
			registerName:{
				onlyLetterNumber:true
			},
			registerPassord:{
				onlyLetterNumber:true,
				rangelength:[8,16]
			},
			registerConfirmPassword:{
				equalTo:'#registerPassword'
			}
		},
		messages:{
			registerEmail:{
				email:"请输入合法的email地址"
			},
			registerSelect:{
				required:"请选择一个用户类型"
			},
			registerName:{
				onlyLetterNumber:"请输入只含有数字和英文字母的用户名"
			},
			registerPassord:{
				onlyLetterNumber:"密码只能包含数字和英文字母",
				rangelength:"请输入8-16位密码"
			},
			registerConfirmPassword:{
				equalTo:"与上述填写的密码不匹配"
			}
		}
	});
	//设置existedEmailDialog和UnpassDialog的效果
	$('#existedEmailDialog, #UnpassDialog').dialog({
		autoOpen:false,
		closeOnEscape:false,
		draggable:false,
		modal:true,
		title:"警告",
		buttons:{
			"确定":function(){
				$(this).dialog('close');
			}
		}
	});
	//设置登录表单提交时的事件
	$('#loginForm').submit(function(evt){
		if($('#rememberUserName').prop('checked')){
			$.cookie('username',$('#loginEmail').val(),{path:'/',expires:1000});
		}else{
			$.cookie("username",null,{path:'/',expires:0});
		}
		if($('#rememberPassword').prop('checked')){
			$.cookie('password',$('#loginPassword').val(),{path:'/',expires:1000});
		}else{
			$.cookie('password',null,{path:'/',expires:0});
		}
		//检查是否已经通过审核
		$.ajax({
			type:'post',
			url:'checkPass',
			data:{Email:$('#loginEmail').val()},
			dataType:'json',
			async:false,
			success:function(data){
				if(data.result=='unpass'){
					$('#UnpassDialog').dialog('open');
					evt.preventDefault();
				}
			}
		});
	});
	//设置注册表单提交时的事件
	$('#registerForm').submit(function(evt){
		//检查是否开启了自动审核
		$.ajax({
			type:'post',
			url:'getAutopass',
			dataType:'json',
			async:false,
			success:function(data){
				if(data.result=='0'){
					$('#UnpassDialog').dialog('open');
				}
			}
		});
	});
});
function fillCookie(){
	var cookie_username = $.cookie('username');
	var cookie_password = $.cookie('password');
	if(typeof(cookie_username) != 'undefined'){
		$('#loginEmail').val(cookie_username);
		$('#loginEmail').css('color','#000');
		$('#rememberUserName').prop('checked',true);
	}
	if(typeof(cookie_password) != 'undefined'){
		$('#loginPassword').val(cookie_password);
		$('#loginPassword').css('color','#000');
		$('#loginPassword').attr('type','password');
		$('#rememberPassword').prop('checked',true);
	}
}
</script>
<link href="css/jquery-ui.min.css" rel="stylesheet" type="text/css" />
<link href="css/index.css" rel="stylesheet" type="text/css" />
</head>
<body>
<%@ include file="view/client.jsp" %>
<p>华南理工大学专利检索系统</p>
<div class="divClass">
<table cellpadding="0" align="center" >
<tr><td><input type="button" id="loginButton" class="buttonClass" value="登录" /></td><td><input type="button" id="registerButton" class="buttonClass" value="注册" /></td></tr>
<tr><td colspan="2">
<div id="login">
<form method="post" id="loginForm" action="login">
<table>
<tr><td colspan="2"><input type="text" id="loginEmail" name="loginEmail" class="tableCss" value="电子邮件" /></td></tr>
<tr><td colspan="2"><input type="text" id="loginPassword" name="loginPassword" class="tableCss" value="密码" /></td></tr>
<tr><td colspan="2">
<select name="loginSelect" class="tableCss">
<option value="" selected="selected">==账户类型==</option>
<option value="user">用户</option>
<option value="classifier">分类者</option>
<option value="administrator">管理员</option>
</select>
</td></tr>
<tr><td></td><td><label id="loginErrorLabel">用户名或者密码不正确</label></td></tr>
<tr>
<td><input type="checkbox" id="rememberUserName" /><label>记住用户名</label></td>
<td><input type="checkbox" id="rememberPassword" /><label>记住密码</label></td>
</tr>
<tr><td colspan="2"><input type="submit" class="submitClass" value="登录" /></td></tr>
</table>
</form>
</div>
<div id="register">
<form method="post" id="registerForm" action="register">
<table>
<tr><td>*为必填</td></tr>
<tr><td>*<input type="text" id="registerEmail" name="registerEmail" class="tableCss" value="电子邮箱" /></td></tr>
<tr><td>*<select name="registerSelect" class="tableCss">
<option value="" selected="selected">==账户类型==</option>
<option value="user">用户</option>
<option value="classifier">分类者</option>
<option value="administrator">管理员</option>
</select>
</td></tr>
<tr><td>*<input type="text" name="registerName" class="tableCss" value="用户名" /></td></tr>
<tr><td>&nbsp;&nbsp;<input type="text" name="registerDepartment" class="tableCss" value="单位" /></td></tr>
<tr><td>*<input type="text" name="registerPassord" id="registerPassword" class="tableCss" value="密码" /></td></tr>
<tr><td>*<input type="text" name="registerConfirmPassword" id="registerConfirmPassword" class="tableCss" value="再次输入密码" /></td></tr>
<tr><td colspan="2"><input type="submit" class="submitClass" value="注册" /></td></tr>
</table>
</form>
</div>
</td></tr>
</table>
</div>
<div id="existedEmailDialog">
对不起，该邮箱已被注册，请选择其他邮箱。
</div>
<div id="UnpassDialog">
您的账户有待审核，请耐心等候。
</div>
</body>
</html>
