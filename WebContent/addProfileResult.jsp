<%@ page import="de.telekom.rethink.discovery.FormularHelper"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Add Profile</title>
</head>
<body>
<h1>Profile added</h1>
<%
FormularHelper helper =new FormularHelper(request);
//1.get request parameter
String headline = helper.filteredHeadline();
String description= helper.filteredDescription();
String hashtags=helper.filteredHashtags();
String contacts=helper.filteredContacts();
String rethinkID = helper.filteredRethinkID();
String visibility = helper.filteredVisibility();


int userID = (int) session.getAttribute("userID");
String username = (String) session.getAttribute("userName");


String hasrethinkID = "true";

//1. check input
if(!helper.validateInput(headline,description,username))
{
request.getRequestDispatcher("addProfileErrorPage.jsp").forward(request,response);
}
else
{
int docID =0;
//2.	create Profile in MariaDB (profile table)
docID = helper.createProfile(userID,headline,description,hashtags,contacts,rethinkID);	
	
//3. 	create ProfileNode in Neo4j
	helper.createProfileNode(docID,visibility,username,userID);

	if(helper.rethinkIDisempty(rethinkID))
				hasrethinkID ="false";

//4.	index Profile in Solr		
	helper.indexProfile(docID,headline,description,hashtags,contacts,hasrethinkID,rethinkID);
		
//5. goto overview	
	response.sendRedirect("profiles.jsp");
}
%>
</body>
</html>