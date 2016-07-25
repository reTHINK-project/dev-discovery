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
//0.	get session attributes
String userName = (String) session.getAttribute("userName");
int userID = (Integer) session.getAttribute("userID"); 

//1. get all request attributes
 
String headline = helper.filteredHeadline();
String description= helper.filteredDescription();
String hashtags=helper.filteredHashtags();
String contacts=helper.filteredContacts();
String rethinkID = helper.filteredRethinkID();
String visibility = helper.filteredVisibility();

String docID = request.getParameter("docID");

//1. 	check input
if(!helper.validateInput(headline,description,userName))
{
request.getRequestDispatcher("changeProfileErrorPage.jsp").forward(request,response);
}
else
{

String hasrethinkID = "true";

if(rethinkID.equals(""))
	 hasrethinkID="false";

//2.	update MariaDB
helper.deleteProfile(new Integer(docID));
//simply create is not possible it would create a new docID so we need to use update
helper.updateProfile(new Integer(docID),new Integer(userID), headline, description, hashtags, contacts,rethinkID);
 
//3.	update Solr
helper.deleteProfileFromIndex(new Integer(docID));
helper.indexProfile(new Integer(docID), headline, description, hashtags, contacts, hasrethinkID, rethinkID );

//4.	update visibility
helper.updateProfileNode(new Integer(docID), visibility);

response.sendRedirect("profiles.jsp");
}
%>
</body>
</html>