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
</head>

<body>
<div id="mainframe">

<!--_____________________________PROFILE_____________________________-->
<div id="container_register">
<form id="form1" name="form1" method="post" action="registerResult.jsp">
<table width="600" border="0" cellspacing="0" cellpadding="0">
   <tr>
	<td colspan="2" class="spacer_1"></td>
   </tr>
   
    <tr>
	<td colspan="2" class="log_text">Headline, Description or Username should not be empty!</td>
   </tr>
   
   <tr>
	<td colspan="2"><input name="headline" type="text" placeholder="Enter Profile Headline"  value="<% out.println(request.getParameter("headline")); %>" class="pname_input"/></td>
   </tr>
   <tr>
	<td class="spacer_2"></td>
   </tr>
   <tr>
	<td colspan="2"><textarea name="description" placeholder="Enter Profile Description" id="textfield" class="pdescript_input"><% out.println(request.getParameter("description")); %></textarea></td>
   </tr>
   <tr>
	<td class="spacer_2"></td>
   </tr>
   <tr>
	<td colspan="2"><input name="rethinkID" type="text"  placeholder="Copy and paste Rethink ID" value="<% out.println(request.getParameter("rethinkID")); %>" class="pname_input"/></td>
   </tr>
   <tr>
	<td class="spacer_2"></td>
   </tr>
   <tr>
	<td colspan="2"><textarea name="contacts" id="textfield" placeholder="Enter Contact Information like Email Address, Web Address, Phone Number..." class="pdescript_input"><% out.println(request.getParameter("contacts")); %></textarea></td>
   </tr>
   <tr>
	<td class="spacer_2"></td>
   </tr>
   <tr bgcolor="#E6E6E6">
	<td colspan="2">
    <ul>
    <li>
    <input type="radio" id="f-option" name="visibility"  value="all" <% if(request.getParameter("visibility").equals("all")) out.println("checked = \"checked\""); %>>
    <label for="f-option">Visible for all Users</label>
    <div class="check"></div>
    </li>  
    <li>
    <input type="radio" id="s-option" name="visibility"  value="rethink" <% if(request.getParameter("visibility").equals("rethink")) out.println("checked = \"checked\""); %>>
    <label for="s-option">Visible for all RETHINK Users</label>
    <div class="check"></div>
    </li>  
    <li>
    <input type="radio" id="t-option" name="visibility"   value="favs" <% if(request.getParameter("visibility").equals("favs")) out.println("checked = \"checked\""); %>>
    <label for="t-option">Visible for all my Favorites</label>
    <div class="check"></div>  
    </li>
    </ul></td>
   </tr>
   <tr>
	<td class="spacer_2"></td>
   </tr>
   <tr>
	<td colspan="2"><input name="hashtags" type="text"  value="<% out.println(request.getParameter("hashtags")); %>" placeholder="Enter Tags for search like #tagname #tagname" class="pname_input"/></td>
   </tr>
   <tr>
	<td class="spacer_2"></td>
   </tr>
  
   <tr>
	<td class="spacer_2"></td>
   </tr>
   <tr>
	<td colspan="2"><input name="username" type="text"  placeholder="Enter Username" class="pname_input"/></td>
   </tr>
   <tr>
	<td class="spacer_2"></td>
   </tr>
   <tr>
	<td colspan="2"><input name="password" type="text"  placeholder="Enter Password" class="pname_input"/></td>
   </tr>
   <tr>
	<td class="spacer_2"></td>
   </tr>
   <tr>
	<td align="left"><a href="searchPage.jsp" target="_parent"><div class="button">CANCEL</div></a></td>
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
