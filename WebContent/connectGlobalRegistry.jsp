<%@ page 	import="de.telekom.rethink.discovery.FormularHelper"
			import="de.telekom.rethink.discovery.GlobalAndDomainRegistryConnector"
		 	import="org.apache.log4j.Logger"
		 	import="com.eclipsesource.json.*"
		 	import="java.util.*"%>
		
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>reTHINK urls</title>
<link rel="stylesheet" type="text/css" href="css/style.css"/>
</head>
<body>
<div align="center">
<%
Logger log = Logger.getLogger(this.getClass());		
String guid = request.getParameter("guid");
String headline = request.getParameter("headline");
String rawAnswerGR ="";
String clearAnswerGR="";

FormularHelper helper =new FormularHelper(request);
GlobalAndDomainRegistryConnector gdrc = new GlobalAndDomainRegistryConnector(request,helper);

//grc.getDomainEntries(guid);
out.println("<h1>reTHINK discovery and resolving process for : "+headline+"</h1><br><br>");

%>
<table style="width:100%">
	<tr>
		<td><b>GUID</b></td>		
		<td><%out.println(guid);%></td>
	</tr>
	<tr>
		<td><b>raw answer of gReg</b></td>
		
		<%
		try{
			rawAnswerGR = gdrc.getRawAnswerOfGlobalRegistry(guid);
			out.println("<td>"+rawAnswerGR+"</td>");
						
		}catch(Exception ex)
		{
			out.println("<br>Network error!"+ex);
		}
	
		%>
	</tr>
	<tr>	
		<td><b>clear answer of gReg</b></td>
		<%
		clearAnswerGR= gdrc.getClearAnswerOfGlobalRegistry(rawAnswerGR);
		out.println("<td>"+clearAnswerGR+"</td>");
		%>	
	</tr>
	
		<%
		JsonObject object = Json.parse(clearAnswerGR).asObject();
		JsonArray arrayOfuserIds = object.get("userIDs").asArray();
		
		for(int i=0;i<arrayOfuserIds.size();i++)
			{
			JsonObject SingleUserID = arrayOfuserIds.get(i).asObject();
			String domain = SingleUserID.getString("domain", "empty domain");
			String domainRegAddress = gdrc.getServerAddressOfDomainRegistry(domain);
			String uID = SingleUserID.getString("uID", "empty uID");
			%>
			<tr>
				<td><b>Contacted Domain</b></td>
				<td><% out.println(domain);%></td>
			</tr>
			<tr>
				<td><b>Domain address</b></td>
				<td><% out.println(domainRegAddress);%></td>
			</tr>
			<tr>
				<td><b>Requested userID</b></td>
				<td><% out.println(uID);%></td>
			</tr>
			<%
			}
			

if(gdrc.GUIDexists(rawAnswerGR))
	{
	List currentHyperties = gdrc.getCurrentHypertiesFromGlobalAndDomainRegistry(rawAnswerGR);
	
	if(currentHyperties.size()==0)
		out.println("GUID is valid but there are no currently running hyperties.<br>");
	else
	{

	Iterator entries=currentHyperties.iterator();
	
	while(entries.hasNext())
		{
		String line = (String) entries.next();
		
		String[] part = line.split("#");
		
		%>
		<tr>
			<td><b>current hyperty URL</b></td>
			<td><% out.println(part[0]);%></td>
		</tr>
		<%
		}
	}	}

else
	out.println("<br>Value: no valid GUID");

   %>
</table>
</div>
</body>
</html>