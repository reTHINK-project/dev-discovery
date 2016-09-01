/********************************************************************************************************************
 * Deutsche Telekom Laboratories																					*
 * Copyright (c) 2016 European Project reTHINK																		*
 * 																													*
 ********************************************************************************************************************/

package de.telekom.rethink.discovery;

import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import org.apache.log4j.Logger;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class JSONHelper {
	
	static Logger log = Logger.getLogger(JSONHelper.class);			
	
	
	public String getTokenPart(String request, String field)
	{
	String token=null;	

	JSONParser parser = new JSONParser();
      
	
	
      try{
        Object obj = parser.parse(request.trim());
        JSONObject jsonObject = (JSONObject)obj;
        
        token=jsonObject.get(field).toString();
        
        /* 
        Set set = jsonObject.entrySet();
        Iterator it =set.iterator();
        
        while(it.hasNext())
        	{
        	 String line = it.next().toString();
        	 System.out.println(":"+it.next());
        	}
		*/
      }catch(ParseException pe){
  		
    	  log.info("Parsing Error"+pe);
    	  log.info("at postion"+pe.getPosition());
         
       }	
	
	
	
	return token;
	}

	
	public String decodeJWToken(String token) throws UnsupportedEncodingException
	{
	String returndata=null;	
	log.debug("Start to decode token.");
		
	String[] jwtParts = token.split("\\.");
	log.debug("Split tokens");
	log.debug("part 0:"+jwtParts[0]);
	log.debug("part 1:"+jwtParts[1]);
	log.debug("part 2:"+jwtParts[2]);
	
	
	//byte[] bytes = "Hello, World!".getBytes("UTF-8");
	//String encoded = Base64.getEncoder().encodeToString(bytes);
	//returns just the data field of the token
	byte[] decoded = Base64.getUrlDecoder().decode(jwtParts[1].trim());
	log.debug("decode Base64");
	String data=new String(decoded);
	log.debug("The clear datapart is:"+data);
	//data are a token in itself so return the data field of it
	String data2 = getTokenPart(data,"data");
	log.debug("The data part of it is:"+data2);
	//data are base64 encoded
	byte[] cleardata = Base64.getUrlDecoder().decode(data2);
	log.debug("decode Base64 again");
	
	returndata = new String(cleardata);
				
	return returndata;
	}
	
}
