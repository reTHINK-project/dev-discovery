<%@page import="org.json.simple.parser.JSONParser"%>
<%@ page import="de.telekom.rethink.discovery.JSONHelper"%>
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

   //out.println(new de.telekom.rethink.discovery.RESTClient().callURL("http://localhost:8080/de.telekom.rethink.discovery/rest/discover","lookup","searchquery","*:*"));
  // out.println(new de.telekom.rethink.discovery.RESTClient().callURL(request.getSession().getServletContext().getInitParameter("GlobalRegistryEndpoint")));
  
  String answer = (new de.telekom.rethink.discovery.RESTClient().callURL(request.getSession().getServletContext().getInitParameter("GlobalRegistryEndpoint"),"bXBhhJm-o40WBIcXQQECH0-_MqNux6p3ANxt7lFA-Mg"));
  
   JSONHelper helper=new JSONHelper();
   //get the datapart of the answer
   String datapartofanswer = helper.getTokenPart(answer,"data");
   String clearpayload = helper.decodeJWToken(datapartofanswer);
   String userIDs = helper.getTokenPart(clearpayload,"userIDs");
   
   Object obj = new JSONParser().parse(userIDs);
   
   org.json.simple.JSONArray array= (org.json.simple.JSONArray)obj;
   java.util.Iterator it = array.iterator();
   
   while(it.hasNext()){
   			out.println("<h2>"+it.next()+"</2>");
   }
%>

</body>
</html>