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

//1. 	get request parameter
String inputText = request.getParameter("searchtext");

//2. 	get result vector
Vector<Hashtable<String,String>> resultVector=helper.doPolicyEnabledSearch(inputText,0);
int zIndex=resultVector.size();
%>
<title>R E T H I N K</title>

<link href='https://fonts.googleapis.com/css?family=Lato:100,300,400,700,900' rel='stylesheet' type='text/css'>
<link rel="stylesheet" type="text/css" href="css/style.css"/>
<link rel="stylesheet" type="text/css" href="css/results.css"/>
<script src="script/jquery.min.js" type="text/javascript"></script>
<style type="text/css">
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
<!--_____________________________RESULTS_____________________________-->
<div id="container_res">
<div class="spacer_1"></div>
<% 
//3. 	start to enumerate results
int i=0;

for(Enumeration<Hashtable<String,String>> el=resultVector.elements();el.hasMoreElements();)
{ 
	Hashtable singleResult = (Hashtable) el.nextElement();		
    String GUID = helper.cleanUpString(singleResult.get("rethinkID").toString());
    String hasGUID = helper.cleanUpString(singleResult.get("hasrethinkID").toString()) ;
    String rawAnswerGR = gdrc.getRawAnswerOfGlobalRegistry(helper.cleanUpString(singleResult.get("rethinkID").toString()));
    boolean GUIDexist = gdrc.GUIDexists(rawAnswerGR);
    List currentHyperties = gdrc.saveGetCurrentHypertiesFromGlobalAndDomainRegistry(rawAnswerGR);
    i++;
%>
<!--_____________________________RESULT 0<%out.print(i);%>_____________________________-->

<div id="result_0<%out.print(i);%>" >

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
		<td width="100"><a href="connectGlobalRegistry.jsp?guid=<%out.print(GUID);%>&headline=<%out.print(helper.cleanUpString(singleResult.get("headline").toString())); %>" target="_parent"><div class="rebutton" id="reb_0<%out.print(i);%>">RETHINK</div></a></td>
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
    <td class="rehead"><%out.print(helper.cleanUpString(singleResult.get("headline").toString())); %></td>
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
			%>
			<a href="<% out.print(helper.analyzeContactString(nextElement));%>" target="_parent"><div class="relink"><% out.print(nextElement);%></div></a>
			<% 
		}
	}

%>
</div>
<div class="spacer_1"></div>
<%
//close enumeration
}
%>
</div>
</div>
</body>
</html>


