package de.telekom.rethink.discovery;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.neo4j.driver.v1.AuthTokens;
import org.neo4j.driver.v1.GraphDatabase;

public class Neo4jHelper {
	
String neo4jURL;
String neo4jDBname;
String neo4jDBstring;
FormularHelper formularHelper;	
	
static Logger log = Logger.getLogger(Neo4jHelper.class);		


	public Neo4jHelper(HttpServletRequest request, FormularHelper formularHelper) 
	{
		this.formularHelper=formularHelper;	
		this.neo4jURL =  request.getSession().getServletContext().getInitParameter("neo4jURL");
		this.neo4jDBname =  request.getSession().getServletContext().getInitParameter("neo4jDBname");
		this.neo4jDBstring =  request.getSession().getServletContext().getInitParameter("neo4jDBstring");
		
	}

	public boolean favoriteAlreadyExist(int userID,String docID)
	{
		boolean returnwert=false;
		
		org.neo4j.driver.v1.Driver driver = GraphDatabase.driver(neo4jURL,AuthTokens.basic(neo4jDBname,neo4jDBstring));
		org.neo4j.driver.v1.Session dbsession =driver.session();

		org.neo4j.driver.v1.StatementResult result = dbsession.run("MATCH (u:user {userID:'"+userID+"'})-[r:favors]->(p:profile {docID:'"+docID+"'}) return sign(COUNT(r)) AS c");
		
		while(result.hasNext())
		{
		org.neo4j.driver.v1.Record record = result.next();
		int count = record.get("c").asInt();
		
		if(count>0)
			{
			log.debug("The favorite already exists");
		    returnwert=true;  
			}
			
		}
		dbsession.close();
		driver.close();
		//log.warn("return wert ist"+returnwert);
		
		return returnwert;
	}
	
	
	public List<String> listFavorites(int userID)
	{
	log.debug("List all Favorites");	
		
		List<String> returnlist = new ArrayList<String>();	
		
		org.neo4j.driver.v1.Driver driver = GraphDatabase.driver(neo4jURL,AuthTokens.basic(neo4jDBname,neo4jDBstring));
		org.neo4j.driver.v1.Session dbsession =driver.session();

		org.neo4j.driver.v1.StatementResult result = dbsession.run("match (u:user{userID:'"+userID+"'})-[r:favors]->(p:profile) return p.docID AS docID, collect(u.userID) ");
		
		while(result.hasNext())
		{
		org.neo4j.driver.v1.Record record = result.next();
		String docID = record.get("docID").asString();
		returnlist.add(docID);
		log.debug("Favorites docID: "+docID);
		}
		dbsession.close();
		driver.close();
		return returnlist;
	}
	
	
	public void createProfileNode(int docID,String visibility,String username,int userID)
	{
		//org.neo4j.driver.v1.Driver driver = GraphDatabase.driver("bolt://localhost",AuthTokens.basic("neo4j","rethink"));
		org.neo4j.driver.v1.Driver driver = GraphDatabase.driver(neo4jURL,AuthTokens.basic(neo4jDBname,neo4jDBstring));
		org.neo4j.driver.v1.Session dbsession =driver.session();

		dbsession.run("CREATE (p:profile {docID:'"+docID+"', visibility:'"+visibility+"', ownerID:'"+userID+"', nodeTyp:'profile' , owner:'"+username+"'})");
			
		dbsession.close();
		driver.close();
		log.info("ProfileNode for docID "+docID+" created in Neo4j.");
		}
	
	
	public void updateProfileNode(int docID,String visibility)
	{
		org.neo4j.driver.v1.Driver driver = GraphDatabase.driver(neo4jURL,AuthTokens.basic(neo4jDBname,neo4jDBstring));
		org.neo4j.driver.v1.Session dbsession =driver.session();

		dbsession.run("MATCH (p:profile {docID:'"+docID+"'}) SET p.visibility='"+visibility+"'");
		
		dbsession.close();
		driver.close();
		
		log.info("Changed visibility to  >> "+visibility+" << for node "+docID+" in Neo4j.");
	}
	
	
	public void createUserNode(int userID,String username)
	{
		org.neo4j.driver.v1.Driver driver = GraphDatabase.driver(neo4jURL,AuthTokens.basic(neo4jDBname,neo4jDBstring));
		org.neo4j.driver.v1.Session dbsession =driver.session();

		dbsession.run("CREATE (u:user {userID:'"+userID+"', userName:'"+username+"', nodeType:'user'})");
		dbsession.close();
		driver.close();	
		
		log.info("UserNode "+userID+" for "+username+" created in Neo4j.");				
	}
	
	public void createFavoriteRelation(int userID,String docID)
	{
		org.neo4j.driver.v1.Driver driver = GraphDatabase.driver(neo4jURL,AuthTokens.basic(neo4jDBname,neo4jDBstring));
		org.neo4j.driver.v1.Session dbsession =driver.session();

		dbsession.run("MATCH (u:user {userID:'"+userID+"'}),(p:profile{docID:'"+docID+"'}) CREATE (u)-[:favors] -> (p)");

		dbsession.close();
		driver.close();	
		
		log.info("User "+userID+" favors "+docID);
	}
	
	public void removeAllFollower(String docID)
	{
		org.neo4j.driver.v1.Driver driver = GraphDatabase.driver(neo4jURL,AuthTokens.basic(neo4jDBname,neo4jDBstring));
		org.neo4j.driver.v1.Session dbsession =driver.session();

		dbsession.run("MATCH ()-[r:favors]-(p:profile {docID:'"+docID+"'}) delete r");
		
		dbsession.close();
		driver.close();	
		log.debug("Remove all Follower from "+docID);
	}
	
	public void removeAllFavorites(int userID)
	{
		org.neo4j.driver.v1.Driver driver = GraphDatabase.driver(neo4jURL,AuthTokens.basic(neo4jDBname,neo4jDBstring));
		org.neo4j.driver.v1.Session dbsession =driver.session();

		dbsession.run("MATCH (u:user {userID:'"+userID+"'})-[r:favors]-() delete r");
		
		dbsession.close();
		driver.close();	
		log.debug("Remove all favorites from "+userID);
	}
	
	public void deleteUserNode(int userID)
	{
		org.neo4j.driver.v1.Driver driver = GraphDatabase.driver(neo4jURL,AuthTokens.basic(neo4jDBname,neo4jDBstring));
		org.neo4j.driver.v1.Session dbsession =driver.session();

		dbsession.run("MATCH (u) WHERE u.userID ='"+userID+"' DELETE u");

		dbsession.close();
		driver.close();
		log.info("UserNode "+userID+"was removed from Neo4j.");
		
	}
	
	
	public void deleteFavoriteRelation(int userID,String profile)
	{
		org.neo4j.driver.v1.Driver driver = GraphDatabase.driver(neo4jURL,AuthTokens.basic(neo4jDBname,neo4jDBstring));
		org.neo4j.driver.v1.Session dbsession =driver.session();

		dbsession.run("MATCH (u:user {userID:'"+userID+"'})-[r:favors]-(p:profile {docID:'"+profile+"'}) delete r");
				
		dbsession.close();
		driver.close();
		log.info("User "+userID+" removes "+profile+" as favorite.");
	}	
	
	public void deleteProfileNode(int docID)
	{
		org.neo4j.driver.v1.Driver driver = GraphDatabase.driver(neo4jURL,AuthTokens.basic(neo4jDBname,neo4jDBstring));
		org.neo4j.driver.v1.Session dbsession =driver.session();

		dbsession.run("MATCH (p) WHERE p.docID ='"+docID+"' DELETE p");

		dbsession.close();
		driver.close();
		log.info("ProfileNode "+docID+" was removed from Neo4j.");
	}	
	
	public String getVisibility(int docID)
	{
	String visibility="";
	
		org.neo4j.driver.v1.Driver driver = GraphDatabase.driver(neo4jURL,AuthTokens.basic(neo4jDBname,neo4jDBstring));
		org.neo4j.driver.v1.Session dbsession =driver.session();

		org.neo4j.driver.v1.StatementResult result = dbsession.run("Match (p:profile) WHERE p.docID ='"+docID+"' RETURN p.visibility AS visibility");
		
		//look in neo4j for visibility
		while(result.hasNext())
		{
		org.neo4j.driver.v1.Record record = result.next();
		visibility=record.get("visibility").asString();
		}
		
		dbsession.close();
		driver.close();
		log.debug("Return visibility [int docID] "+visibility);
		return visibility;
	}
	
	public String getVisibility(String docID)
	{
	String visibility="";
	
		org.neo4j.driver.v1.Driver driver = GraphDatabase.driver(neo4jURL,AuthTokens.basic(neo4jDBname,neo4jDBstring));
		org.neo4j.driver.v1.Session dbsession =driver.session();

		org.neo4j.driver.v1.StatementResult result = dbsession.run("Match (p:profile) WHERE p.docID ='"+docID+"' RETURN p.visibility AS visibility");
		
		//look in neo4j for visibility
		while(result.hasNext())
		{
		org.neo4j.driver.v1.Record record = result.next();
		visibility=record.get("visibility").asString();
		}
		
		dbsession.close();
		driver.close();
		log.debug("Return visibility [String docID]"+visibility);
		return visibility;
	}
	
	String getOwnerID(String docID)
	{
		String returnString="";
		
		org.neo4j.driver.v1.Driver driver = GraphDatabase.driver(neo4jURL,AuthTokens.basic(neo4jDBname,neo4jDBstring));
		org.neo4j.driver.v1.Session dbsession =driver.session();

		org.neo4j.driver.v1.StatementResult result = dbsession.run("MATCH (p) WHERE p.docID ='"+docID+"' RETURN p.ownerID AS ownerID");

		while(result.hasNext())
		{
		org.neo4j.driver.v1.Record record = result.next();
		returnString=record.get("ownerID").asString();
		}
		
		dbsession.close();
		driver.close();
		
		
		return returnString;
	}
	
	
}
