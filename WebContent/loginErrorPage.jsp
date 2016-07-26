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

<div id="log">
<form id="form1" name="form1" method="post" action="loginResult.jsp">
<table width="400" border="0" cellspacing="0" cellpadding="0">
   <tr>
	<td colspan="2" class="log_text h45">Username or Password is incorrect. Please try again!</td>
   </tr>
   <tr>
	<td class="spacer_2"></td>
   </tr>
   <tr>
	<td colspan="2"><input name="username" type="text" placeholder="Username" class="log_input"/></td>
   </tr>
   <tr>
	<td class="spacer_2"></td>
   </tr>
   <tr>
	<td colspan="2"><input name="password" type="password" placeholder="Password" class="log_input"/></td>
   </tr>
   <tr>
	<td class="spacer_2"></td>
   </tr>
	<td align="left"><a href="searchPage.jsp" target="_parent"><div class="button">CANCEL</div></a></td>
	<td align="right"><input name="login" type="submit" class="button_form" value="LOGIN"/></td>
   </tr>
</table>
</form>
</div>

</div>
</body>
</html>
