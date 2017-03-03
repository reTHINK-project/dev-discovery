/********************************************************************************************************************
 * Deutsche Telekom Laboratories																					*
 * Copyright (c) 2016 European Project reTHINK																		*
 * 																													*
 ********************************************************************************************************************/

package de.telekom.rethink.discovery.listener;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Enumeration;
import java.util.Hashtable;

import javax.servlet.annotation.WebListener;
import javax.servlet.http.HttpSessionAttributeListener;
import javax.servlet.http.HttpSessionBindingEvent;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

import org.apache.log4j.Logger;

import de.telekom.rethink.discovery.FormularHelper;

/**
 * Application Lifecycle Listener implementation class SessionListener
 *
 */
@WebListener
public class SessionListener implements HttpSessionListener, HttpSessionAttributeListener {

	
	static Logger log = Logger.getLogger(SessionListener.class);		
	
	private static int activeSessions = 0;
	private static Hashtable sessionMemory;
	
	
    /**
     * Default constructor. 
     */
    public SessionListener() {
    	
    	if(sessionMemory==null)
    		sessionMemory = new Hashtable();
    	System.out.println("reTHINK discovery: SessionListener is initializing");
        // TODO Auto-generated constructor stub
    }

	/**
     * @see HttpSessionListener#sessionCreated(HttpSessionEvent)
     */
    public void sessionCreated(HttpSessionEvent arg0)  { 
         // TODO Auto-generated method stub
    	synchronized(this){
    		activeSessions++;
    		
    		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    		LocalDateTime dateTime = LocalDateTime.now();
    		String dateTimeString = dateTime.format(formatter); 
    		
    		
    		Hashtable session = new Hashtable();
    		session.put("sessionCreated", dateTimeString);
    		sessionMemory.put(arg0.getSession().getId(), session);
    		log.info("Sessions created: "+arg0.getSession().getId()	+ " active sessions: "+activeSessions);
    	}
    }

	/**
     * @see HttpSessionListener#sessionDestroyed(HttpSessionEvent)
     */
    public void sessionDestroyed(HttpSessionEvent arg0)  { 
         // TODO Auto-generated method stub
    	synchronized(this){
    	sessionMemory.remove(arg0.getSession().getId());	
    	activeSessions--;
    	log.info("Sessions killed: "+arg0.getSession().getId()+" active sessions: "+activeSessions);
    	}
    }

	/**
     * @see HttpSessionAttributeListener#attributeAdded(HttpSessionBindingEvent)
     */
    public void attributeAdded(HttpSessionBindingEvent arg0)  { 
         // TODO Auto-generated method stub
    	String sessionID = arg0.getSession().getId();
    	String argumentName= arg0.getName();
    	String argumentValue = arg0.getValue().toString();
    
    	addArgumentToSession(sessionID,argumentName,argumentValue);
   
    	log.info("Attribute was added: "+arg0.getSession().getId()+" : "+arg0.getName()+" : "+arg0.getValue());
    }

	/**
     * @see HttpSessionAttributeListener#attributeRemoved(HttpSessionBindingEvent)
     */
    public void attributeRemoved(HttpSessionBindingEvent arg0)  { 
         // TODO Auto-generated method stub
    	String sessionID = arg0.getSession().getId();
    	String argumentName= arg0.getName();
    	String argumentValue = arg0.getValue().toString();
    
    	removeArgumentToSession(sessionID,argumentName,argumentValue);
   
    	
    	log.info("Attribute was removed: "+arg0.getSession().getId()+" : "+arg0.getName()+" : "+arg0.getValue());
    }

	/**
     * @see HttpSessionAttributeListener#attributeReplaced(HttpSessionBindingEvent)
     */
    public void attributeReplaced(HttpSessionBindingEvent arg0)  { 
         // TODO Auto-generated method stub
    }
	
    public int getActiveSessions()
    {
    return activeSessions;	
    }	
    
    private void addArgumentToSession(String sessionID, String argumentName, String argumentValue)
    {
    Hashtable session = (Hashtable) sessionMemory.get(sessionID);
    session.put(argumentName, argumentValue);
    sessionMemory.put(sessionID,session);
    }
    
    private void removeArgumentToSession(String sessionID, String argumentName, String argumentValue)
    {
    try{	
    	Hashtable session = (Hashtable) sessionMemory.get(sessionID);
    	session.remove(argumentName, argumentValue);
    	sessionMemory.put(sessionID,session);
    }catch(Exception ex)
    	{
    	log.debug("Session was already killed");
    	}	
    }
    
    public int getSessionCount()
    {
    	return sessionMemory.size();
    }
    
    public Hashtable getSession(String sessionID)
    {
    return (Hashtable)sessionMemory.get(sessionID);	
    }	
    
    public Hashtable getSessionMemory()
    {
    log.info("return sessionMemeory");
    return sessionMemory;
    }
    
    public boolean userHasActiveSession(String username)
    {
    boolean returnValue = false;
    
    Enumeration enumeration = sessionMemory.keys();
    
    while(enumeration.hasMoreElements())
    		{
    	try{
    		String sessionID = (String) enumeration.nextElement();
    		Hashtable session =(Hashtable) sessionMemory.get(sessionID);
    		String userName = session.get("userName").toString();
    			
    		
    			if(userName.equals(username))
    				returnValue = true;
    		}catch(Exception ex){}
    		}
    
    return returnValue;
    }
    
    /*
    public void sessionAccessed(String sessionID)
    {
    //triggert by the authenticationfilter		
    Hashtable session = (Hashtable) sessionMemory.get(sessionID);
    session.put("lastAccess", System.currentTimeMillis());
    sessionMemory.put(sessionID,session);
    }
    */
    
    
}
