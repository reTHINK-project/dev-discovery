/********************************************************************************************************************
 * Deutsche Telekom Laboratories																					*
 * Copyright (c) 2016 European Project reTHINK																		*
 * 																													*
 ********************************************************************************************************************/

package de.telekom.rethink.discovery;

import java.io.InputStream;

import javax.net.ssl.SSLContext;
//import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Invocation;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.log4j.Logger;
import org.glassfish.jersey.SslConfigurator;

public class RESTClient {
		
static Logger log = Logger.getLogger(RESTClient.class);		
		
	public String callURL(String URL)
	{
		String returnString="no connection possible";
		
		log.debug("Discovery Service contacts: "+URL);  
		
		Client client = ClientBuilder.newClient();
		WebTarget webTarget= client.target(URL);
		
		Invocation.Builder invocationBuilder = webTarget.request(MediaType.APPLICATION_JSON);
		Response response = invocationBuilder.get();
		
		String answer = response.readEntity(String.class);
		returnString=answer;
		
		
		log.info("get headers: "+ response.getHeaders().toString());
		log.info("get content: "+ answer);
		
		return returnString;	
	}
	
	
	
	public String callSecureURL(String URL)
	{
		String returnString="no connection possible";
		
		log.debug("Discovery Service contacts (SSL): "+URL);  
		
		String completePath = this.getClass().getClassLoader().getResource("de/telekom/rethink/discovery/my.keystore").getPath();
		
		
		
		
		SslConfigurator sslConfig = SslConfigurator.newInstance()
		//.trustStoreFile("D:/workspace/.metadata/.plugins/org.eclipse.wst.server.core/tmp1/wtpwebapps/discovery/WEB-INF/classes/de/telekom/rethink/discovery/my.keystore")
		.trustStoreFile(completePath)
		.trustStorePassword("rethink")
		//.keyStoreFile("D:/workspace/.metadata/.plugins/org.eclipse.wst.server.core/tmp1/wtpwebapps/discovery/WEB-INF/classes/de/telekom/rethink/discovery/my.keystore")
		.keyStoreFile(completePath)
		.keyStorePassword("rethink");
		
		SSLContext sslContext = sslConfig.createSSLContext();
		
		
		
		Client client = ClientBuilder.newBuilder().sslContext(sslContext).build();
		WebTarget webTarget= client.target(URL);
		
		Invocation.Builder invocationBuilder = webTarget.request(MediaType.APPLICATION_JSON);
		Response response = invocationBuilder.get();
		
		String answer = response.readEntity(String.class);
		returnString=answer;
		

		log.debug("Status: "+response.getStatus());
		log.debug("Status: "+response.getStatusInfo());
			
		
		log.debug("get headers: "+ response.getHeaders().toString());
		log.debug("get content: "+ answer);
		
		return returnString;	
	}
	
	
	
	
	/*
	public Response callURL(String URL, String path)
	{
		Response response;
		
		log.info("contacts: "+URL);  
		
		Client client = ClientBuilder.newClient();
		WebTarget webTarget= client.target(URL).path(path);
		
		Invocation.Builder invocationBuilder = webTarget.request(MediaType.APPLICATION_JSON);
		response = invocationBuilder.get();
		
		String answer = response.readEntity(String.class);
		

		log.info("Status:"+response.getStatus());
		log.info("StatusInfo:"+response.getStatusInfo());
			
		
		log.info("get headers: "+ response.getHeaders().toString());
		log.info("get content: "+ answer);
		
		return response;	
	}
	*/
	
	

	public String callSecureURL(String URL, String path)
	{
		String returnString="no connection possible";
		
		log.debug("Discovery Service contacts (SSL): "+URL);  

		String completePath = this.getClass().getClassLoader().getResource("de/telekom/rethink/discovery/my.keystore").getPath();
		
		SslConfigurator sslConfig = SslConfigurator.newInstance()
		//.trustStoreFile("D:/workspace/.metadata/.plugins/org.eclipse.wst.server.core/tmp1/wtpwebapps/discovery/WEB-INF/classes/de/telekom/rethink/discovery/my.keystore")
		.trustStoreFile(completePath)
		.trustStorePassword("rethink")
		//.keyStoreFile("D:/workspace/.metadata/.plugins/org.eclipse.wst.server.core/tmp1/wtpwebapps/discovery/WEB-INF/classes/de/telekom/rethink/discovery/my.keystore")
		.keyStoreFile(completePath)
		.keyStorePassword("rethink");
		
		SSLContext sslContext = sslConfig.createSSLContext();
		
		
		
		Client client = ClientBuilder.newBuilder().sslContext(sslContext).build();
		WebTarget webTarget= client.target(URL).path(path);
		
		Invocation.Builder invocationBuilder = webTarget.request(MediaType.APPLICATION_JSON);
		Response response = invocationBuilder.get();
		
		String answer = response.readEntity(String.class);
		returnString=answer;
		

		log.debug("Status: "+response.getStatus());
		log.debug("Status: "+response.getStatusInfo());
			
		
		log.debug("get headers: "+ response.getHeaders().toString());
		log.debug("get content: "+ answer);
		
		return returnString;	
	}
	
	
	
	
	
	public String callURL(String URL, String path)
	{
		String returnString="no connection possible";
		
		log.debug("Discovery Service contacts: "+URL);  
		
		Client client = ClientBuilder.newClient();
		WebTarget webTarget= client.target(URL).path(path);
		
		Invocation.Builder invocationBuilder = webTarget.request(MediaType.APPLICATION_JSON);
		Response response = invocationBuilder.get();
		
		String answer = response.readEntity(String.class);
		returnString=answer;
		

		log.debug("Status: "+response.getStatus());
		log.debug("Status: "+response.getStatusInfo());
			
		
		log.debug("get headers: "+ response.getHeaders().toString());
		log.debug("get content: "+ answer);
		
		return returnString;	
	}
	
	
	
	public String callURL(String URL, String path,String para_name, String para_value)
	{
		String returnString="no connection possible";
		
		log.debug("Discovery Service contacts: "+URL+"/"+path+" asking for " +para_name+" = " +para_value);  
		
		Client client = ClientBuilder.newClient();
		WebTarget webTarget= client.target(URL).path(path).queryParam(para_name, para_value);
		
		Invocation.Builder invocationBuilder = webTarget.request(MediaType.APPLICATION_JSON);
		Response response = invocationBuilder.get();
		
		String answer = response.readEntity(String.class);
		returnString=answer;
		
		
		log.debug("get headers: "+ response.getHeaders().toString());
		log.debug("get content: "+ answer);
		
		return returnString;	
	}

}