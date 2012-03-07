<%@page pageEncoding="UTF-8" isELIgnored="false" session="false"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@taglib prefix="f" uri="http://www.slim3.org/functions"%>

<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<title>Login</title>
	<link type="text/css" href="../css/cube.css" rel="stylesheet" />	
</head>
<body>
<h1>Career Vision</h1>
<header>
	<a href="">Demo</a>		
</header>
<b><%
if (request.getAttribute("error") != null) {    
    out.println("<I>"+request.getAttribute("error")+"</I>");
}
%>
<div id='login'>
	<form method='post' action=''>
		<label for='username'>Username:</label>
		<input type='text' name='username'></input>
		<label for='password'>Password:</label>
		<input type='password' name='password'></input> 
		<input type='hidden' name='op' value='login'></input>
		<input type='submit' name='submit' value='submit'></input> 
	</form>	
</div>
<footer>
	<a href="">Home</a>	
	<a href="">About Us</a>
</footer>
</body>
</html>
