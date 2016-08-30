/********************************************************************************************************************
 * Deutsche Telekom Laboratories																					*
 * Copyright (c) 2016 European Project reTHINK																		*
 * 																													*
 ********************************************************************************************************************/

package de.telekom.rethink.discovery;

import java.io.IOException;
import java.util.Collection;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;

import javax.servlet.http.HttpServletRequest;
import de.telekom.rethink.discovery.FormularHelper;

import org.apache.log4j.Logger;
import org.apache.solr.client.solrj.SolrClient;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.HttpSolrClient;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;
import org.apache.solr.common.SolrInputDocument;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

public class SolrIndexHelper {
	
static Logger log = Logger.getLogger(SolrIndexHelper.class);		
	
String instanceID; 
String solrIndexURL;
FormularHelper formularHelper;

	public SolrIndexHelper(HttpServletRequest request, FormularHelper formularHelper)
	{	
	this.formularHelper=formularHelper;	
	this.solrIndexURL = request.getSession().getServletContext().getInitParameter("solrIndexURL");
	this.instanceID = request.getSession().getServletContext().getInitParameter("instanceID");
	}

	
	
	/************************************************************************
	 * 
	 * @param docID
	 * @throws SolrServerException
	 * @throws IOException
	 * 
	 * All these methods handle Solr access
	 * 
	 */
	
	public Hashtable<String,String> getProfileFromSolr(String docID) throws SolrServerException, IOException 
	{				
	Hashtable<String,String> resultTable = new Hashtable<String,String>();
			
	SolrClient solr = new HttpSolrClient(solrIndexURL);
	SolrQuery query= new SolrQuery();
		
	query.setFields("docID","headline","description","hashtags","contacts","hasrethinkID","rethinkID","instanceID");
	
	query.set("q", "docID:"+docID.trim());
	
	QueryResponse qr= solr.query(query);
	
		
	SolrDocumentList dlist=qr.getResults();
		
	
	Iterator<SolrDocument> iter = dlist.iterator();
	
		while (iter.hasNext())
		{
		SolrDocument doc=(SolrDocument)iter.next();
		
		try{
	
		resultTable.put("headline",doc.getFieldValue("headline").toString());
		
			}catch(Exception ex)
				{
				resultTable.put("headline","[ ]");
				}	
		
		try{
		
		resultTable.put("docID",doc.getFieldValue("docID").toString());
	
			}catch(Exception ex)
				{
				resultTable.put("docID","[ ]");
				}	
		
		try{
	
			resultTable.put("contacts", doc.getFieldValue("contacts").toString());
		
			}catch(Exception ex)
				{
				resultTable.put("contacts","[ ]");
				}	
		

		try{
			resultTable.put("description", doc.getFieldValue("description").toString());
	
			}catch(Exception ex)
				{
				resultTable.put("description","[ ]");
				}	
		
		try{
		
			resultTable.put("hashtags",doc.getFieldValue("hashtags").toString());
		
			}catch(Exception ex)
				{
				resultTable.put("hashtags","[ ]");
				}		
			
		try{
			resultTable.put("hasrethinkID",doc.getFieldValue("hasrethinkID").toString());			
			}catch(Exception ex)
				{
				resultTable.put("hasrethinkID","[ ]");
				}				
		
		
		try{
			resultTable.put("rethinkID",doc.getFieldValue("rethinkID").toString());			
			}catch(Exception ex)
				{
				resultTable.put("rethinkID","[ ]");
				}				
		

		try{
			resultTable.put("instanceID",doc.getFieldValue("instanceID").toString());			
			}catch(Exception ex)
				{
				resultTable.put("instanceID","[ ]");
				}	
		
		}
						
	solr.close();
	return resultTable;	
	}
	

	@SuppressWarnings("unchecked")
	public String doJSONSearch(String searchString, int userID) throws SolrServerException, IOException
	{
	JSONObject outer=new JSONObject();
	JSONArray obj = new JSONArray();
	outer.put("responseCode", 201);
	outer.put("instanceID", instanceID);
	outer.put("searchString", searchString);
	int i=0;
	
	Vector<Hashtable<String,String>> vector = doPolicyEnabledSearch(searchString,userID);
	
	for(Enumeration<Hashtable<String,String>> el=vector.elements();el.hasMoreElements();)
	{
	    Hashtable<String,String> singleResult = el.nextElement();			     
	    
	    JSONObject singleObj =new JSONObject();
	    singleObj.put("headline", cleanUpString(singleResult.get("headline").toString()));
	    singleObj.put("description", cleanUpString(singleResult.get("description").toString()));
	    singleObj.put("hashtags",  cleanUpString(singleResult.get("hashtags").toString()));
	    singleObj.put("contacts",  cleanUpString(singleResult.get("contacts").toString()));
	    singleObj.put("hasrethinkID",  cleanUpString(singleResult.get("hasrethinkID").toString()));
	    singleObj.put("rethinkID",  cleanUpString(singleResult.get("rethinkID").toString()));
	    singleObj.put("instanceID",  cleanUpString(singleResult.get("instanceID").toString()));
	
	    singleObj.put("resultNo", i++);
	    obj.add(singleObj);
		}
	
	outer.put("results", obj);		
	return outer.toString();
	}
	
	
	public Vector<Hashtable<String,String>> doPolicyEnabledSearch(String searchString,int userID) throws SolrServerException, IOException
	{
	Vector<Hashtable<String,String>> returnVector = new Vector<Hashtable<String,String>>();
		
	Vector<Hashtable<String,String>> rawVector = doSearch(searchString);
	
	for(Enumeration<Hashtable<String,String>> el=rawVector.elements();el.hasMoreElements();)
	{
	    Hashtable<String,String> singleResult = el.nextElement();			     
   
		//get Visibility for doc ID
		String visibility= formularHelper.getVisibility(cleanUpString(singleResult.get("docID").toString()));
		log.debug("profile "+cleanUpString(singleResult.get("docID").toString())+" is visibile for "+visibility);
		
		
		if(visibility.equals("all"))
			{
			returnVector.add(singleResult);
			}
		else if(visibility.equals("rethink"))
		{
			if(userID>0)
				returnVector.add(singleResult);
		}
		else if(visibility.equals("favs"))
		{
			String profileowner	= formularHelper.getOwnerID(cleanUpString(singleResult.get("docID").toString()));
			
			List<String> favorites=formularHelper.listFavorites(new Integer(profileowner).intValue());
			Iterator<String> it= favorites.iterator();
			while(it.hasNext())
			{
				String favorite=it.next();	
				String favoriteOwner=formularHelper.getOwnerID(favorite);
				
				if(favoriteOwner.equals(""+userID))
						returnVector.add(singleResult);
			}
			
		}
	}	 
	log.debug("Search request for >> "+searchString+" << has result count of: "+returnVector.size()+" with policy");	
	log.info("SearchQuery: "+searchString);
	return returnVector;
	}
	
	
	private Vector<Hashtable<String,String>> doSearch(String searchString) throws SolrServerException, IOException 
	{				
	Vector<Hashtable<String,String>> returnVector = new Vector<Hashtable<String,String>>();
			
	SolrClient solr = new HttpSolrClient(solrIndexURL);
	SolrQuery query= new SolrQuery();
	
	query.setFields("docID","headline","description","hashtags","contacts","hasrethinkID","rethinkID","instanceID");
	query.set("q", searchString.trim());
	query.setParam("debug","query");
	query.setShowDebugInfo(true);
	
	
	QueryResponse qr= solr.query(query);
	
		
	SolrDocumentList dlist=qr.getResults();
	long resultNumber= dlist.getNumFound();
	log.debug("number "+resultNumber);
	log.debug("Search needed "+qr.getElapsedTime()+" ms");

	
	String test= qr.getDebugMap().toString();
	log.debug("request debug info "+test);
	
	Iterator<SolrDocument> iter = dlist.iterator();
	
		while (iter.hasNext())
		{
		SolrDocument doc=iter.next();
		
		
		Collection col= doc.getFieldNames();
	
		for(@SuppressWarnings("unchecked")
		Iterator<String> it= col.iterator();it.hasNext();)
							log.debug((String)it.next());
					
		
		Hashtable<String,String> resultTable = new Hashtable<String,String>();
		
		
			try{
				resultTable.put("docID", doc.getFieldValue("docID").toString());
		
			}catch(Exception ex)
				{
				resultTable.put("docID","[ ]");
				}	
		
		
			try{
				resultTable.put("headline", doc.getFieldValue("headline").toString());
				
			}catch(Exception ex)
				{
				resultTable.put("headline","[ ]");
				}	
		
			
			try{
				resultTable.put("description", doc.getFieldValue("description").toString());
				
			}catch(Exception ex)
				{
				resultTable.put("description","[ ]");
				}	
			
			try{
				
				resultTable.put("contacts",doc.getFieldValue("contacts").toString());
			
			}catch(Exception ex)
				{
				resultTable.put("contacts","[ ]");
				}	
			
			try{
				
				resultTable.put("hashtags",doc.getFieldValue("hashtags").toString());
				
			}catch(Exception ex)
				{
				resultTable.put("hashtags","[ ]");
				}		
			
			try{	
				resultTable.put("hasrethinkID",doc.getFieldValue("hasrethinkID").toString());
			
				}catch(Exception ex)
				{
				resultTable.put("hasrethinkID","[ ]");
				}		
			
			try{	
				resultTable.put("rethinkID",doc.getFieldValue("rethinkID").toString());
			
				}catch(Exception ex)
				{
				resultTable.put("rethinkID","[ ]");
				}		
		
			try{	
				resultTable.put("instanceID",doc.getFieldValue("instanceID").toString());
			
				}catch(Exception ex)
				{
				resultTable.put("instanceIDID","[ ]");
				}	
		
		
		returnVector.add(resultTable);		
		}
						
	solr.close();
	log.debug("Search request for >> "+searchString+" << has result count of: "+returnVector.size()+" no policy");	
	return returnVector;	
	}
	
	public long indexedProfileCount() throws SolrServerException, IOException
	{
		SolrClient solr = new HttpSolrClient(solrIndexURL);
  
    	
    
    		SolrQuery query= new SolrQuery();
    		
    		query.set("q", "*:*");
    		//query.set("fl", searchString);
    		query.setFields("docID","headline","description");
    		QueryResponse qr= solr.query(query);
    		
    		SolrDocumentList dlist=qr.getResults();
    	
    		return dlist.getNumFound();
    		
	}


	public void indexProfile(int docID, String headline, String description, String hashtags, String contacts, String hasrethinkID, String rethinkID) throws SolrServerException, IOException
	{
		
		SolrClient solr = new HttpSolrClient(solrIndexURL);
		
		SolrInputDocument doc=new SolrInputDocument();
		doc.clear();
		doc.addField("docID", ""+docID);
		doc.addField("headline", headline);
		doc.addField("description", description);
		doc.addField("hashtags", hashtags);
		doc.addField("contacts", contacts);
		doc.addField("hasrethinkID", hasrethinkID);
		doc.addField("rethinkID", rethinkID);
		doc.addField("instanceID", this.instanceID);
		
		solr.add(doc);
		solr.commit(); 			
		solr.close();
		log.info("Profile "+docID+" is indexed in Solr.");
	}
	
	
	public void deleteProfileFromIndex(int docID) throws SolrServerException, IOException
	{
	SolrClient solr = new HttpSolrClient(solrIndexURL);

	solr.deleteByQuery("docID:"+docID);
	solr.commit();
	solr.close();
	log.info("Profile "+docID+" was deleted from index in Solr");
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

}
