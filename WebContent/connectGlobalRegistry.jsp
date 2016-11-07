<%@ page 	import="de.telekom.rethink.discovery.FormularHelper"
			import="de.telekom.rethink.discovery.GlobalAndDomainRegistryConnector"
		 	import="org.apache.log4j.Logger"
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

FormularHelper helper =new FormularHelper(request);
GlobalAndDomainRegistryConnector gdrc = new GlobalAndDomainRegistryConnector(request,helper);

//grc.getDomainEntries(guid);
out.println("<h1>reTHINK contacts of "+headline+"</h1><br><br>");
out.println("<h3>Lookup registries for GUID: "+guid+"</h3><br><br><br>");

try{
	rawAnswerGR = gdrc.getRawAnswerOfGlobalRegistry(guid);
	String clearAnswerGR= gdrc.getClearAnswerOfGlobalRegistry(rawAnswerGR);
	out.println("<br><h3>The clear answer of GlobalRegistry is:</h3><br>"+clearAnswerGR+"<br><br>");
	
}catch(Exception ex)
{
	out.println("<br>Network error!"+ex);
}

if(gdrc.GUIDexists(rawAnswerGR))
	{
	List currentHyperties = gdrc.getCurrentHypertiesFromGlobalAndDomainRegistry(rawAnswerGR);
	
	if(currentHyperties.size()==0)
		out.println("GUID is valid but there are no currently running hyperties.");
	else
	{
	out.println("<h3>Found following available Hyperties at DomainRegistry:</h3><br>");
	Iterator entries=currentHyperties.iterator();
	
	while(entries.hasNext())
		{
		String line = (String) entries.next();
		
		String[] part = line.split("#");
		
		out.println(part[0]+"<br");
	
		}
	}	}

else
	out.println("<br>Value: no valid GUID");

   %>
</div>
</body>
</html>