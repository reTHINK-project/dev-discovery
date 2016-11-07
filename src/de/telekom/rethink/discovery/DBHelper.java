/********************************************************************************************************************
 * Deutsche Telekom Laboratories																					*
 * Copyright (c) 2016 European Project reTHINK																		*
 * 																													*
 ********************************************************************************************************************/

package de.telekom.rethink.discovery;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.apache.solr.common.SolrDocument;
import org.apache.tomcat.util.codec.binary.Base64;

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
	
	
	public void log(String message)
	{
	log.info(message);	
		
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
	
	
	public int getDBUserCount() throws ClassNotFoundException, SQLException
	{
	int count =-1;	
		
			Class.forName(MariaDBDriver);
			Connection con = DriverManager.getConnection(MariaDBConnection,MariaDBname,MariaDBstring);

			java.sql.Statement st= con.createStatement();
			ResultSet rs;
		
			rs = st.executeQuery("SELECT COUNT(*) FROM users");
		
			
			if(rs.next())
			{
				count = rs.getInt(1);
				
			}
	return count; 
	}
	
	public int getDBProfileCount() throws ClassNotFoundException, SQLException
	{
	int count = -1;	
		Class.forName(MariaDBDriver);
		Connection con = DriverManager.getConnection(MariaDBConnection,MariaDBname,MariaDBstring);

			java.sql.Statement st= con.createStatement();	
			ResultSet rs;
			
			rs = st.executeQuery("SELECT COUNT(*) FROM profiles");
			
			if(rs.next())
			{
				count =rs.getInt(1);	
				
			}
	return count;		
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
	String securePassword = "";
		
	Class.forName(MariaDBDriver);
	Connection con = DriverManager.getConnection(MariaDBConnection,MariaDBname,MariaDBstring);

	Statement st= con.createStatement();
	ResultSet rs;
	ResultSet rs1;
	ResultSet rs2;
	
	
	//get salt for that user
	rs = st.executeQuery("SELECT * FROM users WHERE username ='"+username+"'");
	
	if(rs.next())
		{
		String saltBase64=rs.getString("salt");
		byte[] salt = Base64.decodeBase64(saltBase64.getBytes());
		securePassword = get_SHA_256_SecurePassword(password,salt);
		}
	
	
	//lookup user
	rs1 = st.executeQuery("SELECT * FROM users WHERE username ='"+username+"' AND password ='"+securePassword+"'");
	
	if(rs1.next())
	{
		
		rs2 = st.executeQuery("SELECT userID FROM users WHERE username ='"+username+"' AND password ='"+securePassword+"'");
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
	
	public List<Hashtable> getUserList() throws ClassNotFoundException, SQLException
	{
	Class.forName(MariaDBDriver);
	Connection con = DriverManager.getConnection(MariaDBConnection,MariaDBname,MariaDBstring);

	List<Hashtable> list = new ArrayList();
	java.sql.Statement st= con.createStatement();	
	ResultSet rs;
			
	rs = st.executeQuery("SELECT * FROM users");
	
	
	while(rs.next())
			{
			Hashtable<String,String> resultTable = new Hashtable<String,String>();	
			resultTable.put("userID", rs.getString("userID"));	
			resultTable.put("username", rs.getString("username"));	
			list.add(resultTable);	
			}
	return list;	
	}
	
	
	public int createUser(String username, String password) throws ClassNotFoundException, SQLException, NoSuchAlgorithmException, UnsupportedEncodingException
	{
	int userID=0;
	
	//create salt
	byte[] salt = getSalt();
	//create sat as string
	//String saltstring = new String(salt,"ISO-8859-1");
	String saltBase64 = Base64.encodeBase64String(salt);
	log.debug("pw hascode at register: "+saltBase64);
	//create secure password
	String securePassword = get_SHA_256_SecurePassword(password, salt);
	log.debug("pw hascode at register: "+securePassword);
	
	
	Class.forName(MariaDBDriver);
	Connection con = DriverManager.getConnection(MariaDBConnection,MariaDBname,MariaDBstring);

	java.sql.Statement statement= con.createStatement();
	statement.executeUpdate("insert into users(username,password,salt) values ('"+username+"','"+securePassword+"','"+saltBase64+"')"); 
	
	java.sql.Statement statement2 = con.createStatement();
	ResultSet resultset = statement2.executeQuery("SELECT * FROM users WHERE password = '"+securePassword+"' and username = '"+username+"'");

	while(resultset.next()){
				userID = resultset.getInt("userID");
		}
	con.close();
	
	log.info("User "+userID+" created ("+username+") in MariaDB.");
	
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
	log.info("User "+userID+" created new Profile "+docID+" in MariaDB.");
	
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
	
	
	
	private static String get_SHA_256_SecurePassword(String passwordToHash, byte[] salt)
    {
        String generatedPassword = null;
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            md.update(salt);
            byte[] bytes = md.digest(passwordToHash.getBytes());
            StringBuilder sb = new StringBuilder();
            for(int i=0; i< bytes.length ;i++)
            {
                sb.append(Integer.toString((bytes[i] & 0xff) + 0x100, 16).substring(1));
            }
            generatedPassword = sb.toString();
        } 
        catch (NoSuchAlgorithmException e) 
        {
            e.printStackTrace();
        }
        return generatedPassword;
    }
	
	
	//Add salt
    private static byte[] getSalt() throws NoSuchAlgorithmException
    {
        SecureRandom sr = SecureRandom.getInstance("SHA1PRNG");
        byte[] salt = new byte[16];
        sr.nextBytes(salt);
        return salt;
    }

}
