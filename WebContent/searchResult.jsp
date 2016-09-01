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
</head>
<body>
<div id="mainframe">


<!--_____________________________LOGIN_____________________________-->
<a href="login.jsp" target="_parent"><div id="login">LOGIN</div></a>

<!--_____________________________SEARCH_____________________________-->
<div id="search_res">
<form id="form1" name="form1" method="post" action="">
<table width="450" border="0" cellspacing="0" cellpadding="0">
  <tr>
    <td><input name="searchtext" type="text" placeholder="Search Profile" value="<%out.println((String)request.getParameter("searchtext")); %>" class="input" /></td>
    <td align="right"><input name="login" type="submit" class="arrow" value=""/></td>
  </tr>
</table>
</form>
</div>

<%
FormularHelper helper =new FormularHelper(request);
//1. 	get request parameter
String inputText = request.getParameter("searchtext");


//2. 	get result vector
Vector<Hashtable<String,String>> resultVector=helper.doPolicyEnabledSearch(inputText,0);
%>

<!--_____________________________RESULTS_____________________________-->
<div id="container_res">
<div class="spacer_1"></div>
<% 
//3. 	start to enumerate results
int i=0;

for(Enumeration<Hashtable<String,String>> el=resultVector.elements();el.hasMoreElements();)
{
	 
    Hashtable singleResult = (Hashtable) el.nextElement();		
    i++;
%>
<!--_____________________________RESULT 0<% out.print(i);%>_____________________________-->
<div id="result_0<%out.print(i);%>">
<table width="100%" border="0" cellspacing="0" cellpadding="0">
   <tr> 
<% 
//4. 	check if rethinkID is available
if(helper.cleanUpString(singleResult.get("hasrethinkID").toString()).equals("true"))
{
	%>
	 <td width="100"><a href="connectGlobalRegistry.jsp?guid=<%out.print(helper.cleanUpString(singleResult.get("rethinkID").toString()));%>&headline=<%out.print(helper.cleanUpString(singleResult.get("headline").toString())); %>" target="_parent"><div class="rebutton">RETHINK</div></a></td>
	<%
}
	; %>   
    <td class="rehead"><%out.print(helper.cleanUpString(singleResult.get("headline").toString())); %></td>
  </tr>
</table>
<div class="spacer_2"></div>
<div class="retext">
<%out.print(helper.cleanUpString(singleResult.get("description").toString())); %>
</div>
<div  class="spacer_2"></div>
<%
	String contacts = helper.cleanUpString(singleResult.get("contacts").toString());
	
	if(!contacts.equals(""))
	{
		StringTokenizer st = new StringTokenizer(contacts);
		while(st.hasMoreElements())
		{
			String nextElement = (String) st.nextElement();
			;
			%>
			<a href="<% out.print(helper.analyzeContactString(nextElement));%>" target="_parent"><div class="relink"><% out.print(nextElement);%></div></a>
			<% 
		}
	}

%>
<div class="spacer_1"></div>
<%
//close enumeration
}
%>
</div>

</div>

</body>
</html>


