<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
    
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />

<title>R E T H I N K</title>

<link href='https://fonts.googleapis.com/css?family=Lato:100,300,400,700,900' rel='stylesheet' type='text/css'>
<link rel="stylesheet" type="text/css" href="css/style.css"/>
</head>

<body>
<div id="mainframe">

<!--_____________________________LOGIN_____________________________-->
<a href="login.jsp" target="_parent"><div id="login">LOGIN</div></a>

<!--_____________________________SEARCH_____________________________-->
<div id="search">
<form id="form1" name="form1" method="post" action="searchResult.jsp">
<table width="450" border="0" cellspacing="0" cellpadding="0">
   <tr align="center">
	<td colspan="2" class="text_head h100">RETHINK</td>
   </tr>
   <tr>
	<td><input name="searchtext" type="text" placeholder="Search Profile" class="input" /></td>
    <td align="right"><input name="login" type="submit" class="arrow" value=""/></td>
   </tr>
</table>
</form>
</div>

<!--_____________________________FOOTER_____________________________-->
<div id="logo"><img src="images/telekom.png" width="324" height="30" /></div>
<div id="version">VERSION 0.1</div>

</div>
</body>
</html>
