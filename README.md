# dev-discovery

This repository hosts the soure code of the reTHINK discovery service.
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

4) Deploy the WAR-File
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
	
	
4) Install JSON	H


The service handles JSON Objects and uses json-simple-1.1.jar so download http://www.java2s.com/Code/Jar/j/json-simple.htm
Include it in your buildpath / and at runtime
