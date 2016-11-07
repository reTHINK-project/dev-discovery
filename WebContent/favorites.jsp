<%@ page import="de.telekom.rethink.discovery.FormularHelper"
		 import="de.telekom.rethink.discovery.GlobalAndDomainRegistryConnector"
         import="java.util.*"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<%
FormularHelper helper =new FormularHelper(request);
GlobalAndDomainRegistryConnector gdrc = new GlobalAndDomainRegistryConnector(request,helper);

int userID = (int) session.getAttribute("userID");
List<String> favorites=helper.listFavorites(userID);
int zIndex=favorites.size();
%>
<title>R E T H I N K</title>

<link href='https://fonts.googleapis.com/css?family=Lato:100,300,400,700,900' rel='stylesheet' type='text/css'>
<link rel="stylesheet" type="text/css" href="css/style.css"/>
<link rel="stylesheet" type="text/css" href="css/results.css"/>
<script src="script/jquery.min.js" type="text/javascript"></script>
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
<% for (int j=1;j<=zIndex;j++)
	{
%>
#result_0<%out.print(j);%>{
	position: relative;
	top: 0px;
	left: 0px;
	width: 100%;
	z-index: <%out.print(zIndex-j);%>;
	}
	<%
	}
	%>	
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
Iterator<String> it= favorites.iterator();
int i=0;
while(it.hasNext())
{
	String profile=it.next();	
	Hashtable<String,String> hashtab= helper.getProfileFromSolr(profile);
	String GUID = helper.cleanUpString(hashtab.get("rethinkID").toString());
    String hasGUID = helper.cleanUpString(hashtab.get("hasrethinkID").toString()) ;
    String rawAnswerGR = gdrc.getRawAnswerOfGlobalRegistry(helper.cleanUpString(hashtab.get("rethinkID").toString()));
    boolean GUIDexist = gdrc.GUIDexists(rawAnswerGR);
    List currentHyperties = gdrc.saveGetCurrentHypertiesFromGlobalAndDomainRegistry(rawAnswerGR);
	i++;
%>
<!--_____________________________FAVORITE 01_____________________________-->
<div id="result_0<% out.print(i);%>">

<%
if(hasGUID.equals("true"))
{	
	if(GUIDexist)
		{ 	
		if(currentHyperties.size()>0)
			{
			Iterator entries=currentHyperties.iterator();
			%>
			<div id="repop_0<%out.print(i);%>" class="repop">
			<%
			while(entries.hasNext())
				{
				String line = (String) entries.next();
				
				String[] part = line.split("#");
				%>
				<a href="<%out.print(part[0]);%>"><div class="<%out.print(part[1]);%>"><span class="rp_text1"><%out.print(part[2]);%></span><span class="rp_text2a"><%out.print(part[3]);%></span></div></a>
				<%
				}
			%>
			<div class="spacer_3"></div>
			</div>
			<%
			}	
		}
}				
%>
<table width="100%" border="0" cellspacing="0" cellpadding="0">
  <tr>  
<% 
//4. 	check if rethinkID is available
if(hasGUID.equals("true"))
{
	if(GUIDexist && currentHyperties.size()>0)
	{
	%>
	 <td width="100"><a href="connectGlobalRegistry.jsp?guid=<%out.print(helper.cleanUpString(hashtab.get("rethinkID").toString()));%>" target="_parent"><div class="rebutton" id="reb_0<%out.print(i);%>">RETHINK</div></a></td>
	<%
	}
	else
	{
	%>
	<td width="100"><div class="grebutton" id="reb_0<%out.print(i);%>">RETHINK</div></td>
	<%
	}
}
	 %>    
    <td class="rehead"><% out.println(helper.cleanUpString(hashtab.get("headline"))); %></td>
    <td width="50"><a href="deleteFavoriteResult.jsp?profile=<% out.print(helper.cleanUpString(hashtab.get("docID"))); %>" target="_parent" ><img src="images/delete_01.png" width="33" height="33" class="delete" /></a></td>
  </tr>
</table>

<%
if(hasGUID.equals("true"))
{
	if(GUIDexist)
	{
		if(currentHyperties.size()>0)
		{
%>
<script type="text/javascript">
$( "#reb_0<%out.print(i);%>" ).on( "mouseenter", function() {
	$( repop_0<%out.print(i);%>).css( "display", "block" );		
});
$( "#reb_0<%out.print(i);%>" ).on( "mouseleave", function() {
	$( repop_0<%out.print(i);%>).css( "display", "none" );			
}); 
$( "#repop_0<%out.print(i);%>" ).on( "mouseenter", function() {
	$( repop_0<%out.print(i);%>).css( "display", "block" );	
});
$( "#repop_0<%out.print(i);%>" ).on( "mouseleave", function() {
	$( repop_0<%out.print(i);%>).css( "display", "none" );				
}); 
</script>
<%
		}
	}
}
%>

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
