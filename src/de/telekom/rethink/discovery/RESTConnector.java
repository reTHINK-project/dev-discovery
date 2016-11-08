/********************************************************************************************************************
 * Deutsche Telekom Laboratories																					*
 * Copyright (c) 2016 European Project reTHINK																		*
 * 																													*
 ********************************************************************************************************************/

package de.telekom.rethink.discovery;


import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.QueryParam;

import java.io.IOException;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

	
//import org.json.simple.JSONObject;
import org.apache.log4j.Logger;
import org.apache.solr.client.solrj.SolrServerException;
	//import org.json.simple.JSONArray;
	//import org.json.simple.parser.ParseException;
	//import org.json.simple.parser.JSONParser;
	
import com.eclipsesource.json.Json;
import com.eclipsesource.json.JsonArray;
import com.eclipsesource.json.JsonObject;


import de.telekom.rethink.discovery.FormularHelper;
import de.telekom.rethink.discovery.GlobalAndDomainRegistryConnector;



	// Plain old Java Object it does not extend as class or implements 
	// an interface

	// The class registers its methods for the HTTP GET request using the @GET annotation. 
	// Using the @Produces annotation, it defines that it can deliver several MIME types,
	// text, XML and HTML. 

	// The browser requests per default the HTML MIME type.

	//Sets the path to base URL + /hello
	@Path("/discover")
	public class RESTConnector {
		
		

		
	
	static Logger log = Logger.getLogger(RESTConnector.class);
	
	@Context 
	private HttpServletRequest request; 	
		
	
						
	 	// This method is called if HTML is request
	  	@SuppressWarnings("unchecked")
		@GET
	  	@Produces(MediaType.APPLICATION_JSON)
	  	public Response answerPing(){
	   
	  		log.info("REST_API was pinged.");   
	  		log.info(request.getSession().getServletContext().getInitParameter("instanceID"));
	  		
	  		JsonObject pingAnswer = new JsonObject();
	  		pingAnswer.add("build-date", "2016-11-08");
	  		pingAnswer.add("version", "0.2");
	  		pingAnswer.add("service", "reTHINK discovery");
	  		pingAnswer.add("message", "ping");
	  		pingAnswer.add("instanceID", request.getSession().getServletContext().getInitParameter("instanceID"));
	  		pingAnswer.add("responseCode", 200);
	  		pingAnswer.add("errorCodeCode", 0);
	  		
	  		return Response.ok(pingAnswer.toString(), MediaType.APPLICATION_JSON).build();

	  	}	
	
	

	  	
		 @GET
		 @Path("/lookup")
		 public Response doRESTSearch(@QueryParam("searchquery") String searchquery ) throws SolrServerException, IOException {
		
		 log.info("REST_API got request with searchquery: "+searchquery);   
		 
		 Response response;
				  	
		 if(searchquery==null)
		  		{
		  		response = Response.status(400).entity("400 Bad request. Malformed searchrequest. Please provide a parameter called searchquery.").type(MediaType.TEXT_PLAIN).build();
		  		
		  		log.info("Malformed searchrequest.");
		  		}	  
		  	else
		  		{
		  		FormularHelper helper= new FormularHelper(this.request);	
		  		GlobalAndDomainRegistryConnector gdrc=new GlobalAndDomainRegistryConnector(request,helper);
		  		String JSONString= helper.doJSONSearch(searchquery, 0);		  	
		  		
		  		JsonObject jsonObject = Json.parse(JSONString).asObject();
		  		JsonArray allSearchResults = jsonObject.get("results").asArray();
		  		String searchString = jsonObject.get("searchString").asString();
		  		int responseCode = (Integer) jsonObject.get("responseCode").asInt();
		  		String queryInstanceID =   jsonObject.get("instanceID").asString();
		  		
		  		JsonObject returnObject = new JsonObject();
		  		returnObject.add("instanceID", queryInstanceID);
		  		returnObject.add("responseCode", responseCode);
		  		returnObject.add("searchString", searchString);
		  		
		  		JsonArray results = new JsonArray();
		  		
		  		for(int i=0;i<allSearchResults.size();i++)
		  			{
		  			JsonObject singleResult=(JsonObject) allSearchResults.get(i);
		  			
		  			String hasrethinkID = (String) singleResult.get("hasrethinkID").asString();
		  			String instanceID = (String) singleResult.get("instanceID").asString();
		  			String hashtags = (String) singleResult.get("hashtags").asString();
		  			String description = (String) singleResult.get("description").asString();
		  			String rethinkID = (String) singleResult.get("rethinkID").asString();
		  			String headline = (String) singleResult.get("headline").asString();
		  			String contacts = (String) singleResult.get("contacts").asString();
		  			int resultNo = (Integer) singleResult.get("resultNo").asInt();
		 
		  			JsonObject singleReturnResult = new JsonObject();
		  			singleReturnResult.add("resultNo", resultNo);
		  			singleReturnResult.add("instanceID", instanceID);
		  			singleReturnResult.add("hashtags", hashtags);
		  			singleReturnResult.add("description", description);
		  			singleReturnResult.add("rethinkID", rethinkID);
		  			singleReturnResult.add("headline", headline);
		  			singleReturnResult.add("contacts", contacts);
		  			singleReturnResult.add("hasrethinkID", hasrethinkID);
		  			
		  			
		  			if(hasrethinkID.equals("true"))
		  						{
		  						
		  						String guid = (String) singleResult.get("rethinkID").asString();
		  						
		  						String rawAnswer = gdrc.getRawAnswerOfGlobalRegistry(guid);
		  						
		  						if(gdrc.GUIDexists(rawAnswer))
		  							{			
		  							List hypertylist = gdrc.saveGetCurrentHypertiesFromGlobalAndDomainRegistry(rawAnswer);
		  						
		  							Iterator iterator = hypertylist.iterator();
		  						
		  							JsonArray hypertyArray = new JsonArray();
		  						
		  							while(iterator.hasNext())
		  								{
			  							String resultline=	(String) iterator.next();
			  							String[] part = resultline.split("#");
			  							JsonObject hyperty =new JsonObject();
			  							String combinedURL=part[0];
			  							String[] splitURL = combinedURL.split("\\?");
			  							String hypertyURLplusID = splitURL[1];
			  							String[] hypertyURLplusIDField = hypertyURLplusID.split("&");
			  							String URL =  hypertyURLplusIDField[0];
			  							String uID =  hypertyURLplusIDField[1];
		
			  							hyperty.add("url",URL);
			  							hyperty.add("userID", uID);
			  							hyperty.add("media", part[2]);
			  							hyperty.add("provider", part[3]);
			  							hypertyArray.add(hyperty);  							
		  								}
		  							singleReturnResult.add("hyperties", hypertyArray);
		  							}
		  						}
		  			
		  				results.add(singleReturnResult);	
		  			}
		  		returnObject.add("results", results);
		  		
		  		//response= Response.status(201).entity(JSONString).type(MediaType.APPLICATION_JSON).build();
		  		response= Response.status(201).entity(returnObject.toString()).type(MediaType.APPLICATION_JSON).build();
		  		
		  		
		  		log.info("Provide: "+returnObject.toString());
		  		log.info("Requestor Address:"+request.getRemoteAddr()+"/"+request.getPathInfo());
		  	}
		    return response;
		  }
	} 
	
	
