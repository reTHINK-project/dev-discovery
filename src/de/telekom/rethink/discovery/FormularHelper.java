package de.telekom.rethink.discovery;

import javax.servlet.http.HttpServletRequest;
import org.apache.solr.client.solrj.SolrServerException;

import org.apache.log4j.Logger;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;
import java.sql.*;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class FormularHelper {

HttpServletRequest request;
String emtyreturnstring ="empty value";


SolrIndexHelper solrHelper;
Neo4jHelper neoHelper;
DBHelper dbHelper;
RESTClient restClient;
JSONHelper jsonHelper;

static Logger log = Logger.getLogger(FormularHelper.class);	


		public FormularHelper(HttpServletRequest request)
		{
			this.request=request;
			solrHelper = new SolrIndexHelper(request,this);	
			neoHelper = new Neo4jHelper(request,this);
			dbHelper = new DBHelper(request,this);
			restClient = new RESTClient();
			jsonHelper = new JSONHelper();
		}
		
		public String callURL(String URL, String path)
		{
		return	restClient.callURL(URL, path);
		}
		
		public String cleanUpString(String rawstring)
		{
		String returnString="";	
			
		if(rawstring.length()>2)
		{
			returnString= rawstring.substring(1, rawstring.length()-1);
		}	
		log.debug("Cleanup String "+rawstring+" to "+ returnString);
		
		return returnString;
		}
		
		public void logout(int userID)
		{
			log.info("User "+userID+" logged out.");
		}
		
		public boolean rethinkIDisempty(String rethinkID)
		{
			boolean returnwert=false;
			
			if(rethinkID.equals(""))
				returnwert=true;
			
			log.debug("No reTHINK ID available.");
			
			return returnwert;
		}
		
					
		public String filteredHeadline()
		{
		String returnString = request.getParameter("headline").trim();
		
		log.debug("The headline is: "+returnString);
		
		if(returnString.equals(""))
			log.debug("The headline is empty.");
	
		return returnString;
		}
		
		
		public String filteredDescription()
		{
		String returnString =request.getParameter("description").trim();
		
		log.debug("The description is: "+returnString);
		
		if(returnString.equals(""))
			log.debug("The description is empty.");
		
		return returnString;
		}
		
		
		public String filteredHashtags()
		{
		String returnString =request.getParameter("hashtags").trim();
		
		log.debug("Hashtags are: "+returnString);
		
		if(returnString.equals(""))
			log.debug("Hashtags are empty.");
		
		return returnString;
		}
		
		
		public String filteredPassword()
		{
		String returnString = request.getParameter("password").trim();
		
		log.debug("The password is: "+returnString);
		
		if(returnString.equals(""))
			log.debug("Password is empty.");
		
		return returnString;
		}
		
		
		public String filteredUsername()
		{
		String returnString =request.getParameter("username");
		
		log.debug("The username is: "+returnString);
		
		if(returnString.equals(""))
			log.debug("Username is empty.");
		
		return returnString;
		}
		
		
		public String filteredVisibility()
		{
		String returnString = request.getParameter("visibility");
		 log.debug("The visibility is: "+returnString);
		
		if(returnString.equals(""))
			log.debug("Visibility is empty.");
		
		return returnString;
		}
		
		
		public String filteredContacts()
		{
		String returnString = request.getParameter("contacts").trim();
		
		log.debug("The contacts are: "+returnString);
		
		if(returnString.equals(""))
			log.debug("Contacts are empty.");	
		
		return returnString;
		}
		
		public String filteredRethinkID()
		{
		String returnString = request.getParameter("rethinkID").trim();
		
		log.debug("The reTHINK ID is: "+returnString);
		
		if(returnString.equals(""))
			log.debug("The reTHINK ID is empty.");
		
		return returnString;
		}
		
		
		public boolean validateInput(String headline, String description, String username)
		{
		boolean returnvalue = true;
		
		if(headline.equals(""))
			returnvalue=false;
		else if(description.equals(""))
			returnvalue=false;
		else if(username.equals(""))
			returnvalue=false;

		if(returnvalue)	
			log.debug("Headline, Description and Username are not null");
		else
			{
			log.debug("Either Headline, Description and Username are null");
			log.warn("Either Headline, Description and Username are null");
			}
		
		return returnvalue;	
		}
		
		public void log(String logtext)
		{
			log.info(logtext);
		}
		
		
		public String getTokenPart(String request, String field)
		{
		return jsonHelper.getTokenPart(request, field);
		}
		
		public String decodeJWToken(String token) throws UnsupportedEncodingException
		{
		return jsonHelper.decodeJWToken(token);
		}
				
		/************************
		 * 
		 * 
		 * All these methods handle neo4j access
		 * 
		 */
			
		public boolean favoriteAlreadyExist(int userID,String docID)
		{
			return neoHelper.favoriteAlreadyExist(userID, docID);
		}	
		
		public List<String> listFavorites(int userID)
		{
			return neoHelper.listFavorites(userID);
		}
		
		public void createProfileNode(int docID,String visibility,String username,int userID)
		{
			neoHelper.createProfileNode(docID, visibility, username, userID);
		}
		
		public void updateProfileNode(int docID,String visibility)
		{
		 neoHelper.updateProfileNode(docID, visibility);
		}
		
		public void createUserNode(int userID,String username)
		{
			neoHelper.createUserNode(userID, username);
		}
		
		public void createFavoriteRelation(int userID,String docID)
		{
			neoHelper.createFavoriteRelation(userID, docID);
		}
		

		public void removeAllFollower(String docID)
		{
			neoHelper.removeAllFollower(docID);
		}
		
		
		public void removeAllFavorites(int userID)
		{
			neoHelper.removeAllFavorites(userID);
		}
		
		public void deleteUserNode(int userID)
		{
			neoHelper.deleteUserNode(userID);
		}
		
		public void deleteFavoriteRelation(int userID,String profile)
		{
			neoHelper.deleteFavoriteRelation(userID, profile);
		}	
		
		public void deleteProfileNode(int docID)
		{
			neoHelper.deleteProfileNode(docID);
		}	
		
		public String getVisibility(int docID)
		{
		return neoHelper.getVisibility(docID);
		}
		
		String getOwnerID(String docID)
		{
			return neoHelper.getOwnerID(docID);
		}
		
		public String getVisibility(String docID)
		{
			return neoHelper.getVisibility(docID);
		}
		
		
		
		/************************************************************************
		 * 
	 * 
		 * All these methods handle Solr access
		 * 
		 */
		
		public String doJSONSearch(String searchString, int userID) throws SolrServerException, IOException
		{
		return	solrHelper.doJSONSearch(searchString, userID);
		}
		
		public Vector<Hashtable<String,String>> doPolicyEnabledSearch(String searchString,int userID) throws SolrServerException, IOException
		{
		return solrHelper.doPolicyEnabledSearch(searchString, userID);
		}
		
		public void indexProfile(int docID, String headline, String description, String hashtags, String contacts, String hasrethinkID, String rethinkID) throws SolrServerException, IOException
		{
		solrHelper.indexProfile(docID, headline, description, hashtags, contacts, hasrethinkID, rethinkID);
		}	
		
		public void deleteProfileFromIndex(int docID) throws SolrServerException, IOException
		{
		solrHelper.deleteProfileFromIndex(docID);
		}
		
		public Hashtable<String,String> getProfileFromSolr(String docID) throws SolrServerException, IOException 
		{		
		return solrHelper.getProfileFromSolr(docID);	
		}
		

		
		/**************************************************************************
		 * 
		 * @param docID
		 * @throws ClassNotFoundException
		 * @throws SQLException
		 * 
		 * All these methods handle SQL access
		 * 
		 */
		
		
		public List<Integer> getProfileNumbers(int userID) throws ClassNotFoundException, SQLException
		{
		return dbHelper.getProfileNumbers(userID);
		}
		
		public HashMap<String,String> getUserProfile(int docID) throws ClassNotFoundException, SQLException
		{
		return dbHelper.getUserProfile(docID);
		}
		
		public HashMap<String,String> getUserProfile(String docID) throws ClassNotFoundException, SQLException
		{
		return dbHelper.getUserProfile(docID);
		}
		
		
		public int getProfileCount(int userID) throws ClassNotFoundException, SQLException
		{
		return dbHelper.getProfileCount(userID);	
		}
		
		public void deleteUser(int userID) throws ClassNotFoundException, SQLException
		{
		dbHelper.deleteUser(userID);	
		}
		
		public void deleteProfile(int docID) throws ClassNotFoundException, SQLException
		{
		dbHelper.deleteProfile(docID);
		}
		
		public int loginUser(String username, String password) throws ClassNotFoundException, SQLException 
		{
		return dbHelper.loginUser(username, password);
		}
		
		
		public boolean userAlreadyExistCheck(String username) throws ClassNotFoundException, SQLException 
		{
		return dbHelper.userAlreadyExistCheck(username);
		}
		
		public int createUser(String username, String password) throws ClassNotFoundException, SQLException, NoSuchAlgorithmException, UnsupportedEncodingException
		{
		return dbHelper.createUser(username, password);
		}
		
		public int createProfile(int userID, String headline,String description,String hashtags,String contacts, String rethinkID)throws ClassNotFoundException, SQLException
		{
		return dbHelper.createProfile(userID, headline, description, hashtags, contacts, rethinkID);
		}
		
		public void updateProfile(int docID,int userID,String headline, String description, String hashtags, String contacts, String rethinkID) throws ClassNotFoundException, SQLException
		{
		dbHelper.updateProfile(docID, userID, headline, description, hashtags, contacts, rethinkID);	
		}
		
		
		public String analyzeContactString(String tokenpart)
		{
		String returnString="#";	
		
		
		if(isValidEmailAddress(tokenpart))
				{
				returnString = "mailto:"+tokenpart;
				}
		else if (isValidURLAddress(tokenpart))
		{
			    returnString = tokenpart;
		}
		return returnString;
		}
		
		private boolean isValidEmailAddress(String email) {
	           String ePattern = "^[\\w!#$%&'*+/=?`{|}~^-]+(?:\\.[\\w!#$%&'*+/=?`{|}~^-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,6}$";
	           java.util.regex.Pattern p = java.util.regex.Pattern.compile(ePattern);
	           java.util.regex.Matcher m = p.matcher(email);
	           return m.matches();
	    }
		
		private boolean isValidURLAddress(String urlstring) {

	           //String elPattern =  "^(http://|https://)?(www.)?([a-zA-Z0-9]+).[a-zA-Z0-9]*.[a-z]{3}.?([a-z]+)?$​";
			//String REGEX =  "^(http://|https://)?(www.)?([a-zA-Z0-9]+).[a-zA-Z0-9]*.[a-z]{3}.?([a-z]+)?$​";
			String REGEX ="[wwww.]?[.]";
	          // String INPUT ="telekom";
				
			   Pattern p2 = Pattern.compile(REGEX);
	           Matcher m2 = p2.matcher(urlstring);
	           //log.info("URL addresse "+urlstring+" matchted "+ m2.find()+ " mit "+REGEX);
	           return m2.find();
	    }
		
}
