<%@ page import="de.telekom.rethink.discovery.FormularHelper"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Your are logged in</title>
</head>
<body>
<%
FormularHelper helper =new FormularHelper(request);
String username = request.getParameter("username");
String password = request.getParameter("password");
int userID = 0;

	userID = helper.loginUser(username, password);
	
	if(userID==0)
		{
		response.sendRedirect("loginErrorPage.jsp");
		}
	else
	{
		session.setAttribute("userName",username);
		session.setAttribute("userID",userID);
		response.sendRedirect("searchPage_in.jsp");
	}	
%>
</body>
</html>