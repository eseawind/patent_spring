<%@ page contentType="text/html; charset=utf-8" %>
<%@ page pageEncoding="utf-8"%>
<%
String permission = (String) session.getAttribute("Permission");
if(permission == null || !permission.equals("administrator")){
	response.setStatus(HttpServletResponse.SC_MOVED_PERMANENTLY);
	String newLocation = "/patent_spring/index.jsp";
	response.setHeader("Location",newLocation);
}
%>