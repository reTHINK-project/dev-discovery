package de.telekom.rethink.discovery;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;

public class DBHelper {
	
String MariaDBDriver;
String MariaDBConnection;
String MariaDBname;
String MariaDBstring;	
	
FormularHelper formularHelper;	
	
	static Logger log = Logger.getLogger(DBHelper.class);		

	public DBHelper(HttpServletRequest request, FormularHelper formularHelper) {
		this.formularHelper=formularHelper;	
		this.MariaDBDriver =request.getSession().getServletContext().getInitParameter("MariaDBDriver");
		this.MariaDBConnection = request.getSession().getServletContext().getInitParameter("MariaDBConnection");
		this.MariaDBname =request.getSession().getServletContext().getInitParameter("MariaDBname");
		this.MariaDBstring = request.getSession().getServletContext().getInitParameter("MariaDBstring");
	}
	
	
	public List<Integer> getProfileNumbers(int userID) throws ClassNotFoundException, SQLException
	{
		List<Integer> list =new ArrayList<Integer>();	
				
		Class.forName(MariaDBDriver);
		Connection con = DriverManager.getConnection(MariaDBConnection,MariaDBname,MariaDBstring);

		java.sql.Statement st= con.createStatement();
		ResultSet rs;
		rs = st.executeQuery("SELECT * FROM profiles WHERE userID ='"+userID+"'");
		
		while(rs.next())
			{
			list.add(rs.getInt("docID"));
			}
		return list;
	}
	
	
	public HashMap<String,String> getUserProfile(int docID) throws ClassNotFoundException, SQLException
	{
	HashMap<String,String> list =new HashMap<String,String>();

	Class.forName(MariaDBDriver);
	Connection con = DriverManager.getConnection(MariaDBConnection,MariaDBname,MariaDBstring);

	
	java.sql.Statement st= con.createStatement();
	ResultSet rs;
	rs = st.executeQuery("SELECT * FROM profiles WHERE docID ='"+docID+"'");
	
	while(rs.next())
		{
		list.put("docID",rs.getString("docID"));
		list.put("headline",rs.getString("headline"));
		list.put("description",rs.getString("description"));
		list.put("hashtags",rs.getString("hashtags"));
		list.put("contacts", rs.getString("contacts"));
		list.put("rethinkID", rs.getString("rethinkID"));
		}
	return list;
	}
	
	public HashMap<String,String> getUserProfile(String docID) throws ClassNotFoundException, SQLException
	{
	HashMap<String,String> list =new HashMap<String,String>();
	
	Class.forName(MariaDBDriver);
	Connection con = DriverManager.getConnection(MariaDBConnection,MariaDBname,MariaDBstring);

	java.sql.Statement st= con.createStatement();
	ResultSet rs;
	rs = st.executeQuery("SELECT * FROM profiles WHERE docID ='"+docID+"'");
	
	while(rs.next())
		{
		list.put("docID",rs.getString("docID"));
		list.put("headline",rs.getString("headline"));
		list.put("description",rs.getString("description"));
		list.put("hashtags",rs.getString("hashtags"));
		list.put("contacts", rs.getString("contacts"));
		list.put("rethinkID", rs.getString("rethinkID"));
		}
	return list;
	}
	
	
	public int getProfileCount(int userID) throws ClassNotFoundException, SQLException
	{
		int count=0;
		
		Class.forName(MariaDBDriver);
		Connection con = DriverManager.getConnection(MariaDBConnection,MariaDBname,MariaDBstring);

		java.sql.Statement st= con.createStatement();
		ResultSet rs;
		rs = st.executeQuery("SELECT * FROM profiles WHERE userID ='"+userID+"'");
		
		while(rs.next())
			{
			count=count+1;
			}
		return count;
		
	}
	
	public void deleteUser(int userID) throws ClassNotFoundException, SQLException
	{
		Class.forName(MariaDBDriver);
		Connection con = DriverManager.getConnection(MariaDBConnection,MariaDBname,MariaDBstring);

		java.sql.Statement st= con.createStatement();
		
		st.executeQuery("DELETE FROM users WHERE userID ='"+userID+"'");
		log.info("User "+userID+" was removed from MariaDB");
	}
	
	public void deleteProfile(int docID) throws ClassNotFoundException, SQLException
	{
	Class.forName(MariaDBDriver);
	Connection con = DriverManager.getConnection(MariaDBConnection,MariaDBname,MariaDBstring);

	java.sql.Statement st1= con.createStatement();			
	st1.executeUpdate("DELETE FROM profiles WHERE docID ='"+docID+"'");
	con.close();
	log.info("Delete profile "+docID+" from MariaDB.");
	}
	
	public int loginUser(String username, String password) throws ClassNotFoundException, SQLException 
	{
	int userID=0;
	
	int hashcode=password.trim().hashCode();
	log.debug("pw hashcode login: "+hashcode);
	
	Class.forName(MariaDBDriver);
	Connection con = DriverManager.getConnection(MariaDBConnection,MariaDBname,MariaDBstring);

	Statement st= con.createStatement();
	ResultSet rs;
	ResultSet rs2;
	
	//lookup user
	rs = st.executeQuery("SELECT * FROM users WHERE username ='"+username+"' AND password ='"+hashcode+"'");
	
	if(rs.next())
	{
		
		rs2 = st.executeQuery("SELECT userID FROM users WHERE username ='"+username+"' AND password ='"+hashcode+"'");
		if(rs2.next())
			{	
			userID = rs2.getInt("userID");
			}			
	}
	
		if(userID==0)
			log.info("Login failed with >> "+username+ " <<");
		else
			log.info("User "+userID+" as "+username+ " logged in.");
			
		
	return userID;
	}
	
	
	public boolean userAlreadyExistCheck(String username) throws ClassNotFoundException, SQLException 
	{
	boolean returnvalue = false;
	
	Class.forName(MariaDBDriver);
	Connection con = DriverManager.getConnection(MariaDBConnection,MariaDBname,MariaDBstring);

	java.sql.Statement statement = con.createStatement();
	ResultSet resultset = statement.executeQuery("SELECT * FROM users WHERE username = '"+username+"'");

	while(resultset.next()){
				returnvalue=true;
		}
	
	return returnvalue;
	}
	
	public int createUser(String username, String password) throws ClassNotFoundException, SQLException
	{
	int userID=0;
	
	int hashcode=password.hashCode();
	log.debug("pw hascode at register: "+hashcode);
	
	
	Class.forName(MariaDBDriver);
	Connection con = DriverManager.getConnection(MariaDBConnection,MariaDBname,MariaDBstring);

	java.sql.Statement statement= con.createStatement();
	statement.executeUpdate("insert into users(username,password) values ('"+username+"','"+hashcode+"')"); 
	
	java.sql.Statement statement2 = con.createStatement();
	ResultSet resultset = statement2.executeQuery("SELECT * FROM users WHERE password = '"+hashcode+"' and username = '"+username+"'");

	while(resultset.next()){
				userID = resultset.getInt("userID");
		}
	con.close();
	
	log.info("User "+username+" created with userID "+userID+" in MariaDB.");
	
	return userID;	
	}
	
	public int createProfile(int userID, String headline,String description,String hashtags,String contacts, String rethinkID)throws ClassNotFoundException, SQLException
	{
	int docID=0;
	
	Class.forName(MariaDBDriver);
	Connection con = DriverManager.getConnection(MariaDBConnection,MariaDBname,MariaDBstring);

	
	java.sql.Statement statement= con.createStatement();
	statement.executeUpdate("insert into profiles(userID,headline,description,hashtags,contacts,rethinkID) values ('"+userID+"','"+headline+"','"+description+"','"+hashtags+"','"+contacts+"','"+rethinkID+"')");
	
	java.sql.Statement statement2 = con.createStatement();
	ResultSet resultset = statement2.executeQuery("SELECT * FROM profiles WHERE userID = '"+userID+"' and headline = '"+headline+"' and description = '"+description+"'");

	while(resultset.next()){
			docID = resultset.getInt("docID");
	}
	con.close();
	log.info("Profile "+headline+" with docID "+docID+" for user "+userID+" created in MariaDB.");
	
	return docID;
	}
	
	public void updateProfile(int docID,int userID,String headline, String description, String hashtags, String contacts, String rethinkID) throws ClassNotFoundException, SQLException
	{
		Class.forName(MariaDBDriver);
		Connection con = DriverManager.getConnection(MariaDBConnection,MariaDBname,MariaDBstring);

		java.sql.Statement st= con.createStatement();
			
		st.executeUpdate("insert into profiles(docID,userID,headline,description,hashtags,contacts,rethinkID) values ('"+docID+"','"+userID+"','"+headline+"','"+description+"','"+hashtags+"','"+contacts+"','"+rethinkID+"')");
		log.info("Profile "+docID+" was updated in MariaDB.");
	}
	

}
