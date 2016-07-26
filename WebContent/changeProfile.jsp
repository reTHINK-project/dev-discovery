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
<link rel="stylesheet" type="text/css" href="css/radio.css"/>
<style type="text/css">
body {
	background-image: url(../images/back_01.png);
	background-repeat: repeat-x;
}
</style>
</head>

<body>
<div id="mainframe">
<% 
String docID = request.getParameter("docID");

FormularHelper helper =new FormularHelper(request);

String visi=helper.getVisibility(docID);

HashMap<String,String> profile = helper.getUserProfile(docID.trim());
%>

<!--_____________________________NAVIGATION_____________________________-->
<a href="searchPage_in.jsp" target="_parent"><div id="home">SEARCH</div></a>
<a href="favorites.jsp" target="_parent"><div id="favs">FAVORITES</div></a>
<div id="prof_active" class="bold">EDIT PROFILE</div>
<a href="logout.jsp" target="_parent"><div id="logoff">LOGOUT</div></a>

<!--_____________________________PROFILE_____________________________-->
<div id="container_profile">
<form id="form1" name="form1" method="post" action="changeProfileResult.jsp">
<table width="600" border="0" cellspacing="0" cellpadding="0">
   <tr>
	<td colspan="2" class="spacer_1"></td>
   </tr>
   <tr>
	<td colspan="2"><input name="headline"  placeholder="Enter Profile Headline" value="<% out.print(profile.get("headline")); %>" type="text" value="MAX MUSTERMANN" class="pname_input"/></td>
   </tr>
   <tr>
	<td class="spacer_2"></td>
   </tr>
   <tr>
	<td colspan="2"><textarea name="description" id="textfield" placeholder="Enter Profile Description" class="pdescript_input"><% out.print(profile.get("description")); %></textarea></td>
   </tr>
   <tr>
	<td class="spacer_2"></td>
   </tr>
   <tr>
	<td colspan="2"><input name="rethinkID" placeholder="Copy and paste Rethink ID" type="text" value="<% out.print(profile.get("rethinkID")); %>" class="pname_input"/></td>
   </tr>
   <tr>
	<td class="spacer_2"></td>
   </tr>
   <tr>
	<td colspan="2"><textarea name="contacts" id="textfield"  placeholder="Enter Contact Information like Email Address, Web Address, Phone Number..." class="pdescript_input"><% out.print(profile.get("contacts")); %></textarea></td>
   </tr>
   <tr>
	<td class="spacer_2"></td>
    <tr bgcolor="#E6E6E6">
	<td colspan="2"><ul>
    <li>
    <input type="radio" id="f-option" name="visibility"  value="all" <% if(visi.equals("all")) out.println("checked = \"checked\""); %>>
    <label for="f-option">Visible for all Users</label>
    <div class="check"></div>
    </li>  
    <li>
    <input type="radio" id="s-option" name="visibility" value="rethink" <% if(visi.equals("rethink")) out.println("checked = \"checked\""); %>>
    <label for="s-option">Visible for all RETHINK Users</label>
    <div class="check"></div>
    </li>  
    <li>
    <input type="radio" id="t-option" name="visibility" value="favs" <%  if(visi.equals("favs")) out.println("checked = \"checked\""); %>>
    <label for="t-option">Visible for all my Favorites</label>
    <div class="check"></div>
    </li>
    </ul></td>
   </tr>
   <tr>
	<td class="spacer_2"></td>
   </tr>
   <tr>
	<td colspan="2">
	<input name="hashtags" placeholder="Enter Tages for search like #tagname #tagname" type="text" value="<% out.print(profile.get("hashtags")); %>" class="pname_input"/></td>
	<input name="docID" type="hidden"  value ="<% out.print(docID);%>"></input> 
   </tr>
   <tr>
	<td class="spacer_2"></td>
   </tr>
   <tr>
	<td align="left"><a href="profiles.jsp" target="_parent"><div class="button">CANCEL</div></a></td>
    <td align="right"><input name="login" type="submit" class="button_form" value="SAVE"/></td>
   </tr>
   <tr>
	<td colspan="2" class="spacer_1"></td>
   </tr>
</table>
</form>
</div>

</div>
</body>
</html>
    