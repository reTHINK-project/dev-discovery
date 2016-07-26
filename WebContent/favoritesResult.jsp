<%@ page import="de.telekom.rethink.discovery.FormularHelper"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Insert title here</title>
</head>
<body>
<% 
FormularHelper helper =new FormularHelper(request);

String profileNr = request.getParameter("docID");
String instanceID = request.getParameter("instanceID");
int userID = (int) session.getAttribute("userID");
String username = (String) session.getAttribute("userName");
//String instanceID =  request.getSession().getServletContext().getInitParameter("instanceID");

if(!helper.favoriteAlreadyExist(userID,profileNr))
{
	helper.createFavoriteRelation(userID, profileNr);
}
response.sendRedirect("favorites.jsp");

%>



</body>
</html>