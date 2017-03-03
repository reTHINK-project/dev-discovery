
<%@ page import="de.telekom.rethink.discovery.FormularHelper"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>reTHINK Discovery Configuration Check</title>
</head>
<body>
<div align="center">
<h1>General System Statistics</h1><br>
<h2>Neo4j database</h2>   
	<%
	FormularHelper helper =new FormularHelper(request);
	
	String userName = (String) session.getAttribute("userName");
	
		if(!userName.equals("admin"))
			{
				response.sendRedirect("searchPage_in.jsp");
			}
		
	
	//check neo4j
	try{
			out.println("Profiles in Neo4j: "+helper.getNeo4jProfileCount()+"<br>");
			out.println("Users in Neo4j: "+helper.getNeo4jUserCount());
			
			
		}catch(Exception ex)
			{
			out.println("Neo4j is not available");
			}
	%>    
<h2>SQL database</h2> 
	<% 
	try{
			out.println("Number of user accounts in this instance: "+ helper.getDBUserCount()+"<br>"); 
 			out.println("Number of profiles: "+ helper.getDBProfileCount());  
		}catch(Exception ex)
			{
			out.println("SQL-DB is not available is not available");
			}  	
 	%>
 <h2>Solr Search Engine<br></h2>
 	<%
  	try{
			out.println("Number of indexed profiles "+helper.indexProfileCount());
  		}catch(Exception ex)
 			{
	  		out.println("Solr is not available");
 			}		
 	%>
 <h2>Global Registry Status</h2>
 	<%
    	
    try{
    		out.println("Global Registry is up and running. "+new de.telekom.rethink.discovery.RESTClient().callURL(request.getSession().getServletContext().getInitParameter("GlobalRegistryEndpoint")));
    	}catch(Exception ex)
    		{
    		out.print("Globel Registry is not reachable!");
    		}
 	%>
 <h2>Domain Registry Status</h2>	
 <%
 	try{
 		//out.println("nothing");
 		//out.println("Domain Registry is up and running. Test message"+new de.telekom.rethink.discovery.RESTClient().callURL("http://10.101.2.8:4567/hyperty/user/user%3A%2F%2Fgmail.com%2Fingo%2Efriese"));
 		
 		out.println("Domain Registry is up and running. Test message"+new de.telekom.rethink.discovery.RESTClient().callSecureURL("https://rethink.tlabscloud.com/registry/hyperty/user/user%3A%2F%2Fgmail.com%2Fingo%2Efriese"));
 		
 		//out.println("Domain Registry is up and running. "+new de.telekom.rethink.discovery.RESTClient().callURL("http://10.33.4.165:4567/hyperty/user/user://gmail.com/ingo.friese"));

 		//out.println("Domain Registry is up and running. "+new de.telekom.rethink.discovery.RESTClient().callURL("http://10.33.5.165:4567/hyperty/user/user://rethink-dev.tlabscloud.com/ingo.friese@gmail.com"));
 																																						
 		
	 	}catch(Exception ex)
		{
		out.print("Domain Registry is not reachable!");
		}
 		
 	
    	
    %>
</div>
</body>
</html>