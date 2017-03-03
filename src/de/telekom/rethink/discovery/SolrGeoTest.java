package de.telekom.rethink.discovery;

import java.io.IOException;
import java.util.Iterator;

import org.apache.log4j.Logger;
import org.apache.solr.client.solrj.SolrClient;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.HttpSolrClient;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;
import org.apache.solr.common.SolrInputDocument;

public class SolrGeoTest {

static Logger log = Logger.getLogger(SolrGeoTest.class );	
	
	public SolrGeoTest() throws SolrServerException, IOException {
		// TODO Auto-generated constructor stub
		log.info("Geotest started");
	
		//das feld "position" muss in schema.xml bzw über die oberfläche http://localhost:8983/solr/#/geodata/schema definiert werden 
		//http://localhost:8983/solr/geodata/select?wt=json&indent=true&fl=headline,position&q=*:*&fq={!geofilt%20pt=52.13,13.10%20sfield=position%20d=50}
		
		
		SolrClient solr = new HttpSolrClient("http://localhost:8983/solr/geodata");
		/*
		SolrInputDocument profileLeipzig=new SolrInputDocument();
		profileLeipzig.addField("headline","Nette Studentenstad in Sachsen");
		profileLeipzig.addField("position", "51.20,12.22");
		
		SolrInputDocument profileRom=new SolrInputDocument();
		profileRom.addField("headline","Antike Stadt");
		profileRom.addField("position", "41.53,12.29");
		
		solr.add(profileLeipzig);
		solr.add(profileRom);
		solr.commit(); 			
		solr.close();
		log.info("Profile is indexed in Solr.");
		*/
		
		SolrQuery query= new SolrQuery();
			
		query.setFields("headline","position");
		query.set("q","*:*");
		query.set("wt","json");
		query.set("indent","true");
		query.set("fq","{!geofilt}");
		query.set("pt","52.13,13.10");
		query.set("sfield", "position");
		query.set("d", 5000);
		
		
		
		//query.set("fq","{!geofilt%20pt=52.13,13.10%20sfield=position%20d=5000}");
	
		QueryResponse qr= solr.query(query);
		
			
		SolrDocumentList dlist=qr.getResults();
			
		
		Iterator<SolrDocument> iter = dlist.iterator();
		
			while (iter.hasNext())
			{
			SolrDocument doc=(SolrDocument)iter.next();
			
			try{
		
			log.info(doc.getFieldValue("headline").toString());
			log.info(doc.getFieldValue("position").toString());
			
				}catch(Exception ex)
					{
					log.info("Fehler beim Result lesen");
					}	

					}}

}
