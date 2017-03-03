<%@ page 
import="de.telekom.rethink.discovery.FormularHelper"
import="org.apache.log4j.Logger"
import="java.util.*"
%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Insert title here</title>
</head>
<body>
<%
FormularHelper helper =new FormularHelper(request);
String dUser = request.getParameter("user");
String userName = (String) session.getAttribute("userName");

Logger log = Logger.getLogger(this.getClass());	

	
		if(!userName.equals("admin"))
			{
				response.sendRedirect("searchPage_in.jsp");
			}
	
//if user not all

if(dUser.equals("all"))
	{
	log.info("Admin requirement for delete all users.");
	
	try{	
		List list = helper.getUserList();
			
		Iterator<Hashtable> iter = list.iterator();
		
		while (iter.hasNext())
			{
			Hashtable user=iter.next();
			deleteUser(helper,(String)user.get("userID"));
			}
		}catch(Exception ex)
			{
			log.warn("DB error: "+ex);
			}	
	}
  else
	{
	 
	log.info("Admin requirement for delete user "+dUser+".");
	deleteUser(helper,dUser);
	}
	
	response.sendRedirect("user.jsp");

%>
<%!


	void deleteUser(FormularHelper helper,String dUser) throws Exception
	{
	boolean lastProfile=false;
	
	List<Integer> profileList = helper.getProfileNumbers(new Integer(dUser));
	
	Iterator<Integer> iter = profileList.iterator();
	
	while (iter.hasNext())
		{
		int docID = iter.next();
		
		//0. check whether its the last profile
		if(helper.getProfileCount(new Integer(dUser))==1)
				{
				lastProfile = true;
				}
		
		//2.	delete at Solr
		helper.deleteProfileFromIndex(docID);

		//3.	delete from MariaDB
		helper.deleteProfile(docID);

		//4.	delete visibility
		helper.removeAllFollower(docID+"");
		helper.deleteProfileNode(docID);



		if(lastProfile)
			{
			//delete realtionships or favorites in neo4j
			helper.removeAllFavorites(new Integer(dUser));
			helper.deleteUserNode(new Integer(dUser));
			
			//delete user in SQL-DB
			helper.deleteUser(new Integer(dUser));
			}
		}
	}

%>
</body>
</html>