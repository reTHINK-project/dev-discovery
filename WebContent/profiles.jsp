<%@ page import="de.telekom.rethink.discovery.FormularHelper"
			import="java.util.*"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />

<title>R E T H I N K</title>

<script src="http://code.jquery.com/jquery-latest.js" type="text/javascript"></script>

<link href='https://fonts.googleapis.com/css?family=Lato:100,300,400,700,900' rel='stylesheet' type='text/css'>
<link rel="stylesheet" type="text/css" href="css/style.css"/>
<style type="text/css">
body {
	background-image: url(images/back_01.png);
	background-repeat: repeat-x;
}
</style>
</head>
<body>
<script>
<!-- needed for the delete function -->
var profileNr;
var profileName;
</script>
<div id="mainframe">

<!--_____________________________NAVIGATION_____________________________-->
<a href="searchPage_in.jsp" target="_parent"><div id="home">SEARCH</div></a>
<a href="favorites.jsp" target="_parent"><div id="favs">FAVORITES</div></a>
<div id="prof_active" class="bold">MY PROFILES</div>
<a href="logout.jsp" target="_parent"><div id="logoff">LOGOUT</div></a>

<!--_____________________________PROFILES_____________________________-->
<div id="container_edit">
<div class="spacer_1"></div>

<%
FormularHelper helper =new FormularHelper(request);

//1.	get SessionAttributes
int userID = (Integer) session.getAttribute("userID"); 
String userName = (String) session.getAttribute("userName"); 

//2. list profiles
int profilecount = helper.getProfileCount(userID);
List<Integer> profileNumber = helper.getProfileNumbers(userID);

for (int i=0;i<profilecount;i++)
{
	HashMap<String,String> profile = helper.getUserProfile((Integer)profileNumber.get(i));
%>


<!--_____________________________ START PROFILE 0<% out.print(i);%>_____________________________-->
<div id="profile_0<% out.print(i);%>">
<table width="100%" border="0" cellspacing="0" cellpadding="0">
  <tr>
  
  	<% if(!profile.get("rethinkID").equals(""))
  		{%>
    	<td width="100"><a href="connectGlobalRegistry.jsp?guid=<%out.print(helper.cleanUpString(profile.get("rethinkID").toString()));%>" target="_parent"><div class="rebutton">RETHINK</div></a></td>
    	<%} 
    %>
    <td class="rehead"><% out.print(profile.get("headline")); %></td>
    <td width="50"><a href="addProfile.jsp" target="_parent" ><img src="images/add_01.png" width="33" height="33" class="add" /></a></td>
    <td width="50"><a href="changeProfile.jsp?docID=<% out.print(profile.get("docID")); %>" target="_parent" ><img src="images/edit_01.png" width="33" height="33" class="edit" /></a></td>
    <td width="50"><img src="images/delete_01.png" id="remove_0<% out.print(i);%>" width="33" height="33" class="delete" /></td>
  </tr>
</table>
<script>
$( "#remove_0<% out.print(i);%>" ).on( "click", function() {
	$('#dialogtext').html('Do you really want to remove profile <% out.print(profile.get("headline")+".");
	if(helper.getProfileCount(userID)==1) out.print(" If you delete this profil your account will be gone !");%>');
	$( popup_back).css( "display", "block" );
	$( popup).css( "display", "block" );	
	setTimeout(function(){
	$( popup_back).css( "opacity", "0.5" );
	$( popup).css( "opacity", "1" );	
},50); 
	profileNr = <% out.print(profile.get("docID"));%>	
});
</script>
<div class="spacer_2"></div>
<div class="retext">
<% out.print(profile.get("description")); %>
</div>
<div  class="spacer_2"></div>
<%
	String contacts = profile.get("contacts");
	
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
<div  class="spacer_2"></div>
<div class="line">
Visible for <% out.print(helper.getVisibility(profile.get("docID"))); %>
</div>
<div  class="spacer_3"></div>
<div class="line">
	<% out.print(profile.get("hashtags")); %>
</div>
<div class="spacer_1"></div>
<!--_____________________________ END PROFILE 0<% out.print(i);%>_____________________________-->
<%
}
%>

</div>

<!--_____________________________POPUP_____________________________-->
<div id="popup_back"></div>
<div id="popup">
<table width="360" border="0" cellspacing="0" cellpadding="0">
   <tr align="center">
	<td colspan="2" class="text_popup_head">REMOVE PROFILE</td>
   </tr>
   <tr>
	<td class="spacer_4" colspan="2"></td>
   </tr>
   <tr>
	<td align="center" colspan="2" class="text_popup h80" id ="dialogtext" >Dummytext</td>
   </tr>
   <tr>
	<td class="spacer_4" colspan="2"></td>
   </tr>
	<td align="left"><div id="no" class="button">NO</div></td>
<script>
$( "#no" ).on( "click", function() {
	$( popup_back).css( "opacity", "0" );
	$( popup).css( "opacity", "0" );
	setTimeout(function(){
	$( popup_back).css( "display", "none" );
	$( popup).css( "display", "none" );		
},1000); 
});
</script>
	<td align="right"><div id="yes" class="button">YES</div></td>
<script>
$( "#yes" ).on( "click", function() {
	$( popup_back).css( "opacity", "0" );
	$( popup).css( "opacity", "0" );
	setTimeout(function(){
	$( popup_back).css( "display", "none" );
	$( popup).css( "display", "none" );		
},1000);
	var deleteSite ="deleteProfileResult.jsp?docID"
	var target = deleteSite.concat("=",profileNr);
	window.location = target;
});
</script>
   </tr>
</table>
</div>

</div>
</body>
</html>    