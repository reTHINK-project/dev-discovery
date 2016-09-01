<%@ page import="de.telekom.rethink.discovery.FormularHelper"
		 import="org.apache.log4j.Logger"
		 import="java.net.*"	%>
<%@page import="org.json.simple.parser.JSONParser"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>reTHINK urls</title>
<link href='https://fonts.googleapis.com/css?family=Lato:100,300,400,700,900' rel='stylesheet' type='text/css'>
<link rel="stylesheet" type="text/css" href="css/style.css"/>
</head>
<body>
<div align="center">
<% 
Logger log = Logger.getLogger(this.getClass());		


String guid = request.getParameter("guid");
String headline = request.getParameter("headline");

FormularHelper helper =new FormularHelper(request);


out.println("<h1 class=\"rehead\">reTHINK contacts of "+headline+"</1><br><br><br>");


try{
String answer = helper.callURL(request.getSession().getServletContext().getInitParameter("GlobalRegistryEndpoint"),guid);
  
//get the datapart of the answer
String datapartofanswer = helper.getTokenPart(answer,"data");
log.debug("The data part is"+datapartofanswer);

String clearpayload = helper.decodeJWToken(datapartofanswer);
log.debug("The clear part is"+clearpayload);


String userIDs = helper.getTokenPart(clearpayload,"userIDs");
   
Object obj = new JSONParser().parse(userIDs);
org.json.simple.JSONArray array= (org.json.simple.JSONArray)obj;
java.util.Iterator<String> it = array.iterator();

   while(it.hasNext()){
	   Object objURL = it.next();	
	   String domain = "unknown domain";
	  
	   try{
	   		URL url=new URL(objURL.toString());
	   		domain = url.getHost();
	   }catch(Exception ex)
	   { }
   			out.println("<a href=\""+objURL+"\"> >> Click here to reach "+headline+" at:       <b>"+domain+"</b><< </a><br>");
   }
	}catch(javax.ws.rs.ProcessingException  pex)
		{
		helper.log("processing error: "+pex);
		out.println("<h2>Processing error!+</2>");
		}
	catch(java.net.NoRouteToHostException nrex)
		{
		helper.log("network error: "+nrex);
		out.println("<h2>Network error!</2>");
		}
	catch(Exception ex)
		{
		helper.log("general error: "+ex);
		out.println("<h2>general error! "+ex+"</2>");
		}
	
   %>
</div>
</body>
</html>