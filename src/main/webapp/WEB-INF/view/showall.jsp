<%@ page contentType="text/html; charset=utf-8" language="java" import="java.sql.*,java.util.*"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>专利汇总页</title>
</head>
<body>
<%
try{
// 加载驱动
Class.forName("com.mysql.jdbc.Driver").newInstance();
String url = "jdbc:mysql://localhost/patentdb";
String user = "root";
String password = "123";
Connection conn = DriverManager.getConnection(url, user, password);
//创建用于将SQL语句发送到数据库的SQLServerStatement对象。
Statement st = conn.createStatement();
//执行给定的SQL语句，该语句返回单个ResultSet对象。
String sqlString = "select * from patents";
ResultSet rs = st.executeQuery(sqlString);
out.println("<h1>专利列表</h1><hr><table><tr><td>专利编号</td><td>专利名称</td><td>专利申请号</td><td>专利申请</td></tr>");
while(rs.next()){
out.print("<tr>");
out.print("<td>" + rs.getString("PTT_NUM") + "</td><td>" + rs.getString("PTT_NAME") + "</td><td>" + rs.getString("APPLY_NUM") + "</td><td>" + rs.getString("PROPOSER") + "</td>");
out.print("</tr>");
}
out.print("</table><hr>");
conn.close();
}catch(Exception e){
out.print("无数据");
out.print(e.getMessage());
}
%>
</body>
</html>