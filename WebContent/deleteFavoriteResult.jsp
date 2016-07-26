<%@ page import="de.telekom.rethink.discovery.FormularHelper"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Delete Favorite</title>
</head>
<body>
<%
FormularHelper helper =new FormularHelper(request);
int userID = (int) session.getAttribute("userID");
String profile = (String) request.getParameter("profile");

//helper.deleteFavoriteRelation(instanceID, userID, profile);
helper.deleteFavoriteRelation(userID, profile);
response.sendRedirect("favorites.jsp");
%>
</body>
</html>