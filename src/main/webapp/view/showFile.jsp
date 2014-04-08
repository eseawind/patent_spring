<%@ page contentType="text/html; charset=UTF-8"%>
<%@ page pageEncoding="utf-8"%>
<%@ page language="java"
	import="java.util.*,java.io.*,java.sql.*,java.net.*"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://"
	+ request.getServerName() + ":" + request.getServerPort()
	+ path + "/";
%>
<%
	String path_dir = "http://so.baiten.cn/detail/patentdetail/";
	String APPLY_NUM = java.net.URLEncoder.encode(
	request.getParameter("APPLY_NUM"), "ISO-8859-1");
	APPLY_NUM = java.net.URLDecoder.decode(APPLY_NUM, "UTF-8");
	String PTT_NUM = java.net.URLEncoder.encode(
			request.getParameter("PTT_NUM"), "ISO-8859-1");
	PTT_NUM = java.net.URLDecoder.decode(PTT_NUM, "UTF-8");
	String PTT_TYPE = java.net.URLEncoder.encode(
			request.getParameter("PTT_TYPE"), "ISO-8859-1");
	PTT_TYPE = java.net.URLDecoder.decode(PTT_TYPE, "UTF-8");
	String file_path = path_dir;
	if(PTT_TYPE.equals("11")){
		file_path += "1";
	}else if(PTT_TYPE.equals("22")){
		file_path += "2";
	}else{
		file_path += "3";
	}
	file_path += "/" + "CN" + APPLY_NUM;
	List<String> checkboxList = new ArrayList<String>();
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
<title>专利展示</title>
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
</script>
<base href="<%=basePath%>">
<link rel="stylesheet" href="css/all.css" type="text/css">
</head>
<body>
<div class="image">
<img src="img/school_badge.png"/>
</div>
<hr width=100%/>
	<table border=0 cellspacing="10px" cellpadding="10px">
		<tr>
			<td width="60%" height="100%" class="pdf"><%=PTT_NUM%><iframe id="win"
					name="win" onload="Javascript:dyniframesize('win')"
					src="<%=file_path%>" width="100%" height="100%"></iframe></td>
			</td>
			<td width="40%" height="100%">
				<div id="showdialog">
					40个TRIZ原理
					<form name="submitForm" method="post" action="updateTrizNumber">
						<input type="hidden" name="PTT_NUM" value="<%=PTT_NUM%>" />
						<%
							try {
								// 加载驱动
								Class.forName("com.mysql.jdbc.Driver").newInstance();
								String url = "jdbc:mysql://localhost/patentdb";
								String user = "root";
								String password = "123";
								String htmlString = "";
								Connection conn = DriverManager.getConnection(url, user,
										password);
								//创建用于将SQL语句发送到数据库的SQLServerStatement对象。
								Statement st = conn.createStatement();
								//执行给定的SQL语句，该语句返回单个ResultSet对象。
								String sqlCLASSIFICATION = "select * from CLASSIFICATION where PTT_NUM = '"
										+ PTT_NUM + "';";
								String sqlTRIZ = "select * from TRIZ;";
								ResultSet rsCLASSIFICATION = st.executeQuery(sqlCLASSIFICATION);
								List<String> classificationList = new ArrayList<String>();
								while (rsCLASSIFICATION.next()) {
									classificationList.add(rsCLASSIFICATION
											.getString("TRIZ_NUM"));
								}
								ResultSet rsTRIZ = st.executeQuery(sqlTRIZ);
								htmlString += "<table><tr><td><table>";
								while (rsTRIZ.next()) {
									String value = rsTRIZ.getString("TRIZ_NUM");
									String checkString = "";
									for (int i = 0; i < classificationList.size(); i++) {
										if (classificationList.get(i).equals(value)) {
											checkString = "checked='checked'";
											break;
										}
									}
									htmlString += "<tr><td><input type='checkbox' name='triz' value='"
											+ value
											+ "' "
											+ checkString
											+ " />"
											+ value
											+ "."
											+ rsTRIZ.getString("TRIZ_TEXT") + "</td></tr>";
									if (value.equals("20")) {
										htmlString += "</table></td><td><table>";
									}
								}
								htmlString += "</table></td></tr><tr><td colspan='2' align='center' class='sub_res'><input type='submit' value='提交'><input type='reset' value='重置'></td></tr></table>";
								conn.close();
								out.print(htmlString);
							} catch (Exception e) {
								out.print(e.getMessage());
							}
						%>
					</form>
				</div>
			</td>
		</tr>
	</table>
</body>
</html>