/********************************************************************************************************************
 * Deutsche Telekom Laboratories																					*
 * Copyright (c) 2016 European Project reTHINK																		*
 * 																													*
 ********************************************************************************************************************/

package de.telekom.rethink.discovery;

//import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Invocation;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.log4j.Logger;

public class RESTClient {
		
static Logger log = Logger.getLogger(RESTClient.class);		
		
	public String callURL(String URL)
	{
		String returnString="no connection possible";
		
		log.info("contacts: "+URL);  
		
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
	
	
	public String callURL(String URL, String path)
	{
		String returnString="no connection possible";
		
		log.info("contacts: "+URL);  
		
		Client client = ClientBuilder.newClient();
		WebTarget webTarget= client.target(URL).path(path);
		
		Invocation.Builder invocationBuilder = webTarget.request(MediaType.APPLICATION_JSON);
		Response response = invocationBuilder.get();
		
		String answer = response.readEntity(String.class);
		returnString=answer;
		
		log.info("get headers: "+ response.getHeaders().toString());
		log.info("get content: "+ answer);
		
		return returnString;	
	}
	
	
	
	public String callURL(String URL, String path,String para_name, String para_value)
	{
		String returnString="no connection possible";
		
		log.info("contacts: "+URL+"/"+path+" asking for " +para_name+" = " +para_value);  
		
		Client client = ClientBuilder.newClient();
		WebTarget webTarget= client.target(URL).path(path).queryParam(para_name, para_value);
		
		Invocation.Builder invocationBuilder = webTarget.request(MediaType.APPLICATION_JSON);
		Response response = invocationBuilder.get();
		
		String answer = response.readEntity(String.class);
		returnString=answer;
		
		log.info("get headers: "+ response.getHeaders().toString());
		log.info("get content: "+ answer);
		
		return returnString;	
	}

}