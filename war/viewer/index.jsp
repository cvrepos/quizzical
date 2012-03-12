<%@page pageEncoding="UTF-8" isELIgnored="false" session="false"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@taglib prefix="f" uri="http://www.slim3.org/functions"%>
<html>
<head>

<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<title>viewer Index</title>			
<link type="text/css" href="../css/cube.css" rel="stylesheet" />
<script type="text/javascript" src="../js/jquery-1.6.2.min.js"></script>
<script type="text/javascript" src="../js/json2.js"></script>
<script type="text/javascript">
var modJsons  = new Array();
<c:forEach var="m" items="${modules}">
modJson = ${m};
modJsons.push(modJson);
</c:forEach>
$(function(){
	 $.each(modJsons, function(index, module){		 
		 $("#modules").append("<li><a href='process?QN_action=start&QN_mid=" 
				 + module.mid 
				 + "&QN_mname="
				 + module.mname 
				 + "'>" 
				 + module.mname 
				 + "</a> " 
				 + "Total Questions: " 
				 + module.questionCount
				 + "Total Downloads: " 
				 + module.cloneCount
				 + " </li> ");
	 });
});


</script>
<body>	
<h1>Career Vision</h1>
<header>
	<a href="">Demo</a>	 
	<a href="../login?op=log<%=request.getAttribute("loginop")%>&orig=./viewer">Log<%=request.getAttribute("loginop") %></a>
</header>
<h2>Updates</h2>
<p> Here are the modules that are available.
<ul id='modules'>	
</ul> 
<footer>
	<a href="">Home</a>	
	<a href="">About Us</a>
</footer>
</body>
</html>
