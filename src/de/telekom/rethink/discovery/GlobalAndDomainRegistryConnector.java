package de.telekom.rethink.discovery;

import java.io.IOException;
import java.io.InputStream;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;

import com.eclipsesource.json.Json;
import com.eclipsesource.json.JsonArray;
import com.eclipsesource.json.JsonObject;
import com.eclipsesource.json.JsonValue;
import com.eclipsesource.json.WriterConfig;


public class GlobalAndDomainRegistryConnector {

FormularHelper helper;
HttpServletRequest request;	
static Logger log = Logger.getLogger(GlobalAndDomainRegistryConnector.class);	

	public GlobalAndDomainRegistryConnector(HttpServletRequest request, FormularHelper helper) {
		this.request= request; 
		this.helper = helper;	
	}
	
	public String getRawAnswerOfGlobalRegistry(String guid)
	{
	log.info("GUID found and going to check: "+guid);
	return helper.callURL(request.getSession().getServletContext().getInitParameter("GlobalRegistryEndpoint"),guid);	
	}	
	
	public String getClearAnswerOfGlobalRegistry(String rawAnswer) throws IOException
	{
	String valuepart = getValuePartOfGlobalRegistryAnswer(rawAnswer);
	String clearPart = helper.decodeJWToken(valuepart);
	log.info("Global Registry answer: "+clearPart);
	return clearPart;
	}
	
	
	public String getRawAnswerFromDomainRegistry(String domainRegAddress,String encodedUID)
	{
	String completeURL = domainRegAddress+"/hyperty/user/"+encodedUID; 
	String rawAnswer = helper.callSecureURL(domainRegAddress, "/hyperty/user/"+encodedUID);
	//String rawAnswer ="{\"hyperty://rethink-dev.tlabscloud.com/d2aad579-a213-4400-9e24-af2b23dbaed8\":{\"resources\":[\"audio\",\"video\"],\"dataSchemes\":[\"connection\"],\"descriptor\":\"hyperty-catalogue://catalogue.rethink-dev.tlabscloud.com/.well-known/hyperty/DTWebRTC\",\"startingTime\":\"2016-09-20T07:31:39Z\",\"hypertyID\":\"hyperty://rethink-dev.tlabscloud.com/d2aad579-a213-4400-9e24-af2b23dbaed8\",\"userID\":\"user://gmail.com/ingo.friese\",\"lastModified\":\"2016-09-20T09:20:45Z\",\"expires\":3600},\"hyperty://rethink-dev.tlabscloud.com/b71cd853-f657-4b6a-919b-07916bd3d50e\":{\"resources\":[\"audio\",\"video\"],\"dataSchemes\":[\"connection\"],\"descriptor\":\"hyperty-catalogue://catalogue.rethink-dev.tlabscloud.com/.well-known/hyperty/DTWebRTC\",\"startingTime\":\"2016-09-20T09:23:32Z\",\"hypertyID\":\"hyperty://rethink-dev.tlabscloud.com/b71cd853-f657-4b6a-919b-07916bd3d50e\",\"userID\":\"user://gmail.com/ingo.friese\",\"lastModified\":\"2016-09-20T09:23:32Z\",\"expires\":3600}}";
	//String rawAnswer = "{\"message\" : \"not found\"}";
	log.info("call:"+completeURL );
	return rawAnswer;
	}
	
	public boolean GUIDexists(String rawAnswer)
	{
	boolean returnValue = false;
	
	log.debug("GUID Check für "+rawAnswer);
	
	
	if(getDescriptionPartOfGlobalRegistryAnswer(rawAnswer).equals("OK"))
		{
		returnValue = true;
		log.info("GUID is valid.");
		}
	else
		{
		log.info("GUID is not valid or active.");
		}
		
	return returnValue;
	}
	
	public List saveGetCurrentHypertiesFromGlobalAndDomainRegistry(String rawAnswer, boolean APICall) throws IOException
	{
	List hypertyList = new ArrayList();
		
	if(GUIDexists(rawAnswer))
			{
		    hypertyList =  getCurrentHypertiesFromGlobalAndDomainRegistry(rawAnswer,APICall); 
			}
	return hypertyList;		
	}
	
	public List getCurrentHypertiesFromGlobalAndDomainRegistry(String rawAnswer, boolean APICall) throws IOException
	{
	//WebUI-Call provides Provider instaed of domain and a different "hyperty=" before the Hyperty URL in order to start a client direct from the website
		//API-call provides a more clean result
	log.debug("Methodcall;getCurrentHypertiesFromGlobalAndDomainRegistry");
	
	List listOfCurrentHyperties = new ArrayList();	
	//int numberOfHypertiesRunning = 0;
		
	String valuepart = getValuePartOfGlobalRegistryAnswer(rawAnswer);
	String uncryptedpayload = helper.decodeJWToken(valuepart);
	
	
	if(log.isDebugEnabled())
	{
		JsonValue globalAnswerInJSON = Json.parse(uncryptedpayload);
		String prettyJsonGlobalAnswer = globalAnswerInJSON.toString(WriterConfig.PRETTY_PRINT);
		log.debug(prettyJsonGlobalAnswer);					
	}
	
	
	log.info("Global Registry answer for this: "+uncryptedpayload);
		
	JsonObject object = Json.parse(uncryptedpayload).asObject();
	JsonArray arrayOfuserIds = object.get("userIDs").asArray();
		
	log.debug("Number of userID entries in the GR: "+arrayOfuserIds.size());
		
	for(int i=0;i<arrayOfuserIds.size();i++)
			{
			JsonObject SingleUserID = arrayOfuserIds.get(i).asObject();
			String domain = SingleUserID.getString("domain", "empty domain");
			String domainRegAddress = getServerAddressOfDomainRegistry(domain);		
			String uID = SingleUserID.getString("uID", "empty uID");
			String idUrlEncoded = URLEncoder.encode(uID,"UTF-8");
			
			log.debug("Going to contact DomainRegistry "+domain+" with address: "+domainRegAddress);
			log.debug("For userID "+uID+" encrypted to "+idUrlEncoded);
			
			String domainAnswer = getRawAnswerFromDomainRegistry(domainRegAddress,idUrlEncoded);					
			//JsonObject hypertyURLs = Json.parse(domainAnswer).asObject();
			JsonObject domainAnswerAsJSONObject = Json.parse(domainAnswer).asObject();
					
			if(log.isDebugEnabled())
				{
				JsonValue domainAnswerInJSON = Json.parse(domainAnswer);
				String prettyJsonDomainAnswer = domainAnswerInJSON.toString(WriterConfig.PRETTY_PRINT);
				log.debug(prettyJsonDomainAnswer);					
				}
										
			List tagNamesOfDoamainAnswer = domainAnswerAsJSONObject.names();
			Iterator iteratorOfAnswerTags = tagNamesOfDoamainAnswer.iterator();
			
				
			
				while(iteratorOfAnswerTags.hasNext()) {
				
					String tagORHypertyURL = (String) iteratorOfAnswerTags.next();
					//there is only one sort of tag and this is called hyperty URL according to the current format of the dReg answer format
					String listEntry = "?hyperty="+URLEncoder.encode(tagORHypertyURL,"UTF-8")+"&uid="+uID; 
					         
		       
		        	   

		         if(tagORHypertyURL.equals("message"))
		         {
		        	 log.debug("nothing to return because message not found");
		        	//if the tag is message it means message not found
		        	//no hyperty found and thus nothing to add	 
		         }
		         else
		         { 
		        	 if(APICall)
						{
						//in case of API call give the complete domain registry answer back
						listOfCurrentHyperties.add(domainAnswer);
						log.debug("Add the complete Domain Registry Answer to the list.");
						}
					else
						{
						log.debug("we have a result"); 
						log.debug("Going to extract all relevant data and put it to the list form the WebGUI.");
		         
		        	 
						JsonObject details = domainAnswerAsJSONObject.get(tagORHypertyURL).asObject();
						JsonArray supportedresources = details.get("resources").asArray();
		            
						for(int j=0;j<supportedresources.size();j++)
		         			{
							String resource = supportedresources.get(j).asString();
			        	
			        		if(resource.equals("chat"))
			        			listOfCurrentHyperties.add(getMyChatClientAddressToCommunicateWithHyperties()+listEntry+"#rp_chat#CHAT#"+getProviderNameForADomain(domain));
				        	else if(resource.equals("voice"))
				        		listOfCurrentHyperties.add(getMyVoiceClientAddressToCommunicateWithHyperties()+listEntry+"#rp_call#CALL#"+getProviderNameForADomain(domain));
				        	else if(resource.equals("video"))
				        		listOfCurrentHyperties.add(getMyVideoClientAddressToCommunicateWithHyperties()+listEntry+"#rp_video#VIDEO#"+getProviderNameForADomain(domain));
			        		log.debug(" resources : "+resource);
		         			}
	         
		        	 	JsonArray supportedDataSchemes = details.get("dataSchemes").asArray();
		         
		       
		        	 	for(int k=0;k<supportedDataSchemes.size();k++)
			         		{
		        	 		String scheme = supportedDataSchemes.get(k).asString();
			        
		        	 		log.debug(" data schemes : "+scheme);
			         		}
							}
						}
		         };
		    	
			}return listOfCurrentHyperties; 
	}
	
	
	
	public String getServerAddressOfDomainRegistry(String domain) throws IOException
	{		
	InputStream inputStream = this.getClass().getClassLoader().getResourceAsStream("de/telekom/rethink/discovery/rethinkDomainAddresses.properties");

	Properties properties = new Properties();
	properties.load(inputStream);
	String propValue = properties.getProperty(domain);
	
	log.info("Domain address for "+domain+" is  " + propValue);	
	return propValue;
	}
	
	
	private String getProviderNameForADomain(String domain) throws IOException
	{		
	InputStream inputStream = this.getClass().getClassLoader().getResourceAsStream("de/telekom/rethink/discovery/rethinkProviderNames.properties");

	Properties properties = new Properties();
	properties.load(inputStream);
	String propValue = properties.getProperty(domain);
	
	return propValue;
	}
	
	//this should be a function of the GR
	private String getMyVoiceClientAddressToCommunicateWithHyperties()
	{
	return "https://rethink-dev.tlabscloud.com";
	}
	
	private String getMyVideoClientAddressToCommunicateWithHyperties()
	{
	return "https://rethink-dev.tlabscloud.com";
	}
	
	private String getMyChatClientAddressToCommunicateWithHyperties()
	{
	return "https://rethink.tlabscloud.com";
	}
	

	private String getDescriptionPartOfGlobalRegistryAnswer(String rawAnswer)
	{
	return helper.getDataPartOfJSONObject(rawAnswer,"Description");	
	}
	

	private String getValuePartOfGlobalRegistryAnswer(String rawAnswer)
	{
	return helper.getDataPartOfJSONObject(rawAnswer,"Value");	
	}
	
	

}


