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


int userID = (Integer) session.getAttribute("userID"); 
boolean lastProfile = false;

//0. check whether its the last profile
if(helper.getProfileCount(userID)==1)
{
		lastProfile = true;
}
//1.get request parameter
String docID = request.getParameter("docID").trim();

//2.	delete at Solr
helper.deleteProfileFromIndex(new Integer(docID));

//3.	delete from MariaDB
helper.deleteProfile(new Integer(docID));

//4.	delete visibility
helper.removeAllFollower(docID);
helper.deleteProfileNode(new Integer(docID));



if(lastProfile)
	{
	//delete realtionships or favorites in neo4j
	helper.removeAllFavorites(userID);
	helper.deleteUserNode(userID);
	
	//delete user in SQL-DB
	helper.deleteUser(userID);
	response.sendRedirect("logout.jsp");
	}
else
{
	response.sendRedirect("profiles.jsp");
}	
%>

</body>
</html>