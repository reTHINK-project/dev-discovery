/********************************************************************************************************************
 * Deutsche Telekom Laboratories																					*
 * Copyright (c) 2016 European Project reTHINK																		*
 * 																													*
 ********************************************************************************************************************/

package de.telekom.rethink.discovery;

import java.io.UnsupportedEncodingException;
import java.util.Base64;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class JSONHelper {
	
	
	
	
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
  		
          System.out.println("position: " + pe.getPosition());
          System.out.println(pe);
       }	
	
	
	
	return token;
	}

	
	public String decodeJWToken(String token) throws UnsupportedEncodingException
	{
	String returndata=null;	
	
		
	String[] jwtParts = token.split("\\.");
	//byte[] bytes = "Hello, World!".getBytes("UTF-8");
	//String encoded = Base64.getEncoder().encodeToString(bytes);
	//returns just the data field of the token
	byte[] decoded = Base64.getDecoder().decode(jwtParts[1]);
	String data=new String(decoded);
	//data are a token in itself so return the data field of it
	String data2 = getTokenPart(data,"data");
	//data are base64 encoded
	byte[] cleardata = Base64.getDecoder().decode(data2);
	
	returndata = new String(cleardata);
				
	return returndata;
	}
	
}
