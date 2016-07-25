package de.telekom.rethink.discovery;


import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.QueryParam;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;

	
import org.json.simple.JSONObject;
import org.apache.log4j.Logger;
import org.apache.solr.client.solrj.SolrServerException;
	//import org.json.simple.JSONArray;
	//import org.json.simple.parser.ParseException;
	//import org.json.simple.parser.JSONParser;
	

import de.telekom.rethink.discovery.FormularHelper;



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
		
	  		JSONObject obj = new JSONObject();
	  		obj.put("build-date", "2016-07-18");
	  		obj.put("version", 0.1);
	  		obj.put("message","reTHINK discovery service");
	  		obj.put("instanceID", request.getSession().getServletContext().getInitParameter("instanceID"));
	  		obj.put("responseCode",200);
	  		obj.put("errorCode",0);
	  			  		
	  		return Response.ok(obj.toString(), MediaType.APPLICATION_JSON).build();
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
		  		String JSONString= helper.doJSONSearch(searchquery, 0);		  		
		  		
		  		response= Response.status(201).entity(JSONString).type(MediaType.APPLICATION_JSON).build();
		  		
		  		log.info("Provide: "+JSONString.toString());
		  		log.info("Requestor Address:"+request.getRemoteAddr()+"/"+request.getPathInfo());
		  	}
		    return response;
		  }
	} 
	
	
