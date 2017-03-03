<%@ page	import="de.telekom.rethink.discovery.listener.SessionListener"
			import="java.util.*"
%>

<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>reTHINK Discovery - Current Sessions</title>
</head>
<body>
<div align="center">
<h1>Current sessions</h1>
<table>
<tr>
<th>No</th>
<th>SessionID</th>
<th>Name</th>
<th>created (UTC)</th>
</tr>
<%


String userName = (String) session.getAttribute("userName");

if(!userName.equals("admin"))
		{
			response.sendRedirect("login.jsp");
		}


SessionListener sl = new SessionListener();
int count = sl.getSessionCount();

Hashtable memory = sl.getSessionMemory();

Enumeration enumeration = memory.keys();


int i=1;

while (enumeration.hasMoreElements())
		{
		String sessionID = (String) enumeration.nextElement();
		Hashtable sessiontable = (Hashtable) memory.get(sessionID);
		
		String username = (String)sessiontable.get("userName");
		String sessionCreated =(String) sessiontable.get("sessionCreated");
		
	
		
		if(username==null)
			username = "anonymous";
		
		out.println("<tr><td align=\"center\">"+i+++"</td><td align=\"center\">"+sessionID+"</td><td align=\"center\">"+username+"</td><td align=\"center\">"+sessionCreated+"</td><tr><br>");
		}

%>
</table>
</div>
</body>
</html>