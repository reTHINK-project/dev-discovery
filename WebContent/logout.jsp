<%@ page import="de.telekom.rethink.discovery.FormularHelper"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>logout</title>
</head>
<body>
<% 
try{
int userID = (int) session.getAttribute("userID");

FormularHelper helper =new FormularHelper(request);
if(session!=null)
	{	
	session.setAttribute("userID",null);
	session.setAttribute("userName",null);
	}
helper.logout(userID);
}catch(Exception ex){}

response.sendRedirect("searchPage.jsp");
%>
</body>
</html>