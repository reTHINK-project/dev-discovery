# dev-discovery

This repository hosts the soure code of the reTHINK discovery service.

## overview

reTHINK Discovery Service is basically a service to find other communication partner. We are used to use search engines like google when we want to find information about a certain topic. In the same way we can search for persons or also devices. We formulate a search query like “Michael Mueller T-Labs”  or “reTHINK Project” in order to find a so called profile of a person. Also queries like “who want’s to buy my Mercedes 220 SL “can lead to results. Although this service is easy to use like a classic search engine there are fundamental differences.

**1)      Source of query results**

Search results are profiles that are written by the user that want to be found. Results are not crawled or scanned in the Internet. Every user that wants to be found or that wants his devices to be found can create one or many profiles within the discovery service.

These profiles stay fully under his control.

**2)      Policy-based visibility**

Every profile can be configured with a certain visibility. So the owner of a profile can configure which user or which group of users can see his profile.

###Interfaces

**Web-GUI**

The reTHINK discovery has a Web-Interface for users that want to use reTHINK discovery with their browser like they are used to from a classic search-engine. Furthermore the web-GUI enables to create and manage profile accounts.

**Rest-API** 

Apart from a web interface reTHINK discovery also provides a RESTful API. This REST-interface enables applications and services to discovery communication endpoints of users without using the web-interface. So an developer can create his own application logic or design and is able to use reTHINK discovery in the background..

# Quick guide user view

##1) Plain web search

The user can go to the [reTHINK Discovery Website ](https://rethink.tlabscloud.com/discovery/) and search for users or devices. The search results are so called profiles. 
They have a headline and some text for description. They might have hashtags describing certain topics, communities, locations etc..
Profiles can also contain communication endpoints like e-mail address, phone number, websites, facebook- or linkedin profile URLS.

As an additional button within the profile there is a “reTHINK” button. If this kind of button is in the profile this means the owner of this profile has a “reTHINK”-ID (a.k.a GUID). 
When the button is grey there is either no valid GUID or there is no current hyperty of available.
When the button is blue the user can "mouse over" the button and a pop up menu starts showing a list of current available hyperties. Every link shows supported media and the brand or company name. 
If the system is properly configured the user gets connected with the selected party.

 
##2) Create an own account

Every user has the possibility to create an own account with the reTHINK discovery service.

Press *LOGIN* button in the upper right corner. Then press *REGISTER* button
 
Now opens up a Web-Formular to fill in:

**Headline**
The headline field is mandatory and should be a meaningful name for the profile.

**Description**
The description is also mandatory and should explain what the profile is about.

**reTHINK ID**
The reTHINK ID is an Identifier used to find current communication channels to reTHINK users(a.k.a GUID)

**Contact Information**
Here the user can put URLs that point to social networks or to the company homepage. (Please use full URLs e.g. http://www.my_company.com/)
Also e-mail addresses, phone numbers and many other things can be inserted here.

**Visibility**
Here the user can choose between different visibility options:

- all - means this profile is visible from all over the web
- reTHINK - means that the profile is visible for all users that have an account at reTHINK discovery
- Favorites - Ones the account is created and the user is logged in he can mark profiles of other users as favorites.
   With the visibility option *all my Favorites* only those Favorites can see the profile. This option is a good way to
   create closed or private user groups.
 
**Tags**
Here the user can add certain tags about topics, locations etc..

**Username**
The username is mandatory. It is not shown in the profile.

**Password**
Here the user should provide a password

Now press the *save* button. If no error occur a user is created with a first profile.
    
(A user can have many profiles with different visibilities. Profiles can be added, changed or deleted. When the last profile is deleted the user is deleted automatically)    
  
##3) REST API

The discovery service offers a REST-API for search queries.
The interface can be "pinged" by e.g GET https://rethink.tlabscloud.com/discovery/rest/discover
To search eg. for "Hans Telekom" call GET https://rethink.tlabscloud.com/discovery/rest/discover/lookup?searchquery=Hans+Schmitt

The answer is a JSON Object like for Example:

    {
	"instanceID":"telekom1",
	"responseCode":201,
	"searchString":"Hans+Schmitt",
	"results":
			[
				{
				"resultNo":0,
				"instanceID":"telekom1",
				"hashtags":"T-Labs Telekom",
				"description":"Hans Testprofile text text text",
				"rethinkID":"REAyDT-tYQI2u1km9LgYj05zb1hLmy__XlIN5B1LWUQ",
				"headline":"Hans",
				"contacts":"Hans.Schmitt@telekom.de",
				"hasrethinkID":"true",
				"hyperties":
							{
          				"hyperty": {
           								 "hyperty://rethink.tlabscloud.com/9fa65ac2-4338-4c81-a1f2-1747cb2c5ff9": {
              							"resources": [
                										"audio",
               											"video"
								              ],
								              "dataSchemes": [
								                "connection"
								              ],
								              "descriptor": "hyperty-catalogue://catalogue.rethink.tlabscloud.com/.well-known/hyperty/DTWebRTC",
								              "startingTime": "2017-03-03T10:44:50Z",
								              "hypertyID": "hyperty://rethink.tlabscloud.com/9fa65ac2-4338-4c81-a1f2-1747cb2c5ff9",
								              "userID": "user://gmail.com/hans.schmitt",
								              "lastModified": "2017-03-03T10:44:50Z",
								              "expires": 3600
								            }
								          }
								        }
			]
}

# Quick Guide Setup for Deployment
If you want to deploy discovery service read this section

The environment for where the service was developed and tested:

Java 1.8.0_91-b15
Tomcat 8.0.35

Additional libraries and configurations in order to run the discovery service:

1) Solr Setup

 a.) Download/Install Solr from 
 	 www.apache.org/dyn/closer.lua/lucene/solr/6.X.Y (look for latest version) I use 6.0.0
	
 b.) Make sure to set JAVA_HOME environment varaible

 c.) Once Solr is installed start solr and create a core called "rethink"
	
   There might be differences btw. Linux and Windows to do this:
   In windows it is done by command line  
   "solr start"
   "solr create -c rethink"
	
 d)  edit the solrIndexURL parameter in the web.xml file according to your setting

	 <context-param>
		<param-name>solrIndexURL</param-name>
		<param-value>http://localhost:8983/solr/rethink</param-value>
	 </context-param>
	 
you can check the rethink core by opening a browser and calling
http://localhost:8983/solr/rethink/select?q=*:*
	 
2) Neo4J Setup

  a) Download the community edition of Neo4J https://neo4j.com/download/ we used v 3.0.3
  b) Follow installation guide according to you OS 
	http://neo4j.com/docs/operations-manual/current/
	
   During installation you will recognize that the default password is neo4j
   I changed it to rethink
	
  c) Change web.xml file accordingly
	
	<context-param>
		<param-name>neo4jURL</param-name>
		<param-value>bolt://localhost</param-value>
	</context-param>
	<context-param>
		<param-name>neo4jDBname</param-name>
		<param-value>neo4j</param-value>
	</context-param>
	<context-param>
		<param-name>neo4jDBstring</param-name>
		<param-value>rethink</param-value>
	</context-param>
	
you can check your installation by calling 
http://localhost:7474/browser/		 

3) Setup SQL database (e.g. MariaDB)

 a) download and install MariaDB 
 	https://mariadb.org/ or MySQL or another SQL Database
	Tip: MariaDB comes with a Database browser HeidiSQL; 
	It helps you to manage the Database
 b) create a database called rethink; username = root;  password =rethink (in our example)
    change web.xml accordingly 		   
	create 2 tables
 c) create a table 'user'

		CREATE TABLE `users` 
					(
					`userID` BIGINT(20) NOT NULL AUTO_INCREMENT,
					`username` TEXT NULL,
					`password` TEXT NULL,
					`salt` TEXT NULL,
					PRIMARY KEY (`userID`)
					)
					COLLATE='latin1_swedish_ci'
					ENGINE=InnoDB
					AUTO_INCREMENT=1;
		
  d) create table 'profiles'
			with create code
		
		
		CREATE TABLE `profiles` (
					`docID` BIGINT(20) NOT NULL AUTO_INCREMENT,
					`userID` BIGINT(20) NULL DEFAULT NULL,
					`headline` TEXT NULL,
					`description` TEXT NULL,
					`hashtags` TEXT NULL,
					`contacts` TEXT NULL,
					`rethinkID` TEXT NULL,
					INDEX `docID` (`docID`)
					)
					COLLATE='latin1_swedish_ci'
					ENGINE=InnoDB
					AUTO_INCREMENT=1;
		
  e) change web.xml file accordingly
    
	   	<context-param>
			<param-name>MariaDBDriver</param-name>
			<param-value>org.mariadb.jdbc.Driver</param-value>
		</context-param>
		<context-param>
			<param-name>MariaDBConnection</param-name>
			<param-value>jdbc:mariadb://localhost:3306/rethink</param-value>
		</context-param>
		<context-param>
			<param-name>MariaDBname</param-name>
			<param-value>root</param-value>
		</context-param>
		<context-param>
			<param-name>MariaDBstring</param-name>
			<param-value>rethink</param-value>
		</context-param>
		
4) change rethinkDomainAddresses.properties
	During the discovery process the GUID is mapped to non, one or more entries of services
	These entries contain a domain e.g. company.xyz.com; 
	this property file maps the domain name company.xyz.com to the real end point of the regarded domain registry
	e.g. **company.xyz.com=https.//rethink.dev.doaminregistry.company.xyz.com**
	
5) import ssl certificate of the domain registry in the trust store
	The domain registry is contacted via SSL. Open my.keystore (password rethink) to import trusted certificates
		
6) change rethinkProviderNames.properties
	The Discovery Service provides a GUI. When there is a valid GUID and a hyperty was found there is a popup menu with media type and provider name. This property file maps a URL to the regarded provider name 


7) Deploy the WAR-File
	copy the war-file simply into the webapps folder and then start tomcat
	
		 

# Quick Guide Setup for Developer 

If you want to play with source code you should read this.

The environment for where the service was developed and tested:

Java 1.8.0_91-b15
Tomcat 8.0.35

Additional libraries and configurations in order to run the discovery service:

1) Solr Setup
 a.) www.apache.org/dyn/closer.lua/lucene/solr/6.X.Y (look for latest version) I use 6.0.0
	
 b.) Make sure to set JAVA_HOME environment varaible

 c.) Once Solr is installed create a core called "rethink"
	
   There might be differences btw. Linux and Windows to do this:
   In windows it is done by command line  
   "solr start"
   "solr create -c rethink"
 

   you can check the rethink core by opening a browser and call
	 http://localhost:8983/solr/rethink/select?q=*:*
	
 d)  Build and Run SolrJ
	 Solr has an API for java called SolrJ
	
  Make sure to include solr-solrj-x.y.z.jar to the classpath at build time 
  (its in the dist directory of you Solr installation)
  at runtime all libraries located in the dist directory have to be available

  For more instruction read https://cwiki.apache.org/confluence/display/solr/Using+SolrJ
	
 e)  edit the solrIndexURL parameter in the web.xml file according to your setting

	 <context-param>
		<param-name>solrIndexURL</param-name>
		<param-value>http://localhost:8983/solr/rethink</param-value>
	 </context-param>

2) Neo4J Setup

  a) Download the community edition of Neo4J https://neo4j.com/download/ we used v 3.0.3
  b) Follow installation guide according to you OS 
	http://neo4j.com/docs/operations-manual/current/
	
   During installation you will recognize that the default password is neo4j
   I changed it to rethink
	
  c) Change web.xml file accordingly
	
	<context-param>
		<param-name>neo4jURL</param-name>
		<param-value>bolt://localhost</param-value>
	</context-param>
	<context-param>
		<param-name>neo4jDBname</param-name>
		<param-value>neo4j</param-value>
	</context-param>
	<context-param>
		<param-name>neo4jDBstring</param-name>
		<param-value>rethink</param-value>
	</context-param>
	
	you can check your installation by calling http://localhost:7474/browser/	
	
  d) download neo4j-java-driver-1.0.0.jar at https://neo4j.com/developer/java/#neo4j-java-driver
  e) make it available to the discovery service in build path and at runtime
	
3) Setup SQL database (e.g. MariaDB)


 a) download and install MariaDB https://mariadb.org/ or MySQL or another SQL Database
		   tip: MariaDB comes with a Database browser HeidiSQL; 
		   it helps you to manage the Database
 b) create a database called rethink; username = root;  password =rethink (in our example)
    if you use another one change web.xml accordingly 		   
		   Use HeideSQL to create 2 tables
 b) create a table 'user'

	CREATE TABLE `users` 
					(
					`userID` BIGINT(20) NOT NULL AUTO_INCREMENT,
					`username` TEXT NULL,
					`password` TEXT NULL,
					`salt` TEXT NULL,
					PRIMARY KEY (`userID`)
					)
					COLLATE='latin1_swedish_ci'
					ENGINE=InnoDB
					AUTO_INCREMENT=1;
		
  c) create table 'profiles'
			with create code
		
		
			CREATE TABLE `profiles` (
						`docID` BIGINT(20) NOT NULL AUTO_INCREMENT,
						`userID` BIGINT(20) NULL DEFAULT NULL,
						`headline` TEXT NULL,
						`description` TEXT NULL,
						`hashtags` TEXT NULL,
						`contacts` TEXT NULL,
						`rethinkID` TEXT NULL,
						INDEX `docID` (`docID`)
						)
						COLLATE='latin1_swedish_ci'
						ENGINE=InnoDB
						AUTO_INCREMENT=1;
		
  d) download maria-db-java-client-1.4.5.jar driver at 	https://www.versioneye.com/java/org.mariadb.jdbc:mariadb-java-client/1.4.5	
		
  e)  make mariadb-java-client-1.4.5.jar available at runtime
   
  f) change web.xml file accordingly
   
   
   
	   	<context-param>
			<param-name>MariaDBDriver</param-name>
			<param-value>org.mariadb.jdbc.Driver</param-value>
		</context-param>
		<context-param>
			<param-name>MariaDBConnection</param-name>
			<param-value>jdbc:mariadb://localhost:3306/rethink</param-value>
		</context-param>
		<context-param>
			<param-name>MariaDBname</param-name>
			<param-value>root</param-value>
		</context-param>
		<context-param>
			<param-name>MariaDBstring</param-name>
			<param-value>rethink</param-value>
		</context-param>
		
4) change rethinkDomainAddresses.properties
	During the discovery process the GUID is mapped to non, one or more entries of services
	These entries contain a domain e.g. company.xyz.com; 
	this property file maps the domain name company.xyz.com to the real end point of the regarded domain registry
	e.g. **company.xyz.com=https.//rethink.dev.doaminregistry.company.xyz.com**
	
5) import ssl certificate of the domain registry in the trust store
	The domain registry is contacted via SSL. Open my.keystore (password rethink) to import trusted certificates
		
6) change rethinkProviderNames.properties
	The Discovery Service provides a GUI. When there is a valid GUID and a hyperty was found there is a popup menu with media type and provider name. This property file maps a URL to the regarded provider name 


7) Deploy the WAR-File
	copy the war-file simply into the webapps folder and then start tomcat
			
	
