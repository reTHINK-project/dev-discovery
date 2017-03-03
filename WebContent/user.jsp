<%@ page 
import="de.telekom.rethink.discovery.FormularHelper"
import="de.telekom.rethink.discovery.listener.SessionListener"
import="java.util.*"
%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>reTHINK Discovery User Statistics</title>
</head>
<body>
<div align="center">
<h2>User Management Console</h2><br>
<table style="width:50%">
<tr>
	<th>No</th>
	<th>ID</th>
	<th>Name</th>
	<th>Created</th>
	<th>Profiles</th>
	<th>Last login</th>
	<th>Last logout</th>
	<th>Status</th>
	<th>Action</th>
</tr>	
<% 
FormularHelper helper =new FormularHelper(request);
SessionListener sl = new SessionListener();
String userName = (String) session.getAttribute("userName");

	if(!userName.equals("admin"))
			{
				response.sendRedirect("searchPage_in.jsp");
			}

	try{	
		List list = helper.getUserList();
		int i=1;
	
		Iterator<Hashtable> iter = list.iterator();
	
		while (iter.hasNext())
			{
			Hashtable user=iter.next();
			out.print("<tr><td align=\"center\">"+i+++"</td><td align=\"center\">"+user.get("userID")+"</td><td align=\"center\">"+user.get("username")+"</td><td align=\"center\">"+helper.getCreationTime(user.get("username").toString())+"</td><td align=\"center\">"+helper.getProfileCount(Integer.parseInt(user.get("userID").toString()))+"</td><td align=\"center\">"+helper.getLoginTime(user.get("username").toString())+"</td><td align=\"center\">"+helper.getLogoutTime(user.get("username").toString())+"</td>");
			
			
			//green if there is an activ session
			if(sl.userHasActiveSession(user.get("username").toString()))	
				out.print("<td align=\"center\"><img src=\"images/green.png\" width=\"30%\" height=\"30%\" /></td>");
			else
				{			
				if(helper.getTimeSinceLastLogin(user.get("username").toString())<=86400)
					{
					out.print("<td align=\"center\"><img src=\"images/yellow.png\" width=\"30%\" height=\"30%\" /></td>");
					}
				else
					out.print("<td align=\"center\"><img src=\"images/grey.png\" width=\"30%\" height=\"30%\" /></td>");
				}
				
			out.print("<td align=\"center\"><a href=\"deleteUser.jsp?user="+user.get("userID")+"\">delete</a>"+"</td></tr>");
			}
			out.println("</table><br>");
			%>
			
			<input type="button" value=" Delete all user " onClick="deleteAll()">
			
			
			<script>
			
			function deleteAll(){
			var c = confirm("     Do you really want to delete all users?     ")
			if(c==true)
				window.location="deleteUser.jsp?user=all";
			}
			</script>
			<%
			
		}catch(Exception ex)
			{
			out.println("Database Error: "+ex);
			}
	%>
</div>
</body>
</html>