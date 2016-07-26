<%@ page import="de.telekom.rethink.discovery.FormularHelper"%>
<%@page import="org.json.simple.parser.JSONParser"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Resolved GUID URLs</title>
</head>
<body>
<% 

String guid = request.getParameter("guid");

FormularHelper helper =new FormularHelper(request);

try{
String answer = helper.callURL(request.getSession().getServletContext().getInitParameter("GlobalRegistryEndpoint"),guid);
  
//get the datapart of the answer
String datapartofanswer = helper.getTokenPart(answer,"data");
String clearpayload = helper.decodeJWToken(datapartofanswer);
String userIDs = helper.getTokenPart(clearpayload,"userIDs");
   
Object obj = new JSONParser().parse(userIDs);
org.json.simple.JSONArray array= (org.json.simple.JSONArray)obj;
java.util.Iterator<String> it = array.iterator();

   while(it.hasNext()){
   			out.println("<h2>"+it.next()+"</2>");
   }
	}catch(javax.ws.rs.ProcessingException  pex)
		{
		helper.log("processing error: "+pex);
		out.println("<h2>Processing error!</2>");
		}
	catch(java.net.NoRouteToHostException nrex)
		{
		helper.log("network error: "+nrex);
		out.println("<h2>Network error!</2>");
		}
	catch(Exception ex)
		{
		helper.log("general error: "+ex);
		out.println("<h2>general error!</2>");
		}
	
   %>

</body>
</html>