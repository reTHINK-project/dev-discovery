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
out.println("<h1>reTHINK contacts of "+headline+"</h1><br>");
out.println("<h2>GlobalRegistry for GUID: "+guid+"</h2><br><br><br>");




try{
	rawAnswerGR = gdrc.getRawAnswerOfGlobalRegistry(guid);
	String clearAnswerGR= gdrc.getClearAnswerOfGlobalRegistry(rawAnswerGR);
	out.println("<br>The clear answer of GlobalRegistry is:<br>"+clearAnswerGR+"<br><br>");
	
}catch(Exception ex)
{
	out.println("<br>Network error!"+ex);
}


if(gdrc.GUIDexists(rawAnswerGR))
	{
	List currentHyperties = gdrc.getCurrentHypertiesFromGlobalAndDomainRegistry(rawAnswerGR);
	
	if(currentHyperties.size()==0)
		out.println("GUID is valid but there are no currently running hyperties.");
	
	Iterator entries=currentHyperties.iterator();
	
	while(entries.hasNext())
		{
		String line = (String) entries.next();
		
		String[] part = line.split("#");
		out.println("Found following available Hyperties<br>");
		out.println("<br>"+part[0]);
	
		}
	}	

else
	out.println("<br>Value: no valid GUID");




//helper.getDataPart(rawAnswer);
//out.println("<br>Description: "+grc.getDescriptionPartOfGlobalRegistryAnswer(rawAnswer));

  /*


//get the datapart of the answer
String datapartofanswer = helper.getTokenPart(answer,"Value");
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

			String id = "user://gmail.com/ingo.friese";
			String idUrlEncoded = URLEncoder.encode(id,"UTF-8");
			log.info("Der user lautet: "+idUrlEncoded);
			
		try{
			String domainRegistryAnswer= "{\"hyperty://rethink-dev.tlabscloud.com/d2aad579-a213-4400-9e24-af2b23dbaed8\":{\"resources\":[\"audio\",\"video\"],\"dataSchemes\":[\"connection\"],\"descriptor\":\"hyperty-catalogue://catalogue.rethink-dev.tlabscloud.com/.well-known/hyperty/DTWebRTC\",\"startingTime\":\"2016-09-20T07:31:39Z\",\"hypertyID\":\"hyperty://rethink-dev.tlabscloud.com/d2aad579-a213-4400-9e24-af2b23dbaed8\",\"userID\":\"user://gmail.com/ingo.friese\",\"lastModified\":\"2016-09-20T09:20:45Z\",\"expires\":3600},\"hyperty://rethink-dev.tlabscloud.com/b71cd853-f657-4b6a-919b-07916bd3d50e\":{\"resources\":[\"audio\",\"video\"],\"dataSchemes\":[\"connection\"],\"descriptor\":\"hyperty-catalogue://catalogue.rethink-dev.tlabscloud.com/.well-known/hyperty/DTWebRTC\",\"startingTime\":\"2016-09-20T09:23:32Z\",\"hypertyID\":\"hyperty://rethink-dev.tlabscloud.com/b71cd853-f657-4b6a-919b-07916bd3d50e\",\"userID\":\"user://gmail.com/ingo.friese\",\"lastModified\":\"2016-09-20T09:23:32Z\",\"expires\":3600}}";
			//helper.callURL(request.getSession().getServletContext().getInitParameter("DomainRegistryEndpoint"),idUrlEncoded);	
			out.println("Answer Domain Registry: "+domainRegistryAnswer);
			
			String resources = helper.getTokenPart(domainRegistryAnswer,"resources");
			String hypertyID = helper.getTokenPart(domainRegistryAnswer,"hypertyID");
			out.println("Hyperty type:"+resources);
			out.println("Hyperty ID: "+ hypertyID);
				
		}	
		catch(Exception ex)
				{
				helper.log("general error: "+ex);
			out.println("<h2>general error! "+ex+"</2>");
		}
			
			*/

	
   %>
</div>
</body>
</html>