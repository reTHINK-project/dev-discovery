/********************************************************************************************************************
 * Deutsche Telekom Laboratories																					*
 * Copyright (c) 2016 European Project reTHINK																		*
 * 																													*
 ********************************************************************************************************************/

package de.telekom.rethink.discovery;

import java.io.IOException;
import java.io.StringReader;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Hashtable;
import java.util.Iterator;

import org.apache.log4j.Logger;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;

import com.eclipsesource.json.*;

public class JSONHelper {
	
	static Logger log = Logger.getLogger(JSONHelper.class);			
	
	
	
	
	public String getDataPartOfJSONObject(String token,String keyword)
	{
	JsonObject object = Json.parse(token).asObject();
	String value = object.get(keyword).asString();
		
	return value;
	}
	
	
	
	

	
	public void analyseJSONString(String jsonString)
	{
	log.debug("Analyze JSON String: "+jsonString);
	
	 try{
		 	JSONParser parser = new JSONParser();
	        Object obj = parser.parse(jsonString.trim());
	        JSONObject jsonObject = (JSONObject)obj;
	        Iterator keys= jsonObject.keySet().iterator();
	        
	        while(keys.hasNext())
	        	{
	        	String containedField = keys.next().toString();	
	     
	        	log.debug("-------------->"+containedField);
	        	Object value = jsonObject.get(containedField);
	        	analyseJSONString(value.toString());
	        }
	 }catch(ParseException pe){
		 System.err.println(pe);
	 }
		
	}
	
	public String decodeJWToken(String token) throws IOException
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
	//String data2 = getTokenPart(data,"data");
	String data2 = getDataPartOfJSONObject(data,"data");
	log.debug("The data part of it is:"+data2);
	//data are base64 encoded
	byte[] cleardata = Base64.getUrlDecoder().decode(data2);
	log.debug("decode Base64 again");
	
	returndata = new String(cleardata);
				
	return returndata;
	}
	
	
	
	
	/**
     * Handle an Object. Consume the first token which is BEGIN_OBJECT. Within
     * the Object there could be array or non array tokens. We write handler
     * methods for both. Noe the peek() method. It is used to find out the type
     * of the next token without actually consuming it.
     *
     * @param reader
     * @throws IOException
     */
    private static void handleObject(JsonReader reader) throws IOException
    {
        reader.beginObject();
        while (reader.hasNext()) {
            JsonToken token = reader.peek();
            if (token.equals(JsonToken.BEGIN_ARRAY))
                handleArray(reader);
            else if (token.equals(JsonToken.END_OBJECT)) {
                reader.endObject();
                return;
            } else
                handleNonArrayToken(reader, token);
        }
 
    }
 
    /**
     * Handle a json array. The first token would be JsonToken.BEGIN_ARRAY.
     * Arrays may contain objects or primitives.
     *
     * @param reader
     * @throws IOException
     */
    public static void handleArray(JsonReader reader) throws IOException
    {
        reader.beginArray();
        while (true) {
            JsonToken token = reader.peek();
            if (token.equals(JsonToken.END_ARRAY)) {
                reader.endArray();
                break;
            } else if (token.equals(JsonToken.BEGIN_OBJECT)) {
                handleObject(reader);
            } else if (token.equals(JsonToken.END_OBJECT)) {
                reader.endObject();
            } else
                handleNonArrayToken(reader, token);
        }
    }
 
    /**
     * Handle non array non object tokens
     *
     * @param reader
     * @param token
     * @throws IOException
     */
    public static void handleNonArrayToken(JsonReader reader, JsonToken token) throws IOException
    {
        if (token.equals(JsonToken.NAME))
            System.out.println(reader.nextName());
        else if (token.equals(JsonToken.STRING))
            System.out.println(reader.nextString());
        else if (token.equals(JsonToken.NUMBER))
            System.out.println(reader.nextDouble());
        else
            reader.skipValue();
    }	
	
}
