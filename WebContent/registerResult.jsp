<%@page import="de.telekom.rethink.discovery.FormularHelper"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
    
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Register Profile</title>
</head>
<body>
<h1>Your Profile</h1>
<%
FormularHelper helper =new FormularHelper(request);
//get all request parameter from register.jsp
String username = helper.filteredUsername();
String password = helper.filteredPassword(); 
String headline = helper.filteredHeadline();
String description= helper.filteredDescription();
String hashtags=helper.filteredHashtags();
String contacts=helper.filteredContacts();
String rethinkID = helper.filteredRethinkID();
String visibility = helper.filteredVisibility();

int userID = 0;
int docID = 0;

String hasrethinkID = "true";



//1.   first check wetheruser exist
boolean userexist = helper.userAlreadyExistCheck(username);

if(userexist)
	{
	request.getRequestDispatcher("registerErrorPage.jsp").forward(request,response);
	}
else if(!helper.validateInput(headline,description,username))
	{
	request.getRequestDispatcher("registerErrorPage2.jsp").forward(request,response);
	}
else
	{
//2.	create User (in MariaDB user table)
	userID = helper.createUser(username,password);
//3. 	set User Session (needed for the AuhtN-Filter)
	session.setAttribute("userName",username );
	session.setAttribute("userID",userID ); 
//4.	create Profile	(in MariaDB profile table)
	docID = helper.createProfile(userID,headline,description,hashtags,contacts,rethinkID);	

//5. check if rethinkID exists
	if(helper.rethinkIDisempty(rethinkID))
				hasrethinkID ="false";
	
//6. 	set visibility policy for the profile
	helper.createProfileNode(docID,visibility,username,userID);
		
//7. for later use when favs has to be stored		
	helper.createUserNode(userID,username);	
		
//8.	index Profile	(in Solr)
	helper.indexProfile(docID,headline,description,hashtags,contacts,hasrethinkID,rethinkID);
			
//9. goto overview	
	response.sendRedirect("profiles.jsp");
	}
%>

</body>
</html>