<%@ page contentType="text/html; charset=UTF-8"%>
<%@ page pageEncoding="utf-8"%>
<%@ page language="java" import="java.util.*,java.io.*,java.sql.*,java.net.*"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://"
			+ request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
%>
<%
	String path_dir = new String("file/");
	String file_name = java.net.URLEncoder.encode(
			request.getParameter("FILE_NAME"), "ISO-8859-1");
	file_name = java.net.URLDecoder.decode(file_name, "UTF-8");
	String file_path = path_dir + file_name;
	String ptt_num = java.net.URLEncoder.encode(
			request.getParameter("PTT_NUM"), "ISO-8859-1");
	ptt_num = java.net.URLDecoder.decode(ptt_num, "UTF-8");
	List<String> checkboxList = new ArrayList<String>();
%>
<%
	try {
		// 加载驱动
		Class.forName("com.mysql.jdbc.Driver").newInstance();
		String url = "jdbc:mysql://localhost/patentdb";
		String user = "root";
		String password = "123";
		Connection conn = DriverManager.getConnection(url, user,
				password);
		//创建用于将SQL语句发送到数据库的SQLServerStatement对象。
		Statement st = conn.createStatement();
		//执行给定的SQL语句，该语句返回单个ResultSet对象。
		String sqlString = "select * from CLASSIFICATION where PTT_NUM = '" + ptt_num + "';";
		ResultSet rs = st.executeQuery(sqlString);
		while (rs.next()) {
			checkboxList.add(rs.getString("TRIZ_NUM"));
		}
		conn.close();
	} catch (Exception e) {
		out.print(e.getMessage());
	}
%>
<%--直接在整个页面显示PDF<%
   out.clear();
   out = pageContext.pushBody();
   response.setContentType("application/pdf");

   try {
	String path_dir = new String("E://dir//file//");
	String file_name = request.getParameter("FILE_NAME");
    String file_path = path_dir + file_name;
    //判断该路径下的文件是否存在
    File file = new File(file_path);
    if (file.exists()) {
     DataOutputStream temps = new DataOutputStream(response
       .getOutputStream());
     DataInputStream in = new DataInputStream(
       new FileInputStream(file_path));

     byte[] b = new byte[2048];
     while ((in.read(b)) != -1) {
      temps.write(b);
      temps.flush();
     }
     in.close();
     temps.close();
    } else {
     out.print(file_path + " 文件不存在!");
    }
   } catch (Exception e) {
    out.println(e.getMessage());
   }
%>--%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>PDF展示页</title>
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
	}
	//填充已选的checkbox
	function fillCheckbox() {
		var items = document.getElementsByTagName("input");
		for ( var i = 0; i < items.length; i++) {
			if (items[i].type == "checkbox" && items[i].name == "triz"
					&& items[i].value == "2") {
				items[i].checked = true;
			}
		}
	}
	function fillCheckbox2(checkboxList) {
		var items = document.getElementsByTagName("input");
		for(var j = 0; j < checkboxList.size(); j++){
			for ( var i = 0; i < items.length; i++) {
				if (items[i].type == "checkbox" && items[i].name == "triz"
						&& items[i].value == checkboxList.get(j)) {
					items[i].checked = true;
					return;
				}
			}
		}
	}
</script>
<link rel=stylesheet type="text/css" href="scut.css">
<base href="<%=basePath%>">
</head>
<body onload="fillCheckbox2(<%=checkboxList%>)">
	<table>
		<tr>
			<td width="60%" height="100%"><%=ptt_num%> <iframe id="win"
					name="win" onload="Javascript:dyniframesize('win')"
					src="<%=file_path%>" width="100%" height="100%"></iframe></td>
			<!--<object data="zhuanli_unlock.pdf" type="application/pdf">
<embed src="zhuanli_unlock.pdf" type="application/pdf" />
</object>-->
			</td>
			<td width="40%" height="100%">
				<div id="showdialog">
					40个TRIZ原理
					<form name="submitForm" method="post" action="updateTrizNumber">
						<input type="hidden" name="PTT_NUM" value="<%=ptt_num%>" />
						<table>
							<tr>
								<td>
									<table>
										<tr>
											<td><input type="checkbox" name="triz" value="1" />1.分割原则</td>
										</tr>
										<tr>
											<td><input type="checkbox" name="triz" value="2" />2.拆出原则</td>
										</tr>
										<tr>
											<td><input type="checkbox" name="triz" value="3" />3.局部性质原则</td>
										</tr>
										<tr>
											<td><input type="checkbox" name="triz" value="4" />4.不对称原则</td>
										</tr>
										<tr>
											<td><input type="checkbox" name="triz" value="5" />5.组合原则</td>
										</tr>
										<tr>
											<td><input type="checkbox" name="triz" value="6" />6.多功能原则</td>
										</tr>
										<tr>
											<td><input type="checkbox" name="triz" value="7" />7."玛特廖什卡"原则</td>
										</tr>
										<tr>
											<td><input type="checkbox" name="triz" value="8" />8.重量补偿原则</td>
										</tr>
										<tr>
											<td><input type="checkbox" name="triz" value="9" />9.预先反作用原则</td>
										</tr>
										<tr>
											<td><input type="checkbox" name="triz" value="10" />10.预先作用原则</td>
										</tr>
										<tr>
											<td><input type="checkbox" name="triz" value="11" />11."予先放枕头"原则</td>
										</tr>
										<tr>
											<td><input type="checkbox" name="triz" value="12" />12.等势原则</td>
										</tr>
										<tr>
											<td><input type="checkbox" name="triz" value="13" />13."相反"原则</td>
										</tr>
										<tr>
											<td><input type="checkbox" name="triz" value="14" />14.球形原则</td>
										</tr>
										<tr>
											<td><input type="checkbox" name="triz" value="15" />15.动态原则</td>
										</tr>
										<tr>
											<td><input type="checkbox" name="triz" value="16" />16.局部作用或过量作用原则</td>
										</tr>
										<tr>
											<td><input type="checkbox" name="triz" value="17" />17.向另一维度过渡的原则</td>
										</tr>
										<tr>
											<td><input type="checkbox" name="triz" value="18" />18.机械振动原则</td>
										</tr>
										<tr>
											<td><input type="checkbox" name="triz" value="19" />19.周期作用原则</td>
										</tr>
										<tr>
											<td><input type="checkbox" name="triz" value="20" />20.连续有益作用原则</td>
										</tr>
									</table>
								</td>
								<td>
									<table>
										<tr>
											<td><input type="checkbox" name="triz" value="21" />21.跃过原则</td>
										</tr>
										<tr>
											<td><input type="checkbox" name="triz" value="22" />22.变害为利原则</td>
										</tr>
										<tr>
											<td><input type="checkbox" name="triz" value="23" />23.反向联系原则</td>
										</tr>
										<tr>
											<td><input type="checkbox" name="triz" value="24" />24."中介"原则</td>
										</tr>
										<tr>
											<td><input type="checkbox" name="triz" value="25" />25.自我服务原则</td>
										</tr>
										<tr>
											<td><input type="checkbox" name="triz" value="26" />26.复制原则</td>
										</tr>
										<tr>
											<td><input type="checkbox" name="triz" value="27" />27.用廉价的不持久性代替昂贵的持久性原则</td>
										</tr>
										<tr>
											<td><input type="checkbox" name="triz" value="28" />28.代替力学原理原则</td>
										</tr>
										<tr>
											<td><input type="checkbox" name="triz" value="29" />29.利用气动和液：压结构的原则</td>
										</tr>
										<tr>
											<td><input type="checkbox" name="triz" value="30" />30.利用软壳和薄膜原则</td>
										</tr>
										<tr>
											<td><input type="checkbox" name="triz" value="31" />31.利用多孔材料原则</td>
										</tr>
										<tr>
											<td><input type="checkbox" name="triz" value="32" />32.改变颜色原则</td>
										</tr>
										<tr>
											<td><input type="checkbox" name="triz" value="33" />33.一致原则</td>
										</tr>
										<tr>
											<td><input type="checkbox" name="triz" value="34" />34.部分剔除和再生原则</td>
										</tr>
										<tr>
											<td><input type="checkbox" name="triz" value="35" />35.改变物体聚合态原则</td>
										</tr>
										<tr>
											<td><input type="checkbox" name="triz" value="36" />36.相变原则</td>
										</tr>
										<tr>
											<td><input type="checkbox" name="triz" value="37" />37.利用热膨胀原则</td>
										</tr>
										<tr>
											<td><input type="checkbox" name="triz" value="38" />38.利用强氧化剂原则</td>
										</tr>
										<tr>
											<td><input type="checkbox" name="triz" value="39" />39.采用惰性介质原则</td>
										</tr>
										<tr>
											<td><input type="checkbox" name="triz" value="40" />40.利用混合材料原则</td>
										</tr>
									</table>
								</td>
							</tr>
							<tr>
								<td colspan="2" align="center"><input type="submit"
									value="提交"></td>
							</tr>
							<tr>
								<td colspan="2" align="center"><input type="reset"
									value="重置"></td>
							</tr>
						</table>
					</form>
				</div>
			</td>
		</tr>
	</table>
</body>
</html>