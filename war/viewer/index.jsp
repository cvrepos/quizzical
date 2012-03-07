<%@page pageEncoding="UTF-8" isELIgnored="false" session="false"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@taglib prefix="f" uri="http://www.slim3.org/functions"%>
<html>
<head>

<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<title>viewer Index</title>			
<link type="text/css" href="../css/cube.css" rel="stylesheet" />
<body>	
<h1>Career Vision</h1>
<header>
	<a href="">Demo</a>	 
	<a href="../login?op=log<%=request.getAttribute("loginop")%>&orig=./viewer">Log<%=request.getAttribute("loginop") %></a>
</header>
<h2>Updates</h2>
<p>We have added question solved question papers for year <a href="process?QN_action=start&QN_mname=2010">2010</a> and <a href="process?QN_action=start&QN_mname=2011">2011</a>. 
<footer>
	<a href="">Home</a>	
	<a href="">About Us</a>
</footer>
</body>
</html>
