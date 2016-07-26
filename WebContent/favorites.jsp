
<%@ page import="de.telekom.rethink.discovery.FormularHelper"
         import="java.util.*"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />

<title>R E T H I N K</title>

<link href='https://fonts.googleapis.com/css?family=Lato:100,300,400,700,900' rel='stylesheet' type='text/css'>
<link rel="stylesheet" type="text/css" href="css/style.css"/>
<style type="text/css">
body {
	background-image: url(images/back_01.png);
	background-repeat: repeat-x;
}
#container_res {
	position: absolute;
	left: 20px;
	top: 50px;
	width: calc(100% - 40px);
	min-width: 520px;
	z-index: 1;
}
</style>
</head>
<body>
<div id="mainframe">

<!--_____________________________NAVIGATION_____________________________-->
<a href="searchPage_in.jsp" target="_parent"><div id="home">SEARCH</div></a>
<div id="favs_active" class="bold">FAVORITES</div>
<a href="profiles.jsp" target="_parent"><div id="prof">MY PROFILES</div></a>
<a href="logout.jsp" target="_parent"><div id="logoff">LOGOUT</div></a>

<!--_____________________________FAVORITES_____________________________-->
<div id="container_res">
<div class="spacer_1"></div>
<%

FormularHelper helper =new FormularHelper(request);

int userID = (int) session.getAttribute("userID");

List<String> favorites=helper.listFavorites(userID);
Iterator<String> it= favorites.iterator();
while(it.hasNext())
{
	String profile=it.next();	
	//out.println("Profile "+ profile);
	Hashtable<String,String> hashtab= helper.getProfileFromSolr(profile);
	//out.println(hashtab.get("headline"));
%>

<!--_____________________________FAVORITE 01_____________________________-->
<div id="result_01">
<table width="100%" border="0" cellspacing="0" cellpadding="0">
  <tr>
   <!-- <td width="100"><a href="#" target="_parent"><div class="rebutton">RETHINK</div></a></td> --> 
<% 
//4. 	check if rethinkID is available
if(helper.cleanUpString(hashtab.get("hasrethinkID").toString()).equals("true"))
{
	%>
	 <td width="100"><a href="connectGlobalRegistry.jsp?guid=<%out.print(helper.cleanUpString(hashtab.get("rethinkID").toString()));%>" target="_parent"><div class="rebutton">RETHINK</div></a></td>
	<%
}
	; %> 
    
    <td class="rehead"><% out.println(helper.cleanUpString(hashtab.get("headline"))); %></td>
    <td width="50"><a href="deleteFavoriteResult.jsp?profile=<% out.print(helper.cleanUpString(hashtab.get("docID"))); %>" target="_parent" ><img src="images/delete_01.png" width="33" height="33" class="delete" /></a></td>
  </tr>
</table>
<div class="spacer_2"></div>
<div class="retext">
<% out.println(helper.cleanUpString(hashtab.get("description"))); %>
</div>
<div  class="spacer_2"></div>

<%
String contacts = helper.cleanUpString(hashtab.get("contacts").toString());
	
	if(!contacts.equals(""))
	{
		StringTokenizer st = new StringTokenizer(contacts);
		while(st.hasMoreElements())
		{
			String nextElement = (String) st.nextElement();
			%>
			<a href="<% out.print(helper.analyzeContactString(nextElement));%>" target="_parent"><div class="relink"><% out.print(nextElement);%></div></a>
			<% 
		}
	}
%>

</div>

<div class="spacer_1"></div>
<%

}
%>

</div>

</div>
</body>
</html>
