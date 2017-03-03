<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
import="de.telekom.rethink.discovery.RESTClient"
import="de.telekom.rethink.discovery.FormularHelper"
import="de.telekom.rethink.discovery.GlobalAndDomainRegistryConnector"
import="com.eclipsesource.json.*"
import="java.util.*"
import="java.net.*"

pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Check of Registry Processes</title>
</head>
<body>

Try to contact Global Registry for GUID <b>JaGt8I5U5HWWi8P9ogYuIY6Tnr6CDADx3XDTDLluBmw</b>
</br>
</br>
<b>Raw answer of gReg:</b>
<%
		RESTClient rc = new RESTClient();
		FormularHelper helper =new FormularHelper(request);
		GlobalAndDomainRegistryConnector gdrc = new GlobalAndDomainRegistryConnector(request,helper);
		
		String guid="JaGt8I5U5HWWi8P9ogYuIY6Tnr6CDADx3XDTDLluBmw";	
		String rawGregAnswer = helper.getRawAnswerOfGlobalRegistry(guid);		
%>
<pre><code>  
<%
if(gdrc.GUIDexists(rawGregAnswer))
{
		JsonValue value = Json.parse(rawGregAnswer);
		String prettyJsonrwaGregAnswer = value.toString(WriterConfig.PRETTY_PRINT);
		out.println(prettyJsonrwaGregAnswer);	
%>		
</code></pre>
</br>
</br>		
<b>Clear answer of gReg:</b>	
<pre><code>  
<%		
		String clearGregAnswer = helper.getClearAnswerOfGlobalRegistry(rawGregAnswer);	
		JsonValue value2 = Json.parse(clearGregAnswer);
		String prettyJsonclearGregAnswer = value2.toString(WriterConfig.PRETTY_PRINT);
		out.println(prettyJsonclearGregAnswer);
		

//out.println(rc.callSecureURL("https://rethink.tlabscloud.com/registry/hyperty/user/user%3A%2F%2Fgmail.com%2Fdruesedow"));

%>
</code></pre>
</br>
<b>Analysed answer</b>
</br>
<code><pre>
<%

		JsonObject object = Json.parse(clearGregAnswer).asObject();
		JsonArray arrayOfuserIds = object.get("userIDs").asArray();
		
		for(int i=0;i<arrayOfuserIds.size();i++)
			{
			JsonObject SingleUserID = arrayOfuserIds.get(i).asObject();
			String domain = SingleUserID.getString("domain", "empty domain");
			out.println("No:"+i);
			out.println("Domain: "+domain);
			String domainRegAddress = gdrc.getServerAddressOfDomainRegistry(domain);
			out.println("Server address of domain (source:propertie file): "+domainRegAddress);
			String uID = SingleUserID.getString("uID", "empty uID");
			out.println("UserID (raw): "+uID);
			String idUrlEncoded = URLEncoder.encode(uID,"UTF-8");
			out.println("UserID (UTF-8 encoded): "+idUrlEncoded);
			out.println("");
		//log.debug("Going to contact DomainRegistry "+domain+" with address: "+domainRegAddress);
		//log.debug("For userID"+uID+" encrypted to "+idUrlEncoded);
			}

%>
</code></pre>
<b>Raw answer of domain registries per answer ! YOU HAVE TO HAVE A HYPERTY RUNNING TO SEE USEFUL RESULTS</b>
<code><pre>
<%

			for(int i=0;i<arrayOfuserIds.size();i++)
				{
				JsonObject SingleUserID = arrayOfuserIds.get(i).asObject();
				String domain = SingleUserID.getString("domain", "empty domain");
				String domainRegAddress = gdrc.getServerAddressOfDomainRegistry(domain);
				String uID = SingleUserID.getString("uID", "empty uID");
				String idUrlEncoded = URLEncoder.encode(uID,"UTF-8");
				out.println("No:"+i);
				out.println("Going to contact DomainRegistry: "+domain+" with address: "+domainRegAddress+" for userID: "+uID+" encrypted to: "+idUrlEncoded);
				String domainAnswer = gdrc.getRawAnswerFromDomainRegistry(domainRegAddress,idUrlEncoded);

				JsonValue value3 = Json.parse(domainAnswer);
				String prettyJsonDomainAnswer = value3.toString(WriterConfig.PRETTY_PRINT);
				out.println(prettyJsonDomainAnswer);	
				out.println("");
				}
%>
</code></pre>
<b>Discover REST API resulr</b>
<code><pre>
<%

String discoAnswer=rc.callURL("http://localhost:8080/discovery/rest/discover/lookup?searchquery=test");
JsonValue value4 = Json.parse(discoAnswer);
String prettyJsonDiscoAnswer = value4.toString(WriterConfig.PRETTY_PRINT);
out.println(prettyJsonDiscoAnswer);	
out.println("");

%>
</code></pre>


<%
	
	}
	else
	{
		out.println("Die GUID existiert nicht oder ist ungültig!");
	}
	
%>

</body>
</html>